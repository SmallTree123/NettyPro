package com.nylgsc.netty.http;

import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * HttpObject:客户端和服务器端相互通信数据对象
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     * @param channelHandlerContext
     * @param httpObject
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest){
            //对特定资源进行限制
            HttpRequest httpRequest = (HttpRequest) httpObject;
            URI uri = new URI(httpRequest.getUri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求重复，不作任何响应...");
                return;
            }

            System.out.println("msg 类型="+httpObject.getClass());
            System.out.println("客户端地址："+channelHandlerContext.channel().remoteAddress());

            //回复信息给浏览区（http协议）
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello ,我是自定义服务", CharsetUtil.UTF_8);
            //构建一个http的响应
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());

            //将构建好的response返回
            channelHandlerContext.writeAndFlush(response);

        }

    }
}

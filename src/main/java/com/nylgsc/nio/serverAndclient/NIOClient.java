package com.nylgsc.nio.serverAndclient;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {
    public static void main(String[] args) throws Exception{

        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
        //链接服务器
        if (!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("客户端繁忙...");
            }
        }
        //连接成功，发送数据
        String str  = "hello 北京你好啊";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据将buffer数据写入到channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}

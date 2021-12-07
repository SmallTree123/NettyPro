package com.nylgsc.nio.serverAndclient;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel类似于ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到selector对象
        Selector selector = Selector.open();
        //绑定端口9999，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到Selector，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){
            if (selector.select(1000)==0){  //没有事件发生
                System.out.println("服务器等待了1m...");
            }
            //如果返回的>0,就获取到相应的selectorKey集合
            //1，如果返回的>0，说明已经获取到关注的事件
            //2,selector.selectorKeys()返回关注事件的集合
            //通过selectorKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历Set<SelectionKey>,使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                //获取到selectorKey
                SelectionKey key = keyIterator.next();
                //根据key对应通道发生的事件做相应的处理
                if (key.isAcceptable()){ //如果是OP_ACCEPT，有一个新的链接
                    //给该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功...生成一个channel"+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞的
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector 关注事件为OP_ACCEPT，同时给socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //发生OP_READ
                if (key.isReadable()){
                    //通过key 反向获取到对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from 客户端"+new String(byteBuffer.array()));
                }
                //手动从当前集合remove掉当前SelectionKey
                keyIterator.remove();

            }
        }
    }
}

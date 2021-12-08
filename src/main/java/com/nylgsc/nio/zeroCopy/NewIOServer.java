package com.nylgsc.nio.zeroCopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) throws Exception{
        InetSocketAddress address = new InetSocketAddress(8989);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            while (true){
                int read = socketChannel.read(byteBuffer);
                if (read == -1){
                    break;
                }
                //将buffer对象中的position，mark初始化
                byteBuffer.rewind();
            }
        }
    }
}

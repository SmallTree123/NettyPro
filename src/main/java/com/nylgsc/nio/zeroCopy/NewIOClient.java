package com.nylgsc.nio.zeroCopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8989));
        String filename = "1.txt";
        //得到一个文件管道
        FileChannel fileChannel = new FileInputStream(filename).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();
        //在linux下一个transferTo方法就可以完成传输
        //在windows下一次调用transferTo只能发送8m，需要分段传输文件
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总字节数是= "+transferCount+" 总耗时："+(System.currentTimeMillis()-startTime));
    }
}

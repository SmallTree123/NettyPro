package com.nylgsc.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str = "hello,北京欢迎您";
        //创建一个输出流channel
        FileOutputStream fileOutputStream = new FileOutputStream("g:\\file01.txt");

        //通过fileOutputStream获取channel
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        //读写反转
        byteBuffer.flip();
        //将byteBuffer数据写入到channel
        channel.write(byteBuffer);
        fileOutputStream.close();
    }
}

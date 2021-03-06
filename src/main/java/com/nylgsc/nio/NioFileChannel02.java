package com.nylgsc.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel02 {
    public static void main(String[] args) throws Exception {
        File file = new File("g:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道中的数据读取到byteBuffer中
        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array())) ;
        fileInputStream.close();
    }
}

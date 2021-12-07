package com.nylgsc.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NioFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("g:\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("g:\\a2.jpg");

        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel tagetChannel = fileOutputStream.getChannel();
        //文件的拷贝
        tagetChannel.transferFrom(sourceChannel,0,sourceChannel.size());
        tagetChannel.close();
        sourceChannel.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}

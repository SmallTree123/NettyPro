package com.nylgsc.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {
    public static void main(String[] args) throws IOException {

        ExecutorService threadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("Server服务器启动了...");
        while (true){
            final Socket socket = serverSocket.accept();
            System.out.println("有一个新的客户端连接了："+socket.getPort());
            threadPool.execute(new Runnable() {
                public void run() {
                    System.out.println("当前的处理线程是："+Thread.currentThread().getName());
                    handle(socket);
                }
            });
        }

    }

    public static void handle(Socket socket){
        byte[] bytes = new byte[1024];
        InputStream inputStream = null;
        try {
             inputStream = socket.getInputStream();
            while (true){
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

package com.nylgsc.nio.群聊;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8888;

    //构造器，初始化工作
    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //创建ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监听客户端
    public void listen(){
        try {
            //循环处理
            while (true){
                int count = selector.select();
                if (count > 0){ //有事件处理
                    //得到selectorKeys集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    //遍历selectorKeys集合
                    while (keyIterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = keyIterator.next();

                        //监听到accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将该通道注册到selector
                            sc.register(selector,SelectionKey.OP_READ);
                            //客户端上线提示
                            System.out.println(sc.getRemoteAddress()+" 上线了... ");
                        }
                        //通道发生read事件
                        if (key.isReadable()){
                            //处理读事件
                            readDdata(key);
                        }

                    }
                    //把当前key删除
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readDdata(SelectionKey key){
        //取到关联的channel
        SocketChannel socketChannel = null;
        try {
            //得到channel
            socketChannel = (SocketChannel) key.channel();
            socketChannel.configureBlocking(false);
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            //根据read的值做处理
            if (read > 0){
                //把缓存区的数据转换成字符串
                String msg = new String(byteBuffer.array());
                //输出该消息
                System.out.println("from 客户端"+ msg);

                //向其他客户端转发消息，专门写一个方法来处理
                sendInfoToOtherClient(msg,key);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress()+" 离线了..... ");
                //取消注册
                key.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    //转发消息给其他客户端（通道）
    private void sendInfoToOtherClient(String msg,SelectionKey self) throws IOException {
        System.out.println("服务器转发消息中.....");
        //遍历所有注册到Selector上的SocketChannel并排除self
        for (SelectionKey key : selector.keys()) {
            //通过key，取出对应的SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && key != self){
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg转存到buffer
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer中的数据写入到通道
                dest.write(byteBuffer);
            }
        }



    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}

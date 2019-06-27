package com.hyyn.netty.tcp.client;

import java.io.*;
import java.net.Socket;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/6 15:42
 * @Version 1.0
 */
public class Test2 {
    public static void main(String[] args){
        try {
            Socket socket=new Socket("localhost",9000);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter=new PrintWriter(outputStream);
            String jsonParams="1111111";
            printWriter.write(jsonParams+"\0");
            printWriter.flush();
            socket.shutdownOutput();
            InputStream is = socket.getInputStream();
            DataInputStream input = new DataInputStream(is);
            byte[] b = new byte[1024];

            int len = 0;
            String response = "";
            len = input.read(b);
            response = new String(b, 0, len);
//            while(true){
                System.out.println(response);
//            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

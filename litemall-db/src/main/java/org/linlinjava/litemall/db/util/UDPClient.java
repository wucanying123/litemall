package org.linlinjava.litemall.db.util;

import org.linlinjava.litemall.db.util.StringUtilsXD;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.linlinjava.litemall.db.util.Constant.*;


public class UDPClient {
    private static final int TIMEOUT = 5000;  //设置接收数据的超时时间
    private static final int MAXNUM = 5;      //设置重发数据的最多次数

    public static String getCardInfo(String jsonString) {
        String str_send = StringUtilsXD.replaceBlank(jsonString);
        byte[] buf = new byte[1024];
        String str_receive = "";
        try {
            //客户端在8099端口监听接收到的数据
            DatagramSocket ds = new DatagramSocket();
            //定义用来发送数据的DatagramPacket实例
            DatagramPacket dp_send = new DatagramPacket(str_send.getBytes(), str_send.length(), InetAddress.getByName(UDP_HOSTNAME), UDP_PORT);
            //定义用来接收数据的DatagramPacket实例
            DatagramPacket dp_receive = new DatagramPacket(buf, 1024);

            ds.setSoTimeout(TIMEOUT);              //设置接收数据时阻塞的最长时间
            int tries = 0;                         //重发数据的次数
            boolean receivedResponse = false;     //是否接收到数据的标志位
            //直到接收到数据，或者重发次数达到预定值，则退出循环
            while (!receivedResponse && tries < MAXNUM) {
                //发送数据
                ds.send(dp_send);
                try {
                    //接收从服务端发送回来的数据
                    ds.receive(dp_receive);
                    //如果接收到数据。则将receivedResponse标志位改为true，从而退出循环
                    receivedResponse = true;
                } catch (InterruptedIOException e) {
                    //如果接收数据时阻塞超时，重发并减少一次重发的次数
                    tries += 1;
                    System.out.println("Time out," + (MAXNUM - tries) + " more tries...");
                }
            }
            if (receivedResponse) {
                str_receive = new String(dp_receive.getData(), 0, dp_receive.getLength()) +
                        " from " + dp_receive.getAddress().getHostAddress() + ":" + dp_receive.getPort();
                System.out.println(str_receive);    //如果收到数据，则打印出来
                //由于dp_receive在接收了数据之后，其内部消息长度值会变为实际接收的消息的字节数，所以这里要将dp_receive的内部消息长度重新置为1024
                dp_receive.setLength(1024);
            } else {
                //如果重发MAXNUM次数据后，仍未获得服务器发送回来的数据，则打印如下信息
                System.out.println("No response -- give up.");
            }
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str_receive;
    }
}
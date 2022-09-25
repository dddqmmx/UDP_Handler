package com.dd.udp_handler;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPClient extends Thread{

    public int port = 11451;
    public String host;

    public Boolean IsThreadDisable = false;//指示监听线程是否终止
    private static WifiManager.MulticastLock lock;
    InetAddress mInetAddress;
    private Handler handler;

    public void initialization(String host, WifiManager manager, Handler handler){
        this.host = host;
        this.handler = handler;
        lock= manager.createMulticastLock("UDPwifi");
    }

    @Override
    public void run() {
        int port = 11451;
        byte[] message = new byte[100];
        try {
            // 建立Socket连接
            DatagramSocket datagramSocket = new DatagramSocket(port);
            datagramSocket.setBroadcast(true);
            DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
            try {
                while (!IsThreadDisable) {
                    Log.d("UDP Demo", "准备接受");
                    lock.acquire();
                    datagramSocket.receive(datagramPacket);
                    String strMsg=new String(datagramPacket.getData()).trim();
                    Log.d("UDP Demo", datagramPacket.getAddress()
                            .getHostAddress()
                            + ":" +strMsg );
                    /*Thread.sleep(1000);*/

                    Message handlerMessage = Message.obtain(); //消息实例

                    Bundle bundle = new Bundle();//bundle实例

                    bundle.putString("msg",strMsg);//给bundle添加信息
                    handlerMessage.setData(bundle);// 将bundle存入message
                    handlerMessage.what = 1;    //设置标记（自定义）

                    handler.sendMessage(handlerMessage);	//发送消息
                }
            } catch (Exception e) {//IOException
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setIpPort(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command","setIpPort");
            jsonObject.put("port",port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        send(jsonObject);
    }

    public void send(Object object){
        send(object.toString());
    }

    public void send(String string){
        send(string.getBytes(StandardCharsets.UTF_8));
    }

    public void send(byte[] data){
        DatagramSocket socket = null;
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(data, data.length,inetAddress,2077);
            socket = new DatagramSocket();
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}

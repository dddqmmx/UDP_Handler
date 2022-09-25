
package com.dd.udp_handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView12);

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Looper looper = this.getMainLooper();
        handler = new Handler(looper) {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();
                String showText= (String) textView.getText();
                textView.setText(showText+data.getString("msg"));
            }
        };

        UDPClient udpClient = new UDPClient();
        udpClient.initialization("127.0.0.1",wifiManager,handler);
        udpClient.start();

        /*new Thread(()->{

            int number = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message msg = Message.obtain(); // 实例化消息对象
                    msg.what = 1; // 消息标识
                    Bundle bundle = new Bundle();
                    bundle.putString("msg",number+" : sb\n");
                    msg.setData(bundle); // 消息内容存放
                    handler.sendMessage(msg);
                    number++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
}
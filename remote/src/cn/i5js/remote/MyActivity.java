package cn.i5js.remote;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    TextView tv;

    MyReceiver myReceiver = new MyReceiver();
    float mLastSize = 100.0f;
    String info = "info";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = (TextView)findViewById(R.id.tv);

        SharedPreferences prefs = getSharedPreferences(info, MODE_PRIVATE);
        mLastSize = prefs.getFloat("size",mLastSize);

        tv.setTextSize(mLastSize);

        Intent intent = new Intent(this,MyService.class);
        startService(intent);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String ip = getIP();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("ip:" + ip);
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyHttpServer.ACTION_CONTENT);
        filter.addAction(MyHttpServer.ACTION_SIZE);
        registerReceiver(myReceiver,filter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MyHttpServer.ACTION_CONTENT.equals(action)){
                String str = intent.getStringExtra("content");
                if(!TextUtils.isEmpty(str)){
                    tv.setText(str);
                }
            }else if(MyHttpServer.ACTION_SIZE.equals(action)){
                float tmp = mLastSize;
                int size = intent.getIntExtra("size",0);
                Log.d("aaa" ,"text size=" + tmp + ",size=" + size);
                tmp +=size;
                tmp = safe(tmp);
                mLastSize = tmp;
                saveLastSize();
                tv.setTextSize(tmp);
            }

        }
    }
    void saveLastSize(){
        SharedPreferences prefs = getSharedPreferences(info, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat("size",mLastSize);
        editor.commit();
    }
    float safe(float size){
        Log.d("safe",size+"");
        float max = 500.0f;
        float min = 30.0f;

        if(size>max){
            return max;
        }
        if(size<min){
            return  min;
        }
        return size;
    }
    private static String getIP() {
        String ipstr = "";
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements())
            {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
                        .nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        String s = ip.getHostAddress();
                        if (!s.equals("127.0.0.1")) {
                            ipstr = s;
                            break;
                        }
                    }
                }
                if(ipstr.length()>0){
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipstr;
    }
}

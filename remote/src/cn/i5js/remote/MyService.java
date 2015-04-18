package cn.i5js.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by nid on 2015/4/3.
 */
public class MyService extends Service {
    MyHttpServer server;
    @Override
    public void onCreate() {
        super.onCreate();
        server = new MyHttpServer(this);
        server.run();
    }

    @Override
    public void onDestroy() {
        server.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}

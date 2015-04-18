package cn.i5js.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;
import android.util.Log;
/**
 * Created by nid on 2015/4/4.
 */
public class Util {
    static  final String tag = "Util";
    public static String ip="";
    public static void asyncSend(String content,String ip){
        try {
            content = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format("http://%s:8567/msg?content=%s",ip,content);
        AsyncTask<String,Void,Void> task = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                Util.httpSend(params[0]);
                return null;
            }
        };
        task.execute(url);
    }
    public static void asyncSendMove(float change){
        int size = (int)change/50;
        String url = String.format("http://%s:8567/msg?size=%s",ip,String.valueOf(size));
        AsyncTask<String,Void,Void> task = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                Util.httpSend(params[0]);
                return null;
            }
        };
        task.execute(url);
    }
    public static void httpSend(String strUrl) {
        try {
            Log.d(tag, "send url:" + strUrl);
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Charset", "UTF-8");
            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);
            con.setDoInput(true);
            con.connect();
            if (con.getResponseCode() != 200){
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
            String line  = null;
            StringBuilder sb = new StringBuilder();
            sb.append("content:");

            while( (line = reader.readLine())!=null ){
                sb.append(line);
                sb.append("\n");
            }
            Log.d(tag, sb.toString());


            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

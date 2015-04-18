package cn.i5js.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.util.Log;
import fi.iki.elonen.NanoHTTPD;

public class MyHttpServer extends NanoHTTPD implements Runnable {
    public static String ACTION_CONTENT = "ACTION_CONTENT";
    public static String ACTION_SIZE = "ACTION_SIZE";
    static String tag =  "http";
	 /**
     * Constructs an HTTP server on given port.
     */
     Context mContext;
    public MyHttpServer(Context context) {
        super(8567);
        mContext = context;
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        StringBuilder sb = new StringBuilder();
        
        if("/msg".equals(uri)){
            String content = "";
            try {
                if(parms.get("content")!=null){
                    content = new String(parms.get("content").getBytes("iso-8859-1"),"utf-8");
                    Intent intent = new Intent(ACTION_CONTENT);
                    intent.putExtra("content", content);
                    mContext.sendBroadcast(intent);
                    sb.append(content);
                }
                if(parms.get("size")!=null){
                    int size  = Integer.parseInt(parms.get("size"));
                    Intent intent = new Intent(ACTION_SIZE);
                    intent.putExtra("size", size);

                    mContext.sendBroadcast(intent);
                    sb.append(size);
                    Log.d("size",size+"");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }else{
        	sb.append(uri);
        }
        return new Response(sb.toString());
    }

	@Override
	public void run() {
		try {
			Log.d(tag, "start");
			start();
		} catch (IOException e) {
			Log.d(tag, "error" + e.toString());
			e.printStackTrace();
		}
	}

	

}

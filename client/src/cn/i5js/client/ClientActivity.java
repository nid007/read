package cn.i5js.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ClientActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    EditText etIp;
    EditText etContent;
    final  String info = "info";
    final  String ip = "ip";
    final String ipDefault = "请输入接收端IP";
    View btnHis;
    GestureDetector mDetector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        etIp = (EditText)findViewById(R.id.et_ip);
        etContent = (EditText)findViewById(R.id.et_content);
        btnHis = findViewById(R.id.btn_his);

        btnHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences(info, MODE_PRIVATE);
        String val = prefs.getString(ip,ipDefault);
        Util.ip = val;
        etIp.setText(val);

        etContent.addTextChangedListener(textWatcher);
        mDetector = new GestureDetector(this, new GuestureListener());
        etContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return false;
            }
        });
        etIp.setOnFocusChangeListener(focusChangeListener);
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                SharedPreferences prefs = getSharedPreferences(info, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Util.ip = etIp.getText().toString();
                editor.putString(ip,Util.ip);
                editor.commit();
            }else{
                if(ipDefault.equals(etIp.getText().toString())){
                    etIp.setText("");
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String content = etContent.getText().toString();
            String ip = etIp.getText().toString();
            Util.asyncSend(content,ip);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    class GuestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float xSpan = Math.abs(e2.getX()-e1.getX());
            float ySpan = Math.abs(e1.getY()-e2.getY());
            if(xSpan>ySpan){
                if (e2.getX() - e1.getX() > 100) {
                    saveAndClear();
                }
            }else{
                if(ySpan>100){
                    Util.asyncSendMove(e1.getY() - e2.getY());
                }
            }



            return false;
        }
    }

    void saveAndClear(){
        String val = etContent.getText().toString();
        if(!TextUtils.isEmpty(val)){
            DBService ser = new DBService(this);
            ser.save(val);
        }
        etContent.setText("");
    }


}

package cn.i5js.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by nid on 2015/4/4.
 */
public class HistoryActivity  extends Activity{

    ListView mListView;
    HistoryAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his);
        mListView = (ListView)findViewById(R.id.lv_his);
        mAdapter = new HistoryAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HisItem item = (HisItem) mAdapter.getItem(position);
                Util.asyncSend(item.msg,Util.ip);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }

    private void loadData() {
        final DBService dbService = new DBService(this);
        new AsyncTask<Void,Void,List<HisItem>>(){
            @Override
            protected List<HisItem> doInBackground(Void... params) {
                return dbService.getDatas();
            }

            @Override
            protected void onPostExecute(List<HisItem> list) {
                super.onPostExecute(list);
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}

package cn.i5js.client;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nid on 2015/4/4.
 */
public class HistoryAdapter  extends BaseAdapter {
    private LayoutInflater inflater;
    List<HisItem> list = new ArrayList<HisItem>();


    public HistoryAdapter(Context context) {
        inflater = LayoutInflater.from(context);

    }
    public void setData(List<HisItem> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.his_item, parent, false);
            holder = new ViewHolder();

            holder.tv_msg = (TextView)view.findViewById(R.id.tv_item_msg);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        HisItem tmp = (HisItem) getItem(position);
        if (tmp != null) {
           holder.tv_msg.setText(tmp.msg);
        }
        return view;

    }

    static class ViewHolder{
        TextView tv_msg;

    }

}

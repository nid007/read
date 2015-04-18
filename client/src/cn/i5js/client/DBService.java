package cn.i5js.client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nid on 2015/4/4.
 */
class DBService extends SQLiteOpenHelper {
    private static final String DB_NAME = "client";
    private static final int DB_VERSION = 1;

    private static int maxCount =50;

    public DBService(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE [his] ([msg] varchar(600))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void save(String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("msg",value);
        db.insert("his",null,values);
    }

    public List<HisItem> getDatas(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<HisItem> list = new ArrayList<HisItem>();
        long minID =0;
        Cursor cursor =db.query("his",new String[]{"rowid","msg"},null,null,null,null,"rowid desc");
        int cnt = 0;
        if (cursor != null) {
            cnt = cursor.getCount();
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("rowid"));
                String msg = cursor.getString(cursor.getColumnIndexOrThrow("msg"));
                HisItem item = new HisItem();
                item.msg = msg;
                item.id = id;
                if(list.size()<maxCount) {
                    minID = id;
                    list.add(item);
                }else{
                    break;
                }
            }
            cursor.close();
        }
        if(cnt>maxCount){
            SQLiteDatabase dbDel = this.getWritableDatabase();
            dbDel.delete("his","rowid<?",new String[]{String.valueOf(minID)});
        }
        return list;
    }

}

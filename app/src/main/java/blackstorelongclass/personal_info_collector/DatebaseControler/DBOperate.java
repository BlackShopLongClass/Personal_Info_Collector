package blackstorelongclass.personal_info_collector.DatebaseControler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.util.ArrayList;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import blackstorelongclass.personal_info_collector.listMonitor.*;
/**
 * Created by ycd on 2017/11/11.
 */

public class DBOperate extends AppCompatActivity {

    public boolean create_newTable(String SQL_create, String SQL_createConfig) {
        DBHelper DBH;
        DBH=new DBHelper(this);
        SQLiteDatabase db=DBH.getWritableDatabase();
        try{
            db.execSQL(SQL_createConfig);
            db.execSQL(SQL_create);
        }catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return true;
    }
    public boolean insert_newItem(String SQL_insert) {
        DBHelper DBH;
        DBH=new DBHelper(this);
        SQLiteDatabase db=DBH.getReadableDatabase();
        try{
            db.execSQL(SQL_insert);
        }catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return true;
    }
    public ArrayList<String> get_tableNames() {
        DBHelper DBH;
        DBH=new DBHelper(this);
        SQLiteDatabase db=DBH.getWritableDatabase();
        ArrayList<String> tableNames=new ArrayList<String>();
        Cursor cursor;
        String SQL_search="SELECT name FROM sqlite_master WHERE type='table' order by name;";
        cursor=db.rawQuery(SQL_search,null);
        int i=0;
        while (cursor.moveToNext()) {
            i++;
            if (i>3) {
                tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        }
        cursor.close();
        db.close();
        return tableNames;
    }
    public userList get_allItems(String listName) {
        userList info=new userList(listName);
        String SQL_searchConfig="SELECT tagType FROM Config WHERE listName='"+listName+"';";
        String tagType;
        DBHelper DBH;
        DBH=new DBHelper(this);
        SQLiteDatabase db=DBH.getReadableDatabase();
        Cursor cursor;
        cursor=db.rawQuery(SQL_searchConfig,null);
        tagType=cursor.getString(2);
        int size=tagType.length();
        int i;
        String tag;
        Object content;
        for (i=0;i<size;i++)
        {
            
        }

    }
}

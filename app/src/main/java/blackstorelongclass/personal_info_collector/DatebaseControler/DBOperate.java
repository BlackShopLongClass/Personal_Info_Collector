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

    public boolean create_newTable(String SQL_create) {
        File file = new File(getApplication().getDatabasePath("User.db").getPath());
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file,null);
        try{
            db.execSQL(SQL_create);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean insert_newItem(String SQL_insert) {
        File file = new File(getApplication().getDatabasePath("User.db").getPath());
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file,null);
        try{
            db.execSQL(SQL_insert);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public ArrayList<String> get_tableNames() {
        File file = new File(getApplication().getDatabasePath("User.db").getPath());
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file,null);
        ArrayList<String> tableNames=new ArrayList<String>();
        Cursor cursor;
        String SQL_search="SELECT name FROM sqlite_master WHERE type=’table’ ORDER BY name;";
        cursor=db.rawQuery(SQL_search,null);
        while (cursor.moveToNext()) {
            tableNames.add((String)cursor.getString(1));//获取第二列的值
        }
        cursor.close();
        db.close();
        return tableNames;
    }
}

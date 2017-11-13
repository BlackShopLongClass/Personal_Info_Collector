package blackstorelongclass.personal_info_collector.DatebaseControler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import blackstorelongclass.personal_info_collector.listMonitor.*;
/**
 * Created by ycd on 2017/11/11.
 */

public class DBOperate extends AppCompatActivity {
    private SQLiteDatabase db;
    public DBOperate() {
        File file = new File("/data/data/blackstorelongclass.personal_info_collector/databases/User.db");
        this.db = SQLiteDatabase.openOrCreateDatabase(file,null);
    }
    public void db_close() {
        this.db.close();
    }
    public boolean create_newTable(String SQL_create, String SQL_createConfig) {
        /*DBHelper DBH;
        DBH=new DBHelper(this);*/
        //SQLiteDatabase db=DBH.getWritableDatabase();
        try{
            this.db.execSQL(SQL_createConfig);
            this.db.execSQL(SQL_create);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean insert_newItem(String SQL_insert) {
        try{
            this.db.execSQL(SQL_insert);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public ArrayList<String> get_tableNames() {
        ArrayList<String> tableNames=new ArrayList<String>();
        Cursor cursor;
        String SQL_search="SELECT name FROM sqlite_master WHERE type='table' order by name;";
        cursor=this.db.rawQuery(SQL_search,null);
        int i=0;
        while (cursor.moveToNext()) {
            i++;
            if (i>3) {
                tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        }
        cursor.close();
        return tableNames;
    }
    public ArrayList<String> get_tagNames(String listName) {
        String SQL_getTagName="pragma table_info("+listName+");";
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_getTagName,null);
        ArrayList<String> tagNames=new ArrayList<String>();
        int i=0;
        while (cursor.moveToNext()) {
            tagNames.add(cursor.getColumnName(i++));
        }
        return tagNames;
    }
    public String get_tagTypes(String listName) {
        String SQL_searchConfig="SELECT tagType FROM Config WHERE listName='"+listName+"';";
        String tagTypes;
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        tagTypes=cursor.getString(2);
        return tagTypes;
    }
    public ArrayList<userList> get_allItems(String listName) {
        ArrayList<userList> info=new ArrayList<userList>();
        String SQL_searchConfig="SELECT tagType FROM Config WHERE listName='"+listName+"';";
        String SQL_getItems="SELECT * FROM "+listName+";";
        String SQL_getTagName="pragma table_info("+listName+");";
        String tagType;
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        tagType=cursor.getString(2);
        cursor=this.db.rawQuery(SQL_getTagName,null);
        Vector<String> tagName=new Vector<String>();
        int i=0;
        while (cursor.moveToNext()) {
            tagName.addElement(cursor.getColumnName(i++));
        }
        int size=tagType.length();
        String tag;
        Object content;
        cursor=this.db.rawQuery(SQL_getItems,null);
        while (cursor.moveToNext()) {
            userList temp1=new userList(listName);
            for (i=0;i<size;i++) {
                userTag temp2;
                tag=tagName.elementAt(i);
                if (tagType.charAt(i)=='1')
                {
                    content=(double)cursor.getDouble(i);
                }
                else if (tagType.charAt(i)=='2')
                {
                    content=(int)cursor.getInt(i);
                }
                else if (tagType.charAt(i)=='3')
                {
                    content=(String)cursor.getString(i);
                }
                else
                {
                    content=null;
                }
                temp2=new userTag(tag,content);
                temp1.addTag(listName,temp2);
            }
            info.add(temp1);
        }
        cursor.close();
        return info;
    }
}

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
import android.util.Log;

import blackstorelongclass.personal_info_collector.listMonitor.*;

import static android.support.v7.appcompat.R.id.info;

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
            Log.i("bslc","bslc_DBOperate_create_newTable(): create fail!");
            e.printStackTrace();
        }
        Log.i("bslc","bslc_DBOperate_create_newTable(): create success!");
        return true;
    }
    public boolean insert_newItem(String SQL_insert) {
        try{
            this.db.execSQL(SQL_insert);
        }catch(Exception e){
            Log.i("bslc","bslc_DBOperate_insert_newItem(): insert fail!");
            e.printStackTrace();
        }
        Log.i("bslc","bslc_DBOperate_insert_newItem(): insert success!");
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
        Log.i("bslc","bslc_DBOperate_get_tableNames():tableNames="+tableNames);
        return tableNames;
    }
    public ArrayList<String> get_tagNames(String listName) {
        //String SQL_getTagName="pragma table_info("+listName+");";
        String SQL_getTagName="SELECT * FROM "+listName+";";
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_getTagName,null);
        ArrayList<String> tagNames=new ArrayList<String>();
        int i=0;
        cursor.moveToNext();
        for (i=1;i<cursor.getColumnCount();i++) {
            String currentTagName = cursor.getColumnName(i);
            Log.i("bslc","bslc_DBOperate_get_tagNames():currentTagName="+currentTagName);
            tagNames.add(currentTagName);
        }
        cursor.close();
        return tagNames;
    }
    public String get_tagTypes(String listName) {
        String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listName+"';";
        String tagTypes;
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        cursor.moveToNext();
        tagTypes=cursor.getString(2);
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_tagTypes();tagTypes="+tagTypes);
        return tagTypes;
    }
    public ArrayList<userList> get_allItems(String listName) {
        ArrayList<userList> info=new ArrayList<userList>();
        Log.i("bslc","bslc_DBOperate_get_allItems():listName="+listName);
        String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listName+"';";
        String SQL_getTagName="SELECT * FROM "+listName+";";
        String SQL_getItems="SELECT * FROM "+listName+";";
        String tagType;
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        cursor.moveToNext();
        tagType=cursor.getString(2);
        Log.i("bslc","bslc_DBOperate_get_allItems():tagType="+tagType);
        cursor=this.db.rawQuery(SQL_getTagName,null);
        Vector<String> tagName=new Vector<String>();
        int i=0;
        cursor.moveToNext();
        for (i=1;i<cursor.getColumnCount();i++) {
            String currentTagName = cursor.getColumnName(i);
            Log.i("bslc","bslc_DBOperate_get_allItems():added currentTagName:"+currentTagName);
            tagName.add(currentTagName);
        }
        int size=tagType.length();
        cursor=this.db.rawQuery(SQL_getItems,null);
        while (cursor.moveToNext()) {
            userList temp1=new userList(listName);
            for (i=0;i<size;i++) {
                String tag;
                Object content;
                userTag temp2;
                tag=tagName.elementAt(i);
                if (tagType.charAt(i)=='1')
                {
                    content=(double)cursor.getInt(i+1);
                }
                else if (tagType.charAt(i)=='2')
                {
                    content=cursor.getLong(i+1);
                }
                else if (tagType.charAt(i)=='3')
                {
                    content=(String)cursor.getString(i+1);
                }
                else
                {
                    content=null;
                }
                temp2=new userTag(tag,content);
                temp1.addTag(tag,temp2);
            }
            info.add(temp1);
        }
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_allItems(): finish!");
        return info;
    }
    public userList get_specificItem(String SQL_searchItem,String listName) {
        String SQL_searchConfig="SELECT * FROM Config WHERE listName='"+listName+"';";
        String SQL_getTagName="SELECT * FROM "+listName+";";
        Log.i("bslc","bslc_DBOperate_get_specificItem():listName:"+listName);
        int i=0,size=0;
        String tagType;
        Vector<String> tagNames=new Vector<String>();
        Cursor cursor;
        cursor=this.db.rawQuery(SQL_searchConfig,null);
        cursor.moveToNext();
        tagType=cursor.getString(2);
        Log.i("bslc","bslc_DBOperate_get_specificItem():tagType="+tagType);
        userList specificItem;
        cursor=this.db.rawQuery(SQL_getTagName,null);
        cursor.moveToNext();
        for (i=1;i<cursor.getColumnCount();i++) {
            tagNames.add(cursor.getColumnName(i));
        }
        size=tagType.length();
        cursor=this.db.rawQuery(SQL_searchItem,null);
        //cursor.moveToNext();
        specificItem=new userList(listName);
        while (cursor.moveToNext()) {
            for (i=0;i<size;i++) {
                userTag temp;
                String tag;
                Object content;
                tag=tagNames.elementAt(i);
                if (tagType.charAt(i)=='1')
                {
                    content=cursor.getDouble(i+1);
                    Log.i("bslc","bslc_DBOperate_get_specifigItem():tagType=double;content="+content);
                }
                else if (tagType.charAt(i)=='2')
                {
                    content=cursor.getLong(i+1);
                    Log.i("bslc","bslc_DBOperate_get_specifigItem():tagType=time;content="+content);
                }
                else if (tagType.charAt(i)=='3')
                {
                    content=(String)cursor.getString(i+1);
                    Log.i("bslc","bslc_DBOperate_get_specificItem():tagType=string;content="+content);
                }
                else
                {
                    Log.i("bslc_DBOperate","bslc_DBOperate_get_specificItem():can't figure out data type");
                    content=null;
                }
                temp=new userTag(tag,content);
                specificItem.addTag(tag,temp);
            }
        }
        cursor.close();
        Log.i("bslc","bslc_DBOperate_get_specificItem():finish!");
        return specificItem;
    }
}

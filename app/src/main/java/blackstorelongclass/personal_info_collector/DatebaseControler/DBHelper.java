package blackstorelongclass.personal_info_collector.DatebaseControler;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.util.ArrayList;

import blackstorelongclass.personal_info_collector.listMonitor.*;
/**
 * Created by ycd on 2017/11/11.
 */

public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    //每次你对数据表进行编辑，添加时候，你都需要对数据库的版本进行提升

    //数据库版本
    private static final int DATABASE_VERSION=1;

    //数据库名称
    private static final String DATABASE_NAME="User.db";

    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        String SQL_creatConfig="CREATE TABLE Config(ID INTEGER PRIMARY KEY AUTOINCREMENT, listName TEXT, tagType TEXT);";
        db.execSQL(SQL_creatConfig);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ;
    }
}

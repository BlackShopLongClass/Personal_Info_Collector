package blackstorelongclass.personal_info_collector.DataHandler;
import blackstorelongclass.personal_info_collector.DatebaseControler.DBHelper;
import blackstorelongclass.personal_info_collector.DatebaseControler.DBOperate;
import blackstorelongclass.personal_info_collector.listMonitor.*;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.security.Timestamp;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abc123one on 2017/11/11.
 */

public class listHandler extends AppCompatActivity{
    String name;
    ArrayList<String> tableList;

    public listHandler(String Name){
        name = Name;
        DBOperate DBO = new DBOperate();
        tableList = DBO.get_tableNames();

    }

    /**
     * 添加表单名称
     * @param table
     * 表单名称
     * @return
     * 成功与否
     */
    public boolean addTable(String table){
        return tableList.add(table);
    }

    /**
     * 获取所有表单名称
     * @return
     * ArrayList类型的数组
     */
    public ArrayList<String> getTableList(){
        Calendar time = Calendar.getInstance();
        Class<?> c = time.getClass();
        return tableList;
    }

    public String getUserName(){
        return name;
    }

    /**
     * 向数据库中添加新表单
     * @param List
     * 用户输入的数据类型
     * @return
     * 添加的成功与否
     */
    public boolean addNewList(userList List){
        int number = List.getListSize();
        String sentence = "CREATE TABLE "+ List.getListTitle() + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,";
        ArrayList<String> titleSet = List.getTitleList();
        String config = "INSERT INTO Config (listName,tagType) VALUES ('" +List.getListTitle() + "','";
        for(int i=0 ; i<number; i++){
            userTag t = List.getTag(titleSet.get(i));
            if(t.isDouble()) {
                sentence = sentence + t.getTitle() + " REAL";
                config += "1";
            }
            else if(t.isCalendar()) {
                sentence = sentence + t.getTitle() + " INTEGER";
                config += "2";
            }
            else if(t.isStr()) {
                sentence = sentence + t.getTitle() + " TEXT";
                config += "3";
            }
            if(i+1<number) sentence += ",";
        }
        sentence += ");";
        config += "');";
        DBOperate DBO=new DBOperate();
        return DBO.create_newTable(sentence,config);
    }

    /**
     * 向数据库中的某一个表单添加一项
     * @param List
     * 包含数据的内容的userList
     * @return
     * 添加的成功与否
     */
    public boolean addNewData(userList List){
        int number = List.getListSize();
        ArrayList<String> titleSet = List.getTitleList();
        String sentence = "INSERT INTO "+ List.getListTitle() +" (";
        for(int i=0; i<List.getListSize();i++){
            userTag t = List.getTag(titleSet.get(i));
            if(i>0) sentence += ",";
            sentence += t.getTitle();
        }
        sentence += ") VALUES (";
        for (int i=0; i<List.getListSize();i++){
            if(i>0) sentence += " , ";
            userTag t = List.getTag(titleSet.get(i));
            //if(t.isStr()) sentence = sentence + "'" + (String)t.getObject() + "'";
            if(t.isCalendar())
                sentence = sentence + ((Calendar)t.getObject()).getTimeInMillis();
            else if(t.isDouble())
                sentence = sentence + (double)t.getObject();
            else
                sentence = sentence + "'" + (String)t.getObject() + "'";
        }
        sentence += ");";
        DBOperate DBO=new DBOperate();
        return DBO.insert_newItem(sentence);
    }

    /**
     * 获取某一个表单的数据类型
     * @param tableName
     * 表单名称
     * @return
     * 包含数据类型的userList
     */
    public userList getDataType(String tableName){
        DBOperate DBO = new DBOperate();
        ArrayList<String> tableStr= DBO.get_tagNames(tableName);
        String tableType = DBO.get_tagTypes(tableName);

        userList u = new userList(tableName);
        for(int i=0;i<tableStr.size();i++) {
            Class<?> type = java.lang.String.class;
            switch (tableType.charAt(i)){
                case '1': type = java.lang.Double.class;
                    break;
                case '2': type = java.util.GregorianCalendar.class;
                    break;
                case '3': type = java.lang.String.class;
                    break;
            }
            userTag tag = new userTag(tableStr.get(i),type);
            u.addTag(tableStr.get(i),tag);
        }
        return u;
    }

    public ArrayList<userList> getTableAllData(String table){
        DBOperate DBO= new DBOperate();
        return DBO.get_allItems(table);
    }

    /**
     * 查询一个表的某一项
     * @param table
     * 表名称
     * @param time
     * 字符串类型表示的时间
     * @return
     *
     */
    public userList getATableData(String table,String time) throws ParseException {
        DBOperate DBO = new DBOperate();
        ArrayList<String> titles = DBO.get_tagNames(table);
        String types = DBO.get_tagTypes(table);
        String resultString = "";
        for(int i=0;i<titles.size();i++){
            if(types.charAt(i) == '2') {
                resultString = titles.get(i);
                break;
            }
        }
        String sentence = "SELECT * FROM " + table + " WHERE " + resultString + "=" + timeStr2Long(time) + ";";
        return DBO.get_specificItem(sentence,table);
    }


    private long timeStr2Long(String timeStr) throws ParseException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= sdf.parse(timeStr);
        return date.getTime();
    }

}

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
import android.util.Pair;
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
        Log.i("bslc","bslc_listHandler_listHandler():name="+name);
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
        Log.i("bslc","bslc_listHandler_addNewList():sentence_before="+sentence);
        ArrayList<String> titleSet = List.getTitleList();
        String config = "INSERT INTO Config (listName,tagType) VALUES ('" +List.getListTitle() + "','";
        Log.i("bslc","bslc_listHandler_addNewList():config_before="+config);
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
        Log.i("bslc","bslc_listHandler_addNewList():config_after="+config);
        Log.i("bslc","bslc_listHandler_addNewList():sentence_after="+sentence);
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
        Log.i("bslc","bslc_listHandler_addNewData():sentence_before="+sentence);
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
        Log.i("bslc","bslc_listHandler_addNewData():sentence_after="+sentence);
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
        Log.i("bslc_listHandler","getDataType():Start get table name="+tableName);
        DBOperate DBO = new DBOperate();
        ArrayList<String> tableStr= DBO.get_tagNames(tableName);
        String tagType = DBO.get_tagTypes(tableName);
        Log.i("bslc","bslc_listHandler_getDataType():types of each tag="+tagType+"(1 for num;2 for date; 3 for word.)");
        userList u = new userList(tableName);
        for(int i=0;i<tableStr.size();i++) {
            Class<?> type = java.lang.String.class;
            switch (tagType.charAt(i)){
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
     * 字符串类型表示的时间,形如:"yyyy-MM-dd HH:mm:ss"
     * @return
     * 所得到的表单
     */
    public userList getATableData(String table,String time) throws ParseException {
        DBOperate DBO = new DBOperate();
        ArrayList<String> titles = DBO.get_tagNames(table);
        String tagTypes = DBO.get_tagTypes(table);
        Log.i("bslc","bslc_listHandler_getATableData():tagType="+tagTypes+"(1 for num;2 for date; 3 for word.)");
        String resultString = "";
        for(int i=0;i<titles.size();i++){
            if(tagTypes.charAt(i) == '2') {
                resultString = titles.get(i);
                break;
            }
        }
        String sentence = "SELECT * FROM " + table + " WHERE " + resultString + "=" + timeStr2Long(time) + ";";
        Log.i("bslc","bslc_listHandler_getATableData():sentence="+sentence);
        return DBO.get_specificItem(sentence,table);
    }

    /**
     * 查询一个表单的一个时间点是否可用
     * @param table
     * 表单名称
     * @param time
     * 字符串类型的时间,形如:"yyyy-MM-dd HH:mm:ss"
     * @return
     * true:该时间可以使用
     * false:该时间不能使用
     * @throws ParseException
     */
    public boolean checkTimeAvaliable(String table, String time)throws ParseException{
        userList userlist = getATableData(table,time);
        ArrayList<String> arr = userlist.getTitleList();
        return arr.size()==0 ? true : false;
    }


    public long timeStr2Long(String timeStr) throws ParseException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= sdf.parse(timeStr);
        return date.getTime();
    }

    /**
     * 查询所有表单的时间-标题键值对,用于展示时间轴时使用
     * @return
     * ArraryList<Pair>.其中Pair<Long,String>
     */
    public ArrayList<Pair> getTimeWithTitle(){
        DBOperate DBO = new DBOperate();
        ArrayList<ArrayList<Pair<Long,String>>> allTableArrary = new ArrayList<>();
        for(String title:tableList){
            ArrayList<userList> userlist = DBO.get_allItems(title);
            ArrayList<Pair<Long,String>> currentTable = new ArrayList<>();
            for(userList u:userlist){
                Pair<Long,String> p = new Pair<>(u.getTime(),u.getListTitle());
                currentTable.add(p);
            }
            allTableArrary.add(currentTable);
        }
        ArrayList<Pair> resultList = new ArrayList<>();

        while (true){
            int numOfTable = allTableArrary.size();
            int flag = 0;
            for(int i=1;i<numOfTable;i++){
                if(allTableArrary.get(i).get(0).first<allTableArrary.get(flag).get(0).first)
                    flag = i;
            }
            resultList.add(allTableArrary.get(flag).get(0));
            allTableArrary.get(flag).remove(0);
            if(allTableArrary.get(flag).isEmpty()){
                allTableArrary.remove(flag);
                if(allTableArrary.isEmpty())
                    break;
            }
        }
        return resultList;
    }
}

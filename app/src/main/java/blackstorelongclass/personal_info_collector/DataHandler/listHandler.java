package blackstorelongclass.personal_info_collector.DataHandler;
import blackstorelongclass.personal_info_collector.listMonitor.*;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abc123one on 2017/11/11.
 */

public class listHandler {
    String name;
    ArrayList<String> tableList;

    public listHandler(String Name){
        name = Name;
        tableList = new ArrayList<String>();
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
            else if(t.isGregorianCalendar()) {
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

        Log.e("t_addNewList",sentence);
        return true;
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
            if(t.isGregorianCalendar())
                sentence = sentence + ((GregorianCalendar)t.getObject()).getTime().getTime();
            else if(t.isDouble())
                sentence = sentence + (double)t.getObject();
            else
                sentence = sentence + "'" + (String)t.getObject() + "'";
        }
        sentence += ");";
        Log.e("t_addNewData",sentence);
        return true;
    }

    /**
     * 获取某一个表单的数据类型
     * @param tableName
     * 表单名称
     * @return
     * 包含数据类型的userList
     */
    public userList getDataType(String tableName){
        userList u= new userList("anything");
        return u;

    }

    

}

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
    public boolean addNewList(userList List){
        int number = List.getListSize();
        String sentence = "CREATE TABLE "+ List.getListTitle() + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,";
        ArrayList<String> titleSet = List.getTitleList();

        for(int i=0 ; i<number; i++){
            userTag t = List.getTag(titleSet.get(i));
            if(t.isDouble())
                sentence = sentence + t.getTitle() + " REAL,";
            else if(t.isGregorianCalendar())
                sentence = sentence + t.getTitle() + " INTEGER,";
            else if(t.isStr())
                sentence = sentence + t.getTitle() + " TEXT,";
        }
        sentence += ");";
        Log.e("t_addNewList",sentence);
        return true;
    }

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
            if(t.isStr()) sentence = sentence + "'" + (String)t.getObject() + "'";
            else if(t.isGregorianCalendar())
                sentence = sentence + ((GregorianCalendar)t.getObject()).getTime().getTime();
            else if(t.isDouble())
                sentence = sentence + (double)t.getObject();
        }
        sentence += ");";
        Log.e("t_addNewData",sentence);
        return true;
    }
}

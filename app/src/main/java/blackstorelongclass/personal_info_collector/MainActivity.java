package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;
import blackstorelongclass.personal_info_collector.DatebaseControler.DBHelper;
import blackstorelongclass.personal_info_collector.listMonitor.userList;
import blackstorelongclass.personal_info_collector.listMonitor.userTag;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view) throws IllegalAccessException, InstantiationException {
//        Intent intent = new Intent(this,MainActivity.class);
//        String result = "output";
//
//        intent.putExtra(Output,result);
//        startActivity(intent);
        TextView textView = new TextView(this);
        GregorianCalendar calendar1 = new GregorianCalendar(2017, 02, 23, 18, 29, 51);
        GregorianCalendar calendar2 = new GregorianCalendar(2017, 02, 23, 18, 31, 58);
        Date eclipse = calendar1.getTime();
        String result = "" + eclipse.getTime();
        double a = 11;
        userTag u1 = new userTag("number", a);
        userTag u2 = new userTag("time", calendar2);
        userTag u3 = new userTag("strrr", "it is a place");
        userList list = new userList("test");
        list.addTag("number", u1);
        list.addTag("time", u2);
        list.addTag("strrr", u3);
        listHandler handler = new listHandler("333");
        handler.addNewData(list);
        handler.addNewList(list);
    }

    public void testforeditlist(View view){
        Intent intent = new Intent(this,DynamicAddViewActivity.class);
        startActivity(intent);
    }

    public void creat_DB(View view) throws IllegalAccessException, InstantiationException {
        TextView textView = new TextView(this);
        Calendar calendar = new GregorianCalendar(2017,02,23,18,29,51);
        String result = "";
        long a = 11;
        userTag u = new userTag("number",a);
        userList list = new userList("test");
        list.addTag("number",u);
        result = result+u.getClassType()+""+u.getObject();
        textView.setText(result);
        DBHelper DBH;
        DBH=new DBHelper(this);
        DBH.getReadableDatabase();
    }
    public void add_newtable(View view) throws IllegalAccessException, InstantiationException {
        TextView textView = new TextView(this);
        Calendar calendar = new GregorianCalendar(2017,02,23,18,29,51);
        String result = "";
        long a = 11;
        userTag u = new userTag("number",a);
        userList list = new userList("test");
        list.addTag("number",u);
        result = result+u.getClassType()+""+u.getObject();
        textView.setText(result);
        //
        DBHelper DBH;
        DBH=new DBHelper(this);
        SQLiteDatabase db=DBH.getReadableDatabase();
        ArrayList<String> tableNames=new ArrayList<String>();
        Cursor cursor;
        String SQL_search="SELECT name FROM sqlite_master WHERE type='table' order by name;";
        cursor=db.rawQuery(SQL_search,null);
        int i=0;
        while (cursor.moveToNext()) {
            i++;
            if (i>2) {
                tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        }
        cursor.close();
        db.close();

    }
}

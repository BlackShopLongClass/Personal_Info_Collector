package blackstorelongclass.personal_info_collector;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import blackstorelongclass.personal_info_collector.DatebaseControler.DBHelper;
import blackstorelongclass.personal_info_collector.listMonitor.userList;
import blackstorelongclass.personal_info_collector.listMonitor.userTag;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addEditText(View view) {
        // Do something in response to button
        EditText editText = new EditText(this);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.main);
        //layout.addView(editText);
    }

    public void login_test(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void income(View view){
        Intent intent = new Intent(this,Income.class);
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

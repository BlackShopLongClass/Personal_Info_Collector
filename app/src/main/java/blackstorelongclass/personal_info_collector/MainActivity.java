package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.content.SharedPreferences;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import blackstorelongclass.personal_info_collector.listMonitor.userList;
import blackstorelongclass.personal_info_collector.listMonitor.userTag;
import blackstorelongclass.personal_info_collector.DataHandler.listHandler;

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
//        TextView textView = new TextView(this);
//
//
//        String result = "" + eclipse.getTime();
//        double a = 11;
//        userTag u1 = new userTag("number", a);
//        userTag u3 = new userTag("strrr", "it is a place");
//        userList list = new userList("test");
//        list.addTag("strrr", u3);
//        listHandler handler = new listHandler("uuu");
//        handler.addNewData(list);
//        handler.addNewList(list);
    }

    public void testforeditlist(View view){
        Intent intent = new Intent(this,DynamicAddViewActivity.class);
        startActivity(intent);
    }

    public void testforfilllist(View view){
        Intent intent = new Intent(this,fillList.class);
        startActivity(intent);
    }

    public void testforselecttofill(View view){
        Intent intent = new Intent(this,selecttofill.class);
        startActivity(intent);
    }

    public void testforbuttonnav(View view){
        Intent intent = new Intent(this,testforbuttonnav.class);
        startActivity(intent);
    }
}

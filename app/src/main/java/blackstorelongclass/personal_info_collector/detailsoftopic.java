package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import blackstorelongclass.personal_info_collector.listMonitor.*;
import blackstorelongclass.personal_info_collector.DataHandler.*;

import java.text.ParseException;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class detailsoftopic extends AppCompatActivity {


    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private LinearLayout addView;
    private userList us;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsoftopic);

        TextView text = (TextView) findViewById(R.id.listTopic);
        text.setText(us.getListTitle());

        Intent intent = getIntent();
        String listname = intent.getStringExtra(topicsofonelist.EXTRA_MESSAGE);

        String table,time;
        table = listname.split(",")[0];
        time = listname.split(",")[1];


        listHandler hd = new listHandler("whatever");
        userList us = null;
        try {
            us = hd.getATableData(table,time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        addView = (LinearLayout) findViewById(R.id.dt_addView);
        for(String topic : us.getTitleList()){
            View tagView = View.inflate(this, R.layout.detailsitem, null);
            TextView tagtopic = tagView.findViewById(R.id.tagTopic);
            tagtopic.setText(topic);
            TextView tagcontent = tagView.findViewById(R.id.tagcontent);
            if(us.getTag(topic).isDouble()){
                tagcontent.setText((String)us.getTag(topic).getObject());
            }
            else if(us.getTag(topic).isStr()){
                tagcontent.setText((String)us.getTag(topic).getObject());
            }
            else{
                Calendar calendar = (Calendar) us.getTag(topic).getObject();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                String dateStr = sdf.format(calendar.getTime());
                tagcontent.setText(dateStr);
            }
            addView.addView(tagView);
            addView.requestLayout();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}

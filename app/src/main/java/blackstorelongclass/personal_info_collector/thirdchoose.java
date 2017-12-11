package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;
import blackstorelongclass.personal_info_collector.listMonitor.userList;

public class thirdchoose extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private String str;
    private String timestring;
    private String listname;
    private String dateStr;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentNavigation = new Intent(thirdchoose.this, timeLine.class);
                    startActivity(intentNavigation);
                    return true;
                case R.id.navigation_user:
                    Intent intentNavigation3 = new Intent(thirdchoose.this, Userspage.class);
                    startActivity(intentNavigation3);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdchoose);

        Intent intent = getIntent();
        listname = intent.getStringExtra(secondchoose.EXTRA_MESSAGE);

        String table,time;
        table = listname.split(",")[2];
        time = listname.split(",")[3];


        listHandler hd = new listHandler("whatever");
        userList us = null;
        try {
            us = hd.getATableData(table,time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LinearLayout addView = (LinearLayout) findViewById(R.id.thirdchoose_addView);
        for(String topic : us.getTitleList()){
            View tagView = View.inflate(this, R.layout.thirdchooseitem, null);
            TextView tagtopic = tagView.findViewById(R.id.tagTopic);
            Button bn = tagView.findViewById(R.id.submit);
            bn.setOnClickListener(this);
            if(us.getTag(topic).isDouble()){
                tagtopic.setText(topic + " : " + us.getTag(topic).getObject().toString());
                bn.setTag(topic);
            }
            else if(us.getTag(topic).isStr()){
                tagtopic.setText(topic + " : " + (String)us.getTag(topic).getObject());
                bn.setTag(topic);
            }
            else{

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long)(us.getTag(topic).getObject()));
                //(Calendar) (us.getTag(topic).getObject());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateStr = sdf.format(calendar.getTime());
                tagtopic.setText(topic + " : " + dateStr);
                bn.setTag(topic);
            }
            addView.addView(tagView);
            addView.requestLayout();
        }
        Button homebutton = (Button) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homebutton) {
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
        else {
            String tString = (String) v.getTag();
            String gettopic = listname + "," + tString;
            listHandler handler = new listHandler("333");
            try {
                handler.addBridge(listname.split(",")[0], handler.timeStr2Long(listname.split(",")[1]),
                        listname.split(",")[2], handler.timeStr2Long(listname.split(",")[3]), tString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
    }
}

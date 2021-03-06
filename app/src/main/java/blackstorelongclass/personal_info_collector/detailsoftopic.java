package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.util.Pair;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import blackstorelongclass.personal_info_collector.listMonitor.*;
import blackstorelongclass.personal_info_collector.DataHandler.*;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class detailsoftopic extends AppCompatActivity implements View.OnClickListener {


    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private LinearLayout addView;
    private userList us;
    private String dateStr;
    private String listname;
    private String positionstr;
    private Pair<Pair<String,Long>,String> p;
    private Pair<String,Long> pfirst;
    private String table, time;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentNavigation = new Intent(detailsoftopic.this, timeLine.class);
                    startActivity(intentNavigation);
                    return true;
                case R.id.navigation_user:
                    Intent intentNavigation3 = new Intent(detailsoftopic.this, Userspage.class);
                    startActivity(intentNavigation3);
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


        Intent intent = getIntent();
        listname = intent.getStringExtra(topicsofonelist.EXTRA_MESSAGE);


        table = listname.split(",")[0];
        time = listname.split(",")[1];


        listHandler hd = new listHandler("whatever");
        userList us = null;
        try {
            us = hd.getATableData(table, time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        text.setText(us.getListTitle());

        addView = (LinearLayout) findViewById(R.id.dt_addView);
        for (String topic : us.getTitleList()) {
            View tagView;
            if (us.getTag(topic).isDouble()) {
                tagView = View.inflate(this, R.layout.detailsitem, null);
                TextView tagtopic = tagView.findViewById(R.id.tagTopic);
                tagtopic.setText(topic);
                TextView tagcontent = tagView.findViewById(R.id.tagcontent);
                tagcontent.setText(us.getTag(topic).getObject().toString());
            } else if (us.getTag(topic).isStr()) {
                tagView = View.inflate(this, R.layout.detailsitem, null);
                TextView tagtopic = tagView.findViewById(R.id.tagTopic);
                tagtopic.setText(topic);
                TextView tagcontent = tagView.findViewById(R.id.tagcontent);
                tagcontent.setText((String) us.getTag(topic).getObject());
            } else if (us.getTag(topic).isPos()){
                tagView = View.inflate(this, R.layout.detailsitemposition, null);
                TextView tagtopic = tagView.findViewById(R.id.tagTopic);
                tagtopic.setText(topic);
                Button bn = tagView.findViewById(R.id.positionbutton);
                Pair<Double,Double> p = (Pair)us.getTag(topic).getObject();
                positionstr = p.first+","+p.second;
                bn.setOnClickListener(this);
            }
            else {

                tagView = View.inflate(this, R.layout.detailsitem, null);
                TextView tagtopic = tagView.findViewById(R.id.tagTopic);
                tagtopic.setText(topic);
                TextView tagcontent = tagView.findViewById(R.id.tagcontent);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long) (us.getTag(topic).getObject()));
                //(Calendar) (us.getTag(topic).getObject());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateStr = sdf.format(calendar.getTime());
                tagcontent.setText(dateStr);
            }
            addView.addView(tagView);
            addView.requestLayout();
        }

        try {
            if(hd.getBridge(table,hd.timeStr2Long(time))==null){
                View addb = View.inflate(this, R.layout.addbridgeitem, null);
                Button addbridge = (Button) addb.findViewById(R.id.addbridge);
                addView.addView(addb);
                addView.requestLayout();
                addbridge.setOnClickListener(this);
            }
            else{
                p = hd.getBridge(table,hd.timeStr2Long(time));
                pfirst = p.first;
                View dl = View.inflate(this, R.layout.deletebridheitem, null);
                TextView tx = (TextView)dl.findViewById(R.id.tagitem);
                Button bn = (Button)dl.findViewById(R.id.deletebridge);
                bn.setOnClickListener(this);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long) (pfirst.second));
                //(Calendar) (us.getTag(topic).getObject());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateStr = sdf.format(calendar.getTime());
                tx.setText(pfirst.first + ":" + dateStr + ","+p.second);
                addView.addView(dl);
                addView.requestLayout();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        findViewById(R.id.edit).setOnClickListener(this);

        Button homebutton = (Button) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit) {
            Intent intent = new Intent(this, editList.class);
            intent.putExtra(EXTRA_MESSAGE, listname);
            startActivity(intent);
        }else if(v.getId()==R.id.homebutton) {
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }else if(v.getId() == R.id.positionbutton){
            Intent intent = new Intent(this, MapShowPage.class);
            intent.putExtra(EXTRA_MESSAGE, positionstr);
            startActivity(intent);
        }
        else if(v.getId() == R.id.addbridge){
            Intent intent = new Intent(this, firstchoose.class);
            intent.putExtra(EXTRA_MESSAGE, listname);
            startActivity(intent);
        }
        else{
            listHandler hd = new listHandler("whatever");
            try {
                hd.deleteBridge(table,hd.timeStr2Long(time),pfirst.first,pfirst.second,p.second);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
    }
}


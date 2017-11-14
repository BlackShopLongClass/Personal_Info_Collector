package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java.util.Calendar;
import blackstorelongclass.personal_info_collector.DataHandler.*;
import blackstorelongclass.personal_info_collector.listMonitor.*;

public class topicsofonelist extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private LinearLayout addView;
    private List<String> topics;
    private Calendar calendar;
    private String str="";
    private String listname;
    private String timestring;

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
        setContentView(R.layout.activity_topicsofonelist);

        addView = (LinearLayout) findViewById(R.id.tl_addView);

//        topics = Arrays.asList("xxx", "yyy", "zzz", "yyy", "zzz", "yyy", "zzz", "yyy", "zzz", "yyy", "zzz", "yyy", "zzz", "yyy", "zzz");

        ArrayList<userList> list;

        listHandler hd = new listHandler("whatever");
        Intent intent = getIntent();
        listname = intent.getStringExtra(selecttofill.EXTRA_MESSAGE);
        if(listname==null) listname = intent.getStringExtra(fillList.EXTRA_MESSAGE);
        list = hd.getTableAllData(listname);

        if(list != null) {
            for (userList ulist : list) {
                for (int i = 0; i < ulist.getListSize(); i++) {
                    str = ulist.getTitleList().get(i);
                    if (ulist.getTag(str).getClassType() == java.lang.Long.class) {
                        break;
                    }
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long) (ulist.getTag(str)).getObject());

                View tagView = View.inflate(this, R.layout.topicsofonelistitem, null);
                TextView text = tagView.findViewById(R.id.tagTopic);
                Button bn = tagView.findViewById(R.id.detailoftopic);
                bn.setTag(str);
                bn.setOnClickListener(this);
                String h,m,month,date;
                if(calendar.get(Calendar.HOUR)<10) h = "0"+calendar.get(Calendar.HOUR);
                else h=""+calendar.get(Calendar.HOUR);
                if(calendar.get(Calendar.MINUTE)<10) m = "0"+calendar.get(Calendar.MINUTE);
                else m=""+calendar.get(Calendar.MINUTE);
                if(calendar.get(Calendar.MONTH)<9) month = "0"+(calendar.get(Calendar.MONTH)+1);
                else month=""+(calendar.get(Calendar.MONTH)+1);
                if(calendar.get(Calendar.DATE)<10) date = "0"+calendar.get(Calendar.DATE);
                else date=""+calendar.get(Calendar.DATE);



                timestring = calendar.get(Calendar.YEAR) + "-" + month + "-" + date + " " +
                        h + ":" + m;
                text.setText(timestring);
                addView.addView(tagView);
                addView.requestLayout();
            }
        }


        Button createnewlistbutton = (Button) findViewById(R.id.addData);
        createnewlistbutton.setOnClickListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.addData){
            Intent intent = new Intent(this,fillList.class);
            intent.putExtra(EXTRA_MESSAGE, listname);
            startActivity(intent);
        }
        else {
            timestring = timestring + ":00";
            String gettopic = listname+ "," + timestring;
            Intent intent = new Intent(this, detailsoftopic.class);
            intent.putExtra(EXTRA_MESSAGE, gettopic);
            startActivity(intent);
        }
    }
}

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

import java.util.GregorianCalendar;
import java.util.Calendar;
import blackstorelongclass.personal_info_collector.DataHandler.*;
import blackstorelongclass.personal_info_collector.listMonitor.*;

public class topicsofonelist extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private LinearLayout addView;
    private List<String> topics;
    private GregorianCalendar calendar;
    private String str="";
    private String listname;

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
        list = hd.getTableAllData(listname);

        if(list != null) {
            for (userList ulist : list) {
                for (int i = 0; i < ulist.getListSize(); i++) {
                    str = ulist.getTitleList().get(i);
                    if (ulist.getTag(str).getClassType() == java.lang.Long.class) {
                        break;
                    }
                }
                Date date = new Date();
                date.setTime(((long) (ulist.getTag(str)).getObject())*1000);
                String datetime = date.toString();
                calendar = Calendar.getInstance();
                calendar.setTime(date);

                View tagView = View.inflate(this, R.layout.topicsofonelistitem, null);
                TextView text = tagView.findViewById(R.id.tagTopic);
                Button bn = tagView.findViewById(R.id.detailoftopic);
                bn.setTag(str);
                bn.setOnClickListener(this);
                text.setText(calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月" + calendar.get(Calendar.DATE) + "日" +
                        calendar.get(Calendar.HOUR) + "：" + calendar.get(Calendar.MINUTE));
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-MM-SS");
            String dateStr = sdf.format(calendar.getTime());
            String gettopic = (String) v.getTag()+ "," + dateStr;
            Intent intent = new Intent(this, detailsoftopic.class);
            intent.putExtra(EXTRA_MESSAGE, gettopic);
            startActivity(intent);
        }
    }
}

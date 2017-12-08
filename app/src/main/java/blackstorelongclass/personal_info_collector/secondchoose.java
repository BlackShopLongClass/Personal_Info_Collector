package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;
import blackstorelongclass.personal_info_collector.listMonitor.userList;

public class secondchoose extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private String str;
    private String timestring;
    private String topic;
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
        setContentView(R.layout.activity_secondchoose);

        LinearLayout addView = (LinearLayout) findViewById(R.id.secondchoose_addView);

        Intent intent = getIntent();
        topic = intent.getStringExtra(firstchoose.EXTRA_MESSAGE).split(",")[2];
        listname = intent.getStringExtra(firstchoose.EXTRA_MESSAGE).split(",")[0] + "," +
                intent.getStringExtra(firstchoose.EXTRA_MESSAGE).split(",")[1];


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listHandler listHandler = new listHandler("333");
        ArrayList<userList> list;
        list = listHandler.getTableAllData(topic);
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

                View tagView = View.inflate(this, R.layout.secondchooseitem, null);
                TextView text = tagView.findViewById(R.id.tagTopic);
                Button bn = tagView.findViewById(R.id.enterthelist);
                bn.setTag(str);
                bn.setOnClickListener(this);

                String h, m, month, date;
                if (calendar.get(Calendar.HOUR_OF_DAY) < 10)
                    h = "0" + calendar.get(Calendar.HOUR_OF_DAY);
                else h = "" + calendar.get(Calendar.HOUR_OF_DAY);
                if (calendar.get(Calendar.MINUTE) < 10) m = "0" + calendar.get(Calendar.MINUTE);
                else m = "" + calendar.get(Calendar.MINUTE);
                if (calendar.get(Calendar.MONTH) < 9)
                    month = "0" + (calendar.get(Calendar.MONTH) + 1);
                else month = "" + (calendar.get(Calendar.MONTH) + 1);
                if (calendar.get(Calendar.DATE) < 10) date = "0" + calendar.get(Calendar.DATE);
                else date = "" + calendar.get(Calendar.DATE);


                timestring = calendar.get(Calendar.YEAR) + "-" + month + "-" + date + " " +
                        h + ":" + m;
                text.setText(timestring);
                bn.setTag(timestring);
                Log.i("bslc", "bslc_topicsofonelist_onCreate():time=" + timestring);
                addView.addView(tagView);
                addView.requestLayout();
            }
        }
    }

    public void onClick(View v) {
        String tString = (String)v.getTag();
        tString = tString + ":00";
        String gettopic = topic+ "," + tString;
        Intent intent = new Intent(this, thirdchoose.class);
        intent.putExtra(EXTRA_MESSAGE, listname + "," +gettopic);
        startActivity(intent);
    }
}

package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Handler;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;

import static blackstorelongclass.personal_info_collector.fillList.EXTRA_MESSAGE;

public class timeLine extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private LinearLayout addView;
    Pair<Long,String> p;
    ArrayList<Pair> pairList = new ArrayList<Pair>();
    private String timestring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        addView = (LinearLayout) findViewById(R.id.timeline_addView);
//        Pair<Long , String> p1 = new Pair<>(1511851210000L,"eating");
//        Pair<Long , String> p2 = new Pair<>(1511451210000L,"eating");
//
//        pairList.add(p1);
//        pairList.add(p2);


        listHandler listhandler = new listHandler("1");
        pairList = listhandler.getTimeWithTitle();

        for(Pair<Long,String> p : pairList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) p.first);

            View tagView = View.inflate(this, R.layout.timelineitem, null);
            TextView text = tagView.findViewById(R.id.time);
            Button bn = tagView.findViewById(R.id.timelinebutton);
            bn.setOnClickListener(this);
            String h, m, month, date;
            if (calendar.get(Calendar.HOUR_OF_DAY) < 10)
                h = "0" + calendar.get(Calendar.HOUR_OF_DAY);
            else h = "" + calendar.get(Calendar.HOUR_OF_DAY);
            if (calendar.get(Calendar.MINUTE) < 10) m = "0" + calendar.get(Calendar.MINUTE);
            else m = "" + calendar.get(Calendar.MINUTE);
            if (calendar.get(Calendar.MONTH) < 9) month = "0" + (calendar.get(Calendar.MONTH) + 1);
            else month = "" + (calendar.get(Calendar.MONTH) + 1);
            if (calendar.get(Calendar.DATE) < 10) date = "0" + calendar.get(Calendar.DATE);
            else date = "" + calendar.get(Calendar.DATE);


            timestring = calendar.get(Calendar.YEAR) + "-" + month + "-" + date + " " +
                    h + ":" + m;
            Log.i("bslc","bslc_timeLine_onCreate():timestring="+timestring);
            text.setText(timestring);
            String str = p.second + "," + timestring + ":00";
            bn.setTag(str);
            bn.setText(p.second);

            addView.addView(tagView);
            addView.requestLayout();
        }


        Button mapshow = (Button) findViewById(R.id.mapshow);
        mapshow.setOnClickListener(this);

        Button homebutton = (Button) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homebutton) {
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.mapshow){
            Intent intent = new Intent(this, MapShowPage.class);
            startActivity(intent);
        }
        else {
            String tString = (String) v.getTag();
            String gettopic = tString;
            Intent intent = new Intent(this, detailsoftopic.class);
            intent.putExtra(EXTRA_MESSAGE, gettopic);
            startActivity(intent);
        }

    }
}

package blackstorelongclass.personal_info_collector;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Pair;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.maps2d.model.LatLng;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;
import blackstorelongclass.personal_info_collector.listMonitor.*;

public class editList extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private String TAG = this.getClass().getSimpleName();
    //装在所有动态添加的Item的LinearLayout容器
    private LinearLayout addView;
    private EditText timeEditText;
    private EditText dateEditText;
    private userList taglist;
    private Calendar calendardate,calendartime;
    private String topic;
    private String listname;
    private boolean flag = true;
    private boolean pf = true;
    private String position;
    private String table;
    private String time;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentNavigation = new Intent(editList.this, timeLine.class);
                    startActivity(intentNavigation);
                    return true;
                case R.id.navigation_user:
                    Intent intentNavigation3 = new Intent(editList.this, Userspage.class);
                    startActivity(intentNavigation3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        addView = (LinearLayout) findViewById(R.id.el_addView);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Calendar calendartest = Calendar.getInstance();

        Intent intent = getIntent();
        listname = intent.getStringExtra(detailsoftopic.EXTRA_MESSAGE);


        table = listname.split(",")[0];
        time = listname.split(",")[1];
        topic = table;

        listHandler hd = new listHandler("whatever");
        userList us = null;
        try {
            taglist = hd.getATableData(table,time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userTag tag;
        TextView listTopic = (TextView) findViewById(R.id.listTopic);
//        listTopic.setText(taglist.getListTitle());
        listTopic.setText(table);


        for(int i=0;i<taglist.getListSize();i++) {
            if (taglist.getTag(taglist.getTitleList().get(i)).isCalendar()) {
                LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.filllistitemtime, null);
                TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                tagTopic.setText(taglist.getTitleList().get(i));
                addView.addView(tagView);

                dateEditText = tagView.findViewById(R.id.taginputdate);
                Calendar calendar = Calendar.getInstance();
                topic = taglist.getTitleList().get(i);
                calendar.setTimeInMillis((long)(taglist.getTag(topic).getObject()));
                //(Calendar) (us.getTag(topic).getObject());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(calendar.getTime());
                dateEditText.setText(dateStr);
                dateEditText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            showDatePickDlg();
                            return true;
                        }
                        return false;
                    }
                });

                timeEditText = tagView.findViewById(R.id.taginputtime);
                timeEditText.setText(calendar.get(Calendar.HOUR) + ":" +calendar.get(Calendar.MINUTE));
                timeEditText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            showTimePickDlg();
                            return true;
                        }
                        return false;
                    }
                });
            }
            else if(taglist.getTag(taglist.getTitleList().get(i)).isStr()){
                LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.filllistitem, null);
                TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                tagTopic.setText(taglist.getTitleList().get(i));
                EditText text = (EditText)tagView.findViewById(R.id.taginput);
                topic = taglist.getTitleList().get(i);
                text.setText((String)taglist.getTag(topic).getObject());
                addView.addView(tagView);
            }
            else if(taglist.getTag(taglist.getTitleList().get(i)).isPos()){
                LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.filllistitemposition, null);
                TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                tagTopic.setText(taglist.getTitleList().get(i));
                Button bn = (Button) tagView.findViewById(R.id.positionbutton);
                bn.setOnClickListener(this);
                addView.addView(tagView);
            }
            else {
                LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.filllistitem, null);
                TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                tagTopic.setText(taglist.getTitleList().get(i));
                EditText text = (EditText)tagView.findViewById(R.id.taginput);
                topic = taglist.getTitleList().get(i);
                text.setText((String)taglist.getTag(topic).getObject().toString());
                EditText editText = (EditText)tagView.findViewById(R.id.taginput);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                addView.addView(tagView);
            }
        }
        findViewById(R.id.submit).setOnClickListener(this);

        Button homebutton = (Button) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                listHandler lh = new listHandler("2");

                flag = true;
                if(flag == true){
                    try {
                        getData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, selecttofill.class);
                    intent.putExtra(EXTRA_MESSAGE, table);
                    startActivity(intent);
                    break;}
                else
                    dialog();
            case R.id.homebutton:
                Intent intent1 = new Intent(this, selecttofill.class);
                startActivity(intent1);
                break;

            case R.id.positionbutton:
                Intent intent2 = new Intent(this, MapsActivity.class);
                startActivityForResult(intent2, 1);
        }
    }

    protected void showDatePickDlg() {
        calendardate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(editList.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editList.this.dateEditText.setText(year + "-" + ++monthOfYear + "-" + dayOfMonth);
            }
        }, calendardate.get(Calendar.YEAR), calendardate.get(Calendar.MONTH), calendardate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    protected void showTimePickDlg() {
        calendartime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(editList.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int munite) {
                editList.this.timeEditText.setText(hour + ":" + munite);
            }
        }, calendartime.get(Calendar.HOUR_OF_DAY), calendartime.get(Calendar.MINUTE),true);
        timePickerDialog.show();

    }

    private void getData() throws ParseException {
        TextView listTopic = (TextView) findViewById(R.id.listTopic);
        String inputTitle = listTopic.getText().toString();
        userList inputlist = new userList(inputTitle);
        for (int i = 0; i < addView.getChildCount(); i++) {
            if(taglist.getTag(taglist.getTitleList().get(i)).isCalendar()){
                View childAt = addView.getChildAt(i);
                EditText taginputtime = childAt.findViewById(R.id.taginputtime);
                EditText taginputdate = childAt.findViewById(R.id.taginputdate);
                String timestr = taginputtime.getText().toString();
                String datestr = taginputdate.getText().toString();
                SimpleDateFormat stf= new SimpleDateFormat("HH:MM");
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-mm-dd");
                String dt = datestr + " " + timestr + ":00";
                listHandler LH=new listHandler("n");
                long t=0;
                try {
                    t=LH.timeStr2Long(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar datetime = Calendar.getInstance();
                datetime.setTimeInMillis(t);
                calendartime = datetime;
                calendardate = datetime;

                calendardate.set(Calendar.HOUR,calendartime.get(Calendar.HOUR));
                calendardate.set(Calendar.MINUTE,calendartime.get(Calendar.MINUTE));
                userTag us = new userTag((taglist.getTitleList().get(i)),datetime);
                inputlist.addTag(taglist.getTitleList().get(i),us);
            }
            else if(taglist.getTag(taglist.getTitleList().get(i)).isDouble()){
                View childAt = addView.getChildAt(i);
                EditText taginput = (EditText) childAt.findViewById(R.id.taginput);
                double d = Double.valueOf(taginput.getText().toString());
                userTag us = new userTag((taglist.getTitleList().get(i)),d);
                inputlist.addTag(taglist.getTitleList().get(i),us);
            }
            else if(taglist.getTag(taglist.getTitleList().get(i)).isStr()){
                View childAt = addView.getChildAt(i);
                EditText taginput = (EditText) childAt.findViewById(R.id.taginput);
                userTag us = new userTag((taglist.getTitleList().get(i)),taginput.getText().toString());
                inputlist.addTag(taglist.getTitleList().get(i),us);
            }
            else if (taglist.getTag(taglist.getTitleList().get(i)).isPos()) {
                View childAt = addView.getChildAt(i);
                Object p = taglist.getTag(taglist.getTitleList().get(i)).getObject();
                userTag us = new userTag((taglist.getTitleList().get(i)), p);
                inputlist.addTag(taglist.getTitleList().get(i), us);
            }
        }
        listHandler handler = new listHandler("333");
        Long l = handler.timeStr2Long(time);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        flag = handler.editData(inputlist,c);
    }

    protected void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("确认退出吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,int which) {
                dialog.dismiss();
            }

        });


        builder.create().show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        position = data.getStringExtra(EXTRA_MESSAGE);
    }


}

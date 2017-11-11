package blackstorelongclass.personal_info_collector;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.GregorianCalendar;
import java.util.Calendar;

import blackstorelongclass.personal_info_collector.listMonitor.*;

public class fillList extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    //装在所有动态添加的Item的LinearLayout容器
    private LinearLayout addView;
    private EditText timeEditText;
    private EditText dateEditText;
    private userList taglist = new userList("test");
    private GregorianCalendar calendardate,calendartime;

    private void addViewItem(View view) {
        if (addView.getChildCount() == 0) {//如果一个都没有，就添加一个
            View tagView = View.inflate(this, R.layout.itemlist, null);
            addView.addView(tagView);
            addView.requestLayout();
            //sortHotelViewItem();
        } else if (((String) view.getTag()).equals("add")) {//如果有一个以上的Item,点击为添加的Item则添加
            View hotelEvaluateView = View.inflate(this, R.layout.itemlist, null);
            addView.addView(hotelEvaluateView);
//            sortHotelViewItem();
        }
        //else {
        //  sortHotelViewItem();
        //}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addView = (LinearLayout) findViewById(R.id.fl_addView);


        GregorianCalendar calendartest = new GregorianCalendar();
        double d = 1.0;
        userTag us1 = new userTag("int",d);
        userTag us2 = new userTag("cal",calendartest);
        userTag us3 = new userTag("str","sdfsda");
        taglist.addTag("int",us1);
        taglist.addTag("cal",us2);
        taglist.addTag("str",us3);

        userTag tag;
        TextView listTopic = (TextView) findViewById(R.id.listTopic);
        listTopic.setText(taglist.getListTitle());
        for(int i=0;i<taglist.getListSize();i++) {

            if (taglist.getTag(taglist.getTitleList().get(i)).isGregorianCalendar()) {
                LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.filllistitemtime, null);
                TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                tagTopic.setText(taglist.getTitleList().get(i));
                addView.addView(tagView);

                dateEditText = tagView.findViewById(R.id.taginputdate);
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
            else{
                LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.filllistitem, null);
                TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                tagTopic.setText(taglist.getTitleList().get(i));
                addView.addView(tagView);
            }
        }
        findViewById(R.id.submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit://点击添加按钮就动态添加Item
                getData();
                break;
        }
    }

    protected void showDatePickDlg() {
       calendardate = new GregorianCalendar();
        DatePickerDialog datePickerDialog = new DatePickerDialog(fillList.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fillList.this.dateEditText.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendardate.get(Calendar.YEAR), calendardate.get(Calendar.MONTH), calendardate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    protected void showTimePickDlg() {
        calendartime = new GregorianCalendar();
        TimePickerDialog timePickerDialog = new TimePickerDialog(fillList.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int munite) {
                fillList.this.timeEditText.setText(hour + " : " + munite);
            }
        }, calendartime.get(Calendar.HOUR), calendartime.get(Calendar.MINUTE),true);
        timePickerDialog.show();

    }

    private void getData() {
        TextView listTopic = (TextView) findViewById(R.id.listTopic);
        String inputTitle = listTopic.getText().toString();
        userList inputlist = new userList(inputTitle);
        for (int i = 0; i < addView.getChildCount(); i++) {
            if(taglist.getTag(taglist.getTitleList().get(i)).isGregorianCalendar()){
                calendardate.set(Calendar.HOUR,calendartime.get(Calendar.HOUR));
                calendardate.set(Calendar.MINUTE,calendartime.get(Calendar.MINUTE));
                userTag us = new userTag((taglist.getTitleList().get(i)),calendardate);
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
                userTag us = new userTag((taglist.getTitleList().get(i)),taginput.getText());
                inputlist.addTag(taglist.getTitleList().get(i),us);
            }
        }
        int i;

    }





}

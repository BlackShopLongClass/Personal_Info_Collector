package blackstorelongclass.personal_info_collector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import blackstorelongclass.personal_info_collector.listMonitor.*;
import blackstorelongclass.personal_info_collector.DataHandler.*;


public class DynamicAddViewActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    //装在所有动态添加的Item的LinearLayout容器
    private LinearLayout addHotelNameView;

    //添加ViewItem
    private void addViewItem(View view) {
        if (addHotelNameView.getChildCount() == 0) {//如果一个都没有，就添加一个
            View hotelEvaluateView = View.inflate(this, R.layout.itemlist, null);
            Button btn_add = (Button) hotelEvaluateView.findViewById(R.id.add_item);
            btn_add.setText("+");
            btn_add.setTag("add");
            btn_add.setOnClickListener(this);
            addHotelNameView.addView(hotelEvaluateView);
            addHotelNameView.requestLayout();
            //sortHotelViewItem();
        } else if (((String) view.getTag()).equals("add")) {//如果有一个以上的Item,点击为添加的Item则添加
            View hotelEvaluateView = View.inflate(this, R.layout.itemlist, null);
            addHotelNameView.addView(hotelEvaluateView);
            sortHotelViewItem();
        }
        //else {
        //  sortHotelViewItem();
        //}
    }


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
        setContentView(R.layout.activity_dynamic_add_view);
        addHotelNameView = (LinearLayout) findViewById(R.id.ll_addView);
        findViewById(R.id.submit_button).setOnClickListener(this);

        //默认添加一个Item

        addViewItem(null);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item://点击添加按钮就动态添加Item
                addViewItem(v);
                break;
            case R.id.submit_button://打印数据
                getData();
                Intent intent = new Intent(this,selecttofill.class);
                startActivity(intent);
                break;
        }
    }

    private void sortHotelViewItem() {
        //获取LinearLayout里面所有的view
        for (int i = 0; i < addHotelNameView.getChildCount(); i++) {
            final View childAt = addHotelNameView.getChildAt(i);
            final Button btn_remove = (Button) childAt.findViewById(R.id.add_item);
            btn_remove.setText("-");
            btn_remove.setTextColor(0xFFE11F05);

            btn_remove.setTag("remove");//设置删除标记
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //从LinearLayout容器中删除当前点击到的ViewItem
                    addHotelNameView.removeView(childAt);
                }
            });
            //如果是最后一个ViewItem，就设置为添加
            if (i == (addHotelNameView.getChildCount() - 1)) {
                Button btn_add = (Button) childAt.findViewById(R.id.add_item);
                btn_add.setText("+");
                btn_add.setTextColor(0xFF6FD031);
                btn_add.setTag("add");
                btn_add.setOnClickListener(this);
            }
        }
    }

    //获取所有动态添加的Item，找到控件的id，获取数据
    private void getData() {
        EditText title = (EditText) findViewById(R.id.title);
        String inputTitle = title.getText().toString();
        userList inputlist = new userList(inputTitle);
        for (int i = 0; i < addHotelNameView.getChildCount(); i++) {

            View childAt = addHotelNameView.getChildAt(i);
            EditText hotelName = (EditText) childAt.findViewById(R.id.ed_inputname);

            String str;

            Spinner spinner = (Spinner) childAt.findViewById(R.id.type_spinner);
            str = (String) spinner.getSelectedItem();
//            Log.e(TAG, "表单项名称：" + hotelName.getText().toString() + "类型是" + str );

            userTag us;
            switch (str){
                case "数字":us = new userTag(hotelName.getText().toString(),java.lang.Double.class);
                    break;
                case "文字":us = new userTag(hotelName.getText().toString(),java.lang.String.class);
                    break;
                case "时间":us = new userTag(hotelName.getText().toString(),java.util.GregorianCalendar.class);
                    break;
                default: us = new userTag(hotelName.getText().toString(),java.lang.String.class);
            }

            inputlist.addTag(hotelName.getText().toString(),us);
        }
        listHandler handler = new listHandler("uuu");
        handler.addNewList(inputlist);


    }


}

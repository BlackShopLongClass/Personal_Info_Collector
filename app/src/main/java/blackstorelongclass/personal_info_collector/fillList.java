package blackstorelongclass.personal_info_collector;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class fillList extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    //装在所有动态添加的Item的LinearLayout容器
    private LinearLayout addView;

    private void addViewItem(View view) {
        if (addView.getChildCount() == 0) {//如果一个都没有，就添加一个
            View tagView = View.inflate(this, R.layout.itemlist, null);
            addView.addView(tagView);
            addView.requestLayout();
            //sortHotelViewItem();
        } else if (((String) view.getTag()).equals("add")) {//如果有一个以上的Item,点击为添加的Item则添加
            View hotelEvaluateView = View.inflate(this, R.layout.itemlist, null);
            addView.addView(hotelEvaluateView);
            sortHotelViewItem();
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

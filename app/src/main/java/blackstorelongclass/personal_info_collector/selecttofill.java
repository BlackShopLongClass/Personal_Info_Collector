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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class selecttofill extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private LinearLayout addView;
    private List<String> topics;

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
        setContentView(R.layout.activity_selecttofill);

        addView = (LinearLayout) findViewById(R.id.sf_addView);

        topics = Arrays.asList("xxx","yyy","zzz","yyy","zzz","yyy","zzz","yyy","zzz","yyy","zzz","yyy","zzz","yyy","zzz");

        for(String topic : topics) {
            View tagView = View.inflate(this, R.layout.selecttofillitem, null);
            TextView text = tagView.findViewById(R.id.tagTopic);
            Button bn= tagView.findViewById(R.id.selecttofillbutton);
            bn.setTag(topic);
            bn.setOnClickListener(this);
            text.setText(topic);
            addView.addView(tagView);
            addView.requestLayout();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
        String gettopic = (String) v.getTag();
        Intent intent = new Intent(this,fillList.class);
        intent.putExtra(EXTRA_MESSAGE, gettopic);
        startActivity(intent);
    }

}

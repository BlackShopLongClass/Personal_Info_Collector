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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;
import blackstorelongclass.personal_info_collector.listMonitor.userList;

public class searchresult extends AppCompatActivity {


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
        setContentView(R.layout.activity_searchresult);

        LinearLayout addView = (LinearLayout) findViewById(R.id.sr_addView);

        Intent intent = getIntent();
        String searchstr = intent.getStringExtra(searchactivity.EXTRA_MESSAGE);
        listHandler listHandler = new listHandler("333");
        ArrayList<userList> list;
        list = listHandler.searchItem(searchstr.split(",")[0],searchstr.split(",")[1]);


        for(userList us : list) {
            for(String tag: us.getTitleList()) {
                if(!us.getTag(tag).isCalendar()) {
                    LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.searchresultitem, null);
                    TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
                    tagTopic.setText(us.getListTitle() + " " + tag+ " " + us.getTag(tag).getObject().toString());
                    addView.addView(tagView);
                    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
                }
            }
        }
    }

}

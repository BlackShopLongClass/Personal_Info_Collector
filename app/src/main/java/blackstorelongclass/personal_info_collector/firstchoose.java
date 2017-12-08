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
import android.widget.LinearLayout;
import android.widget.TextView;

import blackstorelongclass.personal_info_collector.DataHandler.listHandler;

public class firstchoose extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
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
        setContentView(R.layout.activity_firstchoose);

        Intent intent = getIntent();
        listname = intent.getStringExtra(detailsoftopic.EXTRA_MESSAGE);

        LinearLayout addView = (LinearLayout) findViewById(R.id.fc_addView);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listHandler listHandler = new listHandler("333");
        for(String str : listHandler.getTableList()){
            LinearLayout tagView = (LinearLayout) View.inflate(this, R.layout.firstchooseitem, null);
            TextView tagTopic = (TextView) tagView.findViewById(R.id.tagTopic);
            tagTopic.setText(str);
            Button bn = (Button) tagView.findViewById(R.id.enterthelist);
            bn.setTag(str);
            bn.setOnClickListener(this);
            addView.addView(tagView);
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, secondchoose.class);
        intent.putExtra(EXTRA_MESSAGE, listname + "," + v.getTag().toString());
        startActivity(intent);
    }
}

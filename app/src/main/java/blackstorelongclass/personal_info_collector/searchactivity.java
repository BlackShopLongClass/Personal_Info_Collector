package blackstorelongclass.personal_info_collector;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static blackstorelongclass.personal_info_collector.R.id.navigation_home;

public class searchactivity extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";
    private String searchstr;
    private Spinner spinner;
    private EditText hotelName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentNavigation = new Intent(searchactivity.this, timeLine.class);
                    startActivity(intentNavigation);
                    return true;
                case R.id.navigation_user:
                    Intent intentNavigation3 = new Intent(searchactivity.this, Userspage.class);
                    startActivity(intentNavigation3);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        Button searchbutton = (Button) findViewById(R.id.submit);
        searchbutton.setOnClickListener(this);

        Button homebutton = (Button) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homebutton) {
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
        else {
            spinner = (Spinner) findViewById(R.id.type_spinner);
            String str = (String) spinner.getSelectedItem();
            hotelName = (EditText) findViewById(R.id.sc_input);
            String s = hotelName.getText().toString();
            if(s.equals(""))
                dialog();
            else {
                searchstr = str + "," + hotelName.getText();
                Intent intent = new Intent(this, searchresult.class);
                intent.putExtra(EXTRA_MESSAGE, searchstr);
                startActivity(intent);
            }
        }
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("页面输入错误！");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.create().show();

    }
}

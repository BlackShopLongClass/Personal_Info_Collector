package blackstorelongclass.personal_info_collector;


import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addEditText(View view) {
        // Do something in response to button
        EditText editText = new EditText(this);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.main);
        //layout.addView(editText);
    }

    public void login_test(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void income(View view){
        Intent intent = new Intent(this,Income.class);
        startActivity(intent);
    }

    public void go(View view){
        Intent intent = new Intent(this,DynamicAddViewActivity.class);
        startActivity(intent);
    }

    public void creatDB(View view) {

    }
}

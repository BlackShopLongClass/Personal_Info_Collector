package blackstorelongclass.personal_info_collector;

import blackstorelongclass.personal_info_collector.listMonitor.*;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    public final static String Output = "blackstorelongclass.personal_info_collector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view) throws IllegalAccessException, InstantiationException {
//        Intent intent = new Intent(this,MainActivity.class);
//        String result = "output";
//
//        intent.putExtra(Output,result);
//        startActivity(intent);
        TextView textView = new TextView(this);
        Calendar calendar = new GregorianCalendar(2017,02,23,18,29,51);
        String result = "";
        long a = 11;
        userTag u = new userTag("number",a);
        userList list = new userList("test");
        list.addTag("number",u);
        result = result+u.getClassType()+""+u.getObject();
        textView.setText(result);
        ConstraintLayout Label = (ConstraintLayout) findViewById(R.id.main);
        Label.addView(textView);
    }
}//edit try

package blackstorelongclass.personal_info_collector;

import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import blackstorelongclass.personal_info_collector.listMonitor.userList;
import blackstorelongclass.personal_info_collector.listMonitor.userTag;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();

    public final static String EXTRA_MESSAGE = "blackstorelongclass.personal_info_collector.MESSAGE";

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
        //Calendar calendar = new GregorianCalendar(2017,02,23,18,29,51);
        String result = "";
        long a = 11;
        userTag u = new userTag("number",a);
        userList list = new userList("test");
        list.addTag("number",u);
        Class<?> longType = java.lang.Long.class;
        if(u.getClassType().equals(longType))
            result = result+u.getClassType()+" "+u.getObject();
        else
            result = u.getClassType().toString() +" " + java.lang.Long.class.toString();
        Log.e(TAG,result);
        //textView.setText(result);
        //ConstraintLayout Label = (ConstraintLayout) findViewById(R.id.main);
        //Label.addView(textView);
    }
}

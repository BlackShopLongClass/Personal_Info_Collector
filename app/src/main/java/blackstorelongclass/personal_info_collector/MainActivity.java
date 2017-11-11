package blackstorelongclass.personal_info_collector;


import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

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
}

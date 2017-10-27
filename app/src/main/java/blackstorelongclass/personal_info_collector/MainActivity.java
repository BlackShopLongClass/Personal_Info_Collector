package blackstorelongclass.personal_info_collector;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static String Output = "blackstorelongclass.personal_info_collector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view){
//        Intent intent = new Intent(this,MainActivity.class);
//        String result = "output";
//
//        intent.putExtra(Output,result);
//        startActivity(intent);
        TextView textView = new TextView(this);
        //listMonitor m = new listMonitor();
        String result = new String();
        //result = result + m.findI();
        textView.setText(result);
        ConstraintLayout Label = (ConstraintLayout) findViewById(R.id.main);
        Label.addView(textView);
    }
}//edit try

package blackstorelongclass.personal_info_collector;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class selecttofill extends AppCompatActivity {

    private LinearLayout addView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttofill);

        addView = (LinearLayout) findViewById(R.id.fl_addView);

        

        View tagView = View.inflate(this, R.layout.selecttofillitem, null);
        addView.addView(tagView);
        addView.requestLayout();
    }

}

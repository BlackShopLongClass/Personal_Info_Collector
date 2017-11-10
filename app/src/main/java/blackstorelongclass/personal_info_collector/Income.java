package blackstorelongclass.personal_info_collector;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Income extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = getListView();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.income_header,(ViewGroup)findViewById(R.id.income_header));
        View footerView = inflater.inflate(R.layout.income_footer,(ViewGroup)findViewById(R.id.income_footer));
        lv.addHeaderView(headerView);
        lv.addFooterView(footerView);


        Resources res = getResources();
        String[] incomeItems = res.getStringArray(R.array.income_placeholder);
        TextView footer = (TextView) footerView.findViewById(R.id.footer_message);
        String footer_message = String.format("There are totally %1$d",incomeItems.length);
        footer.setText(footer_message);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.income_item,incomeItems));
        //lv.setOnClickListener((View.OnClickListener) new IncomeItemClickListener(this));
    }


}

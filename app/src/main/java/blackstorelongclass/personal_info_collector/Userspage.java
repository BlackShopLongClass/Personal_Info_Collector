package blackstorelongclass.personal_info_collector;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;




import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import blackstorelongclass.personal_info_collector.DataHandler.BackupHandler;

public class Userspage extends AppCompatActivity implements View.OnClickListener {

    private static final int FILE_SELECT_CODE = 1;
    private static final String TAG = "ChooseFile";
    private String inputPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userspage);

        Button createnewlistbutton = (Button) findViewById(R.id.selectfile);
        createnewlistbutton.setOnClickListener(this);

        Button homebutton = (Button) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(this);

        Button confirmbutton = (Button) findViewById(R.id.confirmfile);
        confirmbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homebutton) {
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.confirmfile){
            String x = Environment.getExternalStorageDirectory().getPath() + "/tencent/QQfile_recv/lists(1).xls";
            BackupHandler.readXlsFile(x);
        }
        else {
            showFileChooser();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
        //String x="/"+Environment.getExternalStorageDirectory().getPath()+"/tencent/QQfile_recv/lists(1).xls";
        //BackupHandler.readXlsFile(x);
        //BackupHandler.readXlsFile(inputPath);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path

                    try {
                        inputPath = Userspage.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + inputPath);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


}

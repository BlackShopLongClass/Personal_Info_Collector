package blackstorelongclass.personal_info_collector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.BaseKeyListener;
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
    private boolean inflag;
    private boolean outflag;

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

        Button outputbutton = (Button) findViewById(R.id.outputfile);
        outputbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homebutton) {
            Intent intent = new Intent(this, selecttofill.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.confirmfile){
//            inflag = BackupHandler.readXlsFile(inputPath);
            BackupHandler.readXlsFile(inputPath);
            if(!inflag) dialogin();

        }
        else if(v.getId()==R.id.outputfile){
            outflag = BackupHandler.writeXlsFile(inputPath);
            if(!outflag) dialogout();
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
                        inputPath = GetPathFromUri4kitkat.getPath(this,uri);
                    Log.d(TAG, "File Path: " + inputPath);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void dialogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("读取文件失败");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        builder.create().show();

    }

    protected void dialogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("导出文件失败");
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

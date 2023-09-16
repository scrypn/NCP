package wertexpro.fvds.svaroggraphs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Scanner;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import lecho.lib.hellocharts.view.LineChartView;
import wertexpro.fvds.svaroggraphs.ui.home.HomeViewModel;

public class SendFileActivity extends AppCompatActivity {
    public static SharedPreferences sp;
    public static String str_file;
    public static Activity activity;
    public static Sender2 sslSender = new Sender2("ncp@scryp.ru", "nGqjjRPtC4WtzZ3h");

    private static void Act(Activity act){
        activity = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        Toolbar toolbar = findViewById(R.id.toolbar_send);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        final EditText edit = findViewById(R.id.his_email);
        Button b = findViewById(R.id.send_button2);
        Act(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!edit.getText().toString().equals("")){
                        Scanner sc = new Scanner(new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + GlobalValues.file_choose));
                        String file = null;
                        while (sc.hasNext()){
                            file = file + sc.nextLine() + "\n";
                        }
                        str_file = file;
                    }
                }catch (Exception e){
                    Toast.makeText(activity, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Runnable run = new newThreadFile();
        Thread thread = new Thread(run);
        thread.start();

    }
}
class newThreadFile implements Runnable{
    @Override
    public void run() {
        try {
            DataSource source = new FileDataSource(new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data.txt"));
            SendFileActivity.sslSender.send("NCP-данные", "" + SendFileActivity.str_file,
                    "ncp@scryp.ru", "m.neter.century@gmail.com");
        }catch (Exception e){}

    }
}

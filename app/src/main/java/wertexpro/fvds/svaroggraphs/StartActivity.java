package wertexpro.fvds.svaroggraphs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Runnable r = new Client();
        Thread t = new Thread(r);
        t.start();
        try {
            Thread.sleep(GlobalValues.waitForDataInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*Intent intent = new Intent(StartActivity.this, MainActivity.class);

        startActivityForResult(intent, 3);
        overridePendingTransition(R.anim.top,R.anim.bottom);*/
    }
}

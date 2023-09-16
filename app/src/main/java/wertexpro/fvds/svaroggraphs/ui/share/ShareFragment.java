package wertexpro.fvds.svaroggraphs.ui.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

import wertexpro.fvds.svaroggraphs.GlobalValues;
import wertexpro.fvds.svaroggraphs.MainActivity;
import wertexpro.fvds.svaroggraphs.R;
import wertexpro.fvds.svaroggraphs.Sender2;
import wertexpro.fvds.svaroggraphs.ui.Environment;
import wertexpro.fvds.svaroggraphs.ui.gallery.GalleryFragment;
import wertexpro.fvds.svaroggraphs.ui.home.HomeFragment;
import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;

import static wertexpro.fvds.svaroggraphs.GlobalValues.activity;
import static wertexpro.fvds.svaroggraphs.MainActivity.toggle;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private View root;
    private static SharedPreferences sp;
    AlertDialog.Builder ad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_share, container, false);
        GlobalValues.startDestination = "share";
        final Button button = root.findViewById(R.id.url_open_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.scryp.ru/user/feedback"));
                startActivity(browserIntent);
            }
        });
        return root;
        /*if (HomeFragment.thread2 != null)HomeFragment.thread2.interrupt();
        if (GalleryFragment.thread_gallery != null)GalleryFragment.thread_gallery.interrupt();
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
        root = inflater.inflate(R.layout.fragment_share, container, false);
        toggle.syncState();
        GlobalValues.startDestination = "share";
       *//* ScrollView sv = new ScrollView(getActivity());
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout ll = (LinearLayout)root.findViewById(R.id.share);
        if(sv.getParent() != null) {
            ((ViewGroup)sv.getParent()).removeView(sv); // <- fix
        }
        ll.addView(sv);*//*
        if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false)){
            final EditText firstNum = (EditText) root.findViewById(R.id.text_email);
            final Button btnAdd = root.findViewById(R.id.send_button);
            *//*btnAdd.getBackground().setColorFilter(Color.parseColor("#105CF0"), PorterDuff.Mode.MULTIPLY);
            btnAdd.setTextColor(Color.parseColor("#0600A8"));*//*
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                   // MainActivity.hideKeyboardFrom(getActivity(), root);
                            GlobalValues.editText = firstNum;
                            Runnable r = new newThread();
                            Thread t = new Thread(r);
                            t.start();
                }
            });
            if (GlobalValues.internet){
                Toast.makeText(MainActivity.activity, "Нет подключения к Интернету", Toast.LENGTH_LONG)
                        .show();
                GlobalValues.internet = false;
            }

            GlobalValues.build_graph = false;
            return root;
        }else {
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView)root.findViewById(R.id.t);
            textView.setText("Отправка писем невозможна без разрешения доступа к Wi-Fi!");
            String title = "НЕТ ДОСТУПА К Wi-Fi";
            String message = "Вы не можете использовать программу пока не разрешите доступ на использование Wi-Fi";
            String button1String = "Разрешить доступ";
            String button2String = "Выход";

            ad = new AlertDialog.Builder(getContext());
            ad.setTitle(title);  // заголовок
            ad.setMessage(message); // сообщение
            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getContext(), "Вы предоставили доступ к Wi-Fi",
                            Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("wifi_agree", true);
                    e.apply();
                    e.commit();
                }
            });
            ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getContext(), "Все Wi-Fi службы дизактивтрованы", Toast.LENGTH_LONG)
                            .show();
                }
            });
            ad.setCancelable(false);
            ad.show();
            GlobalValues.build_graph = false;
            return root;
        }*/

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    /*class newThread implements Runnable{
        @Override
        public void run() {
            try {

                Thread.sleep(500);
                final EditText firstNum = GlobalValues.editText;
                if (!firstNum.getText().toString().equals("")){
                    if (GlobalValues.sdk_api >= 19){
                        try {
                            URL whatismyip = new URL("https://checkip.amazonaws.com");
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    whatismyip.openStream()));

                            String ip = in.readLine(); //you get the IP as a String
                            sslSender.send("Письмо от Neter-клиента", firstNum.getText().toString().trim() + "\n\nОтправлено с ip-адреса : " + ip,
                                    "support@neter.com", "m.neter.century@gmail.com");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    firstNum.setText("");
                                    Toast.makeText(MainActivity.activity, "Письмо отправлено", Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                        }catch (Exception e){
                            GlobalValues.internet = true;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.activity, "Нет подключения к Интернету", Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                firstNum.setText("");
                                Toast.makeText(MainActivity.activity, "К сожалению, ваше устройство не поддерживает данную функцию", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.activity, "Отправка пустых сообщений запрещена", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }catch (Exception e){

            }
        }
    }*/

}
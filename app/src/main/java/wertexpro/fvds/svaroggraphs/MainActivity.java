package wertexpro.fvds.svaroggraphs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.jjoe64.graphview.series.DataPoint;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import wertexpro.fvds.svaroggraphs.ui.home.HomeFragment;
import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    public static SharedPreferences sp;
    AlertDialog.Builder ad;
    public static Activity activity;
    public static int files;
    public static ActionBarDrawerToggle toggle;
    public TextView mHomeTextView;
    public TextView mGalleryTextView;
    public TextView mSlideshowTextView;
    public TextView mToolsTextView;
    public TextView mShareTextView;
    public static DrawerLayout drawer_main;
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    public static EditText userInput;

    public static void SaveFun(Activity act) {
        activity = act;
        GlobalValues.activity = act;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"WifiManagerLeak", "ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SaveFun(this);
        GlobalValues.save = true;
        GlobalValues.sdk_api = Build.VERSION.SDK_INT;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (GlobalValues.client_and_activity_new) {
            GlobalValues.build_graph = true;
            files = sp.getInt("savefilesnumbers", -1);
            Toast.makeText(this, "Данные обновлены", Toast.LENGTH_SHORT).show();
        }
        GlobalValues.client_and_activity_new = false;
        WifiManager Wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        /*Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        LinearLayout linearLayout = findViewById(R.id.nav_header_main);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ));
*/
        /*if (this.getResources().getConfiguration().orientation == 0 && sp.getBoolean(ToolsFragment.KEY_TURN_DISPLAY, false))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //button.setBackgroundResource(R.drawable.icon);
        if (this.getResources().getConfiguration().orientation == 0 && sp.getBoolean(ToolsFragment.KEY_TURN_DISPLAY, false))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/


        if (sp.getBoolean(ToolsFragment.KEY_WIFI, false)) {
            try {
                Wifi.setWifiEnabled(true);
            } catch (Exception e) {
            }

        }
        WifiInfo wifiInf = Wifi.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        GlobalValues.ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        /*if (GlobalValues.ip_address){
            Runnable runnable = new newThread();
            Thread thread = new Thread(runnable);
            thread.start();
            GlobalValues.ip_address = false;
        }*/
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        long time = calendar.getTimeInMillis();
        AlarmManager manager = (AlarmManager)getSystemService(
                Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.wertexpro.fvds.ru"), this, NewMessageNotification.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));
*/
        /*if (sp.getBoolean(ToolsFragment.KEY_NOTS, true) && GlobalValues.nots && GlobalValues.sdk_api >= 19){
            NewMessageNotification.notify(this, "Добро пожаловать в NCP", 50);
            GlobalValues.nots = false;
        }*/

        /*if (sp.getBoolean("wifi_agree", false)){
            String title = "НЕТ ДОСТУПА К Wi-Fi";
            String message = "Вы не можете использовать программу пока не разрешите доступ на использование Wi-Fi";
            String button1String = "Разрешить доступ";
            String button2String = "Выход";

            ad = new AlertDialog.Builder(MainActivity.this);
            ad.setTitle(title);  // заголовок
            ad.setMessage(message); // сообщение
            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(MainActivity.this, "Вы предоставили доступ к Wi-Fi",
                            Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean("wifi_agree", true);
                    e.apply();
                    e.commit();
                }
            });
            ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                *//*Toast.makeText(MainActivity.this, "Возможно вы правы", Toast.LENGTH_LONG)
                        .show();*//*
                    System.exit(0);
                }
            });
            ad.setCancelable(false);
        *//*ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(MainActivity.this, "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });*//*
            ad.show();
        }
*/
        // GlobalValues.wifi_agree = sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false);
        // GlobalValues.wifi = sp.getBoolean(ToolsFragment.KEY_WIFI, false);
        // GlobalValues.new_data = Integer.parseInt(sp.getString(ToolsFragment.KEY_NEW_DATA, ""));
        // GlobalValues.lines = sp.getBoolean(ToolsFragment.KEY_LINES, false);
        //  GlobalValues.points_data = sp.getBoolean(ToolsFragment.KEY_POINTS_DATA, false);
        //  GlobalValues.turn_display = sp.getBoolean(ToolsFragment.KEY_TURN_DISPLAY, false);
        //  GlobalValues.tema = sp.getBoolean(ToolsFragment.KEY_TEMA, false);
        //  GlobalValues.nots = sp.getBoolean(ToolsFragment.KEY_NOTS, false);
        // for (int i = 0; i < 20; i++) {
        ///     System.out.println(GlobalValues.wifi_agree);
        //     System.out.println(GlobalValues.wifi);
        //    System.out.println(GlobalValues.new_data);
        //    System.out.println(GlobalValues.lines);
        //    System.out.println(GlobalValues.points_data);
        //    System.out.println(GlobalValues.turn_display);
        //    System.out.println(GlobalValues.tema);
        ///    System.out.println(GlobalValues.nots);
        //}

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        final ConstraintLayout content = findViewById(R.id.container_main);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);

                // а также меняем размер
                /* content.setScaleX(1 - slideOffset);*/
                /*content.setScaleY(1 - slideOffset);*/
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_home, true)
                .build();


        drawer_main = drawer;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GlobalValues.app = new MainActivity();

        Runnable r1 = new SaveThread();
        Thread t1 = new Thread(r1);
        t1.start();

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHost != null;
        NavController navController2 = navHost.getNavController();

        NavInflater navInflater = navController2.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);
        switch (GlobalValues.startDestination) {
            case "home":
                graph.setStartDestination(R.id.nav_home);
                System.out.println("home1");
                break;
            case "gallery":
                graph.setStartDestination(R.id.nav_gallery);
                break;
            case "slideshow":
                graph.setStartDestination(R.id.nav_slideshow);
                break;
            case "tools":
                graph.setStartDestination(R.id.nav_tools);
                break;
            case "share":
                graph.setStartDestination(R.id.nav_share);
                break;
            default:
                graph.setStartDestination(R.id.nav_home);//share
                System.out.println("home2");
                break;
        }
        navController2.setGraph(graph);



        /*mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
            mTimer.schedule(mMyTimerTask, 1000, 5000);*/
    }

    public void initializeCountDrawer(NavigationView navigationView) {
        mHomeTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_home));
        mGalleryTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_gallery));
        /*mSlideshowTextView = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_slideshow));*/
        mGalleryTextView.setGravity(Gravity.CENTER_VERTICAL);
        mGalleryTextView.setTypeface(null, Typeface.BOLD);
        mGalleryTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        byte x = 0;
        try {
            Scanner sc = new Scanner(readFile(true));
            while (sc.hasNext()) {
                x++;
            }
        } catch (Exception e) {
        }
        mGalleryTextView.setText(x + "");

        mHomeTextView.setGravity(Gravity.CENTER_VERTICAL);
        mHomeTextView.setTypeface(null, Typeface.BOLD);
        mHomeTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        mHomeTextView.setText(x + "");
        ;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawer_main.isDrawerOpen(GravityCompat.START)) {
            drawer_main.closeDrawer(GravityCompat.START);
        } else {
            drawer_main.openDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        System.out.println(id);
        if (id == R.id.nav_tools) {
            System.out.println("Успех");
            drawer_main.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static File[] files(int number) {
        File[] f = new File[number];
        for (int i = 0; i < number; i++) {
            f[i] = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "save" + (i + 1) + ".txt");
            System.out.println(f[i]);
        }
        return f;
    }

    public static File readFile(boolean b) {
        File myFile = null;
        try {
            if (b) {
                myFile = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data.txt");
            /*Scanner sc = new Scanner(myFile);
            while (sc.hasNext()){
                System.out.println(sc.nextLine());
            }*/
            } else {
                myFile = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data2.txt");
            /*Scanner sc = new Scanner(myFile);
            while (sc.hasNext()){
                System.out.println(sc.nextLine());
            }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return myFile;
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {


            runOnUiThread(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    HomeFragment h = new HomeFragment();
                    h.onAttach(getApplicationContext());
                    if (GlobalValues.inflater != null)
                        h.onCreateView(GlobalValues.inflater, GlobalValues.container, GlobalValues.savedInstanceState);
                    System.out.println("HELLO");
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            drawer_main.closeDrawer(GravityCompat.START);
            /*if (id == R.id.tema){
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                *//*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivityForResult(intent, 3);
                finish();*//*
            }*/
            if (id == R.id.action_settings) {
                GlobalValues.client_and_activity_new = true;
                Runnable r = new Client();
                Thread t = new Thread(r);
                t.start();
                Thread.sleep(GlobalValues.waitForDataInMillis);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivityForResult(intent, 3);
                finish();
                return true;
            }
            if (id == R.id.action_save) {
                try {
                    if (MainActivity.readFile(false).exists() && MainActivity.readFile(true).exists()) {
                        SharedPreferences.Editor editor = sp.edit();
                        if (!sp.contains("savefilesnumbers")) {
                            editor.putInt("savefilesnumbers", 1);
                        } else {
                            editor.putInt("savefilesnumbers", sp.getInt("savefilesnumbers", -1) + 1);
                        }

                        /*editor.putInt("savefilesnumbers", 0);*/
                        editor.apply();
                        editor.commit();
                        System.out.println(sp.getInt("savefilesnumbers", -1));
                        File f = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + sp.getInt("savefilesnumbers", 0));
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
                        Scanner sc = new Scanner(MainActivity.readFile(false));
                        Scanner sc2 = new Scanner(MainActivity.readFile(true));
                        Date currentTime = Calendar.getInstance().getTime();
                        bos.write((currentTime + "\n").getBytes());
                        while (sc.hasNext() && sc2.hasNext()) {
                            bos.write((sc.nextLine() + "-" + sc2.nextLine() + "\n").getBytes());
                        }
                        bos.flush();
                        bos.close();
                        sc.close();
                        sc2.close();
                        Scanner scanner = new Scanner(f);
                        while (scanner.hasNext()) {
                            System.out.println(scanner.nextLine());

                        }
                        scanner.close();
                        Toast.makeText(this, "Данные загружены",
                                Toast.LENGTH_LONG).show();
                /*editor.putInt("savefilesnumbers", 0);
                editor.apply();
                editor.commit();
                if (f.delete()) System.out.println("Успех");*/
                    } else {
                        Toast.makeText(this, "Нет соединения с сервером",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PackageManager.PERMISSION_GRANTED);
                        Toast.makeText(this, "Необходимо разрешение доступа к CD-карте",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (id == R.id.action_send) {
                int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionStatus != PackageManager.PERMISSION_DENIED) {
                    LayoutInflater li = LayoutInflater.from(MainActivity.activity);
                    View promptsView = li.inflate(R.layout.send_email, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.activity);

                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    userInput = (EditText) promptsView.findViewById(R.id.input_text);
                    userInput.setText(sp.getString(ToolsFragment.KEY_EMAIL, ""));

                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("Отправить",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Вводим текст и отображаем в строке ввода на основном экране:
                                            //final_text.setText();
                                            Runnable run = new FileThread2();
                                            Thread thread = new Thread(run);
                                            thread.start();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    //Создаем AlertDialog:
                    AlertDialog alertDialog = mDialogBuilder.create();

                    //и отображаем его:
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PackageManager.PERMISSION_GRANTED);
                    Toast.makeText(this, "Необходимо разрешение доступа к CD-карте",
                            Toast.LENGTH_LONG).show();
                }
            }
            /*if (id == R.id.test_server){
                LayoutInflater li = LayoutInflater.from(MainActivity.activity);
                View promptsView = li.inflate(R.layout.test_server, null);
                sp = PreferenceManager.getDefaultSharedPreferences(this);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.activity);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final TextView userInput = (TextView) promptsView.findViewById(R.id.input_text_server);
                userInput.setText(GlobalValues.ip);
                final EditText edit = (EditText)promptsView.findViewById(R.id.input_text2);
                *//*edit.setText(String.valueOf(sp.getInt(ToolsFragment.KEY_IP_PORT, 0)));*//*

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("Включить",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        try {
                                            String str = edit.getText().toString();
                                            GlobalValues.port = Integer.parseInt(str);
                                            Toast.makeText(MainActivity.activity, "Установлен порт " + str, Toast.LENGTH_LONG).show();
                                        }catch (Exception e){
                                            GlobalValues.port = 9999;
                                            Toast.makeText(MainActivity.activity, "Установлен порт 9999", Toast.LENGTH_LONG).show();
                                        }
                                        *//*Runnable run = new TestServer();
                                        Thread thread = new Thread(run);
                                        thread.start();*//*
                                    }
                                })
                        .setNegativeButton("Отказ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                        Toast.makeText(MainActivity.activity, "Отказ от операции", Toast.LENGTH_LONG).show();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
            }
            if (id == R.id.toolbar) {
                System.out.println("Клик по гамбургеру");
            }*/

            /*File f = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number");
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                if (!f.exists()){
                    f.createNewFile();
                    bos = new BufferedOutputStream(new FileOutputStream(f));
                    bis = new BufferedInputStream(new FileInputStream(f));
                    bos.write("1".getBytes());
                    bos.flush();
                    bos.close();
                    System.out.println("Успех");
                    files(1);
                }else{
                    bos = new BufferedOutputStream(new FileOutputStream(f));
                    bis = new BufferedInputStream(new FileInputStream(f));
                    String str = "-1";
                    byte[] b = new byte[1];
                    System.out.println(bis.read(b));
                    System.out.println(b.length);
                    int value = Integer.parseInt(str) + 1;
                    System.out.println(value);
                        *//*try {
                        System.out.println(dis.readInt());
                    }catch (Exception e){
                        e.printStackTrace();
                    }*//*

             *//*value = sc.nextInt() + 1;*//*
             *//*}catch (Exception e){
                        System.out.println("BadBadBad");e.printStackTrace();
                    }*//*

                    bos.write(String.valueOf(value).getBytes());
                    bos.flush();
                    bos.close();
                    System.out.println(value);
                    files(value);*/


                /*System.out.println(f.length());
                if (f.delete()) System.out.println("File was deleted");;*/
//                try {
//                    Scanner sc = new Scanner(f);
//                    String s = null;
//                    try {
//                        s = sc.nextLine();
//                    }catch (Exception e){
//                        s = "";
//                    }
//
//                    try {
//                        FileOutputStream fos = new FileOutputStream(f);
//                        if (s.equals("")){
//                            fos.write("1".getBytes());
//                            fos.flush();
//                            fos.close();
//                        }
//                    }catch (Exception e){
//                        System.out.println("DIIIICCCCK");
//                    }
//
//                    sc.close();
//                    Scanner scanner = new Scanner(new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number"));
//                    GlobalValues.myFiles = files(scanner.nextInt());
//                    for (File file:GlobalValues.myFiles) {
//                        System.out.println(file);
//                    }
//                    /*System.out.println(GlobalValues.myFiles[0]);*/
//                }catch (Exception e) {
//                    System.out.println("Error bitch");
//                    e.printStackTrace();
//                }


        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        if (toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private DataPoint[] getDataPoint(int n, List<Integer> hours, List<Double> voltmeter) {

        DataPoint[] dp = new DataPoint[n];
        for (int i = 0; i < n; i++) {
            dp[i] = new DataPoint(hours.get(i), voltmeter.get(i));
        }
        return dp;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

class SaveThread implements Runnable {
    @Override
    public void run() {
        final Activity activity = MainActivity.activity;
        activity.runOnUiThread(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                final AlertDialog.Builder ad = new AlertDialog.Builder(activity);
                ScrollView sv = new ScrollView(activity);
                sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                final LinearLayout ll = new LinearLayout(activity);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                sv.addView(ll);
                ArrayList<String> arr = new ArrayList<>(16);
                final Button button = new Button(MainActivity.activity.getApplicationContext());

                button.setText("Очистить сохранённые данные");
                button.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );

                button.getBackground().setColorFilter(Color.parseColor("#105CF0"), PorterDuff.Mode.MULTIPLY);
                button.setTextColor(Color.parseColor("#ffffff"));
                final Button load = new Button(MainActivity.activity.getApplicationContext());

                load.setText("Обновить данные");
                load.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                load.getBackground().setColorFilter(Color.parseColor("#105CF0"), PorterDuff.Mode.MULTIPLY);
                load.setTextColor(Color.parseColor("#ffffff"));
                if (button.getParent() != null) {
                    ((ViewGroup) button.getParent()).removeView(button);
                }
                if (load.getParent() != null) {
                    ((ViewGroup) load.getParent()).removeView(load);
                }
                ll.addView(button);
                ll.addView(load);
                load.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(View view) {
                        System.out.println(MainActivity.files);
                        System.out.println(sp.getInt("savefilesnumbers", -1));
                        ArrayList<String> arr = new ArrayList<>(16);
                        for (int i = MainActivity.files + 1; i <= sp.getInt("savefilesnumbers", -1); i++) {
                            final Button b = new Button(MainActivity.activity.getApplicationContext());
                            System.out.println("+++");
                            System.out.println(i);
                            File f = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + i);
                            try {
                                Scanner sc = new Scanner(f);
                                while (sc.hasNext()) {
                                    arr.add(sc.nextLine());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //GlobalValues.file_choose = i;


                            b.setText(arr.get(0));
//                        b.setTextColor(R.color.colorPrimaryDark);
                            // b.setHighlightColor(R.color.colorPrimary);
                            b.setLayoutParams(
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT)
                            );
                            b.getBackground().setColorFilter(Color.parseColor("#105CF0"), PorterDuff.Mode.MULTIPLY);
                            b.setTextColor(Color.parseColor("#ffffff"));
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String str = b.getText().toString();
                                    for (int j = 1; j <= sp.getInt("savefilesnumbers", 1); j++) {
                                        try {
                                            Scanner sc = new Scanner(new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + j));
                                            if (str.equals(sc.nextLine())) {
                                                GlobalValues.startDestination = "slideshow";
                                                GlobalValues.file_choose = j;
                                                GlobalValues.save = false;
                                                Intent intent = new Intent(GlobalValues.activity, Main2Activity.class);
                                                GlobalValues.activity.startActivityForResult(intent, 3);
                                                GlobalValues.activity.overridePendingTransition(R.anim.right, R.anim.left);
                                                GlobalValues.activity.finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            if (b.getParent() != null) {
                                ((ViewGroup) b.getParent()).removeView(b);
                            }
                            ll.addView(b);
                            System.out.println("+1 button!");
                            MainActivity.files = i;
                            arr.clear();
                        }
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = "Подтвердите операцию";
                        String message = "Вы действительно хотите удалить ВСЕ файлы с сохранёнными показаниями? Операция является безвозвратной!";
                        String button1String = "Подтвердить";
                        String button2String = "Отмена";

                        ad.setTitle(title);  // заголовок
                        ad.setMessage(message); // сообщение
                        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                File f;
                                int counter = 0;
                                for (int i = 1; i <= sp.getInt("savefilesnumbers", 1); i++) {
                                    f = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + i);
                                    if (f.delete()) counter++;
                                }
                                if (counter == sp.getInt("savefilesnumbers", 1)) {
                                    try {
                                        Toast.makeText(MainActivity.activity, "Все файлы успешно удалены",
                                                Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                    }

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("savefilesnumbers", 0);
                                    editor.apply();
                                    editor.commit();
                                } else {
                                    try {
                                        Toast.makeText(MainActivity.activity, "Некоторые файлы не удалены",
                                                Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                    }

                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("savefilesnumbers", 0);
                                    editor.apply();
                                    editor.commit();
                                }
                            /*LinearLayout l = new LinearLayout(getActivity());
                            l.setLayoutParams(
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT)
                            );
                            GlobalValues.ll = l;
                            if (l.getParent() != null){
                                ((ViewGroup)l.getParent()).removeView(l);
                            }
                            ScrollView scrollView = new ScrollView(getActivity());
                            scrollView.addView(l);
                            GlobalValues.sv = scrollView;*/
                                // ll.removeAllViews();
                                MainActivity.files = 0;
                                if (load.getParent() != null) {
                                    ((ViewGroup) load.getParent()).removeView(load);
                                }
                                if (button.getParent() != null) {
                                    ((ViewGroup) button.getParent()).removeView(button);
                                }
                                ll.removeAllViews();
                                ll.addView(button);
                                ll.addView(load);

                            }
                        });
                        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                            }
                        });
                        ad.setCancelable(false);
                        try {
                            ad.show();
                        } catch (Exception e) {
                        }


                    }
                });

                for (int i = 1; i <= sp.getInt("savefilesnumbers", 1); i++) {
                    File f = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + i);
                    try {
                        Scanner sc = new Scanner(f);
                        while (sc.hasNext()) {
                            arr.add(sc.nextLine());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    GlobalValues.file_choose = i;
                    final Button b = new Button(activity.getApplicationContext());
                    try {
                        b.setText(arr.get(0));
//                        b.setTextColor(R.color.colorPrimaryDark);
                    } catch (Exception e) {
                    }

                    b.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT)
                    );
                    b.getBackground().setColorFilter(Color.parseColor("#105CF0"), PorterDuff.Mode.MULTIPLY);
                    b.setTextColor(Color.parseColor("#ffffff"));
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String str = b.getText().toString();
                            for (int j = 1; j <= sp.getInt("savefilesnumbers", 1); j++) {
                                try {
                                    Scanner sc = new Scanner(new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + j));
                                    if (str.equals(sc.nextLine())) {
                                        GlobalValues.file_choose = j;
                                        GlobalValues.save = false;
                                        Intent intent = new Intent(GlobalValues.activity, Main2Activity.class);
                                        GlobalValues.activity.startActivityForResult(intent, 3);
                                        GlobalValues.activity.overridePendingTransition(R.anim.right, R.anim.left);
                                        GlobalValues.activity.finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    ll.addView(b);
                    arr.clear();
                }
                /*Button button = new Button(activity.getApplicationContext());

                button.setText("Очистить сохранённые данные");
                button.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = "Подтвердите операцию";
                        String message = "Вы действительно хотите удалить ВСЕ файлы с сохранёнными показаниями? Операция является безвозвратной!";
                        String button1String = "Подтвердить";
                        String button2String = "Отказ";

                        ad.setTitle(title);  // заголовок
                        ad.setMessage(message); // сообщение
                        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                File f;
                                int counter = 0;
                                for (int i = 1; i <= sp.getInt("savefilesnumbers", 1); i++) {
                                    f = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + i);
                                    if(f.delete())counter++;
                                }
                                if (counter == sp.getInt("savefilesnumbers", 1)){
                                    Toast.makeText(activity, "Все файлы успешно удалены",
                                            Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("savefilesnumbers", 0);
                                    editor.apply();
                                    editor.commit();
                                } else{
                                    Toast.makeText(activity, "Некоторые файлы не удалены",
                                            Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("savefilesnumbers", 0);
                                    editor.apply();
                                    editor.commit();
                                }


                            }
                        });
                        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                Toast.makeText(activity, "Отказ от операции", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                        ad.setCancelable(false);
                        ad.show();
                    }
                });


                ll.addView(button);*/
                GlobalValues.ll = ll;
                GlobalValues.sv = sv;
                sv.removeView(ll);
            }
        });
    }
}

class FileThread2 implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(500);
            if (new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data.txt").exists() &&
                    new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data2.txt").exists()) {
                Main2Activity.email = MainActivity.userInput.getText().toString().trim();
                if (GlobalValues.sdk_api >= 19) {
                    Main2Activity.sslSender = new Sender3("ncp@scryp.ru", "nGqjjRPtC4WtzZ3h", -100);
                    Main2Activity.sslSender.send("NCP-данные", "",
                            "support@neter.com", Main2Activity.email);
                    GlobalValues.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.activity, "Данные отправлены", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    GlobalValues.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.activity, "К сожалению, ваше устройство не поддерживает данную функцию", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                GlobalValues.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.activity, "Нет соединения с сервером", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
        }
    }
}

class TestServer implements Runnable {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(GlobalValues.port)) {
            Socket socket = new Socket();
            socket = serverSocket.accept();

        } catch (Exception e) {
        }
    }
}
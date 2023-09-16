package wertexpro.fvds.svaroggraphs.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimerTask;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;
import wertexpro.fvds.svaroggraphs.BluetoothChatService;
import wertexpro.fvds.svaroggraphs.Client;
import wertexpro.fvds.svaroggraphs.GlobalValues;
import wertexpro.fvds.svaroggraphs.MainActivity;
import wertexpro.fvds.svaroggraphs.R;
import wertexpro.fvds.svaroggraphs.ui.Environment;
import wertexpro.fvds.svaroggraphs.ui.gallery.GalleryFragment;
import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;

import static wertexpro.fvds.svaroggraphs.MainActivity.toggle;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LineChartView plcv;
    public static SharedPreferences sp;
    private AppBarConfiguration mAppBarConfiguration;
    AlertDialog.Builder ad;
    public static Activity activity;
    public static View view;
    public static TextView textView;
    public static View view2;
    HomeFragment home;
    private FragmentTransaction fTrans;
    CheckBox chbStack;
    public static Thread thread2;
    public static long fragmentTime;

    public static void myThread(Activity act, View v1, TextView text, View v2) {
    /*myThread(getActivity(), root, textView, root2);
        Runnable run = new MyOwnThread();
        Thread thread = new Thread(run);
        thread.start();*/
        activity = act;
        view = v1;
        textView = text;
        view2 = v2;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (GalleryFragment.thread_gallery != null)GalleryFragment.thread_gallery.interrupt();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*View view = inflater.inflate(R.layout.nav_header_conn, container, false);
        TextView text = view.findViewById(R.id.inetText);
        text.setText("kkkkk");*/
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
        toggle.syncState();
        System.out.println("Fragment RELOAD");
        GlobalValues.startDestination = "home";
       home = new HomeFragment();
       fragmentTime = new Date().getTime();
        int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false) && sp.getBoolean(ToolsFragment.KEY_FILE_AGREE, false) && permissionStatus != PackageManager.PERMISSION_DENIED) {
            File f1 = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data.txt");
            File f2 = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data2.txt");

            if (f1.exists() && f2.exists() && f1.length()!=0 && f2.length()!=0) {
                plcv = root.findViewById(R.id.chart);
                /*MainActivity m = new MainActivity();
                m.initializeCountDrawer((NavigationView) GlobalValues.activity.findViewById(R.id.nav_view));*/
                    List<String> axisData = new ArrayList<>(16);
                    List<Float> yAxisData = new ArrayList<>(16);
                    try {
                        Scanner sc = new Scanner(MainActivity.readFile(true));
                        while (sc.hasNext()) {
                            axisData.add(sc.nextLine()); //время
                        }
                        sc.close();
                        Scanner sc2 = new Scanner(MainActivity.readFile(false));
                        while (sc2.hasNext()) {
                            yAxisData.add(Float.parseFloat(sc2.nextLine())); //вольты
                        }
                        sc2.close();
                    } catch (Exception e) {
                    }

                    //String[] axisData = {"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8",
                    //        "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};
                    //float[] yAxisData = {0.12f, 0.19f, 0.26f, 0.02f, 0.43f, 0.51f, 0.64f, 0.57f, 0.87f, 0.74f, 0.97f, 1.01f,
                    //       1.07f, 0.76f, 0.93f, 0.65f, 0.81f, 0.75f, 0.97f, 1.05f, 1.12f};
                    List yAxisValues = new ArrayList();
                    List axisValues = new ArrayList();
                    boolean graph;
                    if (sp.getString(ToolsFragment.KEY_GRAPH_OPTIONS, "Кривой график").equals("Ломаный график"))graph = true;
                    else graph = false;

                    Line line = new Line(yAxisValues).setColor(Color.parseColor("#5400AF"))
                            .setCubic(!graph)
                            .setFilled(sp.getBoolean(ToolsFragment.KEY_FILLED, false));
                    for (int i = 0; i < axisData.size(); i++) {
                        axisValues.add(i, new AxisValue(i).setLabel(axisData.get(i) + "     "));
                    }

                    for (int i = 0; i < yAxisData.size(); i++) {
                        if ((sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии") ||
                                sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать всегда")) && i < axisData.size())
                            yAxisValues.add(new PointValue(i, yAxisData.get(i)).setLabel(axisData.get(i) + " - " + yAxisData.get(i) + "В"));
                        else yAxisValues.add(new PointValue(i, yAxisData.get(i)));
                    }
                    if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Не показывать"))
                        line.setHasLabels(false).setHasLabelsOnlyForSelected(false);
                    else if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии"))
                        line.setHasLabelsOnlyForSelected(true);
                    else line.setHasLabelsOnlyForSelected(false).setHasLabels(true);
                    line.setPointColor(Color.parseColor("#0B8AEB"));
                    line.setHasPoints(sp.getBoolean(ToolsFragment.KEY_POINTS, false));
                    List lines = new ArrayList();
                    if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Ромбообразный"))
                        lines.add(line.setShape(ValueShape.DIAMOND));
                    else if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Квадратный"))
                        lines.add(line.setShape(ValueShape.SQUARE));
                    else lines.add(line.setShape(ValueShape.CIRCLE));
                    LineChartData data = new LineChartData();
                    data.setLines(lines);
                    Axis axis = new Axis();
                    axis.setValues(axisValues);
                    axis.setTextSize(12);
                    axis.setTextColor(Color.parseColor("#0C07E9"));
                    axis.setName("x, метр");
                    Viewport viewport = new Viewport(plcv.getMaximumViewport());
                    viewport.top = 110;
                    viewport.right = 20;
                    plcv.setMaximumViewport(viewport);
                    plcv.setCurrentViewport(viewport);
                    data.setAxisXBottom(axis.setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                    Axis yAxis = new Axis();
                    data.setAxisYLeft(yAxis.setMaxLabelChars(4).setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                    yAxis.setTextColor(Color.parseColor("#0C07E9"));
                    yAxis.setTextSize(12);
                    yAxis.setName("U, вольт");
                    GlobalValues.home_data = data;
                    plcv.setLineChartData(data);
                    GlobalValues.build_graph = false;
                System.out.println("Файлы перезаписаны");
                /*List yAxisValues2 = new ArrayList();
                List axisValues2 = new ArrayList();
                Line line2 = new Line(axisValues).setColor(Color.parseColor("#5400AF"))
                        .setCubic(sp.getBoolean(ToolsFragment.KEY_GRAPH_OPTIONS, false))
                        .setFilled(sp.getBoolean(ToolsFragment.KEY_FILLED, false));
                for (int i = 0; i < axisData.size(); i++) {
                    axisValues2.add(new PointValue(i, axisData.get(i)).setLabel("(" + axisData.get(i) + ")"));
                }

                for (int i = 0; i < yAxisData.size(); i++) {
                    if ((sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии") ||
                            sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать всегда")) && i < axisData.size())
                        yAxisValues2.add(new PointValue(i, yAxisData.get(i) + 2).setLabel("(" + axisData.get(i) + ";" + (yAxisData.get(i) + 2) + ")"));
                    else yAxisValues2.add(new PointValue(i, yAxisData.get(i) + 2));
                }
                if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Не показывать"))
                    line2.setHasLabels(false).setHasLabelsOnlyForSelected(false);
                else if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии"))
                    line2.setHasLabelsOnlyForSelected(true);
                else line2.setHasLabelsOnlyForSelected(false).setHasLabels(true);
                line2.setPointColor(Color.parseColor("#0B8AEB"));
                line2.setHasPoints(sp.getBoolean(ToolsFragment.KEY_POINTS, false));
                List lines2 = new ArrayList();
                if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Ромбообразный"))
                    lines2.add(line2.setShape(ValueShape.DIAMOND));
                else if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Квадратный"))
                    lines2.add(line2.setShape(ValueShape.SQUARE));
                else lines2.add(line2.setShape(ValueShape.CIRCLE));
                LineChartData data2 = new LineChartData();
                data.setLines(lines2);
                Axis axis2 = new Axis();
                axis2.setValues(axisValues2);
                axis2.setTextSize(16);
                axis2.setTextColor(Color.parseColor("#0C07E9"));
                axis2.setName("pickets");
                *//*Viewport viewport2 = new Viewport(plcv.getMaximumViewport());
                viewport2.top = 110;
                viewport2.right = 20;
                plcv.setMaximumViewport(viewport);
                plcv.setCurrentViewport(viewport);*//*
                data2.setAxisXBottom(axis2.setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                Axis yAxis2 = new Axis();
                data2.setAxisYLeft(yAxis2.setMaxLabelChars(4).setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                yAxis2.setTextColor(Color.parseColor("#0C07E9"));
                yAxis2.setTextSize(16);
                yAxis2.setName("volts");
                plcv.setLineChartData(data2);*/
            } else {
                root = inflater.inflate(R.layout.fragment_gallery2, container, false);
                TextView textView = (TextView) root.findViewById(R.id.t);
                String host = sp.getString(ToolsFragment.KEY_IP_SERVER, "");
                String port = sp.getString(ToolsFragment.KEY_IP_PORT2, "");
                textView.setText("Произошла ошибка соединения с " + host + ":" + port +
                        " сервером!\nПожалуйста, проверьте подключение к локальной сети и активность порта " + port + " сервера " + host);
                GlobalValues.build_graph = false;
            }
            if (sp.getBoolean(ToolsFragment.KEY_RELOAD, false)){
                Runnable run = new FragmentLoad();
                thread2 = new Thread(run);
                thread2.start();
            }
            return root;
        } else if (permissionStatus == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView) root.findViewById(R.id.t);
            textView.setText("Построение линейного графика невозможно без разрешения на использование CD-карты.\nВы можете дать разрешение на использование файловой системы в Настройках.");
            return root;
        }else if (sp.getBoolean(ToolsFragment.KEY_FILE_AGREE, false)) {
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView) root.findViewById(R.id.t);
            textView.setText("Построение графиков невозможно без разрешения на использование Wi-Fi");
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
        } else if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false)) {
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView) root.findViewById(R.id.t);
            textView.setText("Построение графиков невозможно без разрешения на использование CD-карты");
            String title = "НЕТ ДОСТУПА К CD-КАРТЕ";
            String message = "Вы не можете использовать программу пока не разрешите доступ на использование CD-карты";
            String button1String = "Разрешить доступ";
            String button2String = "Выход";

            ad = new AlertDialog.Builder(getContext());
            ad.setTitle(title);  // заголовок
            ad.setMessage(message); // сообщение
            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getContext(), "Вы предоставили доступ к CD-карте",
                            Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                    e.apply();
                    e.commit();
                }
            });
            ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getContext(), "В доступе к файловым службам отказано", Toast.LENGTH_LONG)
                            .show();
                }
            });
            ad.setCancelable(false);
            ad.show();
            GlobalValues.build_graph = false;
            return root;
        } else {
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView) root.findViewById(R.id.t);
            textView.setText("Построение графиков невозможно без разрешения на чтение/запись на CD-карту\nПостроение графиков невозможно без разрешения на использование Wi-Fi");
            String title = "НЕТ ДОСТУПА К CD-КАРТЕ и Wi-Fi соединению";
            String message = "Вы не можете использовать программу пока не разрешите доступ на использование CD-карты и Wi-Fi служб";
            String button1String = "Разрешить доступ к обоим службам";
            String button2String = "Разрешить доступ к CD-карте";
            String button3String = "Разрешить доступ к Wi-Fi службам";

            ad = new AlertDialog.Builder(getContext());
            ad.setTitle(title);  // заголовок
            ad.setMessage(message); // сообщение
            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                    e.putBoolean(ToolsFragment.KEY_WIFI_AGREE, true);
                    e.apply();
                    e.commit();
                    reLoad(fTrans);
                    Toast.makeText(getContext(), "Вы предоставили доступ к обоим службам",
                            Toast.LENGTH_LONG).show();
                }
            });
            ad.setNeutralButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                    e.apply();
                    e.commit();
                    reLoad(fTrans);
                    Toast.makeText(getContext(), "Вы предоставили доступ к CD-карте",
                            Toast.LENGTH_LONG).show();
                }
            });
            ad.setNegativeButton(button3String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean(ToolsFragment.KEY_WIFI_AGREE, true);
                    e.apply();
                    e.commit();
                    reLoad(fTrans);
                    Toast.makeText(getContext(), "Вы предоставили доступ к Wi-Fi службам",
                            Toast.LENGTH_LONG).show();
                }
            });
            ad.setCancelable(true);
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getContext(), "В доступе к файловым и Wi-Fi службам отказано",
                            Toast.LENGTH_LONG).show();
                }
            });
            ad.show();
            GlobalValues.build_graph = false;
            return root;
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    private void reLoad(FragmentTransaction fTrans){
        try {
            fTrans = getFragmentManager().beginTransaction();
            if (sp.getBoolean(ToolsFragment.KEY_ANIM, false))fTrans.setCustomAnimations(R.anim.left, R.anim.right);
            fTrans.replace(R.id.nav_host_fragment, home);
            fTrans.commit();
        } catch (Exception e) {
            System.out.println("Thread Error");
        }
    }
    class FragmentLoad implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep((Integer.parseInt(sp.getString(ToolsFragment.KEY_INTERVAL, "30")) * 1000) - 2000);
                Runnable r = new Client();
                Thread t = new Thread(r);
                t.start();
                Thread.sleep(GlobalValues.waitForDataInMillis);
                fTrans = getFragmentManager().beginTransaction();
                if (sp.getBoolean(ToolsFragment.KEY_ANIM, false))fTrans.setCustomAnimations(R.anim.left, R.anim.right);
                fTrans.replace(R.id.nav_host_fragment, home);
                fTrans.commit();
            } catch (Exception e) {
                System.out.println("Thread Error");
            }
        }
    }
    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PackageManager.PERMISSION_GRANTED:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    try {
                        Thread.sleep(2000);
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.nav_host_fragment, home);
                        fTrans.commit();
                    } catch (Exception e) {
                        System.out.println("Thread Error");
                    }
                }
                return;
        }
    }*/
}




     /*class MyOwnThread implements Runnable{
         @Override
         public void run() {
             final Activity activity = HomeFragment.activity;
             final View root = HomeFragment.view;
             final View root2 = HomeFragment.view2;
             activity.runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     while (true){
                     final LineChartView plcv;
                     final AlertDialog.Builder ad;
                    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
                    final TextView textView = HomeFragment.textView;
                     if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false) && sp.getBoolean(ToolsFragment.KEY_FILE_AGREE, false)){
                         Runnable r = new Client();
                         Thread t = new Thread(r);
                         t.start();
                *//*boolean serveronoff;
                try {
                    Socket clientSocket = new Socket(MainActivity.sp.getString(ToolsFragment.KEY_IP_SERVER, ""),
                            Integer.parseInt(MainActivity.sp.getString(ToolsFragment.KEY_IP_PORT, "")));
                    InputStream is = clientSocket.getInputStream();
                    is.close();
                    serveronoff = true;
                }catch (Exception e){
                    serveronoff = false;
                }*//*


                         if (GlobalValues.access){
                             plcv = root.findViewById(R.id.chart);
                             List<Float> axisData = new ArrayList<>(16);
                             List<Float> yAxisData = new ArrayList<>(16);
                             try {
                                 Scanner sc = new Scanner(MainActivity.readFile(true));
                                 while (sc.hasNext()){
                                     axisData.add(Float.parseFloat(String.format(Locale.ENGLISH, "%(.2f", (Float.parseFloat(sc.nextLine()) / 100)))); //метры
                                 }
                                 sc.close();
                                 Scanner sc2 = new Scanner(MainActivity.readFile(false));
                                 while (sc2.hasNext()){
                                     yAxisData.add(Float.parseFloat(sc2.nextLine())); //вольты
                                 }
                                 sc2.close();
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }

                             //String[] axisData = {"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8",
                             //        "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};
                             //float[] yAxisData = {0.12f, 0.19f, 0.26f, 0.02f, 0.43f, 0.51f, 0.64f, 0.57f, 0.87f, 0.74f, 0.97f, 1.01f,
                             //       1.07f, 0.76f, 0.93f, 0.65f, 0.81f, 0.75f, 0.97f, 1.05f, 1.12f};
                             List yAxisValues = new ArrayList();
                             List axisValues = new ArrayList();
                             Line line = new Line(yAxisValues).setColor(Color.parseColor("#5400AF"))
                                     .setCubic(sp.getBoolean(ToolsFragment.KEY_GRAPH_OPTIONS, false))
                                     .setFilled(sp.getBoolean(ToolsFragment.KEY_FILLED, false));
                             for(int i = 0; i < axisData.size(); i++){
                                 axisValues.add(i, new AxisValue(i).setLabel("(" + axisData.get(i) + ")"));
                             }

                             for (int i = 0; i < yAxisData.size(); i++){
                                 if ((sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии") ||
                                         sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать всегда")) && i < axisData.size())
                                     yAxisValues.add(new PointValue(i, yAxisData.get(i)).setLabel("("+axisData.get(i)+";"+yAxisData.get(i)+")"));
                                 else yAxisValues.add(new PointValue(i, yAxisData.get(i)));
                             }
                             if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Не показывать"))line.setHasLabels(false).setHasLabelsOnlyForSelected(false);
                             else if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии"))line.setHasLabelsOnlyForSelected(true);
                             else line.setHasLabelsOnlyForSelected(false).setHasLabels(true);
                             line.setPointColor(Color.parseColor("#0B8AEB"));
                             line.setHasPoints(sp.getBoolean(ToolsFragment.KEY_POINTS, false));
                             List lines = new ArrayList();
                             if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Ромбообразный"))lines.add(line.setShape(ValueShape.DIAMOND));
                             else if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Квадратный"))lines.add(line.setShape(ValueShape.SQUARE));
                             else lines.add(line.setShape(ValueShape.CIRCLE));
                             LineChartData data = new LineChartData();
                             data.setLines(lines);
                             Axis axis = new Axis();
                             axis.setValues(axisValues);
                             axis.setTextSize(16);
                             axis.setTextColor(Color.parseColor("#0C07E9"));
                             axis.setName("pickets");
                             Viewport viewport = new Viewport(plcv.getMaximumViewport());
          *//*   viewport.top =110;
             viewport.right = 20;*//*
                             plcv.setMaximumViewport(viewport);
                             plcv.setCurrentViewport(viewport);
                             data.setAxisXBottom(axis.setHasLines( sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                             Axis yAxis = new Axis();
                             data.setAxisYLeft(yAxis.setMaxLabelChars(4).setHasLines( sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                             yAxis.setTextColor(Color.parseColor("#0C07E9"));
                             yAxis.setTextSize(16);
                             yAxis.setName("volts");
                             plcv.setLineChartData(data);
                         }else{
                             String host = sp.getString(ToolsFragment.KEY_IP_SERVER, "");
                             String port = sp.getString(ToolsFragment.KEY_IP_PORT, "");
                             textView.setText("Произошла ошибка соединения с " + host + ":" + port +
                                     " сервером!\nПожалуйста, проверьте подключение к локальной сети и активность порта " + port +" сервера " + host);
                         }
                     }else if (sp.getBoolean(ToolsFragment.KEY_FILE_AGREE, false)){
                         textView.setText("Построение графиков невозможно без разрешения доступа к Wi-Fi!");
                         String title = "НЕТ ДОСТУПА К Wi-Fi";
                         String message = "Вы не можете использовать программу пока не разрешите доступ на использование Wi-Fi";
                         String button1String = "Разрешить доступ";
                         String button2String = "Выход";

                         ad = new AlertDialog.Builder(activity);
                         ad.setTitle(title);  // заголовок
                         ad.setMessage(message); // сообщение
                         ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "Вы предоставили доступ к Wi-Fi",
                                         Toast.LENGTH_LONG).show();
                                 SharedPreferences.Editor e = sp.edit();
                                 e.putBoolean("wifi_agree", true);
                                 e.apply();
                                 e.commit();
                             }
                         });
                         ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "Все Wi-Fi службы дизактивтрованы", Toast.LENGTH_LONG)
                                         .show();
                             }
                         });
                         ad.setCancelable(false);
                         ad.show();
                     }else if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false)){
                         textView.setText("Построение графиков невозможно без разрешения на чтение/запись на CD-карту!");
                         String title = "НЕТ ДОСТУПА К CD-КАРТЕ";
                         String message = "Вы не можете использовать программу пока не разрешите доступ на использование CD-карты";
                         String button1String = "Разрешить доступ";
                         String button2String = "Выход";

                         ad = new AlertDialog.Builder(activity);
                         ad.setTitle(title);  // заголовок
                         ad.setMessage(message); // сообщение
                         ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "Вы предоставили доступ к CD-карте",
                                         Toast.LENGTH_LONG).show();
                                 SharedPreferences.Editor e = sp.edit();
                                 e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                                 e.apply();
                                 e.commit();
                             }
                         });
                         ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "В доступе к файловым службам отказано", Toast.LENGTH_LONG)
                                         .show();
                             }
                         });
                         ad.setCancelable(false);
                         ad.show();
                     }
                     else {
                         textView.setText("Построение графиков невозможно без разрешения на чтение/запись на CD-карту!\nПостроение графиков невозможно без разрешения доступа к Wi-Fi!");
                         String title = "НЕТ ДОСТУПА К CD-КАРТЕ и Wi-Fi соединению";
                         String message = "Вы не можете использовать программу пока не разрешите доступ на использование CD-карты и Wi-Fi служб";
                         String button1String = "Разрешить доступ к обоим службам";
                         String button2String = "Разрешить доступ к CD-карте";
                         String button3String = "Разрешить доступ к Wi-Fi службам";

                         ad = new AlertDialog.Builder(activity);
                         ad.setTitle(title);  // заголовок
                         ad.setMessage(message); // сообщение
                         ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "Вы предоставили доступ к обоим службам",
                                         Toast.LENGTH_LONG).show();
                                 SharedPreferences.Editor e = sp.edit();
                                 e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                                 e.putBoolean(ToolsFragment.KEY_WIFI_AGREE, true);
                                 e.apply();
                                 e.commit();
                             }
                         });
                         ad.setNeutralButton(button2String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "Вы предоставили доступ к CD-карте",
                                         Toast.LENGTH_LONG).show();
                                 SharedPreferences.Editor e = sp.edit();
                                 e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                                 e.apply();
                                 e.commit();
                             }
                         });
                         ad.setNegativeButton(button3String, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int arg1) {
                                 Toast.makeText(activity, "Вы предоставили доступ к Wi-Fi службам",
                                         Toast.LENGTH_LONG).show();
                                 SharedPreferences.Editor e = sp.edit();
                                 e.putBoolean(ToolsFragment.KEY_WIFI_AGREE, true);
                                 e.apply();
                                 e.commit();
                             }
                         });
                         ad.setCancelable(true);
                         ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                             public void onCancel(DialogInterface dialog) {
                                 Toast.makeText(activity, "В доступе к файловым и Wi-Fi службам отказано",
                                         Toast.LENGTH_LONG).show();
                             }
                         });
                         ad.show();
                     }
                 }
             }});
         }
     }}


*/


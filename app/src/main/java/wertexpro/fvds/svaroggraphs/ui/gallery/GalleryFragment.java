package wertexpro.fvds.svaroggraphs.ui.gallery;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import wertexpro.fvds.svaroggraphs.Client;
import wertexpro.fvds.svaroggraphs.GlobalValues;
import wertexpro.fvds.svaroggraphs.MainActivity;
import wertexpro.fvds.svaroggraphs.R;
import wertexpro.fvds.svaroggraphs.ui.Environment;
import wertexpro.fvds.svaroggraphs.ui.home.HomeFragment;
import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;

import static wertexpro.fvds.svaroggraphs.MainActivity.toggle;

public class GalleryFragment extends Fragment{
    private ColumnChartView plcv;
    private GalleryViewModel galleryViewModel;
    public static SharedPreferences sp;
    private View root = null;
    FragmentTransaction fTrans;
    public static Thread thread_gallery;
    GalleryFragment gallery;
    AlertDialog.Builder ad;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        if (HomeFragment.thread2 != null)HomeFragment.thread2.interrupt();
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        toggle.syncState();
        gallery = new GalleryFragment();
        GlobalValues.startDestination = "gallery";
        int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false) && sp.getBoolean(ToolsFragment.KEY_FILE_AGREE, false) && permissionStatus != PackageManager.PERMISSION_DENIED) {

            if (new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data.txt").exists() &&
                    new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data2.txt").exists()) {
                System.out.println("ФАЙЛЫ СУЩЕСТВУЮТ!");
                root = inflater.inflate(R.layout.fragment_gallery, container, false);
                plcv = root.findViewById(R.id.columnchart);
                //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                List<String> axisData = new ArrayList<>(16);
                List<Float> yAxisData = new ArrayList<>(16);
                try {
                    Scanner sc = new Scanner(MainActivity.readFile(true));
                    while (sc.hasNext()) {
                        axisData.add(sc.nextLine());
                    }
                    sc.close();
                    Scanner sc2 = new Scanner(MainActivity.readFile(false));
                    while (sc2.hasNext()) {
                        yAxisData.add(Float.parseFloat(sc2.nextLine()));
                    }
                    sc2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // String[] axisData = {"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8",
                //         "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};
                //float[] yAxisData = {0.12f, 0.19f, 0.26f, 0.02f, 0.43f, 0.51f, 0.64f, 0.57f, 0.87f, 0.74f, 0.97f, 1.01f,
                //        1.07f, 0.76f, 0.93f, 0.65f, 0.81f, 0.75f, 0.97f, 1.05f, 1.12f};
                List<AxisValue> axisValues2 = new ArrayList<AxisValue>();
                List<Column> columns2 = new ArrayList<Column>();
                List<SubcolumnValue> values2;
                for (int i = 0; i < yAxisData.size(); i++) {
                    values2 = new ArrayList<SubcolumnValue>();
                    values2.add(new SubcolumnValue(yAxisData.get(i), ContextCompat.getColor(MainActivity.activity, R.color.colorPrimary)).setLabel(yAxisData.get(i) + "В"));
                    //  values2.add(new SubcolumnValue(7f, ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)));
                    axisValues2.add(new AxisValue(i).setLabel(axisData.get(i) + "     "));
                    Column c = new Column(values2).setHasLabelsOnlyForSelected(true);
                    if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Не показывать"))
                        c.setHasLabels(false).setHasLabelsOnlyForSelected(false);
                    else if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии"))
                        c.setHasLabelsOnlyForSelected(true);
                    else c.setHasLabelsOnlyForSelected(false).setHasLabels(true);
                    columns2.add(c);
                }

                Axis axis = new Axis(axisValues2);
                axis.setTextSize(12);
                axis.setTextColor(Color.parseColor("#0C07E9"));
                axis.setName("x, метр");
                Axis yAxis = new Axis();
                yAxis.setTextColor(Color.parseColor("#105CF0"));
                yAxis.setTextSize(12);
                yAxis.setName("U, вольт");
                ColumnChartData columnData = new ColumnChartData(columns2);
                columnData.setStacked(true);
                columnData.setFillRatio(0.35F);
                columnData.setAxisXBottom(axis.setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
                columnData.setAxisYLeft(yAxis.setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)).setMaxLabelChars(4));
                plcv.setColumnChartData(columnData);
                GlobalValues.build_graph = false;
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
                Runnable run = new FragmentLoadGallery();
                thread_gallery = new Thread(run);
                thread_gallery.start();
            }
            return root;
        }else if (permissionStatus == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView) root.findViewById(R.id.t);
            textView.setText("Построение столбчатой диаграммы невозможно без разрешения на использование CD-карты.\nВы можете дать разрешение на использование файловой системы в Настройках.");
            return root;
        }else if (sp.getBoolean(ToolsFragment.KEY_FILE_AGREE, false)){
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView)root.findViewById(R.id.t);
            textView.setText("Построение графиков невозможно без разрешения доступа к Wi-Fi!");
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
            return root;
        }else if (sp.getBoolean(ToolsFragment.KEY_WIFI_AGREE, false)){
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView)root.findViewById(R.id.t);
            textView.setText("Построение графиков невозможно без разрешения на чтение/запись на CD-карту!");
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
        }
        else {
            root = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView)root.findViewById(R.id.t);
            textView.setText("Построение графиков невозможно без разрешения на чтение/запись на CD-карту!\nПостроение графиков невозможно без разрешения доступа к Wi-Fi!");
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
                    Toast.makeText(getContext(), "Вы предоставили доступ к обоим службам",
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
                    Toast.makeText(getContext(), "Вы предоставили доступ к CD-карте",
                            Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor e = sp.edit();
                    e.putBoolean(ToolsFragment.KEY_FILE_AGREE, true);
                    e.apply();
                    e.commit();
                }
            });
            ad.setNegativeButton(button3String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Toast.makeText(getContext(), "Вы предоставили доступ к Wi-Fi службам",
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
                    Toast.makeText(getContext(), "В доступе к файловым и Wi-Fi службам отказано",
                            Toast.LENGTH_LONG).show();
                }
            });
            ad.show();
            GlobalValues.build_graph = false;
            return root;
        }


       // plcv.setOnValueTouchListener(new ValueAnimator.AnimatorUpdateListener());

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    class FragmentLoadGallery implements Runnable{
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
                fTrans.replace(R.id.nav_host_fragment, gallery);
                fTrans.commit();
            } catch (Exception e) {
                System.out.println("Thread Error");
            }
        }
    }
}
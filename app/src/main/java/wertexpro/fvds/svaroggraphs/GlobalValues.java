package wertexpro.fvds.svaroggraphs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.LineChartData;

public class GlobalValues {
    public static AppCompatActivity app;
    public static String ip;
    public static EditText editText;
    public static boolean files_bank;
    public static TreeMap<String, List<String>> map;
    public static boolean ip_address = true;
    public static boolean nots = true;
    public static File[] myFiles;
    public static int file_choose;
    public static boolean internet = false;
    public static ScrollView sv;
    public static LinearLayout ll;
    public static boolean save = true;
    public static boolean delete_files = false;
    public static Activity activity;
    public static int sdk_api;
    public static boolean build_graph = false;
    public static LineChartData home_data;
    public static int port;
    public static boolean client_and_activity_new = true;
    public static String startDestination = "home";
    public static LayoutInflater inflater;
    public static ViewGroup container;
    public static Bundle savedInstanceState;
    public static int waitForDataInMillis = 300;
}

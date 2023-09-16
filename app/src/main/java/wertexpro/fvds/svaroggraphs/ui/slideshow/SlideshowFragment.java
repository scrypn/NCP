package wertexpro.fvds.svaroggraphs.ui.slideshow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import wertexpro.fvds.svaroggraphs.FileListOfData;
import wertexpro.fvds.svaroggraphs.GlobalValues;
import wertexpro.fvds.svaroggraphs.Main2Activity;
import wertexpro.fvds.svaroggraphs.MainActivity;
import wertexpro.fvds.svaroggraphs.R;
import wertexpro.fvds.svaroggraphs.ui.gallery.GalleryFragment;
import wertexpro.fvds.svaroggraphs.ui.home.HomeFragment;
import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;

import static wertexpro.fvds.svaroggraphs.MainActivity.activity;
import static wertexpro.fvds.svaroggraphs.MainActivity.toggle;


public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    AlertDialog.Builder ad;
    public static Button button;
    public static Button load;
    SlideshowFragment slide;
    FragmentTransaction fTrans;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        if (HomeFragment.thread2 != null)HomeFragment.thread2.interrupt();
        System.out.println("Hello, SlideShowFragment");
        if (GalleryFragment.thread_gallery != null)GalleryFragment.thread_gallery.interrupt();
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        toggle.syncState();
        GlobalValues.startDestination = "slideshow";
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        final ScrollView sv1 = GlobalValues.sv;
        sv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        final LinearLayout ll = GlobalValues.ll;
        int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus != PackageManager.PERMISSION_DENIED) {
            if (ll.getParent() != null) {
                ((ViewGroup) ll.getParent()).removeView(ll); // <- fix
            }
            slide = new SlideshowFragment();
            sv1.addView(ll);
            if (GlobalValues.save) {
                System.out.println(MainActivity.files);
                System.out.println(sp.getInt("savefilesnumbers", -1));

            }
            ScrollView sv = GlobalValues.sv;
            RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.linearLayout);
            if (sv.getParent() != null) {
                ((ViewGroup) sv.getParent()).removeView(sv); // <- fix
            }
            relativeLayout.addView(sv);
            GlobalValues.build_graph = false;
            return root;
        }else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
            View view = inflater.inflate(R.layout.fragment_gallery2, container, false);
            TextView textView = (TextView) view.findViewById(R.id.t);
            textView.setText("Загрузка данных на устройство невозможна без разрешения на использование CD-карты.\nВы можете дать разрешение на использование файловой системы в Настройках.");
            return view;
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}
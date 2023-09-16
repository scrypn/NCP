package wertexpro.fvds.svaroggraphs.ui.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.logging.Logger;

import wertexpro.fvds.svaroggraphs.GlobalValues;
import wertexpro.fvds.svaroggraphs.MainActivity;
import wertexpro.fvds.svaroggraphs.R;
import wertexpro.fvds.svaroggraphs.ui.gallery.GalleryFragment;
import wertexpro.fvds.svaroggraphs.ui.home.HomeFragment;

import static wertexpro.fvds.svaroggraphs.MainActivity.toggle;

public class ToolsFragment extends Fragment {
    public static final String KEY_WIFI_AGREE = "wifi_agree";
    public static final String KEY_EMAIL = "email";
    /*public static final String KEY_IP_AGREE = "ip_agree";*/
    public static final String KEY_FILE_AGREE = "file_agree";

    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_RELOAD = "reload";

    public static final String KEY_WIFI = "wifi";
    public static final String KEY_IP_SERVER = "ipServer";
    public static final String KEY_IP_PORT2 = "ipPort";
    public static final String KEY_LINES = "lines";
    public static final String KEY_POINTS_DATA = "point_data";
    public static final String KEY_GRAPH_OPTIONS = "graph_options2";
    public static final String KEY_FILLED = "filled";
    public static final String KEY_POINTS_FORMAT = "points_format";
    public static final String KEY_POINTS = "points";
    public static final String KEY_ANIM = "animation";
    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
        SharedPreferences.Editor e = sp.edit();
        if(permissionStatus == PackageManager.PERMISSION_DENIED){
            e.putBoolean("file_agree", false);
            e.apply();
            e.commit();
        }else {
            e.putBoolean("file_agree", true);
            e.apply();
            e.commit();
        }

        if (HomeFragment.thread2 != null)HomeFragment.thread2.interrupt();
        if (GalleryFragment.thread_gallery != null)GalleryFragment.thread_gallery.interrupt();
        MainActivity.drawer_main.closeDrawer(GravityCompat.START);
        toolsViewModel =
            ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        toggle.syncState();
        GlobalValues.startDestination = "tools";

        getFragmentManager().beginTransaction()
                .replace(R.id.settool, new Fragment1()).commit();
        GlobalValues.build_graph = false;

        return root;
    }

    public static class Fragment1 extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_settings, rootKey);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    }


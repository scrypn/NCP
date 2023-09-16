package wertexpro.fvds.svaroggraphs.ui;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Environment {
    private static final String DOUBLE = "^[-+]?\\d*\\.?\\d+$";
    private static final String INTEGER = "^[-+]?\\d+$";
    private static final Map<String, Object> map = new HashMap();

    public Environment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void put(File file) {
        try {
            Scanner sc = new Scanner(file);
            Throwable var2 = null;

            try {
                while(true) {
                    if (!sc.hasNextLine()) {
                        wertexpro.fvds.svaroggraphs.ui.Logger.info("Config file loaded");
                        break;
                    }

                    String str = sc.nextLine().trim();
                    int i = str.indexOf(61);
                    if (str.length() != 0 && str.charAt(0) != '#' && i >= 0) {
                        String key = str.substring(0, i).trim();
                        String value = str.substring(i + 1).trim();
                        if (value.matches("^[-+]?\\d+$")) {
                            map.put(key, Integer.parseInt(value));
                        } else if (value.matches("^[-+]?\\d*\\.?\\d+$")) {
                            map.put(key, Double.parseDouble(value));
                        } else {
                            map.put(key, value);
                        }
                    }
                }
            } catch (Throwable var15) {
                var2 = var15;
                throw var15;
            } finally {
                if (sc != null) {
                    if (var2 != null) {
                        try {
                            sc.close();
                        } catch (Throwable var14) {
                            var2.addSuppressed(var14);
                        }
                    } else {
                        sc.close();
                    }
                }

            }
        } catch (Exception var17) {
            Logger.error("Can't load config file" + var17.getMessage());
        }

    }

    public static void put(String key, Object obj) {
        map.put(key, obj);
    }

    public static <T> T get(String key) {
        return (T) get(key, (Object)null);
    }

    public static <T> T get(String key, T def) {
        try {
            T value = (T) map.get(key);
            return value != null ? value : def;
        } catch (ClassCastException var3) {
            return def;
        }
    }

    public static void clear() {
        map.clear();
    }
}

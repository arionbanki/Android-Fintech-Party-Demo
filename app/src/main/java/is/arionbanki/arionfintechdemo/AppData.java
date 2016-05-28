package is.arionbanki.arionfintechdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laufd_000 on 28.05.2016.
 */
public class AppData {

    private static final String FILE = "app_data";
    private static final AppData instance;

    static {
        instance = new AppData();
    }

    public static AppData getInstance() {
        return instance;
    }

    public void delete() {
        //clearAll data from memory as well
        FinTechApplication.getContext()
                .getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit();

        deleteFile();
    }

    private static boolean deleteFile() {
        String filePath = String.format("%s/shared_prefs/%s.xml", FinTechApplication
                .getContext()
                .getFilesDir()
                .getParent(), FILE);

        File f = new File(filePath);

        if (f.exists() && f.isFile()) {
            return f.delete();
        }

        return true;
    }

    public static void putString(String key, String value) {
        FinTechApplication.getContext().getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public void putAll(Map<String, String> map) {
        SharedPreferences.Editor editor = FinTechApplication
                .getContext()
                .getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .edit();
        for (String key : map.keySet()) {
            editor.putString(key, map.get(key));
        }
        editor.apply();
    }

    public static String getString(String key, String fallbackValue) {
        return FinTechApplication.getContext()
                .getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .getString(key, fallbackValue);
    }

    public static void putInt(String key, int value) {
        FinTechApplication.getContext().getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static int getInt(String key, int fallbackValue) {
        return FinTechApplication.getContext()
                .getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .getInt(key, fallbackValue);
    }

    public Map<String, String> getAll(String keyPrefix) {
        SharedPreferences preferences = FinTechApplication.getContext()
                .getSharedPreferences(FILE, Context.MODE_PRIVATE);

        Map<String, String> result = new HashMap<>();

        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().startsWith(keyPrefix)){
                result.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return result;
    }
}

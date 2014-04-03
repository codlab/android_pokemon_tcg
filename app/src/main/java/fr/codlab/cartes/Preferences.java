package fr.codlab.cartes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import java.io.File;

import fr.codlab.cartes.util.Language;

/**
 * Created by kevin on 20/03/14.
 */
public class Preferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    //creation du menu de l'application
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if("use".equals(s)){
            String res = sharedPreferences.getString(s, "en");
            SharedPreferences shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);

            if("en".equals(res)){
                shared.edit().putInt(MainActivity.USE, 0).commit();
                MainActivity.InUse = Language.US;
            }else if("fr".equals(res)){
                shared.edit().putInt(MainActivity.USE, 1).commit();
                MainActivity.InUse = Language.FR;
            }else if("sp".equals(res)){
                shared.edit().putInt(MainActivity.USE, 2).commit();
                MainActivity.InUse = Language.ES;
            }else if("it".equals(res)){
                shared.edit().putInt(MainActivity.USE, 3).commit();
                MainActivity.InUse = Language.IT;
            }else if("de".equals(res)){
                shared.edit().putInt(MainActivity.USE, 4).commit();
                MainActivity.InUse = Language.DE;
            }
        }else if("quality".equals(s)){
            int r = Math.round(sharedPreferences.getFloat(s, 0.5f)*100);
            getApplicationContext().getSharedPreferences(MainActivity.PREFS,0).edit().putInt("quality", r).commit();
        }else if("delete".equals(s)){
            deleteDirectory(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/sdcard/card_images"));
        }
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
}

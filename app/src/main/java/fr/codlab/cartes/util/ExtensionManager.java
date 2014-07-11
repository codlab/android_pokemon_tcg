package fr.codlab.cartes.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import fr.codlab.cartes.listener.IExtensionLoadedListener;

/**
 * Created by kevinleperf on 10/08/13.
 */
public class ExtensionManager {
    private static Object obj = new Object();
    private static ArrayList<Extension> _extensions;

    private static ArrayList<Extension> getExtension() {
        synchronized (obj) {
            if (_extensions == null)
                _extensions = new ArrayList<Extension>();
        }
        return _extensions;
    }

    public static synchronized Extension getExtension(Context principal, IExtensionLoadedListener listener,  int id, int nb, String intitule, String nom, boolean mustParseXml) {
        Log.d("getExtension"," "+id);
        if (getExtension().contains(new Extension(id))) {
            int idx = getExtension().indexOf(new Extension(id));
            Log.d("IndexOf",idx+"");
            return getExtension().get(idx);
        } else {
            Log.d("IndexOf","new");
            Extension tmp = new Extension(principal, listener, id, nb, intitule, nom, true);
            getExtension().add(tmp);
            return tmp;
        }
    }
    public static synchronized boolean isLoading(int id){
        if (getExtension().contains(new Extension(id))) {
            int idx = getExtension().indexOf(new Extension(id));
            Log.d("IndexOf",idx+"");
            return getExtension().get(idx).isLoading();
        }
        return false;
    }
}

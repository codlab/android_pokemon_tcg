package fr.codlab.cartes;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by kevinleperf on 09/08/13.
 */
public class TCGApplication extends Application{

    private class Download extends AsyncTask {

        private final URL _url;

        public Download(URL url){
            _url = url;
        }

        @Override
        protected Object doInBackground(Object... params) {
            if(_url != null)TCGApplication.get(_url);
            return null;
        }


    }

    private class TCGExceptionHandler implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler _system;

        public TCGExceptionHandler(){

        }

        public TCGExceptionHandler(Thread.UncaughtExceptionHandler handler){
            _system = handler;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try{
                if(ex != null){
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionCode+"_"+pInfo.versionName;

                    String message = ex.getMessage();
                    StackTraceElement [] stack = ex.getStackTrace();
                    String stack_msg = "";
                    if(stack != null){
                        for(int i=0;i< stack.length;i++){
                            if(i>0)
                                stack_msg+=";";
                            stack_msg+=stack[i].getFileName()+":"+stack[i].getClassName();
                            stack_msg+=":"+stack[i].getMethodName()+":"+stack[i].getLineNumber();
                        }
                    }
                    if(ex.getCause() != null && ex.getCause().getMessage() != null){
                        if(message != null)
                            message+=" -- "+ex.getCause().getMessage();
                        else
                            message=" -- "+ex.getCause().getMessage();
                    }
                    if(version != null)
                        version = URLEncoder.encode(version, "UTF-8");
                    else
                        version = "-";
                    if(message != null)
                        message = URLEncoder.encode(message, "UTF-8");
                    else
                        message="-";
                    stack_msg = URLEncoder.encode(stack_msg, "UTF-8");
                    Download download = new Download(new URL("http://94.125.160.65:8080/e2co/h.php?v="+version+"&m="+message+"&s="+stack_msg));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        download.execute();
                }
            }catch(Exception e){
                //sad but true...
                e.printStackTrace();
            }
            if(_system != null)
                _system.uncaughtException(thread,  ex);
        }
    }

    private final void create(){
        Thread.UncaughtExceptionHandler default_handler = Thread.getDefaultUncaughtExceptionHandler();
        if(default_handler !=null && !(default_handler instanceof TCGExceptionHandler)){
            TCGExceptionHandler new_default_handler = new TCGExceptionHandler(default_handler);
            Thread.setDefaultUncaughtExceptionHandler(new_default_handler);
        }
    }
    public TCGApplication(){
        super();
        create();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        create();
    }

    public static String get(URL url) {

        // this does no network IO
        URLConnection conn;
        try {
            conn = url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        InputStream in;
        int http_status;
        String res = null;
        try {

            // this opens a connection, then sends GET & headers
            conn.connect();

            // can't get status before getInputStream.  If you try, you'll
            //  get a nasty exception.

            // better check it first
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            // now you can try to consume the data
            in = conn.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                res = total.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
        return res;

    }

}

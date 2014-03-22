package fr.codlab.cartes.manageui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.updater.Updater;

public class DriveUi {
    final boolean blackberry = false;


    private static String CLIENT_ID = "598776849679.apps.googleusercontent.com";
    private static String CLIENT_SECRET = "K1gsxvuAK4GJzNl5v0wB1zs6";

    private static String REDIRECT_URI = "http://localhost";//"urn:ietf:wg:oauth:2.0:oob";

    private static Updater _updater = null;
    private Context c = null;
    private Activity _activity_main;
    private static ProgressDialog _progress;
    private IExtensionMaster _master = null;
    private View _v;

    GoogleAuthorizationCodeFlow flow;
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();

    //load
    //  if no token
    //    show ability to connect
    //  else
    //    if last time > 5minutes from now
    //      send event on token with the token
    //
    //on button connect click
    //  navigate
    //  if landingpage contains localhost and code
    //    close
    //    send event on token
    //  on close click
    //    close
    //
    //on token
    //  save token
    //  show message waiting
    //  sync
    //  on sync error
    //    show error message
    //    show button
    //    delete token error
    //  ok
    //    show sync ok
    //    set message waiting to synced with date
    //    save database
    //
    private GoogleCredential getGoogleCredential() {
        GoogleCredential credential = new GoogleCredential();
        credential.setAccessToken(_activity_main.getSharedPreferences("token", 0).getString("setAccessToken", null));
        credential.setExpirationTimeMilliseconds(_activity_main.getSharedPreferences("token", 0).getLong("setExpirationTimeMilliseconds", 0));
        credential.setExpiresInSeconds(_activity_main.getSharedPreferences("token", 0).getLong("setExpiresInSeconds", 3600));
        credential.setRefreshToken(_activity_main.getSharedPreferences("token", 0).getString("setRefreshToken", null));
        return credential;
    }

    private void setGoogleCredential(GoogleCredential token) {
        _activity_main.getSharedPreferences("token", 0).edit()
                .putString("setAccessToken", token.getAccessToken())
                .putLong("setExpirationTimeMilliseconds", token.getExpirationTimeMilliseconds())
                .putLong("setExpiresInSeconds", token.getExpiresInSeconds())
                .putString("setRefreshToken", token.getRefreshToken())
                .commit();
        //not save time
    }

    private void saveTime(String revision) {
        _activity_main.getSharedPreferences("token", 0).edit()
                .putLong("savedtime", System.currentTimeMillis())
                .putString("revision", revision)
                .commit();
        //not save time
    }

    private long getLocalOriginalSaved() {
        return _activity_main.getSharedPreferences("token", 0).getLong("savedtime", 0);
    }

    private boolean shouldRecreate() {
        long last_sync = _activity_main.getSharedPreferences("token", 0).getLong("time", 0);
        long actual = System.currentTimeMillis();
        if (Math.abs(last_sync - actual) > 600000) {//10minutes
            return true;
        }
        return false;
    }

    private void onLoad() {

        InternalDriveAsync syn = new InternalDriveAsync("", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            syn.executeOnExecutor(InternalDriveAsync.THREAD_POOL_EXECUTOR);
        } else {
            syn.execute(new String[]{""});
        }

    }

    /**
     * @return 0 : ok / -1 : no local file / -2 : error transmitting
     */
    private int uploadDB() {
        try {
            GoogleCredential credential = getGoogleCredential();
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

            FileList list = service.files().list().setQ("title=\'pokemon_tcg.db\'").execute();
            final List<File> files = list.getItems();

            java.io.File fileContent = new java.io.File("/data/data/" + _activity_main.getApplicationInfo().packageName + "/databases/cardmanager");
            fileContent = _activity_main.getDatabasePath("cardmanager");
            Log.d("info", fileContent.getAbsolutePath());
            FileContent mediaContent = new FileContent("application/x-sqlite3", fileContent);

            //if(!fileContent.exists())return -1;

            if (files.size() > 0) {// && files.get(0).getHeadRevisionId() > getRevision()){
                //exists
                // Send the request to the API.

                File updatedFile = service.files().update(files.get(0).getId(), files.get(0), mediaContent).execute();

                saveTime(updatedFile.getHeadRevisionId());
            } else {
                //create
                File body = new File();
                body.setTitle("pokemon_tcg.db");
                body.setDescription("Pokemon TCG Manager database");
                body.setMimeType("application/x-sqlite3");


                File file = service.files().insert(body, mediaContent).execute();
                Log.d("File ID: ", "" + file.getId());

                saveTime(file.getHeadRevisionId());
            }
            return 0;
        } catch (Exception e) {
            return -2;
        }
    }


    /**
     * @return 0 : ok / -1 : no distant file / -2 : error receiving
     */
    private int downloadDB() {
        try {
            GoogleCredential credential = getGoogleCredential();
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

            FileList list = service.files().list().setQ("title=\'pokemon_tcg.db\'").execute();
            final List<File> files = list.getItems();

            java.io.File fileContent = new java.io.File("/data/data/" + _activity_main.getApplicationInfo().packageName + "/databases/cardmanager");
            fileContent = _activity_main.getDatabasePath("cardmanager");
            Log.d("info", fileContent.getAbsolutePath());
            FileContent mediaContent = new FileContent("application/x-sqlite3", fileContent);


            if (files.size() > 0) {// && files.get(0).getHeadRevisionId() > getRevision()){
                //exists so now download it
                File file = files.get(0);
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
                                .execute();
                InputStream stream = resp.getContent();
                OutputStream io = new FileOutputStream(fileContent);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = stream.read(bytes)) != -1) {
                    io.write(bytes, 0, read);
                }

                stream.close();
                io.close();

                saveTime(file.getHeadRevisionId());

                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -2;
        }
    }

    private void updateSyncState(final String string) {
        _activity_main.runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) _v.findViewById(R.id.drive_text)).setText(string);

            }
        });
    }

    private void showUploadDownload() {
        _activity_main.runOnUiThread(new Runnable() {
            public void run() {

                _v.findViewById(R.id.drive_button_area).setVisibility(View.GONE);
                _v.findViewById(R.id.drive_download_upload_area).setVisibility(View.VISIBLE);

                _v.findViewById(R.id.drive_download).setVisibility(View.VISIBLE);
                _v.findViewById(R.id.drive_upload).setVisibility(View.VISIBLE);

            }
        });
    }

    private void showUpload() {
        _activity_main.runOnUiThread(new Runnable() {
            public void run() {
                _v.findViewById(R.id.drive_button_area).setVisibility(View.GONE);
                _v.findViewById(R.id.drive_download_upload_area).setVisibility(View.VISIBLE);

                _v.findViewById(R.id.drive_download).setVisibility(View.INVISIBLE);
                _v.findViewById(R.id.drive_upload).setVisibility(View.VISIBLE);

            }
        });

    }

    private void showAskForLogin() {
        _activity_main.runOnUiThread(new Runnable() {
            public void run() {
                _v.findViewById(R.id.drive_button).setVisibility(View.VISIBLE);
                _v.findViewById(R.id.drive_button_area).setVisibility(View.VISIBLE);
                _v.findViewById(R.id.drive_download_upload_area).setVisibility(View.GONE);
            }
        });
    }

    private void showSyncPossible() {
        _activity_main.runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) _v.findViewById(R.id.drive_text)).setText("You are currently loged with a google account");
                _v.findViewById(R.id.drive_button).setVisibility(View.GONE);
            }
        });
    }

    private void loadSync(GoogleCredential new_token) {
        updateSyncState("Syncing ...");
        setGoogleCredential(new_token);
        long distance_update_time = sync();
        if (distance_update_time >= 0) {
            //sync ok
            if (distance_update_time != 0 && getLocalOriginalSaved() != 0) {
                updateSyncState("Online and local database found. Choose what to do : upload your local version or download the local one - data can be erased!");
                showUploadDownload();
            } else if (distance_update_time == 0) {
                updateSyncState("No online version found - if you have data to save, you should upload them");
                showUpload();
            } else if (getLocalOriginalSaved() != 0) {
                updateSyncState("No distant file have been found but you currently have a donwloaded local file - could be 1/ network issues 2/ you downloaded the database from an over account");
                showUploadDownload();
            } else {
                updateSyncState("An online version exists which seems not have been download locally - but you can also have local data which were never upload. Choose what to do");
                showUploadDownload();
            }
        } else {
            updateSyncState("error while syncing the file - network issue? did you log to your account?");
            showAskForLogin();
        }
        //  sync
        //  on sync error
        //    show error message
        //    show button
        //    delete token error
        //  ok
        //    show sync ok
        //    set message waiting to synced with date
        //    save database
    }


    private class InternalDriveAsyncUpload extends AsyncTask<String, Boolean, Integer> {
        InternalDriveAsyncUpload() {
        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                return uploadDB();
            } catch (Exception e) {
                return -2;
            }
        }

        public void onPostExecute(Integer result) {
            if (result == null || result == -2) {
                Toast.makeText(_activity_main, "An error occured while uploading the data", Toast.LENGTH_LONG).show();
            } else if (result == 0) {
                Toast.makeText(_activity_main, "local database uploaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(_activity_main, "No local database to upload", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class InternalDriveAsyncDownload extends AsyncTask<String, Boolean, Integer> {
        InternalDriveAsyncDownload() {
        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                return downloadDB();
            } catch (Exception e) {
                return -2;
            }
        }

        public void onPostExecute(Integer result) {
            if (result == null || result == -2) {
                Toast.makeText(_activity_main, "An error occured while downloading the data", Toast.LENGTH_LONG).show();
            } else if (result == 0) {
                Toast.makeText(_activity_main, "Distant database downloaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(_activity_main, "No distant database to download", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class InternalDriveAsync extends AsyncTask<String, Boolean, Boolean> {
        private String _code;
        private boolean _download;

        InternalDriveAsync(String code) {
            _code = code;
            _download = true;
        }

        InternalDriveAsync(String code, boolean download) {
            _code = code;
            _download = download;
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                if (_download) {
                    GoogleTokenResponse response = flow.newTokenRequest(_code).setRedirectUri(REDIRECT_URI).execute();
                    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

                    loadSync(credential);
                } else {
                    //onLoad async
                    GoogleCredential token = getGoogleCredential();
                    if (token != null) {
                        showSyncPossible();
                        if (shouldRecreate()) {
                            loadSync(token);
                        }
                    } else {
                        showAskForLogin();
                    }

                }
            } catch (Exception e) {
                showAskForLogin();
            }

            return true;
        }
    }

    private void onLocalhostUrlDetected(String url) {
        String code = url.split("code=")[1];
        code = code.split("&")[0];


        InternalDriveAsync syn = new InternalDriveAsync(code);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            syn.executeOnExecutor(InternalDriveAsync.THREAD_POOL_EXECUTOR);
        } else {
            syn.execute(new String[]{code});
        }
    }

    private long sync() {
        try {
            final GoogleCredential credential = getGoogleCredential();
            //TODO implement sync file
            //Create a new authorized API client
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

            FileList list = service.files().list().setQ("title=\'pokemon_tcg.db\'").execute();
            final List<File> files = list.getItems();

            if (files.size() > 0) {// && files.get(0).getHeadRevisionId() > getRevision()){
                DateTime updated = files.get(0).getModifiedDate();
                return updated.getValue();
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    private void openWebViewDialog() {

    }


    private DriveUi() {

        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();
    }

    public DriveUi(Activity activity_main, IExtensionMaster master, View v) {
        this();
        _activity_main = activity_main;
        _master = master;
        c = v.getContext();
        implement(v);
        _progress = null;
        setThis();
    }

    private void setThis() {
        //if(_updater == null)
        //	_updater = new Updater(this);
        //_updater.setParent(this);
    }

    public void implement(final View v) {
        _v = v;
        setThis();
        c = v.getContext();
        SharedPreferences s = c.getSharedPreferences("__TCGMANAGER__", Context.MODE_PRIVATE);


        EditText email = null;
        EditText password = null;
        if (blackberry) {
            email = (EditText) v.findViewById(R.id.drive_blackberry_email);
            password = (EditText) v.findViewById(R.id.drive_blackberry_password);

            View blackberry_specific = v.findViewById(R.id.drive_blackberry);
            blackberry_specific.setVisibility(View.VISIBLE);
        } else {
            View blackberry_specific = v.findViewById(R.id.drive_blackberry);
            blackberry_specific.setVisibility(View.GONE);
        }
        final EditText _email = email;
        final EditText _password = password;

        Button download = (Button) v.findViewById(R.id.drive_download);
        download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v2) {
                InternalDriveAsyncDownload upload = new InternalDriveAsyncDownload();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    upload.executeOnExecutor(InternalDriveAsync.THREAD_POOL_EXECUTOR);
                } else {
                    upload.execute(new String[]{""});
                }

            }
        });

        Button upload = (Button) v.findViewById(R.id.drive_upload);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v2) {
                InternalDriveAsyncUpload upload = new InternalDriveAsyncUpload();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    upload.executeOnExecutor(InternalDriveAsync.THREAD_POOL_EXECUTOR);
                } else {
                    upload.execute(new String[]{""});
                }

            }
        });

        Button button = (Button) v.findViewById(R.id.drive_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v2) {


                String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

                //create webview

                WebView _web = null;//(WebView) v.findViewById(R.id.drive_webview));
                if (false && v.findViewById(R.id.drive_web) != null) {
                    final WebView web = (WebView) v.findViewById(R.id.drive_web);
                    _web = web;
                    web.clearCache(true);
                    web.clearHistory();
                    web.getSettings().setJavaScriptEnabled(true);
                    web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                    web.setWebViewClient( new WebViewClient() {
                        @Override
                        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            _activity_main.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (url.indexOf("http://localhost") == 0) {
                                        onLocalhostUrlDetected(url);
                                        //dialog.dismiss();
                                        web.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    });

                    web.loadUrl(url);
                    web.setVisibility(View.VISIBLE);
                } else {
                    //_web = new WebView(c);
                    //AlertDialog.Builder alert = new AlertDialog.Builder(c);
                    Dialog alert = new Dialog(_activity_main);
                    alert.setTitle("Connect to your drive account");
                    alert.setContentView(R.layout.main_drive_webview);
                    _web = (WebView)alert.findViewById(R.id.drive_web);

                    final WebView web = _web;

                    //web.setWebChromeClient(new InredisChromeClient(this));
                    //web.setWebViewClient(new InredisWebViewClient(this));
                    web.clearCache(true);
                    web.clearHistory();
                    web.getSettings().setJavaScriptEnabled(true);
                    web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


                    web.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_UP:
                                    if (!v.hasFocus()) {
                                        v.requestFocus();
                                    }
                                    break;
                            }
                            return false;
                        }
                    });

                    //create dialog

                    //wv.setWebViewClient(new WebViewClient(){
                    //    @Override
                    //    public boolean shouldOverrideUrlLoading(WebView view, String url){
                    //        view.loadUrl(url);
                    //        return true;
                    //    }
                    //});

                    //alert.addContentView(web,new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,600));
                    alert.setCancelable(true);
                    //.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    //    @Override
                    //    public void onClick(DialogInterface dialog, int id) {
                    //        dialog.dismiss();
                    //    }
                    //});

                    final Dialog dialog = alert;
                    alert.show();
                    if (false && blackberry) {
                        Thread t = new Thread() {
                            public void run() {

                                try {
                                    Thread.sleep(1500);
                                } catch (Exception e) {
                                }
                                InputMethodManager inputMethodManager = (InputMethodManager) _activity_main
                                        .getSystemService(_activity_main.INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInputFromWindow(
                                        v.getApplicationWindowToken(),
                                        InputMethodManager.SHOW_FORCED, 0);

                                web.requestFocus(View.FOCUS_DOWN);
                                web.requestFocus();
                            }
                        };
                        t.start();
                    }

                    web.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(final WebView view, final String url) {
                            super.onPageFinished(view, url);

                            if (blackberry && url.indexOf("/ServiceLogin") >= 0) {
                                Thread t = new Thread() {
                                    public void run() {

                                        try {
                                            Thread.sleep(1200);
                                        } catch (Exception e) {
                                        }
                                        view.loadUrl("javascript:alert('" + _email.getText().toString() + "');void(0)");
                                        view.loadUrl("javascript:document.getElementById('Email').value ='" + _email.getText().toString() + "';void(0);");
                                        view.loadUrl("javascript: document.getElementById('Passwd').value ='" + _password.getText().toString() + "';void(0);");
                                    }
                                };
                                t.start();
                            }

                        }

                        @Override
                        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                            super.onPageStarted(view, url, favicon);
                            _activity_main.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (url.indexOf("http://localhost") == 0) {
                                        onLocalhostUrlDetected(url);
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
                _web.loadUrl(url);

            }
        });

        onLoad();


    }

}

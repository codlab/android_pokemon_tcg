package fr.codlab.cartes.dl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.codlab.cartes.R;
import fr.codlab.cartes.fragments.MagikarpFragment;

/**
 * From an Activity, Download a file and show it to the user
 * 
 * @author kevin le perf
 *
 */
public class DownloaderMagikarp implements IDownloadFile {
	private Activity _parent;
	private static ProgressDialog _download_progress;
	private static MagikarpAsync activatingMagikarp;
	private String _url;
	private Boolean _finished;

	public DownloaderMagikarp(MagikarpFragment magikarpFragment, Activity parent, String url){
		_parent = parent;
		_url = url;
		_finished = true;
    }

	private void activatingCreate(){
		if(!_finished){
			_download_progress = new ProgressDialog(_parent);
			_download_progress.setMessage(_parent.getString(R.string.connect_magikarp_activating));
			_download_progress.setIndeterminate(true);
			_download_progress.setCancelable(false);
			_download_progress.show();
		}
	}


	public void activatingReLoad(){
		if(activatingMagikarp != null){
			this.activatingCreate();
		}
	}

	public void activatingQuit(){
		if(_download_progress != null)
			_download_progress.dismiss();
        _download_progress = null;
	}


    public void activatingOnResume(){
        if(_download_progress == null && !_finished){
            activatingCreate();
            //activatingMagikarp = new MagikarpAsync();
            //activatingMagikarp.execute(new Object[]{_url});
        }
    }

    public void activatingOnCreate(){
		if(_download_progress == null && _finished){
            _finished = false;
            activatingCreate();
            activatingMagikarp = new MagikarpAsync();
            activatingMagikarp.execute(new Object[]{_url});
		}
	}

	public void receiveProgress(String msg, Double args){
		String str = args.toString();
		if(_download_progress != null && !_finished){
			_download_progress.setMessage(str.subSequence(0, str.length()-1)+msg);
			_download_progress.setProgress((int)(args*1000));
		}
	}

	public void onPost(Long result){
		_download_progress.dismiss();
		_download_progress = null;
        activatingMagikarp = null;
		_finished = true;
	}

	public void onErrorSd(){
	}

	@Override
	public void onErrorUrl() {
	}


    public boolean isFinished(){
        return _finished;
    }

    private class MagikarpAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader reader_in = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(reader_in);
                String res = "";

                while((res = reader.readLine()) != null){
                    Log.d("Downloader", res);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }

            _finished = true;
            if(_parent != null){
                _parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(_parent,
                                _parent.getString(R.string.connect_magikarp_unlocked),
                                Toast.LENGTH_LONG).show();
                        activatingQuit();
                    }
                });
            }


            return null;
        }
    }
}

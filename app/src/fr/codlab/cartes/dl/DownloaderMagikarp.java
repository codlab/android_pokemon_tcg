package fr.codlab.cartes.dl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
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
	private Boolean _finish;

	public DownloaderMagikarp(MagikarpFragment magikarpFragment, Activity parent, String url){
		_parent = parent;
		_url = url;
		_finish = false;

		activating();
	}

	public void activatingCreate(){
		if(!_finish){
			_download_progress = new ProgressDialog(_parent);
			_download_progress.setMessage(_parent.getString(R.string.connect_magikarp_activating));
			_download_progress.setIndeterminate(true);
			_download_progress.setCancelable(false);
			_download_progress.show();
		}
	}

	public void activatingLoad(){
		if(activatingMagikarp != null){
			this.activatingCreate();
		}
	}

	public void activatingQuit(){
		if(_download_progress != null)
			_download_progress.dismiss();
        _download_progress = null;
	}

	public void activating(){
		if(_download_progress == null && !_finish){
            activatingCreate();
            activatingMagikarp = new MagikarpAsync();
            activatingMagikarp.execute(new Object[]{_url});
		}
	}

	public void receiveProgress(String msg, Double args){
		String str = args.toString();
		if(_download_progress != null && !_finish){
			_download_progress.setMessage(str.subSequence(0, str.length()-1)+msg);
			_download_progress.setProgress((int)(args*1000));
		}
	}

	public void onPost(Long result){
		_download_progress.dismiss();
		_download_progress = null;
        activatingMagikarp = null;
		_finish = true;
	}

	public void onErrorSd(){
	}

	@Override
	public void onErrorUrl() {
	}



    private class MagikarpAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(_url);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }

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

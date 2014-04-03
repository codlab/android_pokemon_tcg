package fr.codlab.cartes.manageui;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.widget.TextView;

import fr.codlab.cartes.R;
import fr.codlab.cartes.dl.Downloader;
import fr.codlab.cartes.dl.DownloaderFactory;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.ExtensionManager;

/**
 * Class made to manage how a specified Ui must be modified with set data
 * @author kevin le perf
 *
 */
final public class ExtensionUi {
	private int _id;
	private String _name;
	private String _intitule;
	private static Downloader _downloader;
	private SherlockFragmentActivity _activity;
	private Extension _extension;
	private boolean to_change;

	public ExtensionUi(SherlockFragmentActivity activity){
		this();
		setActivity(activity);
	}

	private ExtensionUi(){
		
	}
	public void setActivity(SherlockFragmentActivity activity){
		_activity = activity;
	}
	public void define(String name, int id, String intitule) {
		_name = name;
		_id=id;
		_intitule=intitule;
		to_change = true;
		update();
	}

	private void update(){
		if(_activity != null && to_change){
			_extension = ExtensionManager.getExtension(_activity.getApplication().getApplicationContext(), null, _id, 0, _intitule, _name, true);
			to_change = false;
		}
	}
	public Extension getExtension(){
		update();
		return _extension;
	}
	
	public void onPause(){
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public void onResume(){
		if(_downloader != null)
			_downloader.downloadLoad();
	}

	public void onDestroy(){
		if(_downloader != null)
			_downloader.downloadQuit();
	}

	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.extension_download:
			_downloader = DownloaderFactory.downloadFR(_activity,_intitule);
			return true;
		case R.id.extension_downloadus:
			_downloader = DownloaderFactory.downloadUS(_activity,_intitule);
			return true;
		default:
			return false;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.extensionmenu, menu);

		return true;
	}
	
    public void updateName(TextView v, String nom){
		v.setText(nom);
    }
    
    public void updateProgress(final TextView v, final int t, final int m){
        _activity.runOnUiThread(new Runnable(){
            public void run(){
                v.setText(" "+t+"/"+m);
            }
        });
    }
    
    public void updatePossessed(final TextView v, final int p){
        _activity.runOnUiThread(new Runnable(){
            public void run(){
                v.setText(" "+p);
            }
        });
    }
}

package fr.codlab.cartes;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.manageui.ExtensionUi;
import fr.codlab.cartes.util.Extension;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Classe de visualisation des cartes d'une extension
 *
 * @author kevin le perf
 *
 */
public class ExtensionActivity extends SherlockFragmentActivity implements IExtensionListener {
	private static ExtensionUi _factorise;
	private Extension _extension;
	public ExtensionActivity(){

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Bundle objetbunble  = this.getIntent().getExtras();

        int _id = 0;
        String _nom="";
        String _intitule="";
        if (objetbunble != null && objetbunble.containsKey("extension")) {
            _id = (objetbunble.getInt("extension",0));
        }

        if (objetbunble != null && objetbunble.containsKey("nom")) {
            _nom = this.getIntent().getStringExtra("nom");
        }
        if (objetbunble != null && objetbunble.containsKey("intitule")) {
            _intitule = this.getIntent().getStringExtra("intitule");
        }

        if(_factorise == null)
			_factorise = new ExtensionUi(this);
		_factorise.setActivity(this);
		_factorise.define(_nom, _id, _intitule);
		this.setContentView(R.layout.extension);
		//mise a jour du nom de l'extension et des informations
		//du nombre de cartes possedees
		_extension = _factorise.getExtension();
		updateName(_nom);
		updateProgress(_extension.getProgress(),_extension.getCount());
		updatePossessed(_extension.getPossessed());

		//liste des images
		ExtensionListeAdapter _adapter = new ExtensionListeAdapter(this, this, _extension);
		ListView _liste = (ListView)findViewById(R.id.visu_extension_liste);
		_liste.setAdapter(_adapter);
	}


	public void onPause(){
		super.onPause();
		_factorise.onPause();
	}

	public void onResume(){
		super.onResume();
		_factorise.onResume();
	}

	public void onDestroy(){
		super.onDestroy();
		_factorise.onDestroy();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return _factorise.onCreateOptionsMenu(menu, getSupportMenuInflater());
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(_factorise.onOptionsItemSelected(item) == false)
			return super.onOptionsItemSelected(item);
		else
			return true;
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent i){
		try{
			super.onActivityResult(requestCode, resultCode, i);
			if(requestCode == 42 && _extension != null){
				updated(_extension.getId());
			}
		}catch(Exception e)
		{
		}
	} 
	
	@Override
	public void onClick(Bundle pack) {
		Intent intent = new Intent().setClass(this, CardActivity.class);
		intent.putExtras(pack);
		startActivityForResult(intent,42);
	}
	@Override
	public void updateName(String nom) {
		_factorise.updateName(((TextView)findViewById(R.id.visu_extension_nom)), nom);
	}

	@Override
	public void updateProgress(int progression, int count) {
		_factorise.updateProgress(((TextView)findViewById(R.id.visu_extension_cartes)), progression, count);
	}

	@Override
	public void updatePossessed(int total) {
		_factorise.updatePossessed(((TextView)findViewById(R.id.visu_extension_possess)), total);
	}
	
	@Override
	public void updated(int id) {
		_extension = _factorise.getExtension();
		_extension.updatePossessed();
		updateProgress(_extension.getProgress(),_extension.getCount());
		updatePossessed(_extension.getPossessed());
		ListView _liste = (ListView)findViewById(R.id.visu_extension_liste);
		if(_liste.getAdapter() != null && _liste.getAdapter() instanceof ExtensionListeAdapter)
			((ExtensionListeAdapter)_liste.getAdapter()).notifyDataSetChanged();
		
		Bundle bundle = new Bundle();
		bundle.putInt("update", id);
		Intent i = new Intent();
		i.putExtras(bundle);
		setResult(RESULT_OK, i); 
	}
}

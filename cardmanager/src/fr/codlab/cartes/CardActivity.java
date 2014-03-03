package fr.codlab.cartes;



import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListImageAdapter;
import fr.codlab.cartes.manageui.CarteUi;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.ExtensionManager;
import fr.codlab.cartes.widget.Gallery3D;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * CLasse de visualisation d'une carte
 * 
 * 
 * @author kevin
 *
 */
public class CardActivity extends SherlockFragmentActivity implements IClickBundle, IExtensionMaster{
	private CarteUi _factorise;
	private Extension _extension;
	private Gallery3D gallery;
	private Bundle _bundle;

	public CardActivity(){
		_factorise = new CarteUi(null);
	}


	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putBundle("BUNDLE", _bundle);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle){
		if(bundle.containsKey("BUNDLE")){
			_bundle = bundle.getBundle("BUNDLE");
		}
		
		createUi();

		if(gallery != null){
			gallery.setSelection(((Card)_bundle.getSerializable("card")).getCarteIdInt()-1);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.visucarte);
		_bundle  = this.getIntent().getExtras();

		if(_bundle.containsKey("BUNDLE")){
			_bundle = _bundle.getBundle("BUNDLE");
		}
		
		if (_bundle != null && _bundle.containsKey("intitule")) {
			_factorise.setSetShortName(this.getIntent().getStringExtra("intitule"));
		}
		_extension = ExtensionManager.getExtension(this.getApplicationContext(), _bundle.getInt("extension"), 0, _factorise.getSetShortName(), "", true);
		createUi();

		if(findViewById(R.id.visucarte_gallery) != null){
   
			gallery = (Gallery3D)findViewById(R.id.visucarte_gallery);
			ExtensionListImageAdapter coverImageAdapter =  new ExtensionListImageAdapter(this.getApplicationContext(),  _extension);
			gallery.setAdapter(coverImageAdapter);
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					onClick(createBundle(position, true));
				}
			});
			gallery.setSelection(((Card)_bundle.getSerializable("card")).getCarteIdInt()-1);
		}
	}

	public Bundle createBundle(int _pos,boolean imgVue){
		return _factorise.createBundle(_pos, imgVue, _extension);
	}

	@Override
	public void onClick(Bundle pack){
		_factorise = new CarteUi(null);
		_bundle = pack;
		createUi();
	}

	public void createUi(){
		//chargement de la carte
		if(_extension != null)
			_factorise.setExtension(_extension);
		
		_factorise.setParent(this);
		if(_bundle != null && _bundle.containsKey("card"))
			_factorise.setCard((Card) _bundle.getSerializable("card"));

		//tout visible ? - actuellement toujours vrai
		if (_bundle != null && _bundle.containsKey("visible")) {
			_factorise.setAllObjectVisible(this.getIntent().getBooleanExtra("visible",true));
		}

		//intitule
		if (_bundle != null && _bundle.containsKey("intitule")) {
			_factorise.setSetShortName(this.getIntent().getStringExtra("intitule"));
		}

		//affichage du texte ou de l'image? - showNext = true > image
		if (_bundle != null && _bundle.containsKey("next")) {
			_factorise.showImageAtFirst(true);
		}

		//mise en forme avec le pager
		_factorise.setContext(this.findViewById(R.id.visucarte_top));
		_factorise.manageFirstPopulate();
	}


	@Override
	public void update(int extension_id) {
		Bundle bundle = new Bundle();
		bundle.putInt("update", extension_id);
		Intent i = new Intent();
		i.putExtras(bundle);
		setResult(RESULT_OK, i); 
	}


	@Override
	public void onClick(String nom, int id, String intitule) {
	}


	@Override
	public void notifyDataChanged() {
	}
}

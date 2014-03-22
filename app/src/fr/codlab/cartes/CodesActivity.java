package fr.codlab.cartes;



import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.ExtensionListImageAdapter;
import fr.codlab.cartes.manageui.CarteUi;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.widget.Gallery3D;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
public class CodesActivity extends SherlockFragmentActivity{

	/*@Override
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
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.codesactivity);
	}

}

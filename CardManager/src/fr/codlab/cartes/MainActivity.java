package fr.codlab.cartes;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.PrincipalExtensionAdapter;
import fr.codlab.cartes.fragments.CardFragment;
import fr.codlab.cartes.fragments.CodesFragment;
import fr.codlab.cartes.fragments.InformationScreenFragment;
import fr.codlab.cartes.fragments.ExtensionFragment;
import fr.codlab.cartes.fragments.ListViewExtensionFragment;
import fr.codlab.cartes.redeemcode.IGetLogin;
import fr.codlab.cartes.redeemcode.ITextCode;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.FileMover;
import fr.codlab.cartes.util.Language;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.view.MenuInflater;
import com.slidingmenu.lib.SlidingMenu;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
/**
 * Classe de dŽmarrage de l'application
 * 
 * utilise un Pager
 * premi�re frame : information textuelle
 * deuxi�me : liste des extensions
 * a venir : troisieme : liste des codes boosters online
 * 
 * @author kevin
 * 
 *
 */
public class MainActivity extends SlidingViewPagerFragmentActivity implements IExtensionMaster, IGetLogin, ITextCode{
	public static final int MAX=60;
	private ArrayList<Extension> _arrayExtension;
	public final static String PREFS = "_CODLABCARTES_";
	public final static String USE = "DISPLAY";
	public static Language InUse = Language.US;
	private final static int US=0;
	private final static int FR=1;
	private final static int ES=2;
	private final static int IT=3;
	private final static int DE=4;

	/**
	 * The Acitivty receive an intent
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent i){
		try{
			super.onActivityResult(requestCode, resultCode, i);
			if(i!=null){
				Bundle bd = i.getExtras();
				//on observe les modifications apportees
				int miseAjour = bd.getInt("update",0);
				if( miseAjour >= 0){
					update(miseAjour);
				}
			}
		}catch(Exception e)
		{
		}
	} 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		/**
		 * open sliding menu
		 */
		this.setBehindContentView(R.layout.main_sliding);



		SharedPreferences shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
		if(!shared.contains(MainActivity.USE)){
			if("FR".equals(this.getString(R.string.lang))){
				shared.edit().putInt(MainActivity.USE, MainActivity.FR).commit();
				InUse = Language.FR;
			}else if("ES".equals(this.getString(R.string.lang))){
				shared.edit().putInt(MainActivity.USE, MainActivity.ES).commit();
				InUse = Language.ES;
			}else if("IT".equals(this.getString(R.string.lang))){
				shared.edit().putInt(MainActivity.USE, MainActivity.IT).commit();
				InUse = Language.IT;
			}else if("DE".equals(this.getString(R.string.lang))){
				shared.edit().putInt(MainActivity.USE, MainActivity.DE).commit();
				InUse = Language.DE;
			}else{
				shared.edit().putInt(MainActivity.USE, MainActivity.US).commit();
				InUse = Language.US;
			}
		}else{
			switch(shared.getInt(MainActivity.USE, MainActivity.US)){
			case MainActivity.FR:
				InUse=Language.FR;break;
			case MainActivity.DE:
				InUse=Language.DE;break;
			case MainActivity.ES:
				InUse=Language.ES;break;
			case MainActivity.IT:
				InUse=Language.IT;break;
			default:
				InUse=Language.US;break;
			}
		}

		createExtensions();
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		View mobile = findViewById( R.id.mobile );
		//on est sur tablette
		//donc gestion avec les fragments

		if(mobile != null){

			SlidingMenu sm = getSlidingMenu();
			sm.setMode(SlidingMenu.LEFT);
			sm.setSlidingEnabled(true);
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setBehindWidthRes(R.dimen.slidingmenu_offset);
			sm.setFadeDegree(0.90f);
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			setSlidingActionBarEnabled(false);
			sm.setSlidingEnabled(true);  

			ListViewExtensionFragment viewer = (ListViewExtensionFragment) getSupportFragmentManager().findFragmentById(R.id.liste_extension_fragment);
			viewer.setListExtension(this);
		}else{
			SlidingMenu sm = getSlidingMenu();
			sm.setSlidingEnabled(false);  


			//on est sur tablette
			//donc gestion avec les fragments
			ListViewExtensionFragment viewer = (ListViewExtensionFragment) getSupportFragmentManager().findFragmentById(R.id.liste_extension_fragment);
			viewer.setListExtension(this);
		}
	}

	/**
	 * Call after onResume or onCreate
	 */
	@Override
	public void onStart(){
		super.onStart();

		/**
		 * open menu
		 */
		if(getSlidingMenu().isSlidingEnabled()){
			Thread t = new Thread(){
				public void run(){
					try{
						Thread.sleep(1000);
					}catch(Exception e){
					}
					runOnUiThread(new Runnable(){
						public void run(){
							if(false == getSlidingMenu().isMenuShowing())
								toggle();
						}
					});
				}
			};
			t.start();
		}

		//on rajoute le fragment si on est sur tablette
		Log.d("MainActivity","findView lol 1");
		if(findViewById( R.id.liste_extension_fragment ) != null && getSupportFragmentManager().getBackStackEntryCount() == 0){
			Log.d("MainActivity","findView lol 2");
			FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
			xact.add(R.id.extension_fragment, new InformationScreenFragment(this));
			xact.commit();
		}
	}
	private void createExtensions(){
		if(_arrayExtension != null)
			return;

		_arrayExtension = new ArrayList<Extension>();

		XmlPullParser parser = getResources().getXml(R.xml.extensions);
		//StringBuilder stringBuilder = new StringBuilder();
		//  <extension nom="Base" nb="1" id="id de l'extension" intitule="tag pour les images" />
		try {
			int id=0;
			int nb = 0;
			String intitule="";
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				intitule = "";
				id=0;
				nb=0;
				String name = parser.getName();
				String extension = null;
				if((name != null) && name.equals("extension")) {
					int size = parser.getAttributeCount();
					for(int i = 0; i < size; i++) {
						String attrNom = parser.getAttributeName(i);
						String attrValue = parser.getAttributeValue(i);
						if((attrNom != null) && attrNom.equals("nom")) {
							extension = attrValue;
						} else if ((attrNom != null) && attrNom.equals("id")) {
							try{
								id = Integer.parseInt(attrValue);
							}
							catch(Exception e){
								id=0;
							}
						} else if ((attrNom != null) && attrNom.equals("nb")) {
							try{
								nb = Integer.parseInt(attrValue);
							}
							catch(Exception e){
								nb=0;
							}
						} else if ((attrNom != null) && attrNom.equals("intitule")) {
							intitule = attrValue;
							
							FileMover.Move(Environment.getExternalStorageDirectory().getAbsolutePath(), intitule);
						}
					}

					if((extension != null) && (id > 0 && id<MAX)) {
						_arrayExtension.add(new Extension(this, id, nb, intitule, extension, false));
					}
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	PrincipalExtensionAdapter _adapter;
	public void setListExtension(View v){
		_adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
		ListView _list = (ListView)v.findViewById(R.id.principal_extensions);
		_list.setAdapter(_adapter);
	}
	public void notifyChanged(){
		_adapter.notifyDataSetChanged();
	}

	public void notifyDataChanged(){
		for(int ind = 0;ind<_arrayExtension.size();_arrayExtension.get(ind).updatePossessed(), ind++);
		notifyChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.principalmenu, menu);
		boolean state = super.onCreateOptionsMenu(menu);

		return state;
	}

	//creation du menu de l'application
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences _shared = null;

		switch (item.getItemId()) {
		//modification en mode US
		case android.R.id.home:
			if(_carte != null || _extension != null || _codes != null){
				FragmentManager fm = getSupportFragmentManager();
				fm.popBackStack();
				if(_carte != null){
					_carte = null;
					setViewPagerSelected(0);
				}else{
					if(_codes != null){
						_codes = null;
						getSupportActionBar().setDisplayHomeAsUpEnabled(false);
					}
					if(_extension != null){
						_extension = null;
						getSupportActionBar().setDisplayHomeAsUpEnabled(false);
					}
				}
			}
			return true;
		case R.principal.paypal:
			Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=SEJ9ZE6WLG2H4");
			startActivity(new Intent(Intent.ACTION_VIEW,uri));
			return true;
		case R.principal.useus:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.US).commit();
			MainActivity.InUse = Language.US;
			return true;
			//modification en mode fr
		case R.principal.usefr:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.FR).commit();
			MainActivity.InUse = Language.FR;
			return true;
			//modification en mode es
		case R.principal.usees:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.ES).commit();
			MainActivity.InUse = Language.ES;
			return true;
			//modification en mode de
		case R.principal.usede:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.DE).commit();
			MainActivity.InUse = Language.DE;
			return true;
			//modification en mode it
		case R.principal.useit:
			_shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
			_shared.edit().putInt(MainActivity.USE, MainActivity.IT).commit();
			MainActivity.InUse = Language.IT;
			return true;
		default:
			return false;
		}
	}

	ExtensionFragment _extension;
	CodesFragment _codes;
	CardFragment _carte;
	String _name;//last extension
	int _id;//last extension
	String _intitule;//last extension

	@Override
	public void onSaveInstanceState(Bundle out){
		if(_codes != null){
			try{
				getSupportFragmentManager().putFragment(out, "CODES", _codes);
			}catch(Exception e){
			}
		}
		if(_name != null){
			out.putString("NAME", _name);
			out.putInt("ID", _id);
			out.putString("INTIT", _intitule);

			try{
				getSupportFragmentManager().putFragment(out, "EXTENSION", _extension);
			}catch(Exception e){
			}
		}

		if(_carte != null){
			try{
				getSupportFragmentManager().putFragment(out, "CARTE", _carte);
			}catch(Exception e){
			}
		}
		_carte = null;
		_extension = null;
		_codes = null;

		super.onSaveInstanceState(out);
	}

	@Override
	public void onRestoreInstanceState(Bundle in){
		if(in != null && in.containsKey("CODES")){
			_codes = (CodesFragment) getSupportFragmentManager().getFragment(in, "CODES");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		if(in!= null && in.containsKey("NAME") && in.containsKey("ID") && in.containsKey("INTIT")){
			_name = in.getString("NAME");
			_id = in.getInt("ID");
			_intitule = in.getString("INTIT");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		if(in != null && in.containsKey("CARTE")){
			_carte = (CardFragment) getSupportFragmentManager().getFragment(in, "CARTE");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		if(in != null)
			super.onRestoreInstanceState(in);

	}
	public void onClick(String nom,
			int id,
			String intitule){
		Fragment viewer = getSupportFragmentManager().findFragmentById(R.id.extension_fragment);
		if(viewer != null){
			_name = nom;
			_id = id;
			_intitule = intitule;
			FragmentManager fm = getSupportFragmentManager();
			if(_carte != null){
				if(_carte.isAdded()){
					FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
					xact.remove(_carte);
					xact.commit();
				}
				_carte = null;
				fm.popBackStackImmediate();
			}
			if(_codes != null && _codes.isVisible() && _codes.isAdded()){
				FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
				xact.remove(_codes);
				xact.commit();
				_codes = null;
				fm.popBackStackImmediate();
			}
			if(_extension == null  || !_extension.isVisible()){
				//Fragment extension = getSupportFragmentManager().findFragmentByTag(nom);
				FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
				_extension = new ExtensionFragment(this, nom, id, intitule);
				//xact.show(_extension);
				//xact.replace(R.id.extension_fragment, _extension, nom);
				xact.replace(R.id.extension_fragment, _extension,"Extensions");
				xact.addToBackStack(null);
				xact.commit();
			}else{
				_extension.setExtension(nom, id, intitule);
			}
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			Bundle objetbundle = new Bundle();
			objetbundle.putString("nom", nom);
			objetbundle.putInt("extension", id);
			objetbundle.putString("intitule", intitule);
			Intent intent = new Intent().setClass(this, ExtensionActivity.class);
			intent.putExtras(objetbundle);
			startActivityForResult(intent,42);
		}
	}

	public void onClickTCGO(){
		Fragment viewer = getSupportFragmentManager().findFragmentById(R.id.extension_fragment);
		if(viewer != null){
			FragmentManager fm = getSupportFragmentManager();
			if(_carte != null){
				if(_carte.isAdded()){
					FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
					xact.remove(_carte);
					xact.commit();
				}
				setViewPagerSelected(0);
				_carte = null;
				fm.popBackStackImmediate();
			}
			if(_extension != null){
				if(_extension.isAdded()){
					FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
					xact.remove(_extension);
					xact.commit();
				}
				_extension = null;
				fm.popBackStackImmediate();
			}
			if(_codes == null  || !_codes.isVisible()){
				//Fragment extension = getSupportFragmentManager().findFragmentByTag(nom);
				FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
				_codes = new CodesFragment();
				//xact.show(_extension);
				//xact.replace(R.id.extension_fragment, _extension, nom);
				xact.replace(R.id.extension_fragment, _codes,"Codes");
				xact.addToBackStack(null);
				xact.commit();
			}
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}else{
			Bundle objetbundle = new Bundle();
			//objetbundle.putString("nom", nom);
			Intent intent = new Intent().setClass(this, CodesActivity.class);
			intent.putExtras(objetbundle);
			startActivityForResult(intent,43);
		}
	}

	public void onClick(Bundle pack) {
		FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(_carte == null || !_carte.isVisible()){
			_carte = new CardFragment(pack, _extension);
			//xact.show(_extension);
			//xact.replace(R.id.extension_fragment, _extension, nom);
			xact.add(R.id.extension_fragment, _carte,"Carte");
			xact.addToBackStack(null);
			xact.commit();	
		}
	}

	/**
	 * Receive a notification about modification
	 * so update the fragment >> cause we have it since this function is called
	 */
	@Override
	public void update(int extension_id) {
		int ind = 0;
		for(;ind<_arrayExtension.size() && 
				_arrayExtension.get(ind).getId()!=extension_id;ind++);
		if(ind<_arrayExtension.size()){
			_arrayExtension.get(ind).updatePossessed();
		}
		notifyChanged();
	}

	public void setCarte(CardFragment carte){
		_carte = carte;
	}

	public void setExtension(ExtensionFragment extension){
		_extension = extension;
		_extension.setParent(this);
	}

	@Override
	public void onLoadOk(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadError(String text) {
		// TODO Auto-generated method stub

	}

}
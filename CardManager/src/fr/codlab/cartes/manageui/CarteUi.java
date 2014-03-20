package fr.codlab.cartes.manageui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.VisuCartePagerAdapter;
import fr.codlab.cartes.attributes.Ability;
import fr.codlab.cartes.attributes.Attack;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.util.Card;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.Language;
import fr.codlab.cartes.util.Rarity;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;

/**
 * Class made to manage how a specified Ui must be modified with card data
 * @author kevin le perf
 *
 */
final public class CarteUi implements OnPageChangeListener {
	private Card _card;
	private String _intitule;
	private boolean showNext = false;
	private boolean _all_visible=true;	
	private Extension _extension;
	private View _root;
	private IExtensionMaster _parent;
	private OnPageChangeListener _listener;

	public CarteUi(OnPageChangeListener listener){
		showNext = false;
		_all_visible=true;
		_listener = listener;
	}

	public void setExtension(Extension extension){
		_extension = extension;
	}
	public void setCard(Card carte){
		_card = carte;
	}
	public Card getCard(){
		return _card;
	}

	/**
	 * In case of trainer / Pokemon for instance
	 * @param state
	 */
	public void setAllObjectVisible(boolean state){
		_all_visible = state;
	}
	public boolean getAllObjectVisible(boolean state){
		return _all_visible;
	}

	public void showImageAtFirst(boolean state){
		showNext = state;
	}
	public boolean getshowImageAtFirst(){
		return showNext;
	}

	public void setSetShortName(String intitule){
		_intitule = intitule;
	}
	public String getSetShortName(){
		return _intitule;
	}

	public void setContext(View root){
		_root = root;
	}

	public void manageFirstPopulate(){
		Log.d("manage","firstpopulate");
		ViewPager pager = (ViewPager)_root.findViewById( R.id.viewpager );
		if(pager != null){
			VisuCartePagerAdapter adapter = new VisuCartePagerAdapter(_root.getContext(), this);
			TitlePageIndicator indicator =
					(TitlePageIndicator)_root.findViewById( R.id.indicator );
			pager.setAdapter(adapter);
			indicator.setOnPageChangeListener(this);
			indicator.setViewPager(pager);
			adapter.notifyDataSetChanged();
			if(showNext)
				pager.setCurrentItem(0);
			else
				pager.setCurrentItem(1);
		}
	}

	public Bundle createBundle(int _pos,boolean imgVue, Extension _extension){
		Bundle objetbundle = new Bundle();
		//objetbundle.putInt("nb", _item.getCarte(_pos).getNb());
		objetbundle.putSerializable("card", _extension.getCarte(_pos));
		if(imgVue)
			objetbundle.putInt("next", 1);
		objetbundle.putInt("extension", _extension.getId());
		objetbundle.putString("intitule", _extension.getShortName());

		return objetbundle;
	}
	public void populateImage(View activity){
		if(_intitule != null){
			//chargement de l'image
			ImageView iv = (ImageView)activity.findViewById(R.id.carte_visu);
			//recuperation hidden file
			Bitmap _bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_intitule+"/."+_intitule+"_"+_card.getCarteId()+(MainActivity.InUse == Language.FR ? "" : "_us" )+".jpg");

			//si le scan existe, on le charge
			if(_bmp != null){
				iv.setImageBitmap(_bmp);
			}else{
				_bmp = BitmapFactory.decodeFile("/sdcard/card_images/"+_intitule+"/"+_intitule+"_"+_card.getCarteId()+(MainActivity.InUse == Language.FR ? "" : "_us" )+".jpg");
				if(_bmp != null){
					iv.setImageBitmap(_bmp);
				}else{
					iv.setImageResource(R.drawable.back);
				}
			}
		}
	}


	public void updateAll(){
		if(_parent != null)
			_parent.update(_extension.getId());
	}

	public void populateEdit(final View activity){
		populateEditUS(activity);
		populateEditFR(activity);
		populateEditES(activity);
		populateEditIT(activity);
		populateEditDE(activity);
	}
	public void populateEditIT(final View activity){
		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)activity.findViewById(R.id.carte_it_sub);
		plus = (ImageView)activity.findViewById(R.id.carte_it_add);
		editQuantite=(TextView)activity.findViewById(R.id.carte_it_possedees);
		LinearLayout normal = (LinearLayout)activity.findViewById(R.id.extension_normal_it);
		LinearLayout reverse = (LinearLayout)activity.findViewById(R.id.extension_reverse_it);
		LinearLayout holo = (LinearLayout)activity.findViewById(R.id.extension_holo_it);

		if(_card.getIsNormal()){
			normal.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.NORMAL,  Language.IT);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_it_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.NORMAL, Language.IT);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_it_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			normal.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.reversecarte_it_sub);
		plus = (ImageView)activity.findViewById(R.id.reversecarte_it_add);
		editQuantite=(TextView)activity.findViewById(R.id.reversecarte_it_possedees);

		//REVERSE OR FIRST EDITION !
		if(_card.getIsReverse() || (_extension != null && _extension.isFirstEdition())){
			if(_extension != null && _extension.isFirstEdition()){
				TextView v2 = (TextView)activity.findViewById(R.id.extension_reversetext_it);
				v2.setText(R.string.firstedition);
			}
			reverse.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(),Language.IT,  Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.REVERSE,  Language.IT);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_it_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.REVERSE, Language.IT);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_it_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			reverse.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.holocarte_it_sub);
		plus = (ImageView)activity.findViewById(R.id.holocarte_it_add);
		editQuantite=(TextView)activity.findViewById(R.id.holocarte_it_possedees);
		if(_card.getIsHolo()){
			holo.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1, Rarity.HOLO, Language.IT);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_it_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.HOLO, Language.IT);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_it_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.IT, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			holo.setVisibility(View.GONE);
		}
	}
	public void populateEditDE(final View activity){
		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)activity.findViewById(R.id.carte_de_sub);
		plus = (ImageView)activity.findViewById(R.id.carte_de_add);
		editQuantite=(TextView)activity.findViewById(R.id.carte_de_possedees);
		LinearLayout normal = (LinearLayout)activity.findViewById(R.id.extension_normal_de);
		LinearLayout reverse = (LinearLayout)activity.findViewById(R.id.extension_reverse_de);
		LinearLayout holo = (LinearLayout)activity.findViewById(R.id.extension_holo_de);

		if(_card.getIsNormal()){
			normal.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.NORMAL,  Language.DE);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_de_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.NORMAL, Language.DE);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_de_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			normal.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.reversecarte_de_sub);
		plus = (ImageView)activity.findViewById(R.id.reversecarte_de_add);
		editQuantite=(TextView)activity.findViewById(R.id.reversecarte_de_possedees);

		//REVERSE OR FIRST EDITION !
		if(_card.getIsReverse() || (_extension != null && _extension.isFirstEdition())){
			if(_extension != null && _extension.isFirstEdition()){
				TextView v2 = (TextView)activity.findViewById(R.id.extension_reversetext_de);
				v2.setText(R.string.firstedition);
			}
			reverse.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(),Language.DE,  Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.REVERSE,  Language.DE);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_de_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.REVERSE, Language.DE);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_de_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			reverse.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.holocarte_de_sub);
		plus = (ImageView)activity.findViewById(R.id.holocarte_de_add);
		editQuantite=(TextView)activity.findViewById(R.id.holocarte_de_possedees);
		if(_card.getIsHolo()){
			holo.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1, Rarity.HOLO, Language.DE);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_de_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.HOLO, Language.DE);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_de_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.DE, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			holo.setVisibility(View.GONE);
		}
	}

	public void populateEditES(final View activity){
		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)activity.findViewById(R.id.carte_es_sub);
		plus = (ImageView)activity.findViewById(R.id.carte_es_add);
		editQuantite=(TextView)activity.findViewById(R.id.carte_es_possedees);
		LinearLayout normal = (LinearLayout)activity.findViewById(R.id.extension_normal_es);
		LinearLayout reverse = (LinearLayout)activity.findViewById(R.id.extension_reverse_es);
		LinearLayout holo = (LinearLayout)activity.findViewById(R.id.extension_holo_es);

		if(_card.getIsNormal()){
			normal.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.NORMAL,  Language.ES);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_es_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.NORMAL, Language.ES);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_es_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			normal.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.reversecarte_es_sub);
		plus = (ImageView)activity.findViewById(R.id.reversecarte_es_add);
		editQuantite=(TextView)activity.findViewById(R.id.reversecarte_es_possedees);

		//REVERSE OR FIRST EDITION !
		if(_card.getIsReverse() || (_extension != null && _extension.isFirstEdition())){
			if(_extension != null && _extension.isFirstEdition()){
				TextView v2 = (TextView)activity.findViewById(R.id.extension_reversetext_es);
				v2.setText(R.string.firstedition);
			}
			reverse.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(),Language.ES,  Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.REVERSE,  Language.ES);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_es_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.REVERSE, Language.ES);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_es_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			reverse.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.holocarte_es_sub);
		plus = (ImageView)activity.findViewById(R.id.holocarte_es_add);
		editQuantite=(TextView)activity.findViewById(R.id.holocarte_es_possedees);
		if(_card.getIsHolo()){
			holo.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1, Rarity.HOLO, Language.ES);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_es_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.HOLO, Language.ES);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_es_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.ES, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			holo.setVisibility(View.GONE);
		}
	}

	public void populateEditFR(final View activity){
		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)activity.findViewById(R.id.carte_fr_sub);
		plus = (ImageView)activity.findViewById(R.id.carte_fr_add);
		editQuantite=(TextView)activity.findViewById(R.id.carte_fr_possedees);
		LinearLayout normal = (LinearLayout)activity.findViewById(R.id.extension_normal_fr);
		LinearLayout reverse = (LinearLayout)activity.findViewById(R.id.extension_reverse_fr);
		LinearLayout holo = (LinearLayout)activity.findViewById(R.id.extension_holo_fr);

		if(_card.getIsNormal()){
			normal.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.NORMAL,  Language.FR);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_fr_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.NORMAL, Language.FR);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_fr_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			normal.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.reversecarte_fr_sub);
		plus = (ImageView)activity.findViewById(R.id.reversecarte_fr_add);
		editQuantite=(TextView)activity.findViewById(R.id.reversecarte_fr_possedees);

		//REVERSE OR FIRST EDITION !
		if(_card.getIsReverse() || (_extension != null && _extension.isFirstEdition())){
			if(_extension != null && _extension.isFirstEdition()){
				TextView v2 = (TextView)activity.findViewById(R.id.extension_reversetext_fr);
				v2.setText(R.string.firstedition);
			}
			reverse.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(),Language.FR,  Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.REVERSE,  Language.FR);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_fr_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.REVERSE, Language.FR);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_fr_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			reverse.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.holocarte_fr_sub);
		plus = (ImageView)activity.findViewById(R.id.holocarte_fr_add);
		editQuantite=(TextView)activity.findViewById(R.id.holocarte_fr_possedees);
		if(_card.getIsHolo()){
			holo.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1, Rarity.HOLO, Language.FR);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_fr_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.HOLO, Language.FR);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_fr_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.FR, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			holo.setVisibility(View.GONE);
		}
	}

	public void populateEditUS(final View activity){
		ImageView moins;
		ImageView plus;
		TextView editQuantite;

		moins = (ImageView)activity.findViewById(R.id.carte_us_sub);
		plus = (ImageView)activity.findViewById(R.id.carte_us_add);
		editQuantite=(TextView)activity.findViewById(R.id.carte_us_possedees);
		LinearLayout normal = (LinearLayout)activity.findViewById(R.id.extension_normal_us);
		LinearLayout reverse = (LinearLayout)activity.findViewById(R.id.extension_reverse_us);
		LinearLayout holo = (LinearLayout)activity.findViewById(R.id.extension_holo_us);

		if(_card.getIsNormal()){
			normal.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.NORMAL)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.NORMAL,  Language.US);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_us_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.NORMAL)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.NORMAL, Language.US);
					TextView editQuantite=(TextView)activity.findViewById(R.id.carte_us_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.NORMAL)));
					updateAll();
				}
			});

		}else{
			normal.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.reversecarte_us_sub);
		plus = (ImageView)activity.findViewById(R.id.reversecarte_us_add);
		editQuantite=(TextView)activity.findViewById(R.id.reversecarte_us_possedees);

		//REVERSE OR FIRST EDITION !
		if(_card.getIsReverse() || (_extension != null && _extension.isFirstEdition())){
			if(_extension != null && _extension.isFirstEdition()){
				TextView v2 = (TextView)activity.findViewById(R.id.extension_reversetext_us);
				v2.setText(R.string.firstedition);
			}
			reverse.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(),Language.US,  Rarity.REVERSE)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1,Rarity.REVERSE,  Language.US);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_us_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.REVERSE)));
					updateAll();
				}
			});
			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.REVERSE, Language.US);
					TextView editQuantite=(TextView)activity.findViewById(R.id.reversecarte_us_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.REVERSE)));
					updateAll();
				}
			});

		}else{
			reverse.setVisibility(View.GONE);
		}

		moins = (ImageView)activity.findViewById(R.id.holocarte_us_sub);
		plus = (ImageView)activity.findViewById(R.id.holocarte_us_add);
		editQuantite=(TextView)activity.findViewById(R.id.holocarte_us_possedees);
		if(_card.getIsHolo()){
			holo.setVisibility(View.VISIBLE);
			editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.HOLO)));
			moins.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), -1, Rarity.HOLO, Language.US);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_us_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.HOLO)));
					updateAll();
				}
			});

			plus.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_card.addQuantite(activity.getContext(), 1, Rarity.HOLO, Language.US);
					TextView editQuantite=(TextView)activity.findViewById(R.id.holocarte_us_possedees);
					editQuantite.setText(Integer.toString(_card.getQuantite(activity.getContext(), Language.US, Rarity.HOLO)));
					updateAll();
				}
			});

		}else{
			holo.setVisibility(View.GONE);
		}
	}

	public void populateText(View activity){
		//mise a jour de la description, nom et numero
		((TextView)activity.findViewById(R.id.carte_description)).setText(_card.getDescription());
		((TextView)activity.findViewById(R.id.carte_numero)).setText(" "+_card.getCarteId());
		((TextView)activity.findViewById(R.id.carte_nom)).setText(_card.getNom());

		boolean _all_visible = this._all_visible;
		_all_visible = _card.getVisible();
		//mise a jour pv
		if(_all_visible && _card.getPV()>0)
			((TextView)activity.findViewById(R.id.carte_pv)).setText(Integer.toString(_card.getPV()));
		if(!_all_visible){
			((View)activity.findViewById(R.id.carte_hpzone)).setVisibility(View.GONE);
		}

		//affichage common, rare, uncommon, holo, ultra
		if(_root.getContext().getResources().getIdentifier(_card.getDrawableRarete() , "drawable", _root.getContext().getPackageName())>0){
			ImageView iv = (ImageView) activity.findViewById(R.id.carte_rarete);
			iv.setImageResource(_root.getContext().getResources().getIdentifier(_card.getDrawableRarete(), "drawable", _root.getContext().getPackageName()));
		}



		//gestion des attaques
		LinearLayout attaques = (LinearLayout)activity.findViewById(R.id.carte_attaques);
		attaques.removeAllViews();
		LayoutInflater inflater = (LayoutInflater)_root.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View _current_attack = null;
		Attack attaque;
		for(int index = 0; _all_visible && index < _card.getNbAttaques(); index++){
			//creation d'un layout secondaire pour l'attaque actuelle
			_current_attack = inflater.inflate(R.layout.infos_attaque, null);
			//recuperation de l'objet attaque actuel
			attaque = _card.getAttaque(index);
			//ajout du layout au layout principal
			attaques.addView(_current_attack);
			//modification du nom, degats et description
			((TextView)_current_attack.findViewById(R.id.nom_attaque)).setText(attaque.getName());
			((TextView)_current_attack.findViewById(R.id.attaque_degat)).setText(""+attaque.getDamage());
			((TextView)_current_attack.findViewById(R.id.description)).setText(attaque.getDescription());
			String [] _types = attaque.getTypes();
			LinearLayout _vue_types = (LinearLayout)_current_attack.findViewById(R.id.type);
			_vue_types.removeAllViews();
			//ajout des types de l'attaque
			for(int i=0;i<_types.length;i++){
				ImageView _type = new ImageView(_root.getContext());
				_type.setImageResource(_root.getContext().getResources().getIdentifier("type_"+_types[i] , "drawable", _root.getContext().getPackageName()));
				_vue_types.addView(_type);
			}
		}

		String [] _faiblesses = _card.getFaiblesses();
		if(_faiblesses!=null){
			LinearLayout _vue_faiblesses = (LinearLayout)activity.findViewById(R.id.carte_faiblesse);
			if(!_all_visible)
				_vue_faiblesses.removeAllViews();
			for(int i=0;i<_faiblesses.length;i++){
				ImageView _type = new ImageView(_root.getContext());
				_type.setImageResource(_root.getContext().getResources().getIdentifier("type_"+_faiblesses[i] , "drawable", _root.getContext().getPackageName()));
				_vue_faiblesses.addView(_type);
			}
		}

		String [] _resistances = _card.getResistances();
		if(_resistances!=null){
			LinearLayout _vue_resistances = (LinearLayout)activity.findViewById(R.id.carte_resistance);
			if(!_all_visible)
				_vue_resistances.removeAllViews();
			for(int i=0;i<_resistances.length;i++){
				ImageView _type = new ImageView(_root.getContext());
				_type.setImageResource(_root.getContext().getResources().getIdentifier("type_"+_resistances[i] , "drawable", _root.getContext().getPackageName()));
				_vue_resistances.addView(_type);
			}
		}

		LinearLayout pouvoirs = (LinearLayout)activity.findViewById(R.id.carte_pokebody_pokepower);
		pouvoirs.removeAllViews();
		//creations et ajouts des PokePower et PokeBody
		PokePower _pokepower = _card.getPokePower();
		if(_all_visible && _pokepower!=null){
			View pouvoir = inflater.inflate(R.layout.pokepower, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokepower_nom)).setText(_pokepower.getName());
			((TextView)pouvoir.findViewById(R.id.pokepower_description)).setText(_pokepower.getDescription());
		}
		PokeBody _pokebody = _card.getPokeBody();
		if(_all_visible && _pokebody!=null){
			View pouvoir = inflater.inflate(R.layout.pokebody, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.pokebody_nom)).setText(_pokebody.getName());
			((TextView)pouvoir.findViewById(R.id.pokebody_description)).setText(_pokebody.getDescription());
		}
		Ability _ability = _card.getAbility();
		if(_all_visible && _ability!=null){
			View pouvoir = inflater.inflate(R.layout.ability, null);
			pouvoirs.addView(pouvoir);
			((TextView)pouvoir.findViewById(R.id.ability_nom)).setText(_ability.getName());
			((TextView)pouvoir.findViewById(R.id.ability_description)).setText(_ability.getDescription());
		}


		String _desc = _card.getDescription();
		TextView _txt_desc = (TextView)activity.findViewById(R.id.carte_description);
		_txt_desc.setText(_desc);

		//ajout des couts de retraite de la carte
		LinearLayout _vue_retraite = (LinearLayout)activity.findViewById(R.id.carte_retraite);

		int _retraite = _card.getRetraite();
		for(int i=0;_all_visible && i<_retraite;i++){
			ImageView _type = new ImageView(_root.getContext());
			_type.setImageResource(_root.getContext().getResources().getIdentifier("type_incolore" , "drawable", _root.getContext().getPackageName()));
			_vue_retraite.addView(_type);
		}
		if(!_all_visible)
			_vue_retraite.removeAllViews();			
	}

	public void setParent(IExtensionMaster parent) {
		_parent = parent;		
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		_listener.onPageScrollStateChanged(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		_listener.onPageScrolled(arg0,arg1,arg2);
	}

	@Override
	public void onPageSelected(int arg0) {
		_listener.onPageSelected(arg0);
	}

}

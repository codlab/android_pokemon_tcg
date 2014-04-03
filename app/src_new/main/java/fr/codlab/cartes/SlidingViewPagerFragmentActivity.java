package fr.codlab.cartes;

import fr.codlab.cartes.R;

import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.util.Log;
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
public class SlidingViewPagerFragmentActivity extends SlidingFragmentActivity{
	public void setViewPagerSelected(int index){
		Log.d("setViewPagerSelected",""+index);
		if(index == 0 && this.findViewById(R.id.mobile) != null){
			this.getSlidingMenu().setSlidingEnabled(true);
		}else{
			this.getSlidingMenu().setSlidingEnabled(false);
		}
	}
}
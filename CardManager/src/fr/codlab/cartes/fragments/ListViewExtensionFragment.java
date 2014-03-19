package fr.codlab.cartes.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment made to implement the different Extension List
 * 
 * @author kevin le perf
 *
 */
final public class ListViewExtensionFragment extends SherlockFragment{
	View _this;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.main_pager_list, container, false);		
		_this = mainView;
		return mainView;
	}
	
	public void setListExtension(MainActivity _activity_main){
		_activity_main.setListExtension(_this);
	}
}

package fr.codlab.cartes.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.SlidingViewPagerFragmentActivity;
import fr.codlab.cartes.manageui.DriveUi;

final public class DriveUiFragment extends SherlockFragment implements OnPageChangeListener{
	private IExtensionMaster _master;

	public DriveUiFragment(){

	}

	public DriveUiFragment(IExtensionMaster master){
		this();
		_master = master;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.main_drive, container, false);

        DriveUi t = new DriveUi(getActivity(), _master, mainView);

        this.setHasOptionsMenu(false);

		return mainView;
	}

    @Override
    public void onResume(){
        super.onResume();

        this.getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return false;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.information, menu);
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		if(getSherlockActivity() instanceof SlidingViewPagerFragmentActivity)
			((SlidingViewPagerFragmentActivity)getSherlockActivity()).setViewPagerSelected(arg0);
	}	
}

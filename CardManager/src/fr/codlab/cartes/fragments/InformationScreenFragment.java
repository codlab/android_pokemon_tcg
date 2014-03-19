package fr.codlab.cartes.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.SlidingViewPagerFragmentActivity;
import fr.codlab.cartes.adaptaters.MainTabletPagerAdapter;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

final public class InformationScreenFragment extends SherlockFragment implements OnPageChangeListener{
	private IExtensionMaster _master;

	public InformationScreenFragment(){

	}

	public InformationScreenFragment(IExtensionMaster master){
		this();
		_master = master;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.informationscreenpager, container, false);

		ViewPager pager = (ViewPager)mainView.findViewById( R.maintablet.pager );
		Log.d("InformationScreenFragment","log");
		if(pager != null){
			Log.d("InformationScreenFragment","log");
			MainTabletPagerAdapter adapter = new MainTabletPagerAdapter(this, _master);
			TitlePageIndicator indicator =
					(TitlePageIndicator)mainView.findViewById( R.maintablet.indicator );
			pager.setAdapter(adapter);
			indicator.setOnPageChangeListener(this);

			indicator.setViewPager(pager);
		}
		return mainView;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Log.d("InformationScreenFragment","onPageScrollStateChanged "+arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.d("InformationScreenFragment","onPageScrolled "+arg0+" "+arg1+" "+arg2);
	}

	@Override
	public void onPageSelected(int arg0) {
		if(getSherlockActivity() instanceof SlidingViewPagerFragmentActivity)
			((SlidingViewPagerFragmentActivity)getSherlockActivity()).setViewPagerSelected(arg0);
	}	
}

package fr.codlab.cartes.adaptaters;


import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.fragments.InformationScreenFragment;
import fr.codlab.cartes.manageui.AccountUi;
import fr.codlab.cartes.viewpagerindicator.TitleProvider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class MainTabletPagerAdapter
extends PagerAdapter
implements TitleProvider{
	private String [] _titles;
	private final IExtensionMaster _master;
	private final InformationScreenFragment _activity_main;


	public MainTabletPagerAdapter(InformationScreenFragment informationScreenFragment, IExtensionMaster master){
		_master = master;
		_activity_main = informationScreenFragment;
		_titles = new String[]{
			_activity_main.getString(R.string.principal_title),
			_activity_main.getString(R.string.accounttitle)
		};
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		try{
			((ViewPager)pager).removeView((View)view);
		}catch(Exception e){
			
		}
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
		return _titles.length;
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		View v = null;
		LayoutInflater inflater = (LayoutInflater)_activity_main.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(position == 0){
			v = inflater.inflate(R.layout.main_pager, null);
		}else{
			v = inflater.inflate(R.layout.main_account, null);
			@SuppressWarnings("unused")
			AccountUi t = new AccountUi(_activity_main.getActivity(), _master, v);
		}
		((ViewPager)pager).addView( v, 0 );
		return v;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	@Override
	public String getTitle(int position) {
		return _titles[position];
	}

}

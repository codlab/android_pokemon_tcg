package fr.codlab.cartes.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.SlidingViewPagerFragmentActivity;
import fr.codlab.cartes.adaptaters.MainTabletPagerAdapter;
import fr.codlab.cartes.dl.DownloaderFactory;
import fr.codlab.cartes.viewpagerindicator.TitlePageIndicator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

final public class InformationScreenFragment extends SherlockFragment implements OnPageChangeListener{
	private IExtensionMaster _master;

	public InformationScreenFragment(){

	}

	public InformationScreenFragment(IExtensionMaster master){
		this();
		_master = master;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.main_pager, null);
        ImageView button_flappi = (ImageView)mainView.findViewById(R.id.flappi);
        if(button_flappi != null){
            button_flappi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=eu.codlab.flappi"));
                    getSherlockActivity().startActivity(i);
                }
            });
        }
        this.setHasOptionsMenu(true);

		return mainView;
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

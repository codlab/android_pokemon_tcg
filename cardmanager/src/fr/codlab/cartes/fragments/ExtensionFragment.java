package fr.codlab.cartes.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import fr.codlab.cartes.IExtensionListener;
import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.SlidingViewPagerFragmentActivity;
import fr.codlab.cartes.adaptaters.ExtensionListeAdapter;
import fr.codlab.cartes.manageui.ExtensionUi;
import fr.codlab.cartes.util.Extension;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

final public class ExtensionFragment extends SherlockFragment implements IExtensionListener, IExtensionMaster, OnDismissCallback, AbsListView.OnScrollListener {
	private static ExtensionUi _factorise = null;
	private IExtensionMaster _parent;
    private ListView _liste;
    private int _default_top;
    private int _default_height;
    private View _header;

    public ExtensionFragment(){
		super();
		if(_factorise == null)
			_factorise = new ExtensionUi(this.getSherlockActivity());
	}

	public ExtensionFragment(IExtensionMaster parent, String name, int id, String intitule){
		this();
		_parent = parent;
		define(name, id, intitule);
	}

	private void define(String name, int id, String intitule) {
		_factorise.define(name, id, intitule);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.extension, container, false);		
		return mainView;

	}

	@Override
	public void onViewCreated(View v, Bundle saved){
		if(getActivity() instanceof MainActivity)
			((MainActivity)getActivity()).setExtension(this);
		if(_factorise == null)
			_factorise = new ExtensionUi(this.getSherlockActivity());
		else
			_factorise.setActivity(getSherlockActivity());
		setHasOptionsMenu(true);
		update();


		if(getSherlockActivity() instanceof SlidingViewPagerFragmentActivity)
			((SlidingViewPagerFragmentActivity)getSherlockActivity()).setViewPagerSelected(0);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		_factorise.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(_factorise.onOptionsItemSelected(item) == false)
			return super.onOptionsItemSelected(item);
		else
			return true;
	}

	public void onPause(){
		super.onPause();
		_factorise.onPause();
	}

	public void onResume(){
		super.onResume();
		_factorise.onResume();
		if(getSherlockActivity() instanceof SlidingViewPagerFragmentActivity)
			((SlidingViewPagerFragmentActivity)getSherlockActivity()).setViewPagerSelected(0);


	}

	public void onDestroy(){
		super.onDestroy();
		_factorise.onDestroy();
	}



	public void updateName(String nom){
		_factorise.updateName(((TextView)getView().findViewById(R.id.visu_extension_nom)), nom);
	}

	public void updateProgress(int t, int m){
		_factorise.updateProgress(((TextView)getView().findViewById(R.id.visu_extension_cartes)), t, m);
	}

	public void updatePossessed(int p){
		_factorise.updatePossessed(((TextView)getView().findViewById(R.id.visu_extension_possess)), p);
	}

	public void updated(int id){
		_parent.update(id); 
	}
	@Override
	public void update(int i){
		ListView _liste = (ListView)getView().findViewById(R.id.visu_extension_liste);
		if(_liste.getAdapter()!=null && _liste.getAdapter() instanceof ExtensionListeAdapter)
			((ExtensionListeAdapter)_liste.getAdapter()).notifyDataSetChanged();
		_parent.update(i);
	}

	private void update(){
		try{
			Extension _extension = _factorise.getExtension();
			updateName(_extension.getName());
			updateProgress(_extension.getProgress(),_extension.getCount());
			updatePossessed(_extension.getPossessed());

			//liste des images
			ExtensionListeAdapter _adapter = new ExtensionListeAdapter(this, this.getActivity().getApplication().getApplicationContext(), _extension);
			_liste = (ListView)getView().findViewById(R.id.visu_extension_liste);
            View v = getActivity().getLayoutInflater().inflate(R.layout.extension_header, null);
            if(_liste.getHeaderViewsCount() == 0)
                _liste.addHeaderView(v);
            //_liste.setAdapter(_adapter);

            if(Build.VERSION.SDK_INT >= 11){
                _liste.setOnScrollListener(this);
                v.setAlpha(0.f);
            }

            _header = getView().findViewById(R.id.extension_list_header);
            _default_top = _header.getTop();
            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(_adapter);
            swingBottomInAnimationAdapter.setInitialDelayMillis(300);
            swingBottomInAnimationAdapter.setAbsListView(_liste);

            _liste.setAdapter(swingBottomInAnimationAdapter);
            _default_height = _header.getHeight();//_liste.getChildAt(0).getHeight();
		}catch(Exception e){

		}
	}

	public void setExtension(String nom, int id, String intitule) {
		define(nom, id, intitule);
		update();
	}

	@Override
	public void onClick(Bundle pack) {
		_parent.onClick(pack);
	}

	public void setParent(IExtensionMaster parent) {
		_parent = parent;		
	}

	@Override
	public void onClick(String nom, int id, String intitule) {

	}

	@Override
	public void notifyDataChanged() {
		_parent.notifyDataChanged();
	}


    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(_default_height == 0){
            _header = getView().findViewById(R.id.extension_list_header);
            _default_height = _header.getHeight();
        }

        if(_liste.getCount() > 0 && firstVisibleItem == 0 && _default_height > 0){

            int top_value = _liste.getChildAt(0).getTop()/2;
            if(top_value < -_default_height- _default_top)
                top_value = -_default_height- _default_top;

            _header.setAlpha(1- (float) (-top_value*2.0/(_default_height)));
            _header.setTop(_default_top+top_value);
            Log.d("onScroll", "actual " + (1 - (-top_value * 1.0 / (_default_height))) + " " + _default_height + " " + _default_top + " " + (_default_top + top_value));
        }else if(_liste.getCount() > 0){
            _header.setTop(_default_top - _default_height);
        }
    }
}

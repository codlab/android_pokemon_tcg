package fr.codlab.cartes.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.PrincipalExtensionAdapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Fragment made to implement the different Extension List
 *
 * @author kevin le perf
 *
 */
final public class ListViewExtensionFragment extends SherlockFragment implements AbsListView.OnScrollListener {
    private View _this;
    private ListView _liste;
    private int _default_top;
    private int _default_height;
    private View _header;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.main_pager_list, container, false);
        _this = mainView;
        return mainView;
    }

    public void onViewCreated(View v, Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);



        if(v != null && v.findViewById(R.id.main_pager_list_header) != null){
            _header = v.findViewById(R.id.main_pager_list_header);
            _default_top = _header.getTop();
            _default_height = _header.getHeight();//_liste.getChildAt(0).getHeight();
        }
    }

    public void setListExtension(MainActivity _activity_main){

        _liste = (ListView) _this.findViewById(R.id.principal_extensions);


        View v = getActivity().getLayoutInflater().inflate(R.layout.main_pager_list_header, null);
        if(_liste.getHeaderViewsCount() == 0)
            _liste.addHeaderView(v);
        //_liste.setAdapter(_adapter);

        _activity_main.setListExtension(_this);

        if(Build.VERSION.SDK_INT >= 11){
            _liste.setOnScrollListener(this);
            v.setAlpha(0.f);
        }

        if(getView() != null && getView().findViewById(R.id.main_pager_list_header) != null){
            _header = getView().findViewById(R.id.main_pager_list_header);
            _default_top = _header.getTop();
            _default_height = _header.getHeight();//_liste.getChildAt(0).getHeight();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(_default_height == 0){
            _header = getView().findViewById(R.id.main_pager_list_header);
            _default_height = _header != null ? _header.getHeight() : 0;
        }

        if(_liste != null && _liste.getCount() > 0 && _liste.getChildAt(0) != null && firstVisibleItem == 0 && _default_height > 0){

            int top_value = _liste.getChildAt(0).getTop()/2;
            if(top_value < -_default_height- _default_top)
                top_value = -_default_height- _default_top;

            _header.setAlpha(1- (float) (-top_value*2.0/(_default_height)));
            _header.setTop(_default_top+top_value);
            Log.d("onScroll", "actual " + (1 - (-top_value * 1.0 / (_default_height))) + " " + _default_height + " " + _default_top + " " + (_default_top + top_value));
        }else if(_liste != null && _header != null && _liste.getCount() > 0){
            _header.setTop(_default_top - _default_height);
        }
    }
}

package fr.codlab.cartes.adaptaters;


import java.util.ArrayList;

import fr.codlab.cartes.IExtensionListener;
import fr.codlab.cartes.R;
import fr.codlab.cartes.bdd.SGBD;
import fr.codlab.cartes.fragments.CodesFragment;
import fr.codlab.cartes.util.Code;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.views.CardImage;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CodesAdapter extends BaseAdapter {
	int _count = 0;
	private ArrayList<Code> _new;
	private CodesFragment _context;

	public CodesAdapter(CodesFragment _parent) {
		_context=_parent;
		_new = new ArrayList<Code>();
		SGBD _s = new SGBD(_context.getActivity());
		_s.open();
		Code [] _c = _s.getCodes();
		_s.close();
		if(_c != null)
			for(int i=0;i<_c.length;i++){
				_new.add(_c[i]);
			}
	}

	public void delete(final int id){
		_context.getActivity().runOnUiThread(new Runnable(){
			public void run(){

				AlertDialog alertDialog = new AlertDialog.Builder(_context.getActivity()).create();
				alertDialog.setTitle(R.string.codestitlemanage);
				alertDialog.setMessage(_context.getActivity().getText(R.string.codeaskdelete));
				alertDialog.setButton(_context.getActivity().getText(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try{
							SGBD sd = new SGBD(_context.getActivity());
							sd.open();
							sd.deleteCodes(_new.get(id).getId());
							sd.close();
							_new.remove(id);
							dialog.dismiss();
							_context.getActivity().runOnUiThread(new Runnable(){
								public void run(){
									CodesAdapter.this.notifyDataSetChanged();
								}
							});
						}catch(Exception e){
							e.printStackTrace();
						}
					} 
				}); 
				alertDialog.setButton2(_context.getActivity().getText(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					} 
				}); 
				alertDialog.show();
			}
		});
	}
	public void add(Code c){
		_new.add(c);
		this.notifyDataSetChanged();
	}

	public View getView(final int pos, View inView, ViewGroup parent) {
		if(inView == null){
			LayoutInflater inflater = (LayoutInflater) _context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inView = inflater.inflate(R.layout.code_list, null);//item_pager_extension
		}

		ImageView img = (ImageView)inView.findViewById(R.codes.delete);
		img.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				delete(pos);
			}

		});
		TextView t = (TextView)inView.findViewById(R.codes.code);

		t.setText(_new.get(pos).getCode());
		Log.d("code", _new.get(pos).getCode());
		/*ItemExtensionListingCardPagerAdapter adapter = new ItemExtensionListing(_context, _util, pos);
		TitlePageIndicator indicator =
				(TitlePageIndicator)findViewById( R.id.indicator );
		ViewPager pager = 
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);*/
		return(inView);
	}
	@Override
	public int getCount() {
		return _new != null ? _new.size() : 0;
	}

	@Override
	public Object getItem(int pos) {
		return _new.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return _new.get(pos).getId();
	}
}

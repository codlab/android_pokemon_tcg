package fr.codlab.cartes.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.CodesAdapter;
import fr.codlab.cartes.bdd.SGBD;
import fr.codlab.cartes.util.Code;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

final public class CodesFragment extends SherlockFragment{
	private View _this;
	private CodesAdapter _a;
	public CodesFragment(){
		super();
		_a = null;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.listview_code, container, false);		
		_this = mainView;
		return mainView;

	}

	@Override
	public void onViewCreated(final View v, Bundle saved){
		//setHasOptionsMenu(true);
		ListView _v = (ListView)v.findViewById(R.codes.liste);
		_a = new CodesAdapter(this);
		_v.setAdapter(_a);

		Button _b = (Button)v.findViewById(R.codes.submit);
		_b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v2) {
				TextView _tv = (TextView)v.findViewById(R.codes.addcode);
				String s = _tv.getText().toString();
				if(s != null)
					add(s);
			}
		});
	}

	void add(String _s){
		_s = _s.toUpperCase().replaceAll("[^A-Z0-9]", "");
		if(_s.length() != 13){
			this.getActivity().runOnUiThread(new Runnable(){
				public void run(){
					AlertDialog alertDialog = new AlertDialog.Builder(CodesFragment.this.getActivity()).create();
					alertDialog.setTitle(R.string.codestitlemanage);
					alertDialog.setMessage(CodesFragment.this.getActivity().getText(R.string.codesubmitwrong));
					alertDialog.setButton(CodesFragment.this.getActivity().getText(R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						} 
					}); 
					alertDialog.show();
					
				}
			});
		}else{
			SGBD sg = new SGBD(this.getActivity());
			sg.open();
			long id = sg.addCode(_s);
			
			View v = this.getView();
			Code c = new Code(id, Code.VALID, _s);
			ListView _v = (ListView)v.findViewById(R.codes.liste);
			ListAdapter a = _v.getAdapter();
			if(a instanceof CodesAdapter)
				((CodesAdapter)a).add(c);
			
			sg.close();
			this.getActivity().runOnUiThread(new Runnable(){
				public void run(){
					AlertDialog alertDialog = new AlertDialog.Builder(CodesFragment.this.getActivity()).create();
					alertDialog.setTitle(R.string.codestitlemanage);
					alertDialog.setMessage(CodesFragment.this.getActivity().getText(R.string.codesubmitok));
					alertDialog.setButton(CodesFragment.this.getActivity().getText(R.string.ok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						} 
					}); 
					alertDialog.show();
					
				}
			});
		}
	}
	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		_factorise.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(_factorise.onOptionsItemSelected(item) == false)
			return super.onOptionsItemSelected(item);
		else
			return true;
	}*/

	public void onPause(){
		super.onPause();
	}

	public void onResume(){
		super.onResume();
	}

	public void onDestroy(){
		super.onDestroy();
	}
}

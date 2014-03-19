package fr.codlab.cartes.adaptaters;

import java.util.ArrayList;

import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.Extension;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

public class PrincipalExtensionAdapter extends BaseAdapter {
	private Context context;
	private MainActivity _principal;
	private ArrayList<Extension> _item;

	public PrincipalExtensionAdapter(MainActivity a,ArrayList<Extension> item) {
		_principal=a;
		context=a;
		_item=item;
	}

	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if(pos == 0){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_extension_code, null);
			v.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_principal.onClickTCGO();
				}
			});
		}else{
			final int position=pos-1;
			if(v == null ||  v.findViewById(R.id.extension_nom) == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.list_extension, null);
			}

			TextView bTitle = (TextView) v.findViewById(R.id.extension_nom);
			bTitle.setText(_item.get(position).getName());

			v.setOnClickListener(new OnClickListener(){
				//@Override
				public void onClick(View v) {
					_principal.onClick(_item.get(position).getName(),
							_item.get(position).getId(),
							_item.get(position).getShortName());
				}
			});
			ImageView icon = (ImageView) v.findViewById(R.id.extensionicon);
			icon.setImageResource(v.getResources().getIdentifier(_item.get(position).getShortName() , "drawable", context.getPackageName()));

			TextView bInfos = (TextView) v.findViewById(R.id.extcollection);
			bInfos.setText(" "+_item.get(position).getProgress()+"/"+_item.get(position).getCount());
			bInfos = (TextView) v.findViewById(R.id.extpossedees);
			bInfos.setText(" "+_item.get(position).getPossessed());
		}
		return(v);
	}

	@Override
	public int getCount() {
		return _item.size()+1;//1 for the code header !
	}

	@Override
	public Object getItem(int pos) {
		return pos > 0 ? _item.get(pos-1) : null;//_items[arg0];
	}

	@Override
	public long getItemId(int pos) {
		return pos > 0 ? _item.get(pos-1).getId() :0;//_items[arg0];
	}
}

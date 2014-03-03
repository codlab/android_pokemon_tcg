package fr.codlab.cartes.adaptaters;

import fr.codlab.cartes.MainActivity;
import fr.codlab.cartes.R;
import fr.codlab.cartes.util.CardImageView;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.Language;
import fr.codlab.cartes.widget.Gallery3D;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ExtensionListImageAdapter extends BaseAdapter {
	int _background;
	private Context _context;

	//private ImageView[] mImages;
	private Extension _extension;

	public ExtensionListImageAdapter(Context c, Extension extension) {
		_context = c;
		_extension = extension;
	}


	public int getCount() {
		return _extension.getCount();
	}

	public Object getItem(int position) {
		return _extension.getCarte(position);
	}

	public long getItemId(int position) {
		return _extension.getCarte(position).getId();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		//Use this code if you want to load from resources
		ImageView i = new ImageView(_context);
		try{
			CardImageView.setBitmapToImageView(i, _extension.getShortName(), _extension.getShortName()+"_"+_extension.getCarte(position).getCarteId()+( MainActivity.InUse == Language.FR ? "" : "_us"), true);
			i.setLayoutParams(new Gallery3D.LayoutParams(80, 130));
			i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 

			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			drawable.setAntiAlias(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return i;
	}
}
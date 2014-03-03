package fr.codlab.cartes.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.widget.ImageView;
import fr.codlab.cartes.R;

public class CardImageView {
	private static int quality = 50;
	private static final float H=100f;
	private static final String thumb=".thumb2_";

	public static void hide(String card){
		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/");
		if(!dir.exists())
			dir.mkdirs();
		File img = new File(dir,".nomedia");
		img.mkdirs();
		if(!img.exists())
			try {
				img.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		if(!card.startsWith(".")){
			File _origin2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+card);
			if(_origin2.exists())
				_origin2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/."+card));
		}
	}
	public static void createThumb(String ext, String original){
		hide(original);
		File _origin = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+ext+"/."+original);
		File _mini = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+ext+"/"+thumb+original);
		if(_origin.exists() && !_mini.exists()){
			Bitmap _bmp_origin = BitmapFactory.decodeFile(_origin.getAbsolutePath());
			if(_bmp_origin != null){
				try {
					//int newWidth = 113;
					//int newHeight = 150;
					float scale_height = H/_bmp_origin.getHeight();
					Matrix matrix = new Matrix();
					// resize the bit map
					matrix.postScale(scale_height, scale_height);
					// recreate the new Bitmap
					Bitmap _bmp = Bitmap.createBitmap(_bmp_origin, 0, 0,
							_bmp_origin.getWidth(), _bmp_origin.getHeight(), matrix, true);
					FileOutputStream out;
					out = new FileOutputStream(_mini);
					_bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);

					_bmp_origin.recycle();

					if(c==10){
						System.gc();
						c=0;
					}else
						c++;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
	
	private static void createThumb(ImageView i, File original, String ext, String name){

		try {
			Bitmap _bmp_origin = BitmapFactory.decodeFile(original.getAbsolutePath());
			if(_bmp_origin != null){
				//int newWidth = 113;
				//int newHeight = 150;
				float scale_height = H/_bmp_origin.getHeight();
				Matrix matrix = new Matrix();
				// resize the bit map
				matrix.postScale(scale_height, scale_height);

				// recreate the new Bitmap
				Bitmap _bmp = Bitmap.createBitmap(_bmp_origin, 0, 0,
						_bmp_origin.getWidth(), _bmp_origin.getHeight(), matrix, true);
				_bmp_origin.recycle();
				if(c==10){
					System.gc();
					c=0;
				}else
					c++;
				FileOutputStream out;
				out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+ext+"/"+thumb+name+".jpg");
				_bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);
				i.setImageBitmap(_bmp);

				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int c=0;
	public static void setBitmapToImageView(ImageView i, String ext, String name, boolean is_mini_back){
		hide(name+".jpg");
		File _mini = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+ext+"/"+thumb+name+".jpg");
		if(_mini.exists()){
			Bitmap _bmp = BitmapFactory.decodeFile(_mini.getAbsolutePath());
			i.setImageBitmap(_bmp);

			if(c==10){
				System.gc();
				c=0;
			}else
				c++;
			_mini = null;
			return;
		}else{
			_mini = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+ext+"/."+name+".jpg");
			File _mini2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/card_images/"+ext+"/"+name+".jpg");
			if(_mini.exists()){
				createThumb(i, _mini, ext, name);
			}else if(_mini2.exists()){
				createThumb(i, _mini2, ext, name);
			}
		}
		if(is_mini_back)
			i.setImageResource(R.drawable.thumb_back);
		else
			i.setImageResource(R.drawable.back);
	}
}

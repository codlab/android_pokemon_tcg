package fr.codlab.cartes.push;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class ManagePush implements OnSharedPreferenceChangeListener {
	private Context _context;
	private boolean _want_register;
	private boolean _is_registered;
	private String _signup = "pokeke100@gmail.com";
	
	public ManagePush(Context context){
		_context = context;
		_is_registered = false;
		_want_register = false;
		registerShared();
		readWant();
	}

	public void registerShared(){
		if(_is_registered == false)
			_context.getSharedPreferences("__PUSH_CARTES_", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
		_is_registered = true;
	}
	public void unRegisterShared(){
		if(_is_registered == true)
			_context.getSharedPreferences("__PUSH_CARTES_", Context.MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
		_is_registered = false;
	}

	private void readWant(){
		_want_register = _context.getSharedPreferences("__PUSH_CARTES_", Context.MODE_PRIVATE).getBoolean("want_register", false);
		if(_want_register == true){
			Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
			registrationIntent.putExtra("app", PendingIntent.getBroadcast(_context, 0, new Intent(), 0));
			registrationIntent.putExtra("sender", _signup);
			_context.startService(registrationIntent);		}
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key != null){
			if("want_register".equals(key)){
				readWant();
			}
		}
	}
}

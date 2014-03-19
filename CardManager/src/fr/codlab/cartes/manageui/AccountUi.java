package fr.codlab.cartes.manageui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.bdd.SGBD;
import fr.codlab.cartes.updater.IUpdater;
import fr.codlab.cartes.updater.Updater;

public class AccountUi implements IUpdater{
	private static Updater _updater = null;
	private Context c = null;
	private Activity _activity_main;
	private static ProgressDialog _progress;
	private IExtensionMaster _master = null;

	private AccountUi(){

	}

	public AccountUi(Activity activity_main, IExtensionMaster master, View v){
		this();
		_activity_main = activity_main;
		_master = master;
		c = v.getContext();
		implement(v);
		_progress = null;
		setThis();
	}

	private void setThis(){
		if(_updater == null)
			_updater = new Updater(this);
		_updater.setParent(this);
	}

	public void implement(final View v){
		setThis();
		c = v.getContext();
		SharedPreferences s = c.getSharedPreferences("__TCGMANAGER__", Context.MODE_PRIVATE);

		((TextView)v.findViewById(R.account.nickname)).setText(s.getString("_NICK_", ""));
		((TextView)v.findViewById(R.account.password)).setText(s.getString("_PASS_", ""));

		Button button = (Button)v.findViewById(R.account.create);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				String login = ((TextView)v.findViewById(R.account.nickname)).getText().toString();
				String password = ((TextView)v.findViewById(R.account.password)).getText().toString();
				c.getSharedPreferences("__TCGMANAGER__", Context.MODE_PRIVATE).edit().putString("_NICK_", login)
				.putString("_PASS_", password).commit();
				_updater.create(c, login, password);
				createWaiter(c, c.getString(R.string.accountcreatetitle),c.getString(R.string.accountwaittext),0);
			}
		});

		button = (Button)v.findViewById(R.account.auth);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				String login = ((TextView)v.findViewById(R.account.nickname)).getText().toString();
				String password = ((TextView)v.findViewById(R.account.password)).getText().toString();
				c.getSharedPreferences("__TCGMANAGER__", Context.MODE_PRIVATE).edit().putString("_NICK_", login)
				.putString("_PASS_", password).commit();
				_updater.authent(c, login, password);
				createWaiter(c, c.getString(R.string.accountauthtitle),c.getString(R.string.accountwaittext),0);
			}
		});

		button = (Button)v.findViewById(R.account.down);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				String login = ((TextView)v.findViewById(R.account.nickname)).getText().toString();
				String password = ((TextView)v.findViewById(R.account.password)).getText().toString();
				c.getSharedPreferences("__TCGMANAGER__", Context.MODE_PRIVATE).edit().putString("_NICK_", login)
				.putString("_PASS_", password).commit();
				createWaiter(c, c.getString(R.string.accountdownloadtitle),c.getString(R.string.accountwaittext),0);
				_updater.download(c, login, password);
			}
		});
		button = (Button)v.findViewById(R.account.up);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v2) {
				String login = ((TextView)v.findViewById(R.account.nickname)).getText().toString();
				String password = ((TextView)v.findViewById(R.account.password)).getText().toString();
				c.getSharedPreferences("__TCGMANAGER__", Context.MODE_PRIVATE).edit().putString("_NICK_", login)
				.putString("_PASS_", password).commit();
				createWaiter(c, c.getString(R.string.accountuploadtitle),c.getString(R.string.accountwaittext),0);
				_updater.upload(c, login, password);
			}
		});

	}

	String title;
	String text;
	@Override
	public void notifyWork() {
		//createWaiter(c,title, text);
	}

	@Override
	public byte[] receiveData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void error() {
		stopWaiter();
		if(_activity_main != null)
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, c.getString(R.string.accounterror), Toast.LENGTH_LONG).show();
				}
			});
	}

	@Override
	public void createWaiter(Context c, String title, String text, int max) {
		if(_progress == null){
			this.title = title;
			this.text = text;
			_progress = new ProgressDialog(c);
			_progress.setTitle(title);
			_progress.setMessage(text);
			_progress.setCancelable(false);
			if(max > 0){
				_progress.setIndeterminate(false);
				_progress.setMax(max);
				_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			}
			_progress.show();
		}
	}

	public void createWaiter(int max) {
		this.createWaiter(_activity_main, c.getString(R.string.accountwritetitle),c.getString(R.string.accountwaittext),max);
	}

	public void incremente(Integer args){
		String str = args.toString();
		if(_progress != null){
			_progress.setProgress(args);
		}
	}
	@Override
	public void stopWaiter() {
		try{
			if(_progress != null){
				_progress.dismiss();
				_progress = null;
			}
		}catch(Exception e){

		}
	}

	@Override
	public void okcreate() {
		stopWaiter();
		if(_activity_main != null)
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, c.getString(R.string.accountcreateok), Toast.LENGTH_LONG).show();
				}
			});
	}

	@Override
	public void okauth() {
		stopWaiter();
		if(_activity_main != null)
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, c.getString(R.string.accountauthok), Toast.LENGTH_LONG).show();
				}
			});
	}

	@Override
	public void okupload(){
		stopWaiter();
		if(_activity_main != null)
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, c.getString(R.string.accountuploadok), Toast.LENGTH_LONG).show();
				}
			});
	}

	@Override
	public void okdownload(final String res){
		stopWaiter();
		if(_activity_main != null)
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					Toast.makeText(c, c.getString(R.string.accountdownloadok), Toast.LENGTH_LONG).show();
				}
			});

		if(_activity_main != null){
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					SGBD s = new SGBD(c);
					s.createfromEncodedPossessions(res,AccountUi.this);
				}
			});
		}
	}

	public void onCreateFinish() {
		if(_activity_main != null){
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					stopWaiter();
					Toast.makeText(c, c.getString(R.string.accountwroteok), Toast.LENGTH_LONG).show();
					if(_master != null)
						_master.notifyDataChanged();
				}
			});
		}
	}
	public void onDLNotFinish() {
		if(_activity_main != null){
			_activity_main.runOnUiThread(new Thread(){
				public void run(){
					stopWaiter();
				}
			});
		}
	}
}

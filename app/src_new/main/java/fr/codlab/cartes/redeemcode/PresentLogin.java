package fr.codlab.cartes.redeemcode;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import fr.codlab.cartes.updater.http.ActionURLS;
import fr.codlab.cartes.updater.http.IURLLoaded;
import fr.codlab.cartes.updater.http.LoadManagement;
import fr.codlab.cartes.updater.http.LoadURL;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * classe used to manage login
 * 
 * a first call is made to the login page from inmobi a second one to the
 * attemptlogin if an error occured > no cookie with *123 or *124 is created >
 * failure and finally to the dashboard > success
 * 
 * note : is this final call to the dashboard usefull?
 */
public class PresentLogin extends LoadManagement implements IURLLoaded, IGetLogin {
	private String _login;
	private String _pwd;
	private String _code;
	private IGetLogin _parent;
	private Context _context;
	private int _con;

	public PresentLogin(Context context, IGetLogin parent, String login,
			String pwd) {
		super(context);
		_context = context;
		_login = login;
		_pwd = pwd;
		_parent = parent;
		_con = 0;

	}

	public void compute() {
		GetLogin t = new GetLogin(_context, this, _login, _pwd);
		t.compute();	
	}

	public void loadEnd(int serial, String text, ProgressDialog progress) {
		/*
		end();

		if (_error) {
			try {
				if (progress != null)
					progress.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return;
		}

		int coupe = text.indexOf("name=\"lt\" value=\"");
		if(coupe >= 0){
			String r = text.substring(coupe+"name=\"lt\" value=\"".length());
			int quote = r.indexOf("\"");
			Log.d("information ok", r);
			if(quote > 0){
				String f = r.substring(0, quote);
				Log.d("information ok", f);
			}
		}else
			Log.d("information", text);*/

	}

	boolean _error = false;

	public void loadFailure(int serial, String text) {
		_error = true;
	}

	public void loadSuccess(int serial, String text) {
		if(serial == 1){
			LoadURL _url = new LoadURL(this, 2, "https://sso.pokemon.com/sso/login?service=http%3A%2F%2Fwww.pokemontcg.com%2Fcas%2Fsignin&locale=en", null);
			String _headers[] = { 
					"Host","www.pokemontcg.com",
					"User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0",
					"Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
					"Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3",
					"Accept-Encoding","gzip, deflate",
					"Connection","keep-alive",
			};
			_url.loadGetUrl(_headers, false);

		}else if(serial == 2){
			Log.d("fin",text);
		}else{
			int index = text.indexOf("top.location.href=\"");
			if(index >= 0){
				text = text.substring(index + "top.location.href=\"".length());
				text = text.split("\"")[0];
				Log.d("text",text);
			}else{
				Log.d("text","text");
			}
		}
	}

	public int getConnectionNumber() {
		return _con;
	}

	public void loadMove(int serial, String text) {
		Log.d("load move",text);
	}

	@Override
	public void onLoadOk(String text) {

		begin();

		Log.d("Information ? ", text);
		LoadURL _url = new LoadURL(this, 1, "https://sso.pokemon.com/sso/login?service=http%3A%2F%2Fwww.pokemontcg.com%2Fcas%2Fsignin&locale=en", null);

		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("lt", text));
		nvps.add(new BasicNameValuePair("_eventId", "submit"));
		nvps.add(new BasicNameValuePair("username", _login));
		nvps.add(new BasicNameValuePair("password", _pwd));
		nvps.add(new BasicNameValuePair("Login", "Log In"));

		String _headers[] = { 
				"Host","sso.pokemon.com",
				"User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0",
				"Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
				"Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3",
				"Accept-Encoding","gzip, deflate",
				"Connection","keep-alive",
				"Referer","https://sso.pokemon.com/sso/login?service=http%3A%2F%2Fwww.pokemontcg.com%2Fcas%2Fsignin&locale=en",
		};
		_url.loadPostUrl(_headers, nvps, false);

	}

	@Override
	public void onLoadError(String text) {
		// TODO Auto-generated method stub

	}

}

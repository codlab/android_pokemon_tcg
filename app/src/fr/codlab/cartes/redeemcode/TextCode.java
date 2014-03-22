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
public class TextCode extends LoadManagement implements IURLLoaded {
	private String _code;
	private ITextCode _parent;
	private int _con;

	public TextCode(Context context, ITextCode parent, String code) {
		super(context);
		_parent = parent;
		_code=code;
		_con = 0;
	}

	public void compute() {
		begin();

		LoadURL _url = new LoadURL(this, 1, "https://items-shop.pokemontcg.com/coupons?wicket:interface=:2::IActivePageBehaviorListener:11:&wicket:ignoreIfNotActive=true", null);
		//String _headers[] = { "Host", getServer(),
		// "User-Agent","Mozilla/5.0 (X11; Linux i686; rv:5.0) Gecko/20100101 Firefox/5.0",
		// "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		// "Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3",
		// "Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7",
		// "Accept-Encoding","gzip, deflate",
		// "Connection","keep-alive",
		//"Referer", getProtocol()+"://" + getServer() };
		String _headers[] = {"Accept","application/json, text/javascript, */*",
				"Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.3",
				"Accept-Encoding","gzip,deflate,sdch",
				"Accept-Language","fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4",
				"Connection","keep-alive",
				"Host","items-shop.pokemontcg.com",
				"Origin","https://items-shop.pokemontcg.com",
				"Referer","https://items-shop.pokemontcg.com/coupons",
				"User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11",
			
				"X-Requested-With","XMLHttpRequest"};
		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("code", _code));
		_url.loadPostUrl(_headers, nvps, false);
	}

	public void loadEnd(int serial, String text, ProgressDialog progress) {
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

		Log.d("information code", text);

	}

	boolean _error = false;

	public void loadFailure(int serial, String text) {
		_error = true;
	}

	public void loadSuccess(int serial, String text) {
	}

	public int getConnectionNumber() {
		return _con;
	}

	public void loadMove(int serial, String text) {
	}

}

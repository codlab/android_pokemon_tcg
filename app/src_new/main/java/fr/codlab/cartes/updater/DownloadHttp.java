package fr.codlab.cartes.updater;

import org.json.JSONException;
import org.json.JSONObject;

import fr.codlab.cartes.updater.http.ActionURLS;
import fr.codlab.cartes.updater.http.IURLLoaded;
import fr.codlab.cartes.updater.http.LoadManagement;
import fr.codlab.cartes.updater.http.LoadURL;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * classe used to manage login
 * 
 * a first call is made to the login page from inmobi a second one to the
 * attemptlogin if an error occured > no cookie with *123 or *124 is created >
 * failure and finally to the dashboard > success
 * 
 * note : is this final call to the dashboard usefull?
 */
class DownloadHttp extends LoadManagement implements IURLLoaded {
	private String _login;
	private String _pwd;
	private IDownloadHttp _parent;
	private int _con;

	public DownloadHttp(Context context, IDownloadHttp parent, String login,
			String pwd) {
		super(context);
		_login = login;
		_pwd = pwd;
		_parent = parent;
		_con = 0;
	}

	public void compute() {
		begin();

		LoadURL _url = new LoadURL(this, 1, getUrl()
				+ ActionURLS.getDownloadURL(_login, _pwd),
				null);
		//String _headers[] = { "Host", getServer(),
				// "User-Agent","Mozilla/5.0 (X11; Linux i686; rv:5.0) Gecko/20100101 Firefox/5.0",
				// "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
				// "Accept-Language","fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3",
				// "Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7",
				// "Accept-Encoding","gzip, deflate",
				// "Connection","keep-alive",
				//"Referer", getProtocol()+"://" + getServer() };
		String _headers[] = { "Host", getServer()+":"+getPort(), 
				"Referer", getProtocol()+"://" + getServer()};
		_url.loadGetUrl(_headers, false);
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

			_parent.onDownloadFailure("log");
			return;
		}
		JSONObject _objet;
		try {
			_objet = new JSONObject(text);
			if (_objet.has("data")) {
				_parent.onDownloadSuccess(_objet.optString("data"));
			} else {
				_parent.onDownloadFailure("Error downloading");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

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

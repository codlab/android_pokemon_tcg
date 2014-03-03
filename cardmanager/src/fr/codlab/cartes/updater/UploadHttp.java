package fr.codlab.cartes.updater;

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
 * Class used to initiate a tex file modification on the server
 * 
 * @author Kevin Le Perf
 * 
 */
class UploadHttp extends LoadManagement implements IURLLoaded {
	private String _login;
	private String _pwd;
	private String _data;
	private IUploadHttp _parent;
	private int _con;
	private boolean _error = false;

	/**
	 * 
	 * @param context
	 *            the activity or programme context
	 * @param parent
	 *            the parent
	 * @param login
	 *            the login info
	 * @param pwd
	 * @param data
	 *            the data we want to update
	 */
	public UploadHttp(Context context, IUploadHttp parent, String login,
			String pwd, String data) {
		super(context);
		_login = login;
		_pwd = pwd;
		_parent = parent;
		_con = 0;
		_data = data;
	}

	/**
	 * compute Call the server to update the file
	 */
	public void compute() {
		begin();

		LoadURL _url = new LoadURL(this, 1, getUrl()
				+ ActionURLS.getUploadURL(_login, _pwd),
				null);
		Log.d("UPLOAD", ActionURLS.getUploadURL(_login, _pwd));
		String _headers[] = { "Host", getServer()+":"+getPort(), 
				"Referer", getProtocol()+"://" + getServer()};
		ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("data", _data));

		_url.loadPostUrl(_headers, nvps, false);
	}

	/**
	 * loadEnd
	 * 
	 * @param serial
	 *            the serial of the page we successfully loaded
	 * @param text
	 *            the text we obtained
	 * @param progress
	 *            a reference to a potential progressbar
	 */
	public void loadEnd(int serial, String text, ProgressDialog progress) {
		end();

		// if we have an error we call the parent with the "log" error
		if (_error) {
			try {
				if (progress != null)
					progress.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}

			_parent.onUploadFailure("log");
			return;
		}

		// else

		JSONObject _objet;
		try {
			_objet = new JSONObject(text);
			// has the text successfully been saved ?
			if (_objet.has("upload")) {
				_parent.onUploadSuccess(_objet.optString("data"));
			} else {
				_parent.onUploadFailure("error "+text);
			}
		} catch (JSONException e) {
			// error lol
			_parent.onUploadFailure(text);
			e.printStackTrace();
		}

	}

	public void loadFailure(int serial, String text) {
		_error = true;
	}

	public void loadSuccess(int serial, String text) {
	}

	public int getConnectionNumber() {
		return _con;
	}

	/**
	 * @deprecated
	 */
	public void loadMove(int serial, String text) {
	}

}

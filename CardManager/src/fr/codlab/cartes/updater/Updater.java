package fr.codlab.cartes.updater;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.codlab.cartes.bdd.SGBD;
import android.content.Context;

public class Updater implements IAuthHttp, ICreateAccountHttp, IDownloadHttp, IUploadHttp{
	private IUpdater _parent;
	private String _login;
	private String _password;
	private String _encrypted_password;
	private int _working;

	public Updater(IUpdater parent){
		setParent(parent);
	}

	private boolean isWorking(){
		return _working == 1;
	}

	public void setParent(IUpdater parent){
		_parent = parent;
		if(isWorking())
			_parent.notifyWork();
	}

	public void start(){
		_working = 1;
	}
	public void stop(){
		_working = 0;
	}

	public void create(Context c, String login, String password){
		try{
			_login = URLEncoder.encode(login, "UTF-8");
			_password = URLEncoder.encode(password, "UTF-8");
			CreateAccountHttp a = new CreateAccountHttp(c, this, _login, _password);
			start();
			a.compute();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void authent(Context c, String login, String password){
		try {
			_login = URLEncoder.encode(login, "UTF-8");
			_password = URLEncoder.encode(password, "UTF-8");
			AuthHttp a = new AuthHttp(c, this, _login, _password);
			start();
			a.compute();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void upload(Context c, String login, String password){
		try{
			String _login_tmp = URLEncoder.encode(login, "UTF-8");
			String _password_tmp =  URLEncoder.encode(password, "UTF-8");
			if(_login != null && _password != null && _login.equals(_login_tmp) && _password.equals(_password_tmp) && _encrypted_password != null){
				SGBD s = new SGBD(c);
				s.open();
				String data = s.getEncodedPossessions();
				UploadHttp a = new UploadHttp(c, this, _login, _encrypted_password, data);
				s.close();
				a.compute();
			}else
				_parent.error();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void download(Context c, String login, String password){
		try{
			String _login_tmp = URLEncoder.encode(login, "UTF-8");
			String _password_tmp =  URLEncoder.encode(password, "UTF-8");
			if(_login != null && _password != null && _login.equals(_login_tmp) && _password.equals(_password_tmp) && _encrypted_password != null){
				DownloadHttp a = new DownloadHttp(c, this, _login, _encrypted_password);
				a.compute();
			}else
				_parent.error();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreateAccountSuccess(String password) {
		_encrypted_password = password;
		stop();
		_parent.okcreate();
	}

	@Override
	public void onCreateAccountFailure(String text) {
		stop();	
		_parent.error();
	}

	@Override
	public void onAuthSuccess(String password) {
		_encrypted_password = password;
		stop();
		_parent.okauth();
	}

	@Override
	public void onAuthFailure(String text) {
		stop();
		_parent.error();
	}

	@Override
	public void onUploadSuccess(String text) {
		stop();
		_parent.okupload();
	}

	@Override
	public void onUploadFailure(String text) {
		stop();
		_parent.error();
	}

	@Override
	public void onDownloadSuccess(String data) {
		stop();
		_parent.okdownload(data);

	}

	@Override
	public void onDownloadFailure(String text) {
		stop();
		_parent.error();

	}

}

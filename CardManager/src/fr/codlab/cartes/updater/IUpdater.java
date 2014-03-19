package fr.codlab.cartes.updater;

import android.content.Context;

public interface IUpdater {
	void notifyWork();
	public byte [] receiveData();
	public void error();
	public void stopWaiter();
	void okcreate();
	void okauth();
	void okupload();
	void okdownload(String res);
	void createWaiter(Context c, String title, String text, int max);
}

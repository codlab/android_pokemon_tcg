package fr.codlab.cartes;

import android.os.Bundle;

public interface IExtensionListener {
	void updateName(String nom);
	void updateProgress(int progression,int count);
	void updatePossessed(int total);
	void updated(int id);
	public void onClick(Bundle pack);

}

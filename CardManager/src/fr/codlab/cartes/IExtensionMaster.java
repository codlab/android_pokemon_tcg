package fr.codlab.cartes;

import android.os.Bundle;

public interface IExtensionMaster {
	/**
	 * Update the extension with the extension_id
	 * 
	 * @param extension_id the extension updated
	 */
	void update(int extension_id);
	
	void onClick(Bundle pack);
	void onClick(String nom,
			int id,
			String intitule);
	void notifyDataChanged();
}

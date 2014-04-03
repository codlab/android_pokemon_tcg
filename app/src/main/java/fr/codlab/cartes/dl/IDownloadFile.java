package fr.codlab.cartes.dl;

interface IDownloadFile {
	public void receiveProgress(String msg, Double args);
	
	public void onPost(Long result);
	
	public void onErrorSd();

	public void onErrorUrl();
}

package fr.codlab.cartes.updater;

/**
 * Interface to manage the file creation in a project
 * 
 * @author Kevin Le Perf
 * 
 */
interface IDownloadHttp {
	/**
	 * onCreateFileSuccess Method called when the file was created
	 * 
	 * @param files
	 *            the new project's file list
	 * @param server_version
	 *            a string representing the file server or null
	 */
	public void onDownloadSuccess(String data);

	/**
	 * onCreateFileFailure
	 * 
	 * @param text
	 *            the text describing the error
	 * @param server_version
	 *            a string representing the file server or null
	 */
	public void onDownloadFailure(String text);
}

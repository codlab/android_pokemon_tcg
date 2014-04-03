package fr.codlab.cartes.updater;
/*
 * Interface created to manage upload
 * 
 * @author Kevin Le Perf
 * 
 */
interface IAuthHttp {
	public void onAuthSuccess(final String password);

	public void onAuthFailure(final String text);
}

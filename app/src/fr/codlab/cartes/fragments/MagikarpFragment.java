package fr.codlab.cartes.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.codlab.cartes.IExtensionMaster;
import fr.codlab.cartes.R;
import fr.codlab.cartes.dl.DownloaderMagikarp;

final public class MagikarpFragment extends SherlockFragment {
    private static DownloaderMagikarp _magikarp;
    private String uri="http://94.125.160.65/?activate_magikarp=true";

    public MagikarpFragment(){
		super();
	}

	public MagikarpFragment(IExtensionMaster parent){
		this();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.magikarp, container, false);
        Button btn = (Button)mainView.findViewById(R.id.magikarp_activate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _magikarp = new DownloaderMagikarp(MagikarpFragment.this, getActivity(), uri);
                _magikarp.activating();
            }
        });
		return mainView;

	}

	@Override
	public void onViewCreated(View v, Bundle saved){
        if(_magikarp != null)
            _magikarp.activating();
	}

	public void onPause(){
		super.onPause();

        if(_magikarp != null)
            _magikarp.activatingQuit();
	}

	public void onResume(){
		super.onResume();
	}

	public void onDestroy(){
		super.onDestroy();
	}


}

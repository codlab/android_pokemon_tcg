package fr.codlab.cartes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.codlab.cartes.R;
import fr.codlab.cartes.adaptaters.PrincipalExtensionAdapter;
import fr.codlab.cartes.fragments.CardFragment;
import fr.codlab.cartes.fragments.CodesFragment;
import fr.codlab.cartes.fragments.InformationScreenFragment;
import fr.codlab.cartes.fragments.ExtensionFragment;
import fr.codlab.cartes.fragments.ListViewExtensionFragment;
import fr.codlab.cartes.redeemcode.IGetLogin;
import fr.codlab.cartes.redeemcode.ITextCode;
import fr.codlab.cartes.util.Extension;
import fr.codlab.cartes.util.ExtensionManager;
import fr.codlab.cartes.util.FileMover;
import fr.codlab.cartes.util.Language;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.MenuInflater;
import com.android.vending.util.IabHelper;
import com.android.vending.util.IabResult;
import com.android.vending.util.Inventory;
import com.android.vending.util.Purchase;
import com.slidingmenu.lib.SlidingMenu;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Classe de dŽmarrage de l'application
 * <p/>
 * utilise un Pager
 * premi�re frame : information textuelle
 * deuxi�me : liste des extensions
 * a venir : troisieme : liste des codes boosters online
 *
 * @author kevin
 */
public class MainActivity extends SlidingViewPagerFragmentActivity implements IExtensionMaster, IGetLogin, ITextCode,

        IabHelper.QueryInventoryFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnConsumeFinishedListener {

    /**
     * PLAYSTORE PART
     */


    private boolean _playstore_ok;


    private Random _random;
    private IabHelper mHelper;


    private boolean _was_don_1 = false;
    private boolean _was_don_2 = false;
    private String base64EncodedPublicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoBrxvOEVfZwjKXu7GTZJoMAIF9C3TkTwryrYuar1+z2Hkn5l5YIE49exfArUjL65xBuOFrhQ2zUZLxnMfbTixuXxR2g1JFTktVdCvl96eGWU+jYkLTDuovLO2JMtTFT/niHrWUaZ7OiuziqSY5HgERYVzt+CA2j0mmr8F2T+G88T5sBHY9gz0lSHN5ErU+mMjmv9vfYuGdLBvoRhXAY8XYfT9YtH+2+cfbV9UqwnhHWmvz70eZQLt9wU7gGiCCMv2itjdbbdk4T+4gXXd8v6Yb07Hl0upe/7BTbB9iTbmpXo8CAaVdX5VJaLYM7FzOj0jFGmtB7dXw4ctMVcmhcepwIDAQAB";
    private IabHelper getHelper(){
        if(mHelper == null)mHelper = new IabHelper(this, base64EncodedPublicKey);
        return mHelper;
    }
    public void initHelper(){
        getHelper().startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(final IabResult result) {
                _playstore_ok = true;
                runOnUiThread(new Runnable(){
                    public void run(){
                        if (!result.isSuccess()) {
                            //Toast.makeText(Int2ExtActivity.this, "Problem setting up inapp " + result, Toast.LENGTH_LONG);
                            // Oh noes, there was a problem.
                        }else{
                            onPlaystoreOK();
                        }
                    }
                });
                // Hooray, IAB is fully set up!
            }
        });
    }
    public void createDonationDialog(boolean don1_purchased, boolean don2_purchased){
        if(don1_purchased == true && don2_purchased == true){
            return;
        }

        if(!_playstore_ok){
            try{
                _was_don_1 = true;
                initHelper();
            }catch(Exception e){

            }
        }else{
            AlertDialog alertDiaLog = new AlertDialog.Builder(this).create();
            alertDiaLog.setTitle(R.string.dialog_donation_title);
            alertDiaLog.setMessage(getString(R.string.dialog_donation_message));
            alertDiaLog.setButton(getString(R.string.dialog_donation_no_thx), new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            if(don1_purchased == false){
                alertDiaLog.setButton2(getString(R.string.dialog_donation_mini), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            getHelper().launchPurchaseFlow(MainActivity.this, "don1", 01,
                                    MainActivity.this, _random.nextInt(1353676232)+"");
                        }catch(Exception e){

                        }
                        arg0.dismiss();
                    }
                });
            }
            if(don2_purchased == false){
                alertDiaLog.setButton3(getString(R.string.dialog_donation_max), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try{
                            getHelper().launchPurchaseFlow(MainActivity.this, "don2", 02,
                                    MainActivity.this, _random.nextInt(1353676232)+"");
                        }catch(Exception e){

                        }
                        arg0.dismiss();
                    }
                });
            }
            alertDiaLog.show();

        }

    }

    public void onPlaystoreOK(){
        try{
            List additionalSkuList = new ArrayList();
            additionalSkuList.add("don1");
            additionalSkuList.add("don2");
            getHelper().queryInventoryAsync(true, additionalSkuList,
                    this);
        }catch(Exception e){

        }
    }


    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        if (result.isFailure() || result == null || inventory == null ||
                inventory.getSkuDetails("don1") == null || inventory.getSkuDetails("don2") == null) {
            return;
        }

        String don1 =
                inventory.getSkuDetails("don1").getPrice();
        String don2 =
                inventory.getSkuDetails("don2").getPrice();

        if(inventory.hasPurchase("don1")){
            getHelper().consumeAsync(inventory.getPurchase("don1"),
                    this);
        }
        if(inventory.hasPurchase("don2")){
            getHelper().consumeAsync(inventory.getPurchase("don2"),
                    this);
        }

        if(_random.nextInt(100) < 20)
            createDonationDialog(inventory.hasPurchase("don1"),inventory.hasPurchase("don2"));

        // update the UI
    }
    @Override
    public void onIabPurchaseFinished(final IabResult result, final Purchase info) {
        runOnUiThread(new Runnable(){
            public void run(){
                if (result.isFailure()) {
                    return;
                }else if (info.getSku().equals("don1")) {
                    Toast.makeText(MainActivity.this, R.string.purchased_don1, Toast.LENGTH_LONG);
                }else if (info.getSku().equals("don2")) {
                    Toast.makeText(MainActivity.this, R.string.purchased_don2, Toast.LENGTH_LONG);
                }
            }
        });
    }
    @Override
    public void onConsumeFinished(Purchase purchase, IabResult result) {
    }

    //create the menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return true;
    }

    /**
     * / PLAYSTORE PART
     */
    public static final int MAX = 60;
    private ArrayList<Extension> _arrayExtension;
    public final static String PREFS = "_CODLABCARTES_";
    public final static String USE = "DISPLAY";
    public static Language InUse = Language.US;
    private final static int US = 0;
    private final static int FR = 1;
    private final static int ES = 2;
    private final static int IT = 3;
    private final static int DE = 4;

    /**
     * The Acitivty receive an intent
     */
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent i) {
        try {
            super.onActivityResult(requestCode, resultCode, i);
            if (i != null) {
                Bundle bd = i.getExtras();
                //on observe les modifications apportees
                int miseAjour = bd.getInt("update", 0);
                if (miseAjour >= 0) {
                    update(miseAjour);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        _random = new Random();
        super.onCreate(savedInstanceState);



        _playstore_ok = false;
        _was_don_1 = false;
        _was_don_2 = false;

        setContentView(R.layout.main);

        initHelper();

        /**
         * open sliding menu
         */
        this.setBehindContentView(R.layout.main_sliding);


        SharedPreferences shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
        if (!shared.contains(MainActivity.USE)) {
            if ("FR".equals(this.getString(R.string.lang))) {
                shared.edit().putInt(MainActivity.USE, MainActivity.FR).commit();
                InUse = Language.FR;
            } else if ("ES".equals(this.getString(R.string.lang))) {
                shared.edit().putInt(MainActivity.USE, MainActivity.ES).commit();
                InUse = Language.ES;
            } else if ("IT".equals(this.getString(R.string.lang))) {
                shared.edit().putInt(MainActivity.USE, MainActivity.IT).commit();
                InUse = Language.IT;
            } else if ("DE".equals(this.getString(R.string.lang))) {
                shared.edit().putInt(MainActivity.USE, MainActivity.DE).commit();
                InUse = Language.DE;
            } else {
                shared.edit().putInt(MainActivity.USE, MainActivity.US).commit();
                InUse = Language.US;
            }
        } else {
            switch (shared.getInt(MainActivity.USE, MainActivity.US)) {
                case MainActivity.FR:
                    InUse = Language.FR;
                    break;
                case MainActivity.DE:
                    InUse = Language.DE;
                    break;
                case MainActivity.ES:
                    InUse = Language.ES;
                    break;
                case MainActivity.IT:
                    InUse = Language.IT;
                    break;
                default:
                    InUse = Language.US;
                    break;
            }
        }

        createExtensions();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View mobile = findViewById(R.id.mobile);
        //on est sur tablette
        //donc gestion avec les fragments

        if (mobile != null) {

            SlidingMenu sm = getSlidingMenu();
            sm.setMode(SlidingMenu.LEFT);
            sm.setSlidingEnabled(true);
            sm.setShadowWidthRes(R.dimen.shadow_width);
            sm.setBehindWidthRes(R.dimen.slidingmenu_offset);
            sm.setFadeDegree(0.90f);
            sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            setSlidingActionBarEnabled(false);
            sm.setSlidingEnabled(true);

            ListViewExtensionFragment viewer = (ListViewExtensionFragment) getSupportFragmentManager().findFragmentById(R.id.liste_extension_fragment);
            viewer.setListExtension(this);
        } else {
            SlidingMenu sm = getSlidingMenu();
            sm.setSlidingEnabled(false);


            //on est sur tablette
            //donc gestion avec les fragments
            ListViewExtensionFragment viewer = (ListViewExtensionFragment) getSupportFragmentManager().findFragmentById(R.id.liste_extension_fragment);
            viewer.setListExtension(this);
        }
    }

    /**
     * Call after onResume or onCreate
     */
    @Override
    public void onStart() {
        super.onStart();

        /**
         * open menu
         */
        if (getSlidingMenu().isSlidingEnabled()) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (false == getSlidingMenu().isMenuShowing())
                                toggle();
                        }
                    });
                }
            };
            t.start();
        }

        //on rajoute le fragment si on est sur tablette
        Log.d("MainActivity", "findView lol 1");
        if (findViewById(R.id.liste_extension_fragment) != null && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Log.d("MainActivity", "findView lol 2");
            FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
            xact.add(R.id.extension_fragment, new InformationScreenFragment(this));
            xact.commit();
        }
    }

    private void createExtensions() {
        if (_arrayExtension != null)
            return;

        _arrayExtension = new ArrayList<Extension>();

        XmlPullParser parser = getResources().getXml(R.xml.extensions);
        //StringBuilder stringBuilder = new StringBuilder();
        //  <extension nom="Base" nb="1" id="id de l'extension" intitule="tag pour les images" />
        try {
            int id = 0;
            int nb = 0;
            String intitule = "";
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                intitule = "";
                id = 0;
                nb = 0;
                String name = parser.getName();
                String extension = null;
                if ((name != null) && name.equals("extension")) {
                    int size = parser.getAttributeCount();
                    for (int i = 0; i < size; i++) {
                        String attrNom = parser.getAttributeName(i);
                        String attrValue = parser.getAttributeValue(i);
                        if ((attrNom != null) && attrNom.equals("nom")) {
                            extension = attrValue;
                        } else if ((attrNom != null) && attrNom.equals("id")) {
                            try {
                                id = Integer.parseInt(attrValue);
                            } catch (Exception e) {
                                id = 0;
                            }
                        } else if ((attrNom != null) && attrNom.equals("nb")) {
                            try {
                                nb = Integer.parseInt(attrValue);
                            } catch (Exception e) {
                                nb = 0;
                            }
                        } else if ((attrNom != null) && attrNom.equals("intitule")) {
                            intitule = attrValue;

                            FileMover.Move(Environment.getExternalStorageDirectory().getAbsolutePath(), intitule);
                        }
                    }

                    if ((extension != null) && (id > 0 && id < MAX)) {
                        _arrayExtension.add(ExtensionManager.getExtension(this, id, nb, intitule, extension, false));
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    PrincipalExtensionAdapter _adapter;

    public void setListExtension(View v) {
        _adapter = new PrincipalExtensionAdapter(this, _arrayExtension);
        ListView _list = (ListView) v.findViewById(R.id.principal_extensions);
        _list.setAdapter(_adapter);
    }

    public void notifyChanged() {
        _adapter.notifyDataSetChanged();
    }

    public void notifyDataChanged() {
        for (int ind = 0; ind < _arrayExtension.size(); _arrayExtension.get(ind).updatePossessed(), ind++)
            ;
        notifyChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.principalmenu, menu);
        boolean state = super.onCreateOptionsMenu(menu);

        return state;
    }

    //creation du menu de l'application
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences _shared = null;

        switch (item.getItemId()) {
            case R.id.main_donation:
                this.createDonationDialog(false, false);
                return true;
            //modification en mode US
            case android.R.id.home:
                if (_carte != null || _extension != null || _codes != null) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.popBackStack();
                    if (_carte != null) {
                        _carte = null;
                        setViewPagerSelected(0);
                    } else {
                        if (_codes != null) {
                            _codes = null;
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        if (_extension != null) {
                            _extension = null;
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                    }
                }
                return true;
            case R.id.principal_paypal:
                Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=SEJ9ZE6WLG2H4");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                return true;
            case R.id.principal_useus:
                _shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
                _shared.edit().putInt(MainActivity.USE, MainActivity.US).commit();
                MainActivity.InUse = Language.US;
                return true;
            //modification en mode fr
            case R.id.principal_usefr:
                _shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
                _shared.edit().putInt(MainActivity.USE, MainActivity.FR).commit();
                MainActivity.InUse = Language.FR;
                return true;
            //modification en mode es
            case R.id.principal_usees:
                _shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
                _shared.edit().putInt(MainActivity.USE, MainActivity.ES).commit();
                MainActivity.InUse = Language.ES;
                return true;
            //modification en mode de
            case R.id.principal_usede:
                _shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
                _shared.edit().putInt(MainActivity.USE, MainActivity.DE).commit();
                MainActivity.InUse = Language.DE;
                return true;
            //modification en mode it
            case R.id.principal_useit:
                _shared = this.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
                _shared.edit().putInt(MainActivity.USE, MainActivity.IT).commit();
                MainActivity.InUse = Language.IT;
                return true;
            default:
                return false;
        }
    }

    ExtensionFragment _extension;
    CodesFragment _codes;
    CardFragment _carte;
    String _name;//last extension
    int _id;//last extension
    String _intitule;//last extension

    @Override
    public void onSaveInstanceState(Bundle out) {
        if (_codes != null) {
            try {
                getSupportFragmentManager().putFragment(out, "CODES", _codes);
            } catch (Exception e) {
            }
        }
        if (_name != null) {
            out.putString("NAME", _name);
            out.putInt("ID", _id);
            out.putString("INTIT", _intitule);

            try {
                getSupportFragmentManager().putFragment(out, "EXTENSION", _extension);
            } catch (Exception e) {
            }
        }

        if (_carte != null) {
            try {
                getSupportFragmentManager().putFragment(out, "CARTE", _carte);
            } catch (Exception e) {
            }
        }
        _carte = null;
        _extension = null;
        _codes = null;

        super.onSaveInstanceState(out);
    }

    @Override
    public void onRestoreInstanceState(Bundle in) {
        if (in != null && in.containsKey("CODES")) {
            _codes = (CodesFragment) getSupportFragmentManager().getFragment(in, "CODES");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (in != null && in.containsKey("NAME") && in.containsKey("ID") && in.containsKey("INTIT")) {
            _name = in.getString("NAME");
            _id = in.getInt("ID");
            _intitule = in.getString("INTIT");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (in != null && in.containsKey("CARTE")) {
            _carte = (CardFragment) getSupportFragmentManager().getFragment(in, "CARTE");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (in != null)
            super.onRestoreInstanceState(in);

    }

    public void onClick(String nom,
                        int id,
                        String intitule) {
        Fragment viewer = getSupportFragmentManager().findFragmentById(R.id.extension_fragment);
        if (viewer != null) {
            _name = nom;
            _id = id;
            _intitule = intitule;
            FragmentManager fm = getSupportFragmentManager();
            if (_carte != null) {
                if (_carte.isAdded()) {
                    FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
                    xact.remove(_carte);
                    xact.commit();
                }
                _carte = null;
                fm.popBackStackImmediate();
            }
            if (_codes != null && _codes.isVisible() && _codes.isAdded()) {
                FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
                xact.remove(_codes);
                xact.commit();
                _codes = null;
                fm.popBackStackImmediate();
            }
            if (_extension == null || !_extension.isVisible()) {
                //Fragment extension = getSupportFragmentManager().findFragmentByTag(nom);
                FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
                _extension = new ExtensionFragment(this, nom, id, intitule);
                //xact.show(_extension);
                //xact.replace(R.id.extension_fragment, _extension, nom);
                xact.replace(R.id.extension_fragment, _extension, "Extensions");
                xact.addToBackStack(null);
                xact.commit();
            } else {
                _extension.setExtension(nom, id, intitule);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Bundle objetbundle = new Bundle();
            objetbundle.putString("nom", nom);
            objetbundle.putInt("extension", id);
            objetbundle.putString("intitule", intitule);
            Intent intent = new Intent().setClass(this, ExtensionActivity.class);
            intent.putExtras(objetbundle);
            startActivityForResult(intent, 42);
        }
    }

    public void onClickTCGO() {
        Fragment viewer = getSupportFragmentManager().findFragmentById(R.id.extension_fragment);
        if (viewer != null) {
            FragmentManager fm = getSupportFragmentManager();
            if (_carte != null) {
                if (_carte.isAdded()) {
                    FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
                    xact.remove(_carte);
                    xact.commit();
                }
                setViewPagerSelected(0);
                _carte = null;
                fm.popBackStackImmediate();
            }
            if (_extension != null) {
                if (_extension.isAdded()) {
                    FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
                    xact.remove(_extension);
                    xact.commit();
                }
                _extension = null;
                fm.popBackStackImmediate();
            }
            if (_codes == null || !_codes.isVisible()) {
                //Fragment extension = getSupportFragmentManager().findFragmentByTag(nom);
                FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
                _codes = new CodesFragment();
                //xact.show(_extension);
                //xact.replace(R.id.extension_fragment, _extension, nom);
                xact.replace(R.id.extension_fragment, _codes, "Codes");
                xact.addToBackStack(null);
                xact.commit();
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Bundle objetbundle = new Bundle();
            //objetbundle.putString("nom", nom);
            Intent intent = new Intent().setClass(this, CodesActivity.class);
            intent.putExtras(objetbundle);
            startActivityForResult(intent, 43);
        }
    }

    public void onClick(Bundle pack) {
        FragmentTransaction xact = getSupportFragmentManager().beginTransaction();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (_carte == null || !_carte.isVisible()) {
            _carte = new CardFragment();
            _carte.setArguments(pack);
            _carte.setParent(_extension);
            //xact.show(_extension);
            //xact.replace(R.id.extension_fragment, _extension, nom);
            xact.add(R.id.extension_fragment, _carte, "Carte");
            xact.addToBackStack(null);
            xact.commit();
        }
    }

    /**
     * Receive a notification about modification
     * so update the fragment >> cause we have it since this function is called
     */
    @Override
    public void update(int extension_id) {
        int ind = 0;
        for (; ind < _arrayExtension.size() &&
                _arrayExtension.get(ind).getId() != extension_id; ind++)
            ;
        if (ind < _arrayExtension.size()) {
            _arrayExtension.get(ind).updatePossessed();
        }
        notifyChanged();
    }

    public void setCarte(CardFragment carte) {
        _carte = carte;
    }

    public void setExtension(ExtensionFragment extension) {
        _extension = extension;
        _extension.setParent(this);
    }

    @Override
    public void onLoadOk(String text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoadError(String text) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        try{
            if (getHelper() != null) getHelper().dispose();
        }catch(Exception e){

        }
        mHelper = null;
    }

}
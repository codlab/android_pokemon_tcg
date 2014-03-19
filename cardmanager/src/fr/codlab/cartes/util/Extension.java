package fr.codlab.cartes.util;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import fr.codlab.cartes.attributes.Ability;
import fr.codlab.cartes.attributes.Attack;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.bdd.SGBDPublic;
import fr.codlab.cartes.listener.IExtensionLoadedListener;

final public class Extension {
    /**
     * The Set's cards
     */
    private ArrayList<Card> _aCartes;
    /**
     * The name of this Set
     */
    private String _name;
    /**
     * The shorten name
     */
    private String _intitule;
    /**
     * The application Context used to get some features
     */
    private Context _p;
    /**
     * The Set id
     */
    private int _id;
    /**
     * Number of cards possessed by the user
     */
    private int _possedees = 0;
    /**
     * Number of cards unit possessed
     */
    private int _progression = 0;
    /**
     * The xml file used to get the Set data
     */
    private int _ressources;
    /**
     * Force the number of cards in the extension
     *
     * @deprecated
     */
    private int _nb;

    private boolean _is_first_edition;
    private boolean _is_reverse;

    private boolean _is_loaded;

    Extension(int id) {
        _id = id;
    }
    /**
     * @param principal
     * @param id
     * @param nb
     * @param intitule
     * @param nom
     * @param mustParseXml
     */
     Extension(Context principal, final IExtensionLoadedListener extensionloaded_listener, int id, int nb, String intitule, String nom, boolean mustParseXml) {
        _name = nom;
        _is_first_edition = false;
        _is_reverse = false;
        _id = id;
        _p = principal;
        _intitule = intitule;
        _aCartes = new ArrayList<Card>();
         _is_loaded = false;
        //_ressources=Principal.extensions[_id-1];
        _ressources = principal.getResources().getIdentifier((_id < 10 ? "e0" : "e") + Integer.toString(id), "xml", "fr.codlab.cartes");
        _nb = nb;

        if (mustParseXml){
            Thread t = new Thread(){
                public void run(){
                    parseXml();
                    if(extensionloaded_listener != null)
                        extensionloaded_listener.onExtensionLoaded(_id);
                    _is_loaded = true;
                }
            };
            t.start();

        }else{
            if(extensionloaded_listener != null){
                extensionloaded_listener.onExtensionLoaded(_id);
                _is_loaded = true;
            }
        }
        updatePossessed();
    }

    public boolean isLoaded(){
        return _is_loaded;
    }
    public boolean equals(Object object){
        return object != null && object instanceof Extension && ((Extension)object)._id == _id;
    }

    /**
     * Parse the Set's XML file
     */
    public void parseXml() {
        XmlPullParser parser = _p.getResources().getXml(_ressources);

        //StringBuilder stringBuilder = new StringBuilder();
        //<extension nom="Base" nb="1">
        Card tampon = null;
        Attack attaque = null;
        PokePower pokepower = null;
        PokeBody pokebody = null;
        Ability ability = null;

        int eventType = 0;
        try {
            eventType = parser.getEventType();
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        }
        String tag = null;
        int _nbCarte = 1;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                tag = parser.getName();
                if ("extension".equals(parser.getName())) {
                    if (parser.getAttributeValue(null, "firstedition") != null)
                        this._is_first_edition = "true".equals(parser.getAttributeValue(null, "firstedition")) ? true : false;
                    if (parser.getAttributeValue(null, "reverse") != null)
                        this._is_reverse = "true".equals(parser.getAttributeValue(null, "reverse")) ? true : false;
                } else if ("retraite".equals(parser.getName())) {
                    if (parser.getAttributeValue(null, "cout") != null)
                        tampon.setRetraite(Integer.parseInt(parser.getAttributeValue(null, "cout")));
                } else if ("pokebody".equals(parser.getName())) {
                    pokebody = new PokeBody();
                    if (parser.getAttributeValue(null, "nom") != null)
                        pokebody.setName(parser.getAttributeValue(null, "nom"));
                } else if ("pokepower".equals(parser.getName())) {
                    pokepower = new PokePower();
                    if (parser.getAttributeValue(null, "nom") != null)
                        pokepower.setName(parser.getAttributeValue(null, "nom"));
                } else if ("capacite".equals(parser.getName()) || "ability".equals(parser.getName())) {
                    ability = new Ability();
                    if (parser.getAttributeValue(null, "nom") != null)
                        ability.setName(parser.getAttributeValue(null, "nom"));
                } else if ("attaque".equals(parser.getName())) {
                    attaque = new Attack();
                    if (parser.getAttributeValue(null, "nom") != null)
                        attaque.setName(parser.getAttributeValue(null, "nom"));
                } else if ("carte".equals(parser.getName())) {
                    tampon = new Card(_id);
                    if (this.isReverse())
                        tampon.setReverse();
                    tampon.setId(_nbCarte);
                    /*if(parser.getAttributeValue(null, "normal") != null &&
							"true".equals(parser.getAttributeValue(null, "normal")))
						tampon.setNormal();
					if(parser.getAttributeValue(null, "holo") != null &&
							"true".equals(parser.getAttributeValue(null, "holo")))
						tampon.setNormal();*/
                    if (parser.getAttributeValue(null, "reverse") != null &&
                            "true".equals(parser.getAttributeValue(null, "reverse")))
                        tampon.setReverse();
                    if (parser.getAttributeValue(null, "spcid") != null)
                        tampon.setNumero(parser.getAttributeValue(null, "spcid"));
                    if (parser.getAttributeValue(null, "visible") != null)
                        tampon.setVisible("true".equals(parser.getAttributeValue(null, "visible")) ? true : false);
                    if (parser.getAttributeValue(null, "nom") != null)
                        tampon.setNom(parser.getAttributeValue(null, "nom"));
                    if (parser.getAttributeValue(null, "types") != null)
                        tampon.setType(parser.getAttributeValue(null, "types"));

                    if (parser.getAttributeValue(null, "pkmnids") != null)
                        try {
                            tampon.setIdPkmn(parser.getAttributeValue(null, "pkmnids"));
                        } catch (Exception e) {
                        }
                    if (parser.getAttributeValue(null, "idspkmn") != null)
                        try {
                            tampon.setIdPkmn(parser.getAttributeValue(null, "idspkmn"));
                        } catch (Exception e) {
                        }
                    if (parser.getAttributeValue(null, "idpkmn") != null)
                        try {
                            tampon.setIdPkmn(Integer.parseInt(parser.getAttributeValue(null, "idpkmn")));
                        } catch (Exception e) {
                        }
                    if (parser.getAttributeValue(null, "pkmnid") != null)
                        //try {
                            tampon.setIdPkmn(Integer.parseInt(parser.getAttributeValue(null, "pkmnid")));
                        //} catch (Exception e) {
                        //    e.printStackTrace();
                        //}
                    if (parser.getAttributeValue(null, "rarete") != null)
                        tampon.setRarete(parser.getAttributeValue(null, "rarete"));
                    if (parser.getAttributeValue(null, "normal") != null &&
                            "true".equals(parser.getAttributeValue(null, "normal")))
                        tampon.setNormal();
                    if (parser.getAttributeValue(null, "holo") != null &&
                            "true".equals(parser.getAttributeValue(null, "holo")))
                        tampon.setHolo();
                    if (parser.getAttributeValue(null, "cid") != null)
                        try {
                            tampon.setCarteId(Integer.parseInt(parser.getAttributeValue(null, "cid")));
                        } catch (Exception e) {
                        }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("pokebody".equals(parser.getName())) {
                    tampon.setPokeBody(pokebody);
                    pokebody = null;
                } else if ("pokepower".equals(parser.getName())) {
                    tampon.setPokePower(pokepower);
                    pokepower = null;
                } else if ("capacite".equals(parser.getName()) || "ability".equals(parser.getName())) {
                    tampon.setAbility(ability);
                    ability = null;
                } else if ("attaque".equals(parser.getName())) {
                    tampon.addAttaque(attaque);
                    attaque = null;
                } else if ("carte".equals(parser.getName())) {
                    _aCartes.add(tampon);
                    //Log.d("Add carte ",tampon.getNom());
                    tampon = new Card(_id);
                }
            } else if (eventType == XmlPullParser.TEXT) {
                if ("retraite".equals(parser.getName()) && parser.getText() != null && parser.getText().length() > 0) {
                    tampon.setRetraite(Integer.parseInt(parser.getText()));
                } else if (tampon != null && "resistance".equals(tag)) {
                    tampon.addResistance(parser.getText());
                } else if (tampon != null && "faiblesse".equals(tag)) {
                    tampon.addFaiblesse(parser.getText());
                } else if (ability != null && ("ability".equals(tag) || "capacity".equals(tag))) {
                    ability.setDescription(parser.getText());
                } else if (pokebody != null && "pokebody".equals(tag)) {
                    pokebody.setDescription(parser.getText());
                } else if (pokepower != null && "pokepower".equals(tag)) {
                    pokepower.setDescription(parser.getText());
                } else if (attaque != null && "degats".equals(tag)) {
                    attaque.setDamage(parser.getText());
                } else if (attaque != null && "description".equals(tag)) {
                    attaque.setDescription(parser.getText());
                } else if ("description".equals(tag)) { //description poke
                    tampon.setDescription(parser.getText());
                } else if (attaque != null && "type".equals(tag)) {
                    attaque.addType(parser.getText());
                } else if ("pv".equals(tag)) {
                    //Log.d("Add carte ",tampon.getNom());
                    try {
                        tampon.setPV(Integer.parseInt(parser.getText()));
                    } catch (Exception e) {
                    }
                }
            }
            try {
                eventType = parser.next();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean isFirstEdition() {
        return _is_first_edition;
    }

    public boolean isReverse() {
        return _is_reverse;
    }

    /**
     * Get the set id
     *
     * @return return the unsigned int
     */
    public int getId() {
        return _id;
    }

    /**
     * Get the Set Name
     *
     * @return
     */
    public String getName() {
        return _name;
    }

    /**
     * Return the shorten name
     * ie AQU, BA, NV, ...
     *
     * @return the String
     */
    public String getShortName() {
        return _intitule;
    }

    /**
     * Return the number of card in the set
     *
     * @return the int number
     */
    public int getCount() {
        return (_aCartes != null && _aCartes.size() > 0) ? _aCartes.size() : _nb;
    }

    /**
     * Return the sum of all card in this Set
     *
     * @return
     */
    public int getPossessed() {
        return _possedees;
    }

    /**
     * Return the progression of the set
     * ie 5/125, 4/34, ...
     *
     * @return
     */
    public int getProgress() {
        return _progression;
    }

    private class AsyncUpdatePossessed extends AsyncTask<Boolean, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            SGBDPublic bdd = new SGBDPublic(_p);
            bdd.open();
            int avant = bdd.getPossessionExtension(_id, Language.US);
            avant += bdd.getPossessionExtension(_id, Language.FR);
            avant += bdd.getPossessionExtension(_id, Language.IT);
            avant += bdd.getPossessionExtension(_id, Language.ES);
            _progression = bdd.getExtensionProgression(_id, Language.US);
            _progression += bdd.getExtensionProgression(_id, Language.FR);
            _progression += bdd.getExtensionProgression(_id, Language.IT);
            _progression += bdd.getExtensionProgression(_id, Language.ES);
            bdd.close();
            bdd = null;
            boolean res = avant != _possedees;
            _possedees = avant;
            //_p.setNbPossedeesExtensionAtIndex(_id, _possedees);
            //_p.setNbCartesExtensionAtIndex(_id, getCount(), _progression);
            return res;

        }

        void executeUpdate() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                this.execute();
            }
        }
    }

    /**
     * Update and return if a modification has been made
     *
     * @return
     */
    public boolean updatePossessed() {
        AsyncUpdatePossessed updater = new AsyncUpdatePossessed();
        updater.executeUpdate();

		/*SGBD bdd = new SGBD(_p);
		bdd.open();
		int avant = bdd.getPossessionExtension(_id, Language.US);
		avant += bdd.getPossessionExtension(_id, Language.FR);
		avant += bdd.getPossessionExtension(_id, Language.IT);
		avant += bdd.getPossessionExtension(_id, Language.ES);
		_progression=bdd.getExtensionProgression(_id, Language.US);
		_progression+=bdd.getExtensionProgression(_id, Language.FR);
		_progression+=bdd.getExtensionProgression(_id, Language.IT);
		_progression+=bdd.getExtensionProgression(_id, Language.ES);
		bdd.close();
		bdd=null;
		boolean res = avant!=_possedees;
		_possedees=avant;
		//_p.setNbPossedeesExtensionAtIndex(_id, _possedees);
		//_p.setNbCartesExtensionAtIndex(_id, getCount(), _progression);
		return res;*/
        return true;
    }

    public Card getCarte(int pos) {
        return (pos < _aCartes.size()) ? _aCartes.get(pos) : null;
    }

    public int getNb() {
        return _nb;
    }

}

	package fr.codlab.cartes.util;

import java.io.Serializable;
import java.util.ArrayList;

import fr.codlab.cartes.attributes.Ability;
import fr.codlab.cartes.attributes.Attack;
import fr.codlab.cartes.attributes.PokeBody;
import fr.codlab.cartes.attributes.PokePower;
import fr.codlab.cartes.bdd.SGBDPublic;

import android.content.Context;
import android.util.Log;

public class Card implements Serializable{
	private static final long serialVersionUID = -5371807270118129999L;
	private int _extension;
	private int _idImage;
	private boolean _visible;
	private String _nom=null;
	private int _id_pkmn=0;
	private String _ids_pkmn = "";
	private String _rarete=null;
	private int _carteId=0;
	private String _specialId=null;
	private int _pv;
	private int _quantite_normal;
	private int _quantite_reverse;
	private int _quantite_holo;

	private int _quantite_normal_fr;
	private int _quantite_reverse_fr;
	private int _quantite_holo_fr;

	private int _quantite_normal_es;
	private int _quantite_reverse_es;
	private int _quantite_holo_es;

	private int _quantite_normal_it;
	private int _quantite_reverse_it;
	private int _quantite_holo_it;

	private int _quantite_normal_de;
	private int _quantite_reverse_de;
	private int _quantite_holo_de;

	private int _id;
	private String [] _type;
	private ArrayList<Attack> _attaques;
	private PokePower _pokepower;
	private PokeBody _pokebody;
	private Ability _ability;
	private int _retraite;
	private String _faiblesses;
	private String _resistances;
	private String _numero;
	private String _description;
	private Rarity _card_state_normal;//NORMAL, HOLO, UNDEFINED >> NOT N, R, H, UR
	private Rarity _card_state_holo;//NORMAL, HOLO, UNDEFINED >> NOT N, R, H, UR
	private boolean _is_reverse;

	public Card(int extension){
		_faiblesses="";
		_resistances="";
		_extension=extension;
		_description="";
		_pv=0;
		_quantite_normal=-1;
		_quantite_reverse=-1;
		_quantite_holo=-1;
		_quantite_normal_de=-1;
		_quantite_reverse_de=-1;
		_quantite_holo_de=-1;
		_quantite_normal_fr=-1;
		_quantite_reverse_fr=-1;
		_quantite_holo_fr=-1;
		_quantite_normal_es=-1;
		_quantite_reverse_es=-1;
		_quantite_holo_es=-1;
		_quantite_normal_it=-1;
		_quantite_reverse_it=-1;
		_quantite_holo_it=-1;
		_visible=true;
		_retraite=0;
		_rarete="aucune";
		_type = new String[1];
		_type[0]="type";
		_numero="";
		_attaques = new ArrayList<Attack>();
		_card_state_normal= Rarity.UNDEFINED;
		_card_state_holo= Rarity.UNDEFINED;
		_is_reverse = false;
		_pokepower = null;
		_pokebody = null;
		_ability = null;
	}

	public void setVisible(boolean b){
		_visible=b;
	}
	public boolean getVisible(){
		return isTrainer() ? false : _visible ;
	}

	public void setRetraite(int i){
		_retraite = i;
	}
	public void addRetraite(int i){
		_retraite+= i;
	}
	public int getRetraite(){
		return _retraite;
	}
	public String getDescription(){
		return _description;
	}
	public void setDescription(String description){
		_description=description;
	}
	public void setPokeBody(PokeBody pokebody){
		_pokebody=pokebody;
	}
	public void setAbility(Ability ability){
		_ability = ability;
	}
	public PokeBody getPokeBody(){
		return _pokebody;
	}
	public void setPokePower(PokePower pokepower){
		_pokepower=pokepower;
	}
	public PokePower getPokePower(){
		return _pokepower;
	}
	public Ability getAbility(){
		return _ability;
	}
	public void addAttaque(Attack at){
		_attaques.add(at);
	}
	public int getNbAttaques(){
		return _attaques.size();
	}
	public Attack getAttaque(int i){
		return _attaques.get(i);
	}
	public void setId(int id){
		_id=id;
	}
	public int getId(){
		return _id;
	}

	public void setPV(int pv){
		_pv=pv;
	}
	public int getPV(){
		return _pv;
	}

	public void setNumero(String numero){
		_numero=numero;
	}
	public String getNumero(){
		return (_numero==null || _numero.length()==0)?"#"+Integer.toString(_carteId):_numero;
	}

	public void setType(String t){
		if(t.split(",").length>0){
			_type = null;
			_type = t.split(",");
			configureTrainer();
		}
	}
	private void configureTrainer(){
		for(int i=0;i<_type.length;i++){
			if("supporter".equals(_type[i]) ||
					"dresseur".equals(_type[i]) ||
					"trainer".equals(_type[i]) ||
					"stadium".equals(_type[i]) ||
					"stade".equals(_type[i]))
				this._visible = false;
		}
	}
	private boolean isTrainer(){
		for(int i=0;_type != null && i<_type.length;i++){
			if("supporter".equals(_type[i]) ||
					"dresseur".equals(_type[i]) ||
					"trainer".equals(_type[i]) ||
					"stadium".equals(_type[i]) ||
					"stade".equals(_type[i]))
				return true;
		}
		return false;
	}
	public void addFaiblesse(String t){
		_faiblesses=((_faiblesses.length()>0)?",":"")+
				t;
	}
	public void addResistance(String t){
		_resistances=((_resistances.length()>0)?",":"")+
				t;
	}

	public String [] getResistances(){
		return _resistances.split(",");
	}
	public String getResistance(){
		return _resistances;
	}
	public String [] getFaiblesses(){
		return _faiblesses.split(",");
	}
	public String getFaiblesse(){
		return _faiblesses;
	}
	public String [] getTypes(){
		return _type;
	}

	public void setSpecialId(String spid){
		_specialId=spid;
	}
	public void setCarteId(int id){
		_carteId=id;
	}
	public int getCarteIdInt(){
		return _carteId;
	}
	public String getCarteId(){
		return _specialId == null || _specialId.length() == 0 ? 
				Integer.toString(_carteId) : _specialId;
	}

	/**
	 * 
	 * @param principal
	 * @param p
	 * @param rarete
	 */
	public void addQuantiteUS(Context principal, int p, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			_quantite_normal+=p;
			if(_quantite_normal<0)
				_quantite_normal=getQuantite(principal, Language.US, Rarity.NORMAL);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, Language.US, _quantite_normal);
			dd.close();
		}
		if(rarete == Rarity.REVERSE){
			_quantite_reverse+=p;
			if(_quantite_reverse<0)
				_quantite_reverse=getQuantite(principal, Language.US, Rarity.REVERSE);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, Language.US, _quantite_reverse);
			dd.close();
		}
		if(rarete == Rarity.HOLO){
			_quantite_holo+=p;
			if(_quantite_holo<0)
				_quantite_holo=getQuantite(principal, Language.US, Rarity.HOLO);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, Language.US, _quantite_holo);
			dd.close();
		}
	}
	public void addQuantiteIT(Context principal, int p, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			_quantite_normal_it+=p;
			if(_quantite_normal_it<0)
				_quantite_normal_it=getQuantite(principal, Language.IT, Rarity.NORMAL);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, Language.IT, _quantite_normal_it);
			dd.close();
		}
		if(rarete == Rarity.REVERSE){
			_quantite_reverse_it+=p;
			if(_quantite_reverse_it<0)
				_quantite_reverse_it=getQuantite(principal, Language.IT, Rarity.REVERSE);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, Language.IT, _quantite_reverse_it);
			dd.close();
		}
		if(rarete == Rarity.HOLO){
			_quantite_holo_it+=p;
			if(_quantite_holo_it<0)
				_quantite_holo_it=getQuantite(principal, Language.IT, Rarity.HOLO);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, Language.IT, _quantite_holo_it);
			dd.close();
		}
	}
	public void addQuantiteFR(Context principal, int p, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			_quantite_normal_fr+=p;
			if(_quantite_normal_fr<0)
				_quantite_normal_fr=getQuantite(principal, Language.FR, Rarity.NORMAL);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, Language.FR, _quantite_normal_fr);
			dd.close();
		}
		if(rarete == Rarity.REVERSE){
			_quantite_reverse_fr+=p;
			if(_quantite_reverse_fr<0)
				_quantite_reverse_fr=getQuantite(principal, Language.FR, Rarity.REVERSE);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, Language.FR, _quantite_reverse_fr);
			dd.close();
		}
		if(rarete == Rarity.HOLO){
			_quantite_holo_fr+=p;
			if(_quantite_holo_fr<0)
				_quantite_holo_fr=getQuantite(principal, Language.FR, Rarity.HOLO);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, Language.FR, _quantite_holo_fr);
			dd.close();
		}
	}
	public void addQuantiteES(Context principal, int p, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			_quantite_normal_es+=p;
			if(_quantite_normal_es<0)
				_quantite_normal_es=getQuantite(principal, Language.ES, Rarity.NORMAL);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, Language.ES, _quantite_normal_es);
			dd.close();
		}
		if(rarete == Rarity.REVERSE){
			_quantite_reverse_es+=p;
			if(_quantite_reverse_es<0)
				_quantite_reverse_es=getQuantite(principal, Language.ES, Rarity.REVERSE);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, Language.ES, _quantite_reverse_es);
			dd.close();
		}
		if(rarete == Rarity.HOLO){
			_quantite_holo_es+=p;
			if(_quantite_holo_es<0)
				_quantite_holo_es=getQuantite(principal, Language.ES, Rarity.HOLO);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, Language.ES, _quantite_holo_es);
			dd.close();
		}
	}
	public void addQuantiteDE(Context principal, int p, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			_quantite_normal_de+=p;
			if(_quantite_normal_de<0)
				_quantite_normal_de=getQuantite(principal, Language.DE, Rarity.NORMAL);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionNormal(_extension, _carteId, Language.DE, _quantite_normal_de);
			dd.close();
		}
		if(rarete == Rarity.REVERSE){
			_quantite_reverse_de+=p;
			if(_quantite_reverse_de<0)
				_quantite_reverse_de=getQuantite(principal, Language.DE, Rarity.REVERSE);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionReverse(_extension, _carteId, Language.DE, _quantite_reverse_de);
			dd.close();
		}
		if(rarete == Rarity.HOLO){
			_quantite_holo_de+=p;
			if(_quantite_holo_de<0)
				_quantite_holo_de=getQuantite(principal, Language.DE, Rarity.HOLO);
			SGBDPublic dd=new SGBDPublic(principal);
			dd.open();
			dd.updatePossessionCarteExtensionHolo(_extension, _carteId, Language.DE, _quantite_holo_de);
			dd.close();
		}
	}
	public void addQuantite(Context principal, int p, Rarity rarete, Language lang){
		switch(lang){
		case FR:
			addQuantiteFR(principal, p, rarete);
			break;
		case ES:
			addQuantiteES(principal, p, rarete);
			break;
		case IT:
			addQuantiteIT(principal, p, rarete);
			break;
		case DE:
			addQuantiteDE(principal, p, rarete);
			break;
		default:
			addQuantiteUS(principal, p, rarete);
		}
	}
	public int getQuantiteUS(Context principal, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			if(true || _quantite_normal==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_normal = dd.getPossessionCarteExtensionNormal(_extension, Language.US, _carteId);
				dd.close();
			}
			return _quantite_normal;
		}
		if(rarete == Rarity.REVERSE){
			if(true || _quantite_reverse==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_reverse = dd.getPossessionCarteExtensionReverse(_extension, Language.US, _carteId);
				dd.close();
			}
			return _quantite_reverse;
		}
		if(rarete == Rarity.HOLO){
			if(true || _quantite_holo==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_holo = dd.getPossessionCarteExtensionHolo(_extension, Language.US, _carteId);
				dd.close();
			}
			return _quantite_holo;
		}
		return _quantite_reverse;
	}
	public int getQuantiteFR(Context principal, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			if(true || _quantite_normal_fr==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_normal_fr = dd.getPossessionCarteExtensionNormal(_extension, Language.FR, _carteId);
				dd.close();
			}
			return _quantite_normal_fr;
		}
		if(rarete == Rarity.REVERSE){
			if(true || _quantite_reverse_fr==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_reverse_fr = dd.getPossessionCarteExtensionReverse(_extension, Language.FR, _carteId);
				dd.close();
			}
			return _quantite_reverse_fr;
		}
		if(rarete == Rarity.HOLO){
			if(true || _quantite_holo_fr==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_holo_fr = dd.getPossessionCarteExtensionHolo(_extension, Language.FR, _carteId);
				dd.close();
			}
			return _quantite_holo_fr;
		}
		return _quantite_reverse_fr;
	}
	public int getQuantiteES(Context principal, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			if(true || _quantite_normal_es==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_normal_es = dd.getPossessionCarteExtensionNormal(_extension, Language.ES, _carteId);
				dd.close();
			}
			return _quantite_normal_es;
		}
		if(rarete == Rarity.REVERSE){
			if(true || _quantite_reverse_es==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_reverse_es = dd.getPossessionCarteExtensionReverse(_extension, Language.ES, _carteId);
				dd.close();
			}
			return _quantite_reverse_es;
		}
		if(rarete == Rarity.HOLO){
			if(true || _quantite_holo_es==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_holo_es = dd.getPossessionCarteExtensionHolo(_extension, Language.ES, _carteId);
				dd.close();
			}
			return _quantite_holo_es;
		}
		return _quantite_reverse_es;
	}
	public int getQuantiteIT(Context principal, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			if(true || _quantite_normal_it==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_normal_it = dd.getPossessionCarteExtensionNormal(_extension, Language.IT, _carteId);
				dd.close();
			}
			return _quantite_normal_it;
		}
		if(rarete == Rarity.REVERSE){
			if(true || _quantite_reverse_it==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_reverse_it = dd.getPossessionCarteExtensionReverse(_extension, Language.IT, _carteId);
				dd.close();
			}
			return _quantite_reverse_it;
		}
		if(rarete == Rarity.HOLO){
			if(true || _quantite_holo_it==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_holo_it = dd.getPossessionCarteExtensionHolo(_extension, Language.IT, _carteId);
				dd.close();
			}
			return _quantite_holo_it;
		}
		return _quantite_reverse;
	}
	public int getQuantiteDE(Context principal, Rarity rarete){
		if(rarete == Rarity.NORMAL){
			if(true || _quantite_normal_de==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_normal_de = dd.getPossessionCarteExtensionNormal(_extension, Language.DE, _carteId);
				dd.close();
			}
			return _quantite_normal_de;
		}
		if(rarete == Rarity.REVERSE){
			if(true || _quantite_reverse_de==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_reverse_de = dd.getPossessionCarteExtensionReverse(_extension, Language.DE, _carteId);
				dd.close();
			}
			return _quantite_reverse_de;
		}
		if(rarete == Rarity.HOLO){
			if(true || _quantite_holo_de==-1){
				SGBDPublic dd=new SGBDPublic(principal);
				dd.open();
				_quantite_holo_de = dd.getPossessionCarteExtensionHolo(_extension, Language.DE, _carteId);
				dd.close();
			}
			return _quantite_holo_de;
		}
		return _quantite_reverse;
	}
	public int getQuantite(Context principal, Language lang, Rarity rarete){
		switch(lang){
		case FR:
			return getQuantiteFR(principal, rarete);
		case ES:
			return getQuantiteES(principal, rarete);
		case IT:
			return getQuantiteIT(principal, rarete);
		case DE:
			return getQuantiteDE(principal, rarete);
		default:
			return getQuantiteUS(principal, rarete);
		}
	}
	public void setRarete(String rarete){
		_rarete=rarete;
		if("common".equals(_rarete) || "uncommon".equals(_rarete) || "rare".equals(_rarete)){
			this.setNormal();
		}else if("holo".equals(_rarete) || "ultra".equals(_rarete)){
			this.setHolo();
		}else{
			this._card_state_normal = Rarity.UNDEFINED;
			this._card_state_holo = Rarity.UNDEFINED;
		}
	}
	public void setIdPkmn(String ids_pkmn){
		Log.d("SET PKMN","IDS"+ids_pkmn);
		if(ids_pkmn != null)
			_ids_pkmn = ids_pkmn;
	}
	public void setIdPkmn(int id_pkmn){
		_id_pkmn=id_pkmn;
	}
	public int getIdPkmn(int n){
		if(_ids_pkmn == null)
			return 0;
		String [] tmp = _ids_pkmn.replace(" ", "").split(",");
		if(tmp.length > 1 && tmp.length > n && Integer.getInteger(tmp[n]) != null)
			return Integer.getInteger(tmp[n]);
		return 0;
	}
	public int getIdPkmn(){
		return _id_pkmn;
	}
	public void setNom(String nom){
		_nom=nom;
	}

	public String getDrawableString(){
		return "img"+Integer.toString(_extension)+"_"+Integer.toString(_carteId);
	}

	public String getDrawableRarete(){
		return "rarete_"+_rarete;
	}

	public int getDrawableImage(){
		return _idImage;
	}

	public String getNom(){
		if(_nom == null && _id_pkmn > 0 && Pokemon.valid(_id_pkmn))
			return Pokemon.getName(_id_pkmn);
		else if(_nom != null  && _id_pkmn > 0 && Pokemon.valid(_id_pkmn))
			return _nom.replace("%s",Pokemon.getName(_id_pkmn)).replace("%1", Pokemon.getName(_id_pkmn));
		else if(_nom != null  && _ids_pkmn!= null && _ids_pkmn.indexOf(",") > 0){
			Log.d("get","nom");
			String [] tmp = _ids_pkmn.split(",");
			int ind = 0;
			String res=_nom;
			for(int i=0;i<tmp.length;i++){
				Log.d("get","nom" + tmp[i]);
				ind = Integer.parseInt(tmp[i]);
				if(Pokemon.valid(ind)){
					Log.d("replace","nom"+"%"+i+" "+ tmp[i]);
					res = res.replace("%"+(i+1), Pokemon.getName(ind));
				}
			}
			return res;
		}else if(_nom != null)
			return _nom;
		return "";
	}

	public String getInfos(){
		return getNumero();
	}

	void setNormal(){
		_card_state_normal = Rarity.NORMAL;
	}
	private boolean isAllStateUndefined(){
		return (_card_state_holo == Rarity.UNDEFINED && _card_state_normal == Rarity.UNDEFINED);
	}
	public boolean getIsNormal(){
		//TODO implement reverse="true"
		return _card_state_normal == Rarity.NORMAL || isAllStateUndefined();
	}

	void setHolo(){
		_card_state_holo = Rarity.HOLO;
	}

	public boolean getIsHolo(){
		//TODO implement rarete="holo" rarete="ultra"
		return _card_state_holo == Rarity.HOLO || isAllStateUndefined();
	}

	public void setReverse(){
		_is_reverse = true;
	}
	public boolean getIsReverse(){
		//TODO implement reverse="true"
		return _is_reverse;
	}
}

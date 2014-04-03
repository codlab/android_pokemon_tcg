package fr.codlab.cartes.attributes;

import java.io.Serializable;

public class Pouvoir implements Serializable{
	private static final long serialVersionUID = -6544978278047792655L;
	String _nom;
	String _description;
	
	public Pouvoir(){
		_nom="";
		_description="";
	}
	
	public void setName(String nom){
		_nom=nom;
	}
	
	public void setDescription(String description){
		_description=description;
	}

	public String getName(){
		return _nom;
	}
	
	public String getDescription(){
		return _description;
	}
}

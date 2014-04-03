package fr.codlab.cartes.attributes;

import java.io.Serializable;


public class Attack implements Serializable{
	/**
	 * Auto Generated
	 */
	private static final long serialVersionUID = -5023627505698314231L;
	/**
	 * Attack name
	 */
	private String _name;
	/**
	 * attack types [type in french](,[type in french])?
	 */
	private String _types;
	/**
	 * The information
	 */
	private String _description;
	/**
	 * Damage from this attack. +30, x30, ...
	 */
	private String _degats;
	
	public Attack(){
		_name="";
		_types="";
		_description="";
		_degats="";
		
	}
	
	public void setName(String nom){
		_name=nom;
	}
	public String getName(){
		return _name;
	}
	
	public void setTypes(String types){
		_types=types;
	}
	public void addType(String type){
		_types=_types+((_types.length()>0)?",":"")+type;
	}
	public String [] getTypes(){
		return _types.split(",");
	}
	
	public void setDescription(String description){
		_description=description;
	}
	public String getDescription(){
		return _description;
	}
	
	public void setDamage(String degats){
		_degats=degats;
	}
	public String getDamage(){
		return _degats;
	}
}

package fr.codlab.cartes.util;

public class Code {
	private int _status;
	public final static int UNKNOW=0;
	public final static int USED=1;
	public final static int VALID=2;
	public final static int WRONG=3;
	private String _code;
	private long _id;

	public Code(){
		_id = 0;
		_status = 0;
		_code = "";
	}

	public Code(String code){
		this();
		_code = code.substring(0, 3)+"-"+code.substring(3, 7)+"-"+code.substring(7, 10)+"-"+code.substring(10);
		_status = UNKNOW;
	}
	public Code(long id, int status, String code){
		this(code);
		_id = id;
		setStatus(status);
	}

	public long getId(){
		return _id;
	}
	
	public int getStatus(){
		return _status;
	}

	public String getCode(){
		return _code;
	}

	private void setStatus(int status){
		switch(status){
		case UNKNOW:
			_status = UNKNOW;
			break;
		case VALID:
			_status = VALID;
			break;
		case USED:
			_status = USED;
			break;
		default:
			_status = WRONG;
		}
	}
}

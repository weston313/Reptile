package com.wozipa.reptile.id.encrypt;

public class NormalEncrypt extends Encrypt{
	
	private String id;
	
	public NormalEncrypt(){}
	
	public NormalEncrypt(String id)
	{
		this.id=id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String encrypt() {
		// TODO Auto-generated method stub
		return this.id;
	}

}

package com.wozipa.reptile.id.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class EncryptUtil {
	
	private static final String PREIFX="rep_";
	
	public static final int normal=0;		//无加密
	public static final int invert=1;		//倒转加密
	public static final int prefix=2;		//前缀加密
	public static final int random=3;		//随机 加密
	public static final int md5=4;			//MD5码加密
	public static final int sha=5;			//SHA加密
	
	public static String encrypt(int type,String id)
	{
		switch (type) {
		case normal:
			return id;
		case md5:
			return md5Encrypt(id);
		case sha:
			return shaEncrypt(id);
		case random:
			return randomEncrypt(id);
		case invert:
			return invertEncrypt(id);
		case prefix:
			return prefixEncrypt(id);
		default:
			return id;
		}
	}
	
	public static String prefixEncrypt(String id)
	{
		return PREIFX+id;
	}
	
	public static String md5Encrypt(String id)
	{
		String result=null;
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			byte[] resultBytes=md5.digest(id.getBytes());
			result=byteArrayToHex(resultBytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String shaEncrypt(String id)
	{
		String result=null;
		try {
			MessageDigest md5=MessageDigest.getInstance("SHA");
			byte[] resultBytes=md5.digest(id.getBytes());
			result=byteArrayToHex(resultBytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String randomEncrypt(String id)
	{
		return UUID.randomUUID().toString();
	}
	
	public static String invertEncrypt(String id)
	{
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<id.length();i++)
		{
			sb.append(id.indexOf(i));
		}
		return sb.toString();
	}
	
	public static String byteArrayToHex(byte[] byteArray) {

	      // 首先初始化一个字符数组，用来存放每个16进制字符
	      char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
	      // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
	      char[] resultCharArray =new char[byteArray.length * 2];
	      // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
	      int index = 0;
	      for (byte b : byteArray) {
	         resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
	         resultCharArray[index++] = hexDigits[b& 0xf];
	      }
	      // 字符数组组合成字符串返回
	      return new String(resultCharArray);

	}

}

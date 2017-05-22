package com.wozipa.reptile.id.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.print.attribute.standard.Media;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5Encrypt extends Encrypt{
	
	private static final Log LOG=LogFactory.getLog(MD5Encrypt.class);
	
	private String id;
	
	public MD5Encrypt() {
		// TODO Auto-generated constructor stub
	}
	
	public MD5Encrypt(String id) {
		// TODO Auto-generated constructor stub
		this.id=id;
	}
	
	public void setId(String id)
	{
		this.id=id;
	}

	@Override
	public String encrypt() {
		// TODO Auto-generated method stub
		String result=null;
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			byte[] resultBytes=md5.digest(this.id.getBytes());
			result=byteArrayToHex(resultBytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
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

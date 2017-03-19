package com.wozipa.reptile.id.encrypt;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptUtilTest {

	@Test
	public void test() {
		System.out.println(EncryptUtil.invertEncrypt("543603412569"));
		System.out.println(EncryptUtil.randomEncrypt("543603412569"));
		System.out.println(EncryptUtil.md5Encrypt("543603412569"));
		System.out.println(EncryptUtil.shaEncrypt("543603412569"));
	}
	
	@Test
	public void testInvertId()
	{
		System.out.println(EncryptUtil.invertEncrypt("543603412569"));
	}

}

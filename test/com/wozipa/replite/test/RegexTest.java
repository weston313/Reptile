package com.wozipa.replite.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	
	public static void main(String[] args)
	{
		String string="<script src=\"//desc.alicdn.com/i4/540/581/542589151236/TB1Ke9XPFXXXXbcXFXX8qtpFXlX.desc%7Cvar%5Edesc%3Bsign%5E93353466a9fe9feaa26fd5ec4aa597c1%3Blang%5Egbk%3Bt%5E1488950910\"></script>";
		String reg="^(<script src=\"//desc.alicdn.com/)(.*)$";
		Pattern pattern=Pattern.compile(reg);
		Matcher matcher=pattern.matcher(string);
		System.out.println(matcher.matches());
	}

}

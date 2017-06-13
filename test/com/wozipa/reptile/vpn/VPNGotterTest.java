package com.wozipa.reptile.vpn;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wozipa.reptile.app.config.AppConfiguration;

public class VPNGotterTest {

	@Test
	public void test() {
		VPNGotter gotter=new VPNGotter(AppConfiguration.getConfiguration());
		gotter.getProxySource();
	}

}

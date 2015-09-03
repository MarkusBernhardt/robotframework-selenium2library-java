package com.github.markusbernhardt.selenium2library.keywords;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserManagementTest {

	@Test
	public void testOpenBrowser() {
		BrowserManagement bm = mock(BrowserManagement.class);
		FirefoxDriver wdMock = mock(FirefoxDriver.class);
		String desired = "{\"platform\":\"WINDOWS\",\"browserName\":\"firefox\",\"version\":\"\"}";
		String[] arguments = {"unitTestUrl", "desiredCapabilities="+desired, "browserOptions={}"};
		try {
			when(bm.createLocalWebDriver(eq("firefox"), any(DesiredCapabilities.class))).thenReturn(wdMock);
			System.out.println(bm.openBrowser(arguments));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

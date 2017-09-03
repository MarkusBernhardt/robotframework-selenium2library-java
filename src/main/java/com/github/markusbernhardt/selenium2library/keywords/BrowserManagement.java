package com.github.markusbernhardt.selenium2library.keywords;

import io.appium.java_client.ios.IOSDriver;
import io.selendroid.client.SelendroidDriver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryFatalException;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.ElementFinder;
import com.github.markusbernhardt.selenium2library.locators.WindowManager;
import com.github.markusbernhardt.selenium2library.utils.Robotframework;
import com.github.markusbernhardt.selenium2library.utils.WebDriverCache;
import com.github.markusbernhardt.selenium2library.utils.WebDriverCache.SessionIdAliasWebDriverTuple;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.opera.core.systems.OperaDriver;

@SuppressWarnings("deprecation")
@RobotKeywords
public class BrowserManagement extends RunOnFailureKeywordsAdapter {

	public String remoteWebDriverProxyHost = "";
	public String remoteWebDriverProxyPort = "";
	public String remoteWebDriverProxyUser = "";
	public String remoteWebDriverProxyPassword = "";
	public String remoteWebDriverProxyDomain = "";
	public String remoteWebDriverProxyWorkstation = "";

	/**
	 * Cache for all open browsers.
	 */
	protected WebDriverCache webDriverCache = new WebDriverCache();

	/**
	 * Timeout in milliseconds
	 */
	protected double timeout = 5.0;

	/**
	 * Implicit wait in milliseconds
	 */
	protected double implicitWait = 0;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

	// ##############################
	// Getter / Setter
	// ##############################

	public WebDriverCache getWebDriverCache() {
		return webDriverCache;
	}

	public WebDriver getCurrentWebDriver() {
		return webDriverCache.getCurrent();
	}

	public double getTimeout() {
		return timeout;
	}

	// ##############################
	// Keywords
	// ##############################

	@RobotKeywordOverload
	public void addLocationStrategy(String strategyName, String functionDefinition) {
		addLocationStrategy(strategyName, functionDefinition, null);
	}

	/**
	 * Registers a JavaScript function as locator with the specified strategy
	 * name.<br>
	 * <br>
	 * The registered function has to return a WebElement, a List of WebElements or
	 * null. Optionally a delimiter can be given to split the value of the locator
	 * in multiple arguments when executing the JavaScript function. <br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>Add Location Strategy</td>
	 * <td>byId</td>
	 * <td>return window.document.getElementById(arguments[0]);</td>
	 * </tr>
	 * <tr>
	 * <td>Input Text</td>
	 * <td>byId=firstName</td>
	 * <td>Max</td>
	 * </tr>
	 * </table>
	 * <br>
	 * Example with delimiter:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>Add Location Strategy</td>
	 * <td>byClassname</td>
	 * <td>return
	 * window.document.getElementsByClassName(arguments[0])[arguments[1]];</td>
	 * <td>,</td>
	 * </tr>
	 * <tr>
	 * <td>Input Text</td>
	 * <td>byClassname=input,3</td>
	 * <td>Max</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 * @param strategyName
	 *            Name of the location strategy.
	 * @param functionDefinition
	 *            The JavaScript function to register.
	 * @param delimiter
	 *            Default=NONE. The delimiter to split the given locator value
	 */
	@RobotKeyword
	@ArgumentNames({ "strategyName", "functionDefinition", "delimiter=NONE" })
	public void addLocationStrategy(String strategyName, String functionDefinition, String delimiter) {
		ElementFinder.addLocationStrategy(strategyName, functionDefinition, delimiter);
	}

	/**
	 * Closes the current browser instance.<br>
	 * 
	 * @see BrowserManagement#closeAllBrowsers
	 * @see BrowserManagement#openBrowser
	 * @see BrowserManagement#switchBrowser
	 */
	@RobotKeyword
	public void closeBrowser() {
		if (webDriverCache.getCurrentSessionId() != null) {
			logging.debug(String.format("Closing browser with session id %s", webDriverCache.getCurrentSessionId()));
			webDriverCache.close();
		}
	}

	@RobotKeywordOverload
	public String openBrowser(String url) throws Throwable {
		return openBrowser(url, "firefox");
	}

	@RobotKeywordOverload
	public String openBrowser(String url, String browserName) throws Throwable {
		return openBrowser(url, browserName, null);
	}

	@RobotKeywordOverload
	public String openBrowser(String url, String browserName, String alias) throws Throwable {
		return openBrowser(url, browserName, alias, null);
	}

	@RobotKeywordOverload
	public String openBrowser(String url, String browserName, String alias, String remoteUrl) throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl, null);
	}

	public String openBrowser(String url, String browserName, String alias, String remoteUrl,
			String desiredCapabilities) throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl, desiredCapabilities, null);
	}

	/**
	 * Opens a new browser instance to given URL.<br>
	 * <br>
	 * Possible values for browser are as follows:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>firefox</td>
	 * <td>FireFox</td>
	 * </tr>
	 * <tr>
	 * <td>ff</td>
	 * <td>FireFox</td>
	 * </tr>
	 * <tr>
	 * <td>internetexplorer</td>
	 * <td>Internet Explorer</td>
	 * </tr>
	 * <tr>
	 * <td>ie</td>
	 * <td>Internet Explorer</td>
	 * </tr>
	 * <tr>
	 * <td>googlechrome</td>
	 * <td>Google Chrome</td>
	 * </tr>
	 * <tr>
	 * <td>gc</td>
	 * <td>Google Chrome</td>
	 * </tr>
	 * <tr>
	 * <td>chrome</td>
	 * <td>Google Chrome</td>
	 * </tr>
	 * <tr>
	 * <td>opera</td>
	 * <td>Opera</td>
	 * </tr>
	 * <tr>
	 * <td>phantomjs</td>
	 * <td>PhantomJS</td>
	 * </tr>
	 * <tr>
	 * <td>htmlunitwithjs</td>
	 * <td>HTMLUnit with Javascipt support</td>
	 * </tr>
	 * <tr>
	 * <td>safari</td>
	 * <td>Safari</td>
	 * </tr>
	 * <tr>
	 * <td>ipad</td>
	 * <td>iPad</td>
	 * </tr>
	 * <tr>
	 * <td>iphone</td>
	 * <td>iPhone</td>
	 * </tr>
	 * <tr>
	 * <td>android</td>
	 * <td>Android</td>
	 * </tr>
	 * <tr>
	 * <td>htmlunit</td>
	 * <td>HTMLUnit</td>
	 * </tr>
	 * </table>
	 * <br>
	 * Returns the index of the newly created browser instance which can be used
	 * later to switch back to it. Index starts from 1 and is reset back to it when
	 * the `Close All Browsers` keyword is used.<br>
	 * <br>
	 * <b>DesiredCapabilities</b><br>
	 * The DesiredCapabilities can be specified in a simple key:value format or as a
	 * JSON object. With the JSON format more complex parameters, like the proxy,
	 * can be configured.<br>
	 * <br>
	 * Example of desiredCapabilities as simple string:<br>
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>platform:Windows 8,browserName:firefox,version:25</td>
	 * </tr>
	 * </table>
	 * <br>
	 * Example of desiredCapabilities as JSON object:<br>
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>{<br>
	 * &emsp;"platform":"Windows 8",<br>
	 * &emsp;"browserName":"firefox",<br>
	 * &emsp;"version":"25",<br>
	 * &emsp;"proxy":<br>
	 * &emsp;{<br>
	 * &emsp;&emsp;"proxyType":"manual",<br>
	 * &emsp;&emsp;"httpProxy":"localhost:8118"<br>
	 * &emsp;}<br>
	 * }<br>
	 * </td>
	 * </tr>
	 * </table>
	 * <br>
	 * <b>BrowserOptions</b><br>
	 * The BrowserOptions can be specified as JSON object to set more complex
	 * browser specific parameters. At the moment only the following browsers with
	 * the listed options are implemented.<br>
	 * <br>
	 * Firefox:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>{<br>
	 * &emsp;"preferences":<br>
	 * &emsp;{<br>
	 * &emsp;&emsp;"extensions.firebug.currentVersion":"1.8.1",<br>
	 * &emsp;&emsp;"extensions.firebug.addonBarOpened":true,<br>
	 * &emsp;&emsp;"extensions.firebug.enableSites":true<br>
	 * &emsp;}<br>
	 * &emsp;"extensions":<br>
	 * &emsp;[<br>
	 * &emsp;&emsp;"firebug-1.8.1.xpi",<br>
	 * &emsp;&emsp;"modify_headers-0.7.1.1-fx.xpi"<br>
	 * &emsp;]<br>
	 * }<br>
	 * </td>
	 * </tr>
	 * </table>
	 * <br>
	 * <b>Internet Explorer</b><br>
	 * Note, that you will encounter strange behavior, if you open multiple Internet
	 * Explorer browser instances. That is also why `Switch Browser` only works with
	 * one IE browser at most. For more information see: <a href=
	 * "http://selenium-grid.seleniumhq.org/faq.html#i_get_some_strange_errors_when_i_run_multiple_internet_explorer_instances_on_the_same_machine"
	 * >Strange errors with multiple IE instances</a><br>
	 * 
	 * @param url
	 *            The URL to open in the newly created browser instance.
	 * @param browserName
	 *            Default=firefox. Optional name of the browser to start.
	 * @param alias
	 *            Default=NONE. Optional alias for the newly created browser
	 *            instance. The alias can be used later for switching between
	 *            browsers instances, just as returned index.
	 * @param remoteUrl
	 *            Default=False. Optional remote grid URL. When specified no local
	 *            WebDriver instance is created, but a network connection to a
	 *            Selenium 2 WebDriver Grid Hub at the given URL is opened.
	 * @param desiredCapabilities
	 *            Default=NONE. Optional desired capabilities of the newly created
	 *            remote browser instances can be specified in a simple
	 *            key1:val1,key2:val2 format or as a JSON object (see examples
	 *            above). Used to communicate to the remote grid, which kind of
	 *            browser, etc. should be used. For more information see: <a href=
	 *            "http://code.google.com/p/selenium/wiki/DesiredCapabilities" >
	 *            DesiredCapabilities</a>
	 * @param browserOptions
	 *            Default=NONE. Extended browser options as JSON structure.
	 * @return The index of the newly created browser instance.
	 * @throws Throwable
	 *             - if anything goes wrong
	 * 
	 * @see BrowserManagement#closeAllBrowsers
	 * @see BrowserManagement#closeBrowser
	 * @see BrowserManagement#switchBrowser
	 */
	@RobotKeyword
	@ArgumentNames({ "url", "browserName=firefox", "alias=NONE", "remoteUrl=False", "desiredCapabilities=NONE",
			"browserOptions=NONE" })
	public String openBrowser(String url, String browserName, String alias, String remoteUrl,
			String desiredCapabilities, String browserOptions) throws Throwable {
		try {
			logging.info("browserName: " + browserName);
			if (remoteUrl != null) {
				logging.info(String.format("Opening browser '%s' to base url '%s' through remote server at '%s'",
						browserName, url, remoteUrl));
			} else {
				logging.info(String.format("Opening browser '%s' to base url '%s'", browserName, url));
			}

			WebDriver webDriver = createWebDriver(browserName, desiredCapabilities, remoteUrl, browserOptions);
			webDriver.get(url);
			String sessionId = webDriverCache.register(webDriver, alias);
			logging.debug(String.format("Opened browser with session id %s", sessionId));
			return sessionId;
		} catch (Throwable t) {
			if (remoteUrl != null) {
				logging.warn(String.format("Opening browser '%s' to base url '%s' through remote server at '%s' failed",
						browserName, url, remoteUrl));
			} else {
				logging.warn(String.format("Opening browser '%s' to base url '%s' failed", browserName, url));
			}
			throw new Selenium2LibraryFatalException(t);
		}
	}

	/**
	 * "Switches between active browser instances using an <b>index</b> or an
	 * <b>alias</b>.<br>
	 * <br>
	 * The index is returned from `Open Browser` and an alias can be given to
	 * it.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>Open Browser</td>
	 * <td>http://google.com</td>
	 * <td>ff</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Location Should Be</td>
	 * <td>http://google.com</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Open Browser</td>
	 * <td>http://yahoo.com</td>
	 * <td>ie</td>
	 * <td>2nd conn</td>
	 * </tr>
	 * <tr>
	 * <td>Location Should Be</td>
	 * <td>http://yahoo.com</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Switch Browser</td>
	 * <td>1</td>
	 * <td># index</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Page Should Contain</td>
	 * <td>I'm feeling lucky</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Switch Browser</td>
	 * <td>2nd conn</td>
	 * <td># alias</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Page Should Contain</td>
	 * <td>More Yahoo!</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Close All Browsers</td>
	 * <td></td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * <br>
	 * The above example expects that there was no other open browsers when opening
	 * the first one because it used index '1' when switching to it later. If you
	 * aren't sure about that you can store the index into a variable as below.
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>${id} =</td>
	 * <td>Open Browser</td>
	 * <td>http://google.com</td>
	 * </tr>
	 * <tr>
	 * <td># Do something ...</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Switch Browser</td>
	 * <td>${id}</td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * 
	 * @param indexOrAlias
	 *            the index or alias of the browser instance to switch to
	 * 
	 * @see BrowserManagement#closeAllBrowsers
	 * @see BrowserManagement#closeBrowser
	 * @see BrowserManagement#openBrowser
	 */
	@RobotKeyword
	@ArgumentNames({ "indexOrAlias" })
	public void switchBrowser(String indexOrAlias) {
		try {
			webDriverCache.switchBrowser(indexOrAlias);
			logging.debug(String.format("Switched to browser with Selenium session id %s",
					webDriverCache.getCurrentSessionId()));
		} catch (Throwable t) {
			throw new Selenium2LibraryFatalException(
					String.format("No browser with index or alias '%s' found.", indexOrAlias));
		}
	}

	/**
	 * Closes all open browser instances and resets the browser cache.<br>
	 * <br>
	 * After this keyword new indexes returned from `Open Browser` keyword are reset
	 * to 1. This keyword should be used in test or suite teardown to make sure all
	 * browsers are closed.<br>
	 * 
	 * @see BrowserManagement#closeBrowser
	 * @see BrowserManagement#openBrowser
	 * @see BrowserManagement#switchBrowser
	 */
	@RobotKeyword
	public void closeAllBrowsers() {
		logging.debug("Closing all browsers");
		webDriverCache.closeAll();
	}

	/**
	 * Closes the currently open pop-up window.
	 */
	@RobotKeyword
	public void closeWindow() {
		webDriverCache.getCurrent().close();
	}

	/**
	 * Returns the id attributes of all windows known to the current browser
	 * instance.<br>
	 * 
	 * @return List of window id attributes
	 * 
	 * @see Logging#logWindowIdentifiers
	 */
	@RobotKeyword
	public List<String> getWindowIdentifiers() {
		return toList(WindowManager.getWindowIds(webDriverCache.getCurrent()), "Window Id");
	}

	/**
	 * Returns the names of all windows known to the current browser instance. <br>
	 * 
	 * @return List of window names
	 * 
	 * @see Logging#logWindowNames
	 */
	@RobotKeyword
	public List<String> getWindowNames() {
		List<String> windowNames = WindowManager.getWindowNames(webDriverCache.getCurrent());
		if (windowNames.size() != 0 && windowNames.get(0).equals("undefined")) {
			windowNames.set(0, "selenium_main_app_window");
		}
		return toList(windowNames, "Window Name");
	}

	/**
	 * Returns the titles of all windows known to the current browser instance. <br>
	 * 
	 * @return List of window titles
	 * 
	 * @see Logging#logWindowTitles
	 */
	@RobotKeyword
	public List<String> getWindowTitles() {
		return toList(WindowManager.getWindowTitles(webDriverCache.getCurrent()), "Window Title");
	}

	/**
	 * Maximizes current browser window.<br>
	 */
	@RobotKeyword
	public void maximizeBrowserWindow() {
		webDriverCache.getCurrent().manage().window().maximize();
	}

	/**
	 * Returns current window size as <b>width</b> then <b>height</b>.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>${width}</td>
	 * <td>${height}=</td>
	 * <td>Get Window Size</td>
	 * </tr>
	 * </table>
	 * 
	 * @return The window <b>width</b> and <b>height</b> in px.
	 */
	@RobotKeyword
	public Object[] getWindowSize() {
		Dimension size = getCurrentWebDriver().manage().window().getSize();
		return new Object[] { Integer.toString(size.width), Integer.toString(size.height) };
	}

	/**
	 * Sets the <b>width</b> and <b>height</b> of the current window to the
	 * specified values.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>Set Window Size</td>
	 * <td>800</td>
	 * <td>600</td>
	 * </tr>
	 * <tr>
	 * <td>${width}</td>
	 * <td>${height}=</td>
	 * <td>Get Window Size</td>
	 * </tr>
	 * <tr>
	 * <td>Should Be Equal</td>
	 * <td>${width}</td>
	 * <td>800</td>
	 * </tr>
	 * <tr>
	 * <td>Should Be Equal</td>
	 * <td>${height}</td>
	 * <td>600</td>
	 * </tr>
	 * </table>
	 * 
	 * @param width
	 *            The window width to set in px.
	 * @param height
	 *            The window height to set in px.
	 */
	@RobotKeyword
	@ArgumentNames({ "width", "height" })
	public void setWindowSize(String width, String height) {
		getCurrentWebDriver().manage().window()
				.setSize(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
	}

	/**
	 * Selects the frame identified by <b>locator</b> as current frame.<br>
	 * <br>
	 * Key attributes for frames are <b>id</b> and <b>name</b>. See `Introduction`
	 * for details about locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the frame
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void selectFrame(String locator) {
		logging.info(String.format("Selecting frame '%s'.", locator));
		List<WebElement> elements = element.elementFind(locator, true, true);
		webDriverCache.getCurrent().switchTo().frame(elements.get(0));
	}

	@RobotKeywordOverload
	public void selectWindow() {
		selectWindow(null);
	}

	/**
	 * Selects the window identified by <b>locator</b> as the context of
	 * actions.<br>
	 * <br>
	 * If the window is found, all subsequent commands use that window, until this
	 * keyword is used again. If the window is not found, this keyword fails.<br>
	 * <br>
	 * By default, when a locator value is provided, it is matched against the title
	 * of the window and the javascript name of the window. If multiple windows with
	 * same identifier are found, the first one is selected.<br>
	 * <br>
	 * The special locator main (default) can be used to select the main window.
	 * <br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>Click Link</td>
	 * <td>popup_link</td>
	 * <td># opens new window</td>
	 * </tr>
	 * <tr>
	 * <td>Select Window</td>
	 * <td>popupName</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Title Should Be</td>
	 * <td>Popup Title</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Select Window</td>
	 * <td></td>
	 * <td># Chooses the main window again</td>
	 * </tr>
	 * </table>
	 * <br>
	 * It is also possible to specify the approach Selenium2Library should take to
	 * find a window by specifying a locator strategy. See `Introduction` for
	 * details about locators:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td><b>Strategy</b></td>
	 * <td><b>Example</b></td>
	 * <td><b>Description</b></td>
	 * </tr>
	 * <tr>
	 * <td>title</td>
	 * <td>Select Window | title=My Document</td>
	 * <td>Matches by window title</td>
	 * </tr>
	 * <tr>
	 * <td>name</td>
	 * <td>Select Window | name=${name}</td>
	 * <td>Matches by window javascript name</td>
	 * </tr>
	 * <tr>
	 * <td>url</td>
	 * <td>Select Window | url=http://google.com</td>
	 * <td>Matches by window's current URL</td>
	 * </tr>
	 * </table>
	 * 
	 * @param locator
	 *            The locator to locate the window
	 */
	@RobotKeyword
	@ArgumentNames({ "locator=NONE" })
	public void selectWindow(String locator) {
		WindowManager.select(webDriverCache.getCurrent(), locator);
	}

	/**
	 * Selects the top frame as the current frame.
	 */
	@RobotKeyword
	public void unselectFrame() {
		webDriverCache.getCurrent().switchTo().defaultContent();
	}

	/**
	 * Returns the current location.
	 * 
	 * @return The current location.
	 * 
	 * @see Logging#logLocation
	 */
	@RobotKeyword
	public String getLocation() {
		return webDriverCache.getCurrent().getCurrentUrl();
	}

	/**
	 * Returns the entire HTML source of the current page or frame.
	 * 
	 * @return The HTML source.
	 * 
	 * @see Logging#logSource
	 */
	@RobotKeyword
	public String getSource() {
		return webDriverCache.getCurrent().getPageSource();
	}

	/**
	 * Returns the title of current page.
	 * 
	 * @return The title.
	 * 
	 * @see Logging#logTitle
	 */
	@RobotKeyword
	public String getTitle() {
		return webDriverCache.getCurrent().getTitle();
	}

	/**
	 * Returns the actually supported capabilities of the remote browser
	 * instance.<br>
	 * <br>
	 * Not all server implementations will support every WebDriver feature.
	 * Therefore, the client and server should use JSON objects with the properties
	 * listed below when describing which features a user requests that a session
	 * support. <b>If a session cannot support a capability that is requested in the
	 * desired capabilities, no error is thrown;</b> a read-only capabilities object
	 * is returned that indicates the capabilities the session actually supports.
	 * For more information see:
	 * <a href= "http://code.google.com/p/selenium/wiki/DesiredCapabilities" >
	 * DesiredCapabilities</a><br>
	 * 
	 * @return The capabilities of the remote node.
	 * 
	 * @see Logging#logRemoteCapabilities
	 */
	@RobotKeyword
	public String getRemoteCapabilities() {
		// Null returned from jbrowserdriver
		if (getCurrentWebDriver() instanceof RemoteWebDriver
				&& ((RemoteWebDriver) getCurrentWebDriver()).getCapabilities() != null) {
			System.out.println(getCurrentWebDriver());
			return ((RemoteWebDriver) getCurrentWebDriver()).getCapabilities().toString();
		} else {
			return "No remote session id or capabilities available";
		}
	}

	/**
	 * Returns the session id of the remote browser instance.<br>
	 * 
	 * @return The remote session id.
	 * 
	 * @see Logging#logRemoteSessionId
	 */
	@RobotKeyword
	public String getRemoteSessionId() {
		if (getCurrentWebDriver() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) getCurrentWebDriver()).getSessionId().toString();
		} else {
			return "No remote session id";
		}
	}

	/**
	 * Returns basic system information about the execution environment.<br>
	 * 
	 * @return System information.
	 * 
	 * @see Logging#logSystemInfo
	 */
	@RobotKeyword
	public String getSystemInfo() {
		return String.format("      os.name: '%s'\n      os.arch: '%s'\n   os.version: '%s'\n java.version: '%s'",
				System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"),
				System.getProperty("java.version"));
	}

	/**
	 * Verify the current page URL is exactly <b>url</b>.<br>
	 * 
	 * @param url
	 *            The URL to verify.
	 * 
	 * @see BrowserManagement#locationShouldContain
	 */
	@RobotKeyword
	@ArgumentNames({ "url" })
	public void locationShouldBe(String url) {
		String actual = getLocation();
		if (!actual.equals(url)) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Location should have been '%s', but was '%s'", url, actual));
		}
		logging.info(String.format("Current location is '%s'.", url));
	}

	/**
	 * Verify the current page URL contains <b>url</b>.<br>
	 * 
	 * @param url
	 *            The URL to verify.
	 * 
	 * @see BrowserManagement#locationShouldBe
	 */
	@RobotKeyword
	@ArgumentNames({ "url" })
	public void locationShouldContain(String url) {
		String actual = getLocation();
		if (!actual.contains(url)) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Location should have contained '%s', but was '%s'", url, actual));
		}
		logging.info(String.format("Current location is '%s'.", url));
	}

	/**
	 * Verify the current page title is exactly <b>title</b>.<br>
	 * 
	 * @param title
	 *            The title to verify.
	 * 
	 * @see BrowserManagement#titleShouldNotBe
	 * @see BrowserManagement#titleShouldContain
	 * @see BrowserManagement#titleShouldNotContain
	 */
	@RobotKeyword
	@ArgumentNames({ "title" })
	public void titleShouldBe(String title) {
		String actual = getTitle();
		if (!actual.equals(title)) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Title should have been '%s', but was '%s'", title, actual));
		}
		logging.info(String.format("Page title is '%s'.", title));
	}

	/**
	 * Verify the current page title is not exactly <b>title</b>.<br>
	 * 
	 * @param title
	 *            The title to verify.
	 * 
	 * @see BrowserManagement#titleShouldBe
	 * @see BrowserManagement#titleShouldContain
	 * @see BrowserManagement#titleShouldNotContain
	 */
	@RobotKeyword
	@ArgumentNames({ "title" })
	public void titleShouldNotBe(String title) {
		String actual = getTitle();
		if (actual.equals(title)) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Title should not have been '%s', but was '%s'", title, actual));
		}
		logging.info(String.format("Page title is '%s'.", title));
	}

	/**
	 * Verify the current page title contains <b>title</b>.<br>
	 * 
	 * @param title
	 *            The title to verify.
	 * 
	 * @see BrowserManagement#titleShouldBe
	 * @see BrowserManagement#titleShouldNotBe
	 * @see BrowserManagement#titleShouldNotContain
	 */
	@RobotKeyword
	@ArgumentNames({ "title" })
	public void titleShouldContain(String title) {
		String actual = getTitle();
		if (!actual.contains(title)) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Title should have contained '%s', but was '%s'", title, actual));
		}
		logging.info(String.format("Page title is '%s'.", title));
	}

	/**
	 * Verify the current page title does not contain <b>title</b>.<br>
	 * 
	 * @param title
	 *            The title to verify.
	 * 
	 * @see BrowserManagement#titleShouldBe
	 * @see BrowserManagement#titleShouldNotBe
	 * @see BrowserManagement#titleShouldContain
	 */
	@RobotKeyword
	@ArgumentNames({ "title" })
	public void titleShouldNotContain(String title) {
		String actual = getTitle();
		if (actual.contains(title)) {
			throw new Selenium2LibraryNonFatalException(
					String.format("Title should not have contained '%s', but was '%s'", title, actual));
		}
		logging.info(String.format("Page title is '%s'.", title));
	}

	/**
	 * Simulates the user clicking the "back" button on their browser.<br>
	 */
	@RobotKeyword
	public void goBack() {
		webDriverCache.getCurrent().navigate().back();
	}

	/**
	 * Navigates the active browser instance to the provided URL.<br>
	 * 
	 * @param url
	 *            The URL to open.
	 */
	@RobotKeyword
	@ArgumentNames({ "url" })
	public void goTo(String url) {
		logging.info(String.format("Opening url '%s'", url));
		webDriverCache.getCurrent().get(url);
	}

	/**
	 * Simulates user reloading page.<br>
	 */
	@RobotKeyword
	public void reloadPage() {
		webDriverCache.getCurrent().navigate().refresh();
	}

	/**
	 * <b>(NOT IMPLEMENTED)</b> Returns the delay in seconds that is waited after
	 * each Selenium command.<br>
	 * 
	 * @return The delay in seconds.
	 * 
	 * @see BrowserManagement#setSeleniumSpeed
	 */
	@RobotKeyword
	public String getSeleniumSpeed() {
		return Robotframework.secsToTimestr(0);
	}

	/**
	 * <b>(NOT IMPLEMENTED)</b> Sets and returns the delay in seconds that is waited
	 * after each Selenium command.<br>
	 * 
	 * @param timestr
	 *            The delay in seconds.
	 * @return The previous delay in seconds.
	 * 
	 * @see BrowserManagement#getSeleniumSpeed
	 */
	@RobotKeyword("(NOT IMPLEMENTED)\n\nSets the delay in seconds that is waited after each " + "Selenium command.\n")
	@ArgumentNames({ "timestr" })
	public String setSeleniumSpeed(String timestr) {
		return "0s";
	}

	/**
	 * Returns the timeout in seconds that is used by various keywords.<br>
	 * 
	 * @return The timeout in seconds.
	 * 
	 * @see BrowserManagement#setSeleniumTimeout
	 */
	@RobotKeyword
	public String getSeleniumTimeout() {
		return Robotframework.secsToTimestr(timeout);
	}

	/**
	 * Sets and returns the timeout in seconds that is used by various keywords.
	 * <br>
	 * <br>
	 * There are several Wait ... keywords that take a timeout as an argument. All
	 * of these timeout arguments are optional. The timeout used by all of them can
	 * be set globally using this keyword. See `Introduction` for more information
	 * about timeouts.<br>
	 * <br>
	 * The previous timeout value is returned by this keyword and can be used to set
	 * the old value back later. The default timeout is 5 seconds, but it can be
	 * altered in importing the library.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>${orig timeout} =</td>
	 * <td>Set Selenium Timeout</td>
	 * <td>15 seconds</td>
	 * </tr>
	 * <tr>
	 * <td># Open page that loads slowly</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Set Selenium Timeout</td>
	 * <td>${orig timeout}</td>
	 * <td># Reset to old value</td>
	 * </tr>
	 * </table>
	 * 
	 * @param timestr
	 *            The timeout in seconds.
	 * @return The previous timeout in seconds.
	 * 
	 * @see BrowserManagement#getSeleniumTimeout
	 */
	@RobotKeyword
	@ArgumentNames({ "timestr" })
	public String setSeleniumTimeout(String timestr) {
		String oldWait = getSeleniumTimeout();
		timeout = Robotframework.timestrToSecs(timestr);

		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache.getWebDrivers()) {
			sessionIdAliasWebDriverTuple.webDriver.manage().timeouts().setScriptTimeout((int) (timeout * 1000.0),
					TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	/**
	 * Returns the implicit wait time in seconds that is used by Selenium.<br>
	 * 
	 * @return The implicit wait time in seconds.
	 * 
	 * @see BrowserManagement#setSeleniumImplicitWait
	 */
	@RobotKeyword
	public String getSeleniumImplicitWait() {
		return Robotframework.secsToTimestr(implicitWait);
	}

	/**
	 * Sets and returns the implicit wait time in seconds that is used by all
	 * Selenium 2 WebDriver instances. This affects all currently open and from now
	 * on opened instances.<br>
	 * <br>
	 * From selenium 2 function: <i>Sets a sticky timeout to implicitly wait for an
	 * element to be found, or a command to complete. This method only needs to be
	 * called one time per session.</i><br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>${orig wait} =</td>
	 * <td>Set Selenium Implicit Wait</td>
	 * <td>10 seconds</td>
	 * </tr>
	 * <tr>
	 * <td># Perform AJAX call that is slow</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Set Selenium Implicit Wait</td>
	 * <td>${orig wait}</td>
	 * <td># Reset to old value</td>
	 * </tr>
	 * </table>
	 * 
	 * @param timestr
	 *            The implicit wait time in seconds.
	 * @return The previous implicit wait time in seconds.
	 * 
	 * @see BrowserManagement#setBrowserImplicitWait
	 * @see BrowserManagement#getSeleniumImplicitWait
	 */
	@RobotKeyword
	@ArgumentNames({ "timestr" })
	public String setSeleniumImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = Robotframework.timestrToSecs(timestr);

		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache.getWebDrivers()) {
			sessionIdAliasWebDriverTuple.webDriver.manage().timeouts().implicitlyWait((int) (implicitWait * 1000.0),
					TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	/**
	 * Sets and returns the implicit wait time in seconds that is used by the
	 * current Selenium 2 WebDriver instance.<br>
	 * <br>
	 * From selenium 2 function: <i>Sets a sticky timeout to implicitly wait for an
	 * element to be found, or a command to complete. This method only needs to be
	 * called one time per session.</i><br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0" summary="">
	 * <tr>
	 * <td>${orig wait} =</td>
	 * <td>Set Browser Implicit Wait</td>
	 * <td>10 seconds</td>
	 * </tr>
	 * <tr>
	 * <td># Perform AJAX call that is slow</td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Set Browser Implicit Wait</td>
	 * <td>${orig wait}</td>
	 * <td># Reset to old value</td>
	 * </tr>
	 * </table>
	 * 
	 * @param timestr
	 *            The implicit wait time in seconds.
	 * @return The previous implicit wait time in seconds.
	 * 
	 * @see BrowserManagement#setSeleniumImplicitWait
	 */
	@RobotKeyword
	@ArgumentNames({ "timestr" })
	public String setBrowserImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = Robotframework.timestrToSecs(timestr);
		webDriverCache.getCurrent().manage().timeouts().implicitlyWait((int) (implicitWait * 1000.0),
				TimeUnit.MILLISECONDS);
		return oldWait;
	}

	@RobotKeywordOverload
	public void setRemoteWebDriverProxy(String host, String port) {
		setRemoteWebDriverProxy(host, port, "");
	}

	@RobotKeywordOverload
	public void setRemoteWebDriverProxy(String host, String port, String user) {
		setRemoteWebDriverProxy(host, port, user, "");
	}

	@RobotKeywordOverload
	public void setRemoteWebDriverProxy(String host, String port, String user, String password) {
		setRemoteWebDriverProxy(host, port, user, password, "");
	}

	@RobotKeywordOverload
	public void setRemoteWebDriverProxy(String host, String port, String user, String password, String domain) {
		setRemoteWebDriverProxy(host, port, user, password, domain, "");
	}

	/**
	 * Configures proxy handling for remote WebDriver instances.<br>
	 * <br>
	 * This is needed to connect to an external Selenium 2 WebDriver rid through a
	 * local HTTP proxy. This implementation handles BASIC, DIGEST and NTLM based
	 * authentication schemes correctly.<br>
	 * <br>
	 * The given configuration will be used for all subsequent calls of `Open
	 * Browser`. To remove the proxy call:<br>
	 * Set Remote Web Driver Proxy | ${EMPTY} | ${EMPTY}<br>
	 * <br>
	 * Some additional info:
	 * <ul>
	 * <li>If no <b>username</b> is provided, it looks for a username at the Java
	 * property http.proxyUser and the environment variables HTTP_PROXY and
	 * http_proxy. If a username is found, it is only used, if the host and port
	 * also match.</li>
	 * <li>If no <b>password</b> is provided, it looks for a password at the Java
	 * property http.proxyUser and the environment variables HTTP_PROXY and
	 * http_proxy. If a password is found, it is only used, if the host, port and
	 * username also match.</li>
	 * <li>If a <b>domain</b> is provided, NTLM based authentication is used</li>
	 * <li>If no <b>workstation</b> is provided and NTLM based authentication is
	 * used, the hostname is used as workstation name.</li>
	 * </ul>
	 * 
	 * @param host
	 *            The hostname of the proxy
	 * @param port
	 *            The port of the proxy
	 * @param username
	 *            Default=NONE. The usename
	 * @param password
	 *            Default=NONE. The password of the user
	 * @param domain
	 *            Default=NONE. The NTLM domain name
	 * @param workstation
	 *            Default=NONE. The name of the workstation
	 */
	@RobotKeyword
	@ArgumentNames({ "host", "port", "username=NONE", "password=NONE", "domain=NONE", "workstation=NONE" })
	public void setRemoteWebDriverProxy(String host, String port, String username, String password, String domain,
			String workstation) {

		if (host.length() == 0 || port.length() == 0) {
			// No host and port given as proxy
			remoteWebDriverProxyHost = "";
			remoteWebDriverProxyPort = "";
			remoteWebDriverProxyUser = "";
			remoteWebDriverProxyPassword = "";
			remoteWebDriverProxyDomain = "";
			remoteWebDriverProxyWorkstation = "";
			return;
		}

		URL proxyUrl = null;
		try {
			String httpProxy = System.getenv().get("http_proxy");
			if (httpProxy != null) {
				proxyUrl = new URL(httpProxy);
			} else {
				httpProxy = System.getenv().get("HTTP_PROXY");
				if (httpProxy != null) {
					proxyUrl = new URL(httpProxy);
				}
			}
		} catch (MalformedURLException e) {
			throw new Selenium2LibraryNonFatalException(e.getMessage());
		}

		if (username.length() == 0) {
			// look for a username from properties
			if (System.getProperty("http.proxyHost", "").equals(host)
					&& System.getProperty("http.proxyPort", "").equals(port)) {
				username = System.getProperty("http.proxyUser", "");
			}
			// look for a username from environment
			if (username.length() == 0) {
				if (proxyUrl != null && proxyUrl.getHost().equals(host)
						&& Integer.toString(proxyUrl.getPort()).equals(port)) {
					username = getUserFromURL(proxyUrl);
				}
			}
		}

		if (password.length() == 0) {
			// look for a password from properties
			if (System.getProperty("http.proxyHost", "").equals(host)
					&& System.getProperty("http.proxyPort", "").equals(port)
					&& System.getProperty("http.proxyUser", "").equals(username)) {
				password = System.getProperty("http.proxyPassword", "");
			}
			// look for a password from environment
			if (password.length() == 0) {
				if (proxyUrl != null && proxyUrl.getHost().equals(host)
						&& Integer.toString(proxyUrl.getPort()).equals(port)
						&& getUserFromURL(proxyUrl).equals(username)) {
					password = getPasswordFromURL(proxyUrl);
				}
			}
		}

		if (domain.length() != 0 && workstation.length() == 0) {
			try {
				workstation = InetAddress.getLocalHost().getHostName().split("\\.")[0];
			} catch (UnknownHostException e) {
				logging.warn("No workstation name found");
			}
		}

		remoteWebDriverProxyHost = host;
		remoteWebDriverProxyPort = port;
		remoteWebDriverProxyUser = username;
		remoteWebDriverProxyPassword = password;
		remoteWebDriverProxyDomain = domain;
		remoteWebDriverProxyWorkstation = workstation;
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected String getUserFromURL(URL url) {
		String auth = url.getUserInfo();
		int index = auth.indexOf(':');
		if (index == -1) {
			return auth;
		}
		return auth.substring(0, index);
	}

	protected String getPasswordFromURL(URL url) {
		String auth = url.getUserInfo();
		int index = auth.indexOf(':');
		if (index == -1) {
			return "";
		}
		return auth.substring(index + 1);
	}

	protected WebDriver createWebDriver(String browserName, String desiredCapabilitiesString, String remoteUrlString,
			String browserOptions) throws MalformedURLException {
		browserName = browserName.toLowerCase().replace(" ", "");
		DesiredCapabilities desiredCapabilities = createDesiredCapabilities(browserName, desiredCapabilitiesString,
				browserOptions);

		WebDriver webDriver;
		if (remoteUrlString != null && !"False".equals(remoteUrlString)) {
			webDriver = createRemoteWebDriver(desiredCapabilities, new URL(remoteUrlString));
		} else {
			webDriver = createLocalWebDriver(browserName, desiredCapabilities);
		}

		webDriver.manage().timeouts().setScriptTimeout((int) (timeout * 1000.0), TimeUnit.MILLISECONDS);
		webDriver.manage().timeouts().implicitlyWait((int) (implicitWait * 1000.0), TimeUnit.MILLISECONDS);

		return webDriver;
	}

	protected WebDriver createLocalWebDriver(String browserName, DesiredCapabilities desiredCapabilities) {
		System.out.println(browserName);
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			return new FirefoxDriver(desiredCapabilities);
		} else if ("ie".equals(browserName) || "internetexplorer".equals(browserName)) {
			return new InternetExplorerDriver(desiredCapabilities);
		} else if ("gc".equals(browserName) || "chrome".equals(browserName) || "googlechrome".equals(browserName)) {
			return new ChromeDriver(desiredCapabilities);
		} else if ("opera".equals(browserName)) {
			return new OperaDriver(desiredCapabilities);
		} else if ("phantomjs".equals(browserName)) {
			return new PhantomJSDriver(desiredCapabilities);
		} else if ("safari".equals(browserName)) {
			return new SafariDriver(desiredCapabilities);
		} else if ("htmlunit".equals(browserName)) {
			return new HtmlUnitDriver(desiredCapabilities);
		} else if ("htmlunitwithjs".equals(browserName)) {
			HtmlUnitDriver driver = new HtmlUnitDriver(desiredCapabilities);
			driver.setJavascriptEnabled(true);
			return driver;
		} else if ("iphone".equals(browserName) || "ipad".equals(browserName)) {
			try {
				return new IOSDriver<WebElement>(new URL(""), desiredCapabilities);
			} catch (Exception e) {
				throw new Selenium2LibraryFatalException("Creating " + browserName + " instance failed.", e);
			}
		} else if ("android".equals(browserName)) {
			try {
				return new SelendroidDriver(desiredCapabilities);
			} catch (Exception e) {
				throw new Selenium2LibraryFatalException(e);
			}
		} else if ("jbrowser".equals(browserName)) {
			return new JBrowserDriver(Settings.builder().build());
		}

		throw new Selenium2LibraryFatalException(browserName + " is not a supported browser.");
	}

	protected WebDriver createRemoteWebDriver(DesiredCapabilities desiredCapabilities, URL remoteUrl) {
		HttpCommandExecutor httpCommandExecutor = new HttpCommandExecutor(remoteUrl);
		setRemoteWebDriverProxy(httpCommandExecutor);
		return new Augmenter().augment(new RemoteWebDriver(httpCommandExecutor, desiredCapabilities));
	}

	protected DesiredCapabilities createDesiredCapabilities(String browserName, String desiredCapabilitiesString,
			String browserOptions) {
		DesiredCapabilities desiredCapabilities;
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.firefox();
			parseBrowserOptionsFirefox(browserOptions, desiredCapabilities);
		} else if ("ie".equals(browserName) || "internetexplorer".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.internetExplorer();
		} else if ("gc".equals(browserName) || "chrome".equals(browserName) || "googlechrome".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.chrome();
		} else if ("opera".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.opera();
		} else if ("phantomjs".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.phantomjs();
		} else if ("safari".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.safari();
		} else if ("ipad".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.ipad();
		} else if ("iphone".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.iphone();
		} else if ("android".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.android();
		} else if ("htmlunit".equals(browserName) || "htmlunitwithjs".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.htmlUnit();
		} else if ("jbrowser".equals(browserName)) {
			desiredCapabilities = new DesiredCapabilities("jbrowser", "1", Platform.ANY);
		} else {
			throw new Selenium2LibraryFatalException(browserName + " is not a supported browser.");
		}

		if (desiredCapabilitiesString != null && !"None".equals(desiredCapabilitiesString)) {
			JSONObject jsonObject = (JSONObject) JSONValue.parse(desiredCapabilitiesString);
			if (jsonObject != null) {
				// Valid JSON
				Iterator<?> iterator = jsonObject.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
					desiredCapabilities.setCapability(entry.getKey().toString(), entry.getValue());
				}
			} else {
				// Invalid JSON. Old style key-value pairs
				for (String capability : desiredCapabilitiesString.split(",")) {
					String[] keyValue = capability.split(":");
					if (keyValue.length == 2) {
						desiredCapabilities.setCapability(keyValue[0], keyValue[1]);
					} else {
						logging.warn("Invalid desiredCapabilities: " + desiredCapabilitiesString);
					}
				}
			}
		}
		return desiredCapabilities;
	}

	protected void parseBrowserOptionsFirefox(String browserOptions, DesiredCapabilities desiredCapabilities) {
		if (browserOptions != null && !"NONE".equals(browserOptions)) {
			JSONObject jsonObject = (JSONObject) JSONValue.parse(browserOptions);
			if (jsonObject != null) {
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				Iterator<?> iterator = jsonObject.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
					String key = entry.getKey().toString();
					if (key.equals("preferences")) {
						// Preferences
						JSONObject preferences = (JSONObject) entry.getValue();
						Iterator<?> iteratorPreferences = preferences.entrySet().iterator();
						while (iteratorPreferences.hasNext()) {
							Entry<?, ?> entryPreferences = (Entry<?, ?>) iteratorPreferences.next();
							Object valuePreferences = entryPreferences.getValue();
							if (valuePreferences instanceof Number) {
								firefoxProfile.setPreference(entryPreferences.getKey().toString(),
										((Number) valuePreferences).intValue());
							} else if (valuePreferences instanceof Boolean) {
								firefoxProfile.setPreference(entryPreferences.getKey().toString(),
										((Boolean) valuePreferences).booleanValue());
							} else {
								firefoxProfile.setPreference(entryPreferences.getKey().toString(),
										valuePreferences.toString());
							}
						}
					} else if (key.equals("extensions")) {
						// Extensions
						JSONArray extensions = (JSONArray) entry.getValue();
						Iterator<?> iteratorExtensions = extensions.iterator();
						while (iteratorExtensions.hasNext()) {
							File file = new File(iteratorExtensions.next().toString().replace('/', File.separatorChar));
							firefoxProfile.addExtension(file);
						}
					} else {
						logging.warn("Unknown browserOption: " + key + ":" + entry.getValue());
					}
				}
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
			} else {
				logging.warn("Invalid browserOptions: " + browserOptions);
			}
		}
	}

	protected void setRemoteWebDriverProxy(HttpCommandExecutor httpCommandExecutor) {
		if (remoteWebDriverProxyHost.length() == 0) {
			return;
		}

		String fieldName = "<unknown>";
		String className = "<unknown>";
		try {
			// Get access to the client instance
			fieldName = "client";
			className = "DefaultHttpClient";
			Field field = HttpCommandExecutor.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			DefaultHttpClient client = (DefaultHttpClient) field.get(httpCommandExecutor);

			// set the credentials for the proxy
			AuthScope authScope = new AuthScope(remoteWebDriverProxyHost, Integer.parseInt(remoteWebDriverProxyPort));
			if (remoteWebDriverProxyDomain.length() == 0) {
				// BASIC Authentication
				client.getCredentialsProvider().setCredentials(authScope,
						new UsernamePasswordCredentials(remoteWebDriverProxyUser, remoteWebDriverProxyPassword));
			} else {
				// NTLM Authentication
				client.getCredentialsProvider().setCredentials(authScope, new NTCredentials(remoteWebDriverProxyUser,
						remoteWebDriverProxyPassword, remoteWebDriverProxyWorkstation, remoteWebDriverProxyDomain));
			}

			// Set the RoutePlanner back to something that handles
			// proxies correctly.
			client.setRoutePlanner(new DefaultHttpRoutePlanner(client.getConnectionManager().getSchemeRegistry()));
			HttpHost proxy = new HttpHost(remoteWebDriverProxyHost, Integer.parseInt(remoteWebDriverProxyPort));
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		} catch (SecurityException e) {
			throw new Selenium2LibraryFatalException(
					String.format("The SecurityManager does not allow us to lookup to the %s field.", fieldName));
		} catch (NoSuchFieldException e) {
			throw new Selenium2LibraryFatalException(
					String.format("The RemoteWebDriver dose not declare the %s field any more.", fieldName));
		} catch (IllegalArgumentException e) {
			throw new Selenium2LibraryFatalException(
					String.format("The field %s does not belong to the given object.", fieldName));
		} catch (IllegalAccessException e) {
			throw new Selenium2LibraryFatalException(
					String.format("The SecurityManager does not allow us to access to the %s field.", fieldName));
		} catch (ClassCastException e) {
			throw new Selenium2LibraryFatalException(
					String.format("The %s field does not contain a %s.", fieldName, className));
		}
	}

	protected List<String> toList(List<String> items) {
		return toList(items, "item");
	}

	protected List<String> toList(List<String> items, String what) {
		List<String> msg = new ArrayList<String>();
		msg.add(String.format("Altogether %d %s%s.\n", items.size(), what, items.size() == 1 ? "" : "s"));
		for (int index = 0; index < items.size(); index++) {
			msg.add(String.format("%d: %s", index + 1, items.get(index)));
		}
		return items;
	}
}

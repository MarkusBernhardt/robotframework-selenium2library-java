package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.iphone.IPhoneDriver;
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
import com.opera.core.systems.OperaDriver;

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
	private WebDriverCache webDriverCache = new WebDriverCache();

	/**
	 * Timeout in milliseconds
	 */
	private double timeout = 5.0;

	/**
	 * Implicit wait in milliseconds
	 */
	private double implicitWait = 0;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	private Logging logging;

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	private Element element;

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

	@RobotKeyword
	@ArgumentNames({ "strategyName", "functionDefinition", "delimiter=NONE" })
	public void addLocationStrategy(String strategyName, String functionDefinition, String delimiter) {
		ElementFinder.addLocationStrategy(strategyName, functionDefinition, delimiter);
	}

	@RobotKeyword("Closes the current browser.")
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

	@RobotKeywordOverload
	public String openBrowser(String url, String browserName, String alias, String remoteUrl, String desiredCapabilities)
			throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl, desiredCapabilities, null);
	}

	@RobotKeyword("Opens a new browser instance to given URL.\n\n"

			+ "Returns the index of this browser instance which can be used later to switch "
			+ "back to it. Index starts from 1 and is reset back to it when `Close All "
			+ "Browsers` keyword is used. See `Switch Browser` for example.\n\n"

			+ "Optional alias is an alias for the browser instance and it can be used for "
			+ "switching between browsers (just as index can be used). See `Switch Browser` "
			+ "for more details.\n\n"

			+ "Possible values for browser are as follows:\n"
			+ "| firefox | FireFox |\n"
			+ "| ff | FireFox |\n"
			+ "| internetexplorer | Internet Explorer |\n"
			+ "| ie | Internet Explorer |\n"
			+ "| googlechrome | Google Chrome |\n"
			+ "| gc | Google Chrome |\n"
			+ "| chrome | Google Chrome |\n"
			+ "| opera | Opera |\n"
			+ "| phantomjs | PhantomJS |\n"
			+ "| htmlunitwithjs | HTMLUnit with Javascipt support |\n"
			+ "| safari | Safari |\n"
			+ "| ipad | iPad |\n"
			+ "| iphone | iPhone |\n"
			+ "| android | Android |\n"
			+ "| htmlunit | HTMLUnit |\n\n"

			+ "Note, that you will encounter strange behavior, if you open multiple Internet "
			+ "Explorer browser instances. That is also why `Switch Browser` only works with "
			+ "one IE browser at most. For more information see:\n"
			+ "http://selenium-grid.seleniumhq.org/faq.html#i_get_some_strange_errors_when_i_run_multiple_internet_explorer_instances_on_the_same_machine\n\n"

			+ "Optional 'remote_url' is the url for a remote selenium server for example "
			+ "http://127.0.0.1/wd/hub. If you specify a value for remote you can also specify "
			+ "'desired_capabilities' which is a string in the form key1:val1,key2:val2 that "
			+ "will be used to specify desired_capabilities to the remote server. This is "
			+ "useful for doing things like specify a proxy server for internet explorer or for "
			+ "specify browser and os if your using saucelabs.com.\n\n"

			+ "Optional 'ff_profile_dir' is the path to the firefox profile dir if you wish to overwrite the default.\n")
	@ArgumentNames({ "url", "browserName=firefox", "alias=NONE", "remoteUrl=NONE", "desiredCapabilities=NONE",
			"ffProfileDir=NONE" })
	public String openBrowser(String url, String browserName, String alias, String remoteUrl,
			String desiredCapabilities, String ffProfileDir) throws Throwable {
		try {
			logging.info("browserName: " + browserName);
			if (remoteUrl != null) {
				logging.info(String.format("Opening browser '%s' to base url '%s' through remote server at '%s'",
						browserName, url, remoteUrl));
			} else {
				logging.info(String.format("Opening browser '%s' to base url '%s'", browserName, url));
			}

			WebDriver webDriver = createWebDriver(browserName, desiredCapabilities, ffProfileDir, remoteUrl);
			webDriver.get(url);
			String sessionId = webDriverCache.register(webDriver, alias);
			logging.debug(String.format("Opened browser with session id %s", sessionId));
			return sessionId;
		} catch (Throwable t) {
			if (remoteUrl != null) {
				logging.warn(String.format(
						"Opening browser '%s' to base url '%s' through remote server at '%s' failed", browserName, url,
						remoteUrl));
			} else {
				logging.warn(String.format("Opening browser '%s' to base url '%s' failed", browserName, url));
			}
			throw new Selenium2LibraryFatalException(t);
		}
	}

	@RobotKeyword("Switches between active browsers using _index_ or _alias_.\n"
			+ "Index is returned from `Open Browser` and alias can be given to it.\n\n"

			+ "Example:\n" + "| Open Browser | http://google.com | ff |\n"
			+ "| Location Should Be | http://google.com |\n" + "| Open Browser | http://yahoo.com | ie | 2nd conn |\n"
			+ "| Location Should Be | http://yahoo.com |\n" + "| Switch Browser | 1 | #index |\n"
			+ "| Page Should Contain | I'm feeling lucky |\n" + "| Switch Browser | 2nd conn | # alias |\n"
			+ "| Page Should Contain | More Yahoo! |\n" + "| Close All Browsers |\n\n"

			+ "Above example expects that there was no other open browsers when "
			+ "opening the first one because it used index '1' when switching to it "
			+ "later. If you aren't sure about that you can store the index into a " + "variable as below.\n"
			+ "| ${id} = | Open Browser | http://google.com | *firefox |\n" + "| # Do something ... |\n"
			+ "| Switch Browser | ${id} |\n")
	@ArgumentNames({ "indexOrAlias" })
	public void switchBrowser(String indexOrAlias) {
		try {
			webDriverCache.switchBrowser(indexOrAlias);
			logging.debug(String.format("Switched to browser with Selenium session id %s",
					webDriverCache.getCurrentSessionId()));
		} catch (Throwable t) {
			throw new Selenium2LibraryFatalException(String.format("No browser with index or alias '%s' found.",
					indexOrAlias));
		}
	}

	@RobotKeyword("Closes all open browsers and resets the browser cache.\n\n"

	+ "After this keyword new indexes returned from `Open Browser` keyword are reset " + "to 1.\n\n"

	+ "This keyword should be used in test or suite teardown to make sure all browsers " + "are closed.\n")
	public void closeAllBrowsers() {
		logging.debug("Closing all browsers");
		webDriverCache.closeAll();
	}

	@RobotKeyword("Closes currently opened pop-up window.\n")
	public void closeWindow() {
		webDriverCache.getCurrent().close();
	}

	@RobotKeyword("Returns and logs id attributes of all windows known to the browser.\n")
	public List<String> getWindowIdentifiers() {
		return logging.logList(WindowManager.getWindowIds(webDriverCache.getCurrent()), "Window Id");
	}

	@RobotKeyword("Returns and logs names of all windows known to the browser.\n")
	public List<String> getWindowNames() {
		List<String> windowNames = WindowManager.getWindowNames(webDriverCache.getCurrent());
		if (windowNames.size() != 0 && windowNames.get(0).equals("undefined")) {
			windowNames.set(0, "selenium_main_app_window");
		}
		return logging.logList(windowNames, "Window Name");
	}

	@RobotKeyword("Returns and logs titles of all windows known to the browser.\n")
	public List<String> getWindowTitles() {
		return logging.logList(WindowManager.getWindowTitles(webDriverCache.getCurrent()), "Window Title");
	}

	@RobotKeyword("Maximizes current browser window.\n")
	public void maximizeBrowserWindow() {
		webDriverCache.getCurrent().manage().window().maximize();
	}

	@RobotKeyword("Sets frame identified by _locator_ as current frame.\n\n"

	+ "Key attributes for frames are id and name. See `Introduction` for details " + "about locating elements.\n")
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

	@RobotKeyword("Selects the window found with _locator_ as the context of actions.\n\n"

	+ "If the window is found, all subsequent commands use that window, until this keyword "
			+ "is used again. If the window is not found, this keyword fails.\n\n"

			+ "By default, when a locator value is provided, it is matched against the title of the "
			+ "window and the javascript name of the window. If multiple windows with same identifier"
			+ " are found, the first one is selected.\n\n"

			+ "Special locator main (default) can be used to select the main window.\n"
			+ "It is also possible to specify the approach Selenium2Library should take to find a window"
			+ " by specifying a locator strategy:\n\n"

			+ "| Strategy | Example | Description |\n"
			+ "| title | Select Window title=My Document | Matches by window title |\n"
			+ "| name | Select Window name=${name} | Matches by window javascript name |\n"
			+ "| url | Select Window url=http://google.com | Matches by window's current URL |\n\n"

			+ "Example:\n" + "| Click Link | popup_link | #opens new window |\n" + "| Select Window | popupName | |\n"
			+ "| Title Should Be | Popup Title | |\n" + "| Select Window | | #Chooses the main window again |\n")
	@ArgumentNames({ "locator=NONE" })
	public void selectWindow(String locator) {
		WindowManager.select(webDriverCache.getCurrent(), locator);
	}

	@RobotKeyword("Sets the top frame as the current frame.\n")
	public void unselectFrame() {
		webDriverCache.getCurrent().switchTo().defaultContent();
	}

	@RobotKeyword("Returns the current location.\n")
	public String getLocation() {
		return webDriverCache.getCurrent().getCurrentUrl();
	}

	@RobotKeyword("Returns the entire html source of the current page or frame.\n")
	public String getSource() {
		return webDriverCache.getCurrent().getPageSource();
	}

	@RobotKeyword("Returns title of current page.\n")
	public String getTitle() {
		return webDriverCache.getCurrent().getTitle();
	}

	@RobotKeyword
	public String getRemoteCapabilities() {
		if (getCurrentWebDriver() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) getCurrentWebDriver()).getCapabilities().toString();
		} else {
			return "No remote session id";
		}
	}

	@RobotKeyword
	public String getRemoteSessionId() {
		if (getCurrentWebDriver() instanceof RemoteWebDriver) {
			return ((RemoteWebDriver) getCurrentWebDriver()).getSessionId().toString();
		} else {
			return "No remote session id";
		}
	}

	@RobotKeyword
	public String getSystemInfo() {
		return String.format("      os.name: '%s'\n      os.arch: '%s'\n   os.version: '%s'\n java.version: '%s'",
				System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"),
				System.getProperty("java.version"));
	}

	@RobotKeyword("Verifies that current URL is exactly _url_.\n")
	@ArgumentNames({ "url" })
	public void locationShouldBe(String url) {
		String actual = getLocation();
		if (!actual.equals(url)) {
			throw new Selenium2LibraryNonFatalException(String.format("Location should have been '%s' but was '%s'",
					url, actual));
		}
		logging.info(String.format("Current location is '%s'.", url));
	}

	@RobotKeyword("Verifies that current URL contains _url_.\n")
	@ArgumentNames({ "url" })
	public void locationShouldContain(String url) {
		String actual = getLocation();
		if (!actual.contains(url)) {
			throw new Selenium2LibraryNonFatalException(String.format("Location should have been '%s' but was '%s'",
					url, actual));
		}
		logging.info(String.format("Current location is '%s'.", url));
	}

	@RobotKeyword("Verifies that current page title equals _title_.\n")
	@ArgumentNames({ "title" })
	public void titleShouldBe(String title) {
		String actual = getTitle();
		if (!actual.equals(title)) {
			throw new Selenium2LibraryNonFatalException(String.format("Title should have been '%s' but was '%s'",
					title, actual));
		}
		logging.info(String.format("Page title is '%s'.", title));
	}

	@RobotKeyword("Simulates the user clicking the \"back\" button on their browser.\n")
	public void goBack() {
		webDriverCache.getCurrent().navigate().back();
	}

	@RobotKeyword("Navigates the active browser instance to the provided URL.\n")
	@ArgumentNames({ "url" })
	public void goTo(String url) {
		logging.info(String.format("Opening url '%s'", url));
		webDriverCache.getCurrent().get(url);
	}

	@RobotKeyword("Simulates user reloading page.\n")
	public void reloadPage() {
		webDriverCache.getCurrent().navigate().refresh();
	}

	@RobotKeyword("Gets the delay in seconds that is waited after each Selenium command.\n\n"

	+ "See `Set Selenium Speed` for an explanation.\n")
	public String getSeleniumSpeed() {
		return Robotframework.secsToTimestr(0);
	}

	@RobotKeyword("Gets the timeout in seconds that is used by various keywords.\n\n"

	+ "See `Set Selenium Timeout` for an explanation.\n")
	public String getSeleniumTimeout() {
		return Robotframework.secsToTimestr(timeout);
	}

	@RobotKeyword("Gets the wait in seconds that is waited by Selenium.\n\n"

	+ "See `Set Selenium Implicit Wait` for an explanation.\n")
	public String getSeleniumImplicitWait() {
		return Robotframework.secsToTimestr(implicitWait);
	}

	@RobotKeyword("(NOT IMPLEMENTED)\n\nSets the delay in seconds that is waited after each " + "Selenium command.\n")
	@ArgumentNames({ "timestr" })
	public String setSeleniumSpeed(String timestr) {
		return "0s";
	}

	@RobotKeyword("Sets the timeout in seconds used by various keywords.\n\n"

	+ "There are several Wait ... keywords that take timeout as an argument. All of "
			+ "these timeout arguments are optional. The timeout used by all of them can be "
			+ "set globally using this keyword. See `Introduction` for more information about " + "timeouts.\n\n"

			+ "The previous timeout value is returned by this keyword and can be used to set "
			+ "the old value back later. The default timeout is 5 seconds, but it can be "
			+ "altered in importing.\n\n"

			+ "Example:\n" + "| ${orig timeout} = | Set Selenium Timeout | 15 seconds |\n"
			+ "| Open page that loads slowly |\n" + "| Set Selenium Timeout | ${orig timeout} |\n")
	@ArgumentNames({ "timestr" })
	public String setSeleniumTimeout(String timestr) {
		String oldWait = getSeleniumTimeout();
		timeout = Robotframework.timestrToSecs(timestr);

		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache.getWebDrivers()) {
			sessionIdAliasWebDriverTuple.webDriver.manage().timeouts()
					.setScriptTimeout((int) (timeout * 1000.0), TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	@RobotKeyword("Sets Selenium 2's default implicit wait in seconds and sets the implicit wait "
			+ "for all open browsers.\n\n"

			+ "From selenium 2 function 'Sets a sticky timeout to implicitly wait for an element "
			+ "to be found, or a command to complete. This method only needs to be called one time "
			+ "per session.'\n\n"

			+ "	Example:\n" + "| ${orig wait} = | Set Selenium Implicit Wait | 10 seconds |\n"
			+ "| Perform AJAX call that is slow |\n" + "| Set Selenium Implicit Wait | ${orig wait} |\n")
	@ArgumentNames({ "timestr" })
	public String setSeleniumImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = Robotframework.timestrToSecs(timestr);

		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache.getWebDrivers()) {
			sessionIdAliasWebDriverTuple.webDriver.manage().timeouts()
					.implicitlyWait((int) (implicitWait * 1000.0), TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	@RobotKeyword("Sets current browser's implicit wait in seconds.\n\n"

	+ "From selenium 2 function 'Sets a sticky timeout to implicitly wait for an element to be found, "
			+ "or a command to complete. This method only needs to be called one time per session.'\n\n"

			+ "Example:\n" + "| Set Browser Implicit Wait | 10 seconds |\n\n"

			+ "See also `Set Selenium Implicit Wait`.\n")
	@ArgumentNames({ "timestr" })
	public String setBrowserImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = Robotframework.timestrToSecs(timestr);
		webDriverCache.getCurrent().manage().timeouts()
				.implicitlyWait((int) (implicitWait * 1000.0), TimeUnit.MILLISECONDS);
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

	@RobotKeyword("Configures proxy handling for RemoteWebDriver instances.\n\n"

			+ "This is needed, if you want to connect to an external Selenium grid through a "
			+ "local HTTP proxy. This implementation handles BASIC, DIGEST and NTLM "
			+ "based authentication schemes correctly.\n\n"

			+ "If you set a proxy, it will be used for all subsequent calls of Open Browser.\n"
			+ "You can remove the proxy by calling: Set Remote Web Driver Proxy    ${EMPTY}    ${EMPTY}\n\n"

			+ "Some additional info:\n"
			+ "|If no _username_ is provided, it looks for a username at the Java property http.proxyUser and the environment variables HTTP_PROXY and http_proxy. If a username is found, it is only used, if the host and port also match.|\n"
			+ "|If no _password_ is provided, it looks for a password at the Java property http.proxyUser and the environment variables HTTP_PROXY and http_proxy. If a password is found, it is only used, if the host, port and username also match.|\n"
			+ "|If a _domain_, NTLM based authentication is used|\n"
			+ "|If no _workstation_ is provided and NTLM based authentication is used, the hostname is used as workstation name.|\n")
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

	protected WebDriver createWebDriver(String browserName, String desiredCapabilitiesString, String profileDirectory,
			String remoteUrlString) throws MalformedURLException {
		browserName = browserName.toLowerCase().replace(" ", "");
		DesiredCapabilities desiredCapabilities = createDesiredCapabilities(browserName, desiredCapabilitiesString,
				profileDirectory);

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
				return new IPhoneDriver(desiredCapabilities);
			} catch (Exception e) {
				throw new Selenium2LibraryFatalException("Creating " + browserName + " instance failed.", e);
			}
		} else if ("android".equals(browserName)) {
			return new AndroidDriver(desiredCapabilities);
		}

		throw new Selenium2LibraryFatalException(browserName + " is not a supported browser.");
	}

	protected WebDriver createRemoteWebDriver(DesiredCapabilities desiredCapabilities, URL remoteUrl) {
		HttpCommandExecutor httpCommandExecutor = new HttpCommandExecutor(remoteUrl);
		setRemoteWebDriverProxy(httpCommandExecutor);
		return new Augmenter().augment(new RemoteWebDriver(httpCommandExecutor, desiredCapabilities));
	}

	protected DesiredCapabilities createDesiredCapabilities(String browserName, String desiredCapabilitiesString,
			String profileDirectory) {
		DesiredCapabilities desiredCapabilities;
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.firefox();
			if (profileDirectory != null) {
				FirefoxProfile profile = new FirefoxProfile(new File(profileDirectory));
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
			}
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
		} else {
			throw new Selenium2LibraryFatalException(browserName + " is not a supported browser.");
		}

		if (desiredCapabilitiesString != null && !"None".equals(desiredCapabilitiesString)) {
			for (String capability : desiredCapabilitiesString.split(",")) {
				String[] keyValue = capability.split(":");
				desiredCapabilities.setCapability(keyValue[0], keyValue[1]);
			}
		}
		return desiredCapabilities;
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
				client.getCredentialsProvider().setCredentials(
						authScope,
						new NTCredentials(remoteWebDriverProxyUser, remoteWebDriverProxyPassword,
								remoteWebDriverProxyWorkstation, remoteWebDriverProxyDomain));
			}

			// Set the RoutePlanner back to something that handles
			// proxies correctly.
			client.setRoutePlanner(new DefaultHttpRoutePlanner(client.getConnectionManager().getSchemeRegistry()));
			HttpHost proxy = new HttpHost(remoteWebDriverProxyHost, Integer.parseInt(remoteWebDriverProxyPort));
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		} catch (SecurityException e) {
			throw new Selenium2LibraryFatalException(String.format(
					"The SecurityManager does not allow us to lookup to the %s field.", fieldName));
		} catch (NoSuchFieldException e) {
			throw new Selenium2LibraryFatalException(String.format(
					"The RemoteWebDriver dose not declare the %s field any more.", fieldName));
		} catch (IllegalArgumentException e) {
			throw new Selenium2LibraryFatalException(String.format("The field %s does not belong to the given object.",
					fieldName));
		} catch (IllegalAccessException e) {
			throw new Selenium2LibraryFatalException(String.format(
					"The SecurityManager does not allow us to access to the %s field.", fieldName));
		} catch (ClassCastException e) {
			throw new Selenium2LibraryFatalException(String.format("The %s field does not contain a %s.", fieldName,
					className));
		}

	}

}

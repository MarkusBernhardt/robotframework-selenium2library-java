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
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryFatalException;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.WindowManager;
import com.github.markusbernhardt.selenium2library.utils.Robotframework;
import com.github.markusbernhardt.selenium2library.utils.WebDriverCache;
import com.github.markusbernhardt.selenium2library.utils.WebDriverCache.SessionIdAliasWebDriverTuple;
import com.opera.core.systems.OperaDriver;

@RobotKeywords
public abstract class BrowserManagement {

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

	// ##############################
	// Keywords
	// ##############################

	public void closeBrowser() {
		if (webDriverCache.getCurrentSessionId() != null) {
			debug(String.format("Closing browser with session id %s",
					webDriverCache.getCurrentSessionId()));
			webDriverCache.close();
		}
	}

	@RobotKeyword("Opens a new browser instance to given URL.\n\n"
 				
			+ "Returns the index of this browser instance which can be used later to switch "
			+ "back to it. Index starts from 1 and is reset back to it when Close All "
			+ "Browsers keyword is used. See Switch Browser for example.\n\n"
 				
 			+ "Optional alias is an alias for the browser instance and it can be used for "
 			+ "switching between browsers (just as index can be used). See Switch Browser "
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
			+ "| htmlunit | HTMLUnit |\n"
			+ "| htmlunitwithjs | HTMLUnit with Javascipt support |\n\n"

			+ "Note, that you will encounter strange behavior, if you open multiple Internet "
			+ "Explorer browser instances. That is also why Switch Browser only works with "
			+ "one IE browser at most. For more information see:\n"
			+ "http://selenium-grid.seleniumhq.org/faq.html#i_get_some_strange_errors_when_i_run_multiple_internet_explorer_instances_on_the_same_machine\n\n"

			+ "Optional 'remote_url' is the url for a remote selenium server for example "
			+ "http://127.0.0.1/wd/hub. If you specify a value for remote you can also specify "
			+ "'desired_capabilities' which is a string in the form key1:val1,key2:val2 that "
			+ "will be used to specify desired_capabilities to the remote server. This is "
			+ "useful for doing things like specify a proxy server for internet explorer or for "
			+ "specify browser and os if your using saucelabs.com.\n\n"

			+"Optional 'ff_profile_dir' is the path to the firefox profile dir if you wish to overwrite the default.")
	@ArgumentNames({"url"})
	public String openBrowser(String url) throws Throwable {
		return openBrowser(url, "firefox");
	}

	public String openBrowser(String url, String browserName) throws Throwable {
		return openBrowser(url, browserName, null);
	}

	public String openBrowser(String url, String browserName, String alias)
			throws Throwable {
		return openBrowser(url, browserName, alias, null);
	}

	public String openBrowser(String url, String browserName, String alias,
			String remoteUrl) throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl, null);
	}

	public String openBrowser(String url, String browserName, String alias,
			String remoteUrl, String desiredCapabilities) throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl,
				desiredCapabilities, null);
	}

	public String openBrowser(String url, String browserName, String alias,
			String remoteUrl, String desiredCapabilities, String ffProfileDir)
			throws Throwable {
		try {
			info("browserName: " + browserName);
			if (remoteUrl != null) {
				info(String
						.format("Opening browser '%s' to base url '%s' through remote server at '%s'",
								browserName, url, remoteUrl));
			} else {
				info(String.format("Opening browser '%s' to base url '%s'",
						browserName, url));
			}

			WebDriver webDriver = createWebDriver(browserName,
					desiredCapabilities, ffProfileDir, remoteUrl);
			webDriver.get(url);
			String sessionId = webDriverCache.register(webDriver, alias);
			debug(String.format("Opened browser with session id %s", sessionId));
			return sessionId;
		} catch (Throwable t) {
			if (remoteUrl != null) {
				warn(String
						.format("Opening browser '%s' to base url '%s' through remote server at '%s' failed",
								browserName, url, remoteUrl));
			} else {
				warn(String.format(
						"Opening browser '%s' to base url '%s' failed",
						browserName, url));
			}
			throw new Selenium2LibraryFatalException(t);
		}
	}

	public void switchBrowser(String indexOrAlias) {
		try {
			webDriverCache.switchBrowser(indexOrAlias);
			debug(String.format(
					"Switched to browser with Selenium session id %s",
					webDriverCache.getCurrentSessionId()));
		} catch (Throwable t) {
			throw new Selenium2LibraryFatalException(String.format(
					"No browser with index or alias '%s' found.", indexOrAlias));
		}
	}

	public void closeAllBrowsers() {
		debug("Closing all browsers");
		webDriverCache.closeAll();
	}

	public void closeWindow() {
		webDriverCache.getCurrent().close();
	}

	public List<String> getWindowIdentifiers() {
		return logList(WindowManager.getWindowIds(webDriverCache.getCurrent()),
				"Window Id");
	}

	public List<String> getWindowNames() {
		List<String> windowNames = WindowManager.getWindowNames(webDriverCache
				.getCurrent());
		if (windowNames.size() != 0 && windowNames.get(0).equals("undefined")) {
			windowNames.set(0, "selenium_main_app_window");
		}
		return logList(windowNames, "Window Name");
	}

	public List<String> getWindowTitles() {
		return logList(
				WindowManager.getWindowTitles(webDriverCache.getCurrent()),
				"Window Title");
	}

	public void maximizeBrowserWindow() {
		webDriverCache.getCurrent().manage().window().maximize();
	}

	public void selectFrame(String locator) {
		info(String.format("Selecting frame '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true);
		webDriverCache.getCurrent().switchTo().frame(elements.get(0));
	}

	public void selectWindow() {
		selectWindow(null);
	}

	public void selectWindow(String locator) {
		WindowManager.select(webDriverCache.getCurrent(), locator);
	}

	public void unselectFrame() {
		webDriverCache.getCurrent().switchTo().defaultContent();
	}

	public String getLocation() {
		return webDriverCache.getCurrent().getCurrentUrl();
	}

	public String getSource() {
		return webDriverCache.getCurrent().getPageSource();
	}

	public String getTitle() {
		return webDriverCache.getCurrent().getTitle();
	}

	public void locationShouldBe(String url) {
		String actual = getLocation();
		if (!actual.equals(url)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Location should have been '%s' but was '%s'", url, actual));
		}
		info(String.format("Current location is '%s'.", url));
	}

	public void locationShouldContain(String url) {
		String actual = getLocation();
		if (!actual.contains(url)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Location should have been '%s' but was '%s'", url, actual));
		}
		info(String.format("Current location is '%s'.", url));
	}

	public String logLocation() {
		String actual = getLocation();
		info(actual);
		return actual;
	}

	public String logSource() {
		return logSource("INFO");
	}

	public String logSource(String logLevel) {
		String actual = getSource();
		log(actual, logLevel);
		return actual;
	}

	public String logTitle() {
		String actual = getTitle();
		info(actual);
		return actual;
	}

	public void titleShouldBe(String title) {
		String actual = getTitle();
		if (!actual.equals(title)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Title should have been '%s' but was '%s'", title, actual));
		}
		info(String.format("Page title is '%s'.", title));
	}

	public void goBack() {
		webDriverCache.getCurrent().navigate().back();
	}

	public void goTo(String url) {
		info(String.format("Opening url '%s'", url));
		webDriverCache.getCurrent().get(url);
	}

	public void reloadPage() {
		webDriverCache.getCurrent().navigate().refresh();
	}

	public String getSeleniumSpeed() {
		return Robotframework.secsToTimestr(0);
	}

	public String getSeleniumTimeout() {
		return Robotframework.secsToTimestr(timeout);
	}

	public String getSeleniumImplicitWait() {
		return Robotframework.secsToTimestr(implicitWait);
	}

	public String setSeleniumSpeed(String timestr) {
		return "0s";
	}

	public String setSeleniumTimeout(String timestr) {
		String oldWait = getSeleniumTimeout();
		timeout = Robotframework.timestrToSecs(timestr);

		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache
				.getWebDrivers()) {
			sessionIdAliasWebDriverTuple.webDriver
					.manage()
					.timeouts()
					.setScriptTimeout((int) (timeout * 1000.0),
							TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	public String setSeleniumImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = Robotframework.timestrToSecs(timestr);

		for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache
				.getWebDrivers()) {
			sessionIdAliasWebDriverTuple.webDriver
					.manage()
					.timeouts()
					.implicitlyWait((int) (implicitWait * 1000.0),
							TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	public String setBrowserImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = Robotframework.timestrToSecs(timestr);
		webDriverCache
				.getCurrent()
				.manage()
				.timeouts()
				.implicitlyWait((int) (implicitWait * 1000.0),
						TimeUnit.MILLISECONDS);
		return oldWait;
	}

	public void setRemoteWebDriverProxy(String host, String port) {
		setRemoteWebDriverProxy(host, port, "", "", "", "");
	}

	public void setRemoteWebDriverProxy(String host, String port, String user,
			String password) {
		setRemoteWebDriverProxy(host, port, user, password, "", "");
	}

	public void setRemoteWebDriverProxy(String host, String port, String user,
			String password, String domain, String workstation) {

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

		if (user.length() == 0) {
			// look for a username from properties
			if (System.getProperty("http.proxyHost", "").equals(host)
					&& System.getProperty("http.proxyPort", "").equals(port)) {
				user = System.getProperty("http.proxyUser", "");
			}
			// look for a username from environment
			if (user.length() == 0) {
				if (proxyUrl != null && proxyUrl.getHost().equals(host)
						&& Integer.toString(proxyUrl.getPort()).equals(port)) {
					user = getUserFromURL(proxyUrl);
				}
			}
		}

		if (password.length() == 0) {
			// look for a password from properties
			if (System.getProperty("http.proxyHost", "").equals(host)
					&& System.getProperty("http.proxyPort", "").equals(port)
					&& System.getProperty("http.proxyUser", "").equals(user)) {
				password = System.getProperty("http.proxyPassword", "");
			}
			// look for a password from environment
			if (password.length() == 0) {
				if (proxyUrl != null && proxyUrl.getHost().equals(host)
						&& Integer.toString(proxyUrl.getPort()).equals(port)
						&& getUserFromURL(proxyUrl).equals(user)) {
					password = getPasswordFromURL(proxyUrl);
				}
			}
		}

		if (domain.length() != 0 && workstation.length() == 0) {
			try {
				workstation = InetAddress.getLocalHost().getHostName()
						.split("\\.")[0];
			} catch (UnknownHostException e) {
				warn("No workstation name found");
			}
		}

		remoteWebDriverProxyHost = host;
		remoteWebDriverProxyPort = port;
		remoteWebDriverProxyUser = user;
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

	protected WebDriver createWebDriver(String browserName,
			String desiredCapabilitiesString, String profileDirectory,
			String remoteUrlString) throws MalformedURLException {
		browserName = browserName.toLowerCase().replace(" ", "");
		DesiredCapabilities desiredCapabilities = createDesiredCapabilities(
				browserName, desiredCapabilitiesString, profileDirectory);

		WebDriver webDriver;
		if (remoteUrlString != null && !"False".equals(remoteUrlString)) {
			webDriver = createRemoteWebDriver(desiredCapabilities, new URL(
					remoteUrlString));
		} else {
			webDriver = createLocalWebDriver(browserName, desiredCapabilities);
		}

		webDriver
				.manage()
				.timeouts()
				.setScriptTimeout((int) (timeout * 1000.0),
						TimeUnit.MILLISECONDS);
		webDriver
				.manage()
				.timeouts()
				.implicitlyWait((int) (implicitWait * 1000.0),
						TimeUnit.MILLISECONDS);

		return webDriver;
	}

	protected WebDriver createLocalWebDriver(String browserName,
			DesiredCapabilities desiredCapabilities) {
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			return new FirefoxDriver(desiredCapabilities);

		} else if ("ie".equals(browserName)
				|| "internetexplorer".equals(browserName)) {
			return new InternetExplorerDriver(desiredCapabilities);
		} else if ("gc".equals(browserName) || "chrome".equals(browserName)
				|| "googlechrome".equals(browserName)) {
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
				throw new Selenium2LibraryFatalException("Creating "
						+ browserName + " instance failed.", e);
			}
		} else if ("android".equals(browserName)) {
			return new AndroidDriver(desiredCapabilities);
		}

		throw new Selenium2LibraryFatalException(browserName
				+ " is not a supported browser.");
	}

	protected WebDriver createRemoteWebDriver(
			DesiredCapabilities desiredCapabilities, URL remoteUrl) {
		HttpCommandExecutor httpCommandExecutor = new HttpCommandExecutor(
				remoteUrl);
		setRemoteWebDriverProxy(httpCommandExecutor);
		return new Augmenter().augment(new RemoteWebDriver(httpCommandExecutor,
				desiredCapabilities));
	}

	protected DesiredCapabilities createDesiredCapabilities(String browserName,
			String desiredCapabilitiesString, String profileDirectory) {
		DesiredCapabilities desiredCapabilities;
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.firefox();
			if (profileDirectory != null) {
				FirefoxProfile profile = new FirefoxProfile(new File(
						profileDirectory));
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE,
						profile);
			}
		} else if ("ie".equals(browserName)
				|| "internetexplorer".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.internetExplorer();
		} else if ("gc".equals(browserName) || "chrome".equals(browserName)
				|| "googlechrome".equals(browserName)) {
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
		} else if ("htmlunit".equals(browserName)
				|| "htmlunitwithjs".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.htmlUnit();
		} else {
			throw new Selenium2LibraryFatalException(browserName
					+ " is not a supported browser.");
		}

		if (desiredCapabilitiesString != null
				&& !"None".equals(desiredCapabilitiesString)) {
			for (String capability : desiredCapabilitiesString.split(",")) {
				String[] keyValue = capability.split(":");
				desiredCapabilities.setCapability(keyValue[0], keyValue[1]);
			}
		}
		return desiredCapabilities;
	}

	protected void setRemoteWebDriverProxy(
			HttpCommandExecutor httpCommandExecutor) {
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
			DefaultHttpClient client = (DefaultHttpClient) field
					.get(httpCommandExecutor);

			// set the credentials for the proxy
			AuthScope authScope = new AuthScope(remoteWebDriverProxyHost,
					Integer.parseInt(remoteWebDriverProxyPort));
			if (remoteWebDriverProxyDomain.length() == 0) {
				// BASIC Authentication
				client.getCredentialsProvider().setCredentials(
						authScope,
						new UsernamePasswordCredentials(
								remoteWebDriverProxyUser,
								remoteWebDriverProxyPassword));
			} else {
				// NTLM Authentication
				client.getCredentialsProvider().setCredentials(
						authScope,
						new NTCredentials(remoteWebDriverProxyUser,
								remoteWebDriverProxyPassword,
								remoteWebDriverProxyWorkstation,
								remoteWebDriverProxyDomain));
			}

			// Set the RoutePlanner back to something that handles
			// proxies correctly.
			client.setRoutePlanner(new DefaultHttpRoutePlanner(client
					.getConnectionManager().getSchemeRegistry()));
			HttpHost proxy = new HttpHost(remoteWebDriverProxyHost,
					Integer.parseInt(remoteWebDriverProxyPort));
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		} catch (SecurityException e) {
			throw new Selenium2LibraryFatalException(
					String.format(
							"The SecurityManager does not allow us to lookup to the %s field.",
							fieldName));
		} catch (NoSuchFieldException e) {
			throw new Selenium2LibraryFatalException(
					String.format(
							"The RemoteWebDriver dose not declare the %s field any more.",
							fieldName));
		} catch (IllegalArgumentException e) {
			throw new Selenium2LibraryFatalException(String.format(
					"The field %s does not belong to the given object.",
					fieldName));
		} catch (IllegalAccessException e) {
			throw new Selenium2LibraryFatalException(
					String.format(
							"The SecurityManager does not allow us to access to the %s field.",
							fieldName));
		} catch (ClassCastException e) {
			throw new Selenium2LibraryFatalException(
					String.format("The %s field does not contain a %s.",
							fieldName, className));
		}

	}

	// ##############################
	// Forward Declarations
	// ##############################

	protected abstract List<WebElement> elementFind(String locator,
			boolean firstOnly, boolean required);

	protected abstract void log(String msg, String logLevel);

	protected abstract void trace(String msg);

	protected abstract void debug(String msg);

	protected abstract void info(String msg);

	protected abstract void html(String msg);

	protected abstract void warn(String msg);

	protected abstract List<String> logList(List<String> items);

	protected abstract List<String> logList(List<String> items, String what);

	protected abstract File getLogDir();
}

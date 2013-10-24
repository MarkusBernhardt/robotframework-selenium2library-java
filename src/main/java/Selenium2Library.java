import java.io.File;
import java.util.ResourceBundle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;

import com.github.markusbernhardt.selenium2library.keywords.BrowserManagement;
import com.github.markusbernhardt.selenium2library.keywords.Cookie;
import com.github.markusbernhardt.selenium2library.keywords.Element;
import com.github.markusbernhardt.selenium2library.keywords.FormElement;
import com.github.markusbernhardt.selenium2library.keywords.JavaScript;
import com.github.markusbernhardt.selenium2library.keywords.Logging;
import com.github.markusbernhardt.selenium2library.keywords.RunOnFailure;
import com.github.markusbernhardt.selenium2library.keywords.Screenshot;
import com.github.markusbernhardt.selenium2library.keywords.SelectElement;
import com.github.markusbernhardt.selenium2library.keywords.TableElement;
import com.github.markusbernhardt.selenium2library.keywords.Waiting;
import com.github.markusbernhardt.selenium2library.utils.Javadoc2Libdoc;

/**
 * Selenium2Library is a web testing library for the Robot Framework and was
 * originally written in Python. This is the Java port of the Selenium 2
 * (WebDriver) Python library for Robot Framework. It uses the Selenium 2
 * (WebDriver) libraries internally to control a web browser. See <a
 * href="http://seleniumhq.org/docs/03_webdriver.html">WebDriver</a> for more
 * information on Selenium 2 and WebDriver. It runs tests in a real browser
 * instance and should work with most modern browsers and can be used with the
 * Jython interpreter or any other Java application.<br>
 * <br>
 * <font size="+1"><b>Before running tests</b></font><br>
 * Prior to running test cases using Selenium2Library, the library must be
 * imported into your Robot Framework test suite (see importing section), and
 * the `Open Browser` keyword must be used to open a browser to the desired
 * location.<br>
 * <br>
 * <font size="+1"><b>Locating elements</b></font><br>
 * All keywords in Selenium2Library that need to find an element on the page
 * take an locator argument.<br>
 * <br>
 * <b>Key attributes</b><br>
 * By default, when a locator value is provided, it is matched against the key
 * attributes of the particular element type. The attributes <i>id</i> and
 * <i>name</i> are key attributes to all elements.<br>
 * <br>
 * List of key attributes:
 * <table border="1" cellspacing="0">
 * <tr>
 * <td><b>Element Type</b></td>
 * <td><b>Key Attributes</b></td>
 * </tr>
 * <tr>
 * <td>A</td>
 * <td>@id,@name,@href,text</td>
 * </tr>
 * <tr>
 * <td>IMG</td>
 * <td>@id,@name,@src,@alt</td>
 * </tr>
 * <tr>
 * <td>INPUT</td>
 * <td>@id,@name,@value,@src</td>
 * </tr>
 * <tr>
 * <td>BUTTON</td>
 * <td>@id,@name,@value,text</td>
 * </tr>
 * <tr>
 * <td>*</td>
 * <td>@id,@name</td>
 * </tr>
 * </table>
 * <br>
 * Example:
 * <table border="1" cellspacing="0">
 * <tr>
 * <td>Click Element</td>
 * <td>my_element</td>
 * </tr>
 * </table>
 * <br>
 * <b>Locator strategies</b><br>
 * It is also possible to specify the approach Selenium2Library should take to
 * find an element by specifying a locator strategy with a locator prefix.<br>
 * <br>
 * Supported strategies are:
 * <table border="1" cellspacing="0">
 * <tr>
 * <td><b>Strategy</b></td>
 * <td><b>Example</b></td>
 * <td><b>Description</b></td>
 * </tr>
 * <tr>
 * <td>identifier</td>
 * <td>Click Element | identifier=my_element</td>
 * <td>Matches by @id or @name attribute</td>
 * </tr>
 * <tr>
 * <td>id</td>
 * <td>Click Element | id=my_element</td>
 * <td>Matches by @id attribute</td>
 * </tr>
 * <tr>
 * <td>name</td>
 * <td>Click Element | name=my_element</td>
 * <td>Matches by @name attribute</td>
 * </tr>
 * <tr>
 * <td>xpath</td>
 * <td>Click Element | xpath=//div[@id='my_element']</td>
 * <td>Matches by arbitrary XPath expression</td>
 * </tr>
 * <tr>
 * <td>dom</td>
 * <td>Click Element | dom=document.images[56]</td>
 * <td>Matches by arbitrary DOM expression</td>
 * </tr>
 * <tr>
 * <td>link</td>
 * <td>Click Element | link=My Link</td>
 * <td>Matches by the link text</td>
 * </tr>
 * <tr>
 * <td>css</td>
 * <td>Click Element | css=div.my_class</td>
 * <td>Matches by CSS selector</td>
 * </tr>
 * <tr>
 * <td>jquery</td>
 * <td>Click Element | jquery=div.my_class</td>
 * <td>Matches by jQuery/sizzle selector</td>
 * </tr>
 * <tr>
 * <td>sizzle</td>
 * <td>Click Element | sizzle=div.my_class</td>
 * <td>Matches by jQuery/sizzle selector</td>
 * </tr>
 * <tr>
 * <td>tag</td>
 * <td>Click Element | tag=div</td>
 * <td>Matches by HTML tag name</td>
 * </tr>
 * </table>
 * <br>
 * <b>Locating tables</b><br>
 * Table related keywords, such as `Table Should Contain`, work differently. By
 * default, when a table locator value is provided, it will search for a table
 * with the specified id attribute.<br>
 * <br>
 * Example:<br>
 * <table border="1" cellspacing="0">
 * <tr>
 * <td>Table Should Contain</td>
 * <td>my_table</td>
 * <td>text</td>
 * </tr>
 * </table>
 * <br>
 * More complex table locator strategies:
 * <table border="1" cellspacing="0">
 * <tr>
 * <td><b>Strategy</b></td>
 * <td><b>Example</b></td>
 * <td><b>Description</b></td>
 * </tr>
 * <td>xpath</td>
 * <td>Table Should Contain | xpath=//table/[@name="my_table"] | text</td>
 * <td>Matches by arbitrary XPath expression</td>
 * </tr>
 * <td>css</td>
 * <td>Table Should Contain | css=table.my_class | text</td>
 * <td>Matches by CSS selector</td>
 * </tr>
 * </table>
 * <br>
 * <b>Custom location strategies</b><br>
 * It is also possible to register custom location strategies. See `Add Location
 * Strategy` for details about custom location strategies.<br>
 * <br>
 * Example:
 * <table border="1" cellspacing="0">
 * <tr>
 * <td>Add Location Strategy</td>
 * <td>custom</td>
 * <td>return window.document.getElementById(arguments[0]);</td>
 * </tr>
 * <tr>
 * <td>Input Text</td>
 * <td>custom=firstName</td>
 * <td>Max</td>
 * </tr>
 * </table>
 * <br>
 * <font size="+1"><b>Timeouts</b></font><br>
 * There are several Wait ... keywords that take <b>timeout</b> as an argument.
 * All of these timeout arguments are optional. The timeout used by all of them
 * can be set globally using the `Set Selenium Timeout keyword`.<br>
 * <br>
 * All timeouts can be given as numbers considered seconds (e.g. 0.5 or 42) or
 * in Robot Framework's time syntax (e.g. '1.5 seconds' or '1 min 30 s'). See <a
 * href=
 * "http://robotframework.googlecode.com/svn/trunk/doc/userguide/RobotFrameworkUserGuide.html#time-format"
 * >Time Format</a> for details about the time syntax.<br>
 * <br>
 * <font size="+1"><b>Log Level</b></font><br>
 * There are several keywords that take <b>timeout</b> as an argument. All of
 * these timeout arguments are optional. The default is usually INFO.<br>
 * <br>
 * List of log levels:
 * <table border="1" cellspacing="0">
 * <tr>
 * <td><b>Log Level</b></td>
 * <td><b>Description</b></td>
 * </tr>
 * <tr>
 * <td>DEBUG</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>INFO</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>HTML</td>
 * <td>Same as INFO, but message is in HTML format</td>
 * </tr>
 * <tr>
 * <td>TRACE</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>WARN</td>
 * <td></td>
 * </tr>
 * </table>
 */
public class Selenium2Library extends AnnotationLibrary {

	/**
	 * The list of keyword patterns for the AnnotationLibrary
	 */
	public static final String KEYWORD_PATTERN = "com/github/markusbernhardt/selenium2library/keywords/**/*.class";

	/**
	 * The javadoc to libdoc converter
	 */
	public static final Javadoc2Libdoc JAVADOC_2_LIBDOC = new Javadoc2Libdoc(Selenium2Library.class);

	/**
	 * The library documentation is written in HTML
	 */
	public static final String ROBOT_LIBRARY_DOC_FORMAT = "HTML";

	/**
	 * The scope of this library is global.
	 */
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	/**
	 * The actual version of this library. Loaded from maven project.
	 */
	public static final String ROBOT_LIBRARY_VERSION = loadRobotLibraryVersion();

	private static String loadRobotLibraryVersion() {
		try {
			return ResourceBundle.getBundle(Selenium2Library.class.getCanonicalName().replace(".", File.separator))
					.getString("version");
		} catch (RuntimeException e) {
			return "unknown";
		}
	}

	public Selenium2Library() {
		this("5.0");
	}

	public Selenium2Library(String timeout) {
		this(timeout, "0.0");
	}

	public Selenium2Library(String timeout, String implicitWait) {
		this(timeout, implicitWait, "Capture Page Screenshot");
	}

	/**
	 * Selenium2Library can be imported with optional arguments.<br>
	 * <br>
	 * <b>timeout</b> is the default timeout used to wait for all waiting
	 * actions. It can be changed later with `Set Selenium Timeout`.<br>
	 * <br>
	 * <b>implicitWait</b> is the implicit timeout that Selenium waits, when
	 * looking for elements. It can be changed later with `Set Selenium Implicit
	 * Wait`. See <a
	 * href="http://docs.seleniumhq.org/docs/04_webdriver_advanced.jsp"
	 * >WebDriver: Advanced Usage</a> of the SeleniumHQ documentation for
	 * details about WebDriver's implicit wait functionality.<br>
	 * <br>
	 * <b>runOnFailure</b> specifies the name of a keyword (from any available
	 * libraries) to execute when a Selenium2Library keyword fails. By default
	 * `Capture Page Screenshot` will be used to take a screenshot of the
	 * current page. Using the value \"Nothing\" will disable this feature
	 * altogether. See `Register Keyword To Run On Failure` keyword for details
	 * about this functionality.<br>
	 * <br>
	 * Examples:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Library</td>
	 * <td>Selenium2Library</td>
	 * <td></td>
	 * <td></td>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>Library</td>
	 * <td>Selenium2Library</td>
	 * <td>15</td>
	 * <td></td>
	 * <td></td>
	 * <td># Sets timeout to 15 seconds</td>
	 * </tr>
	 * <tr>
	 * <td>Library</td>
	 * <td>Selenium2Library</td>
	 * <td>0</td>
	 * <td>5</td>
	 * <td></td>
	 * <td># Sets timeout to 0 seconds and implicitWait to 5 seconds</td>
	 * </tr>
	 * <tr>
	 * <td>Library</td>
	 * <td>Selenium2Library</td>
	 * <td>0</td>
	 * <td>5</td>
	 * <td>Log Source</td>
	 * <td># Sets timeout to 0 seconds, implicitWait to 5 seconds and runs `Log
	 * Source` on failure</td>
	 * </tr>
	 * <tr>
	 * <td>Library</td>
	 * <td>Selenium2Library</td>
	 * <td>0</td>
	 * <td>5</td>
	 * <td>Nothing</td>
	 * <td># Sets timeout to 0 seconds, implicitWait to 5 seconds and does
	 * nothing on failure</td>
	 * </tr>
	 * </table>
	 * 
	 * @param timeout
	 *            Default=5.0. Optional custom timeout.
	 * @param implicitWait
	 *            Default=0.0. Optional custom implicit wait time.
	 * @param runOnFailure
	 *            Default=Capture Page Screenshot. Optional custom keyword to
	 *            run on failure.
	 */
	public Selenium2Library(String timeout, String implicitWait, String runOnFailure) {
		super();
		addKeywordPattern(KEYWORD_PATTERN);
		createKeywordFactory(); // => init annotations
		browserManagement.setSeleniumTimeout(timeout);
		browserManagement.setSeleniumImplicitWait(implicitWait);
		this.runOnFailure.registerKeywordToRunOnFailure(runOnFailure);
	}

	// ##############################
	// Autowired References
	// ##############################

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	/**
	 * Instantiated Cookie keyword bean
	 */
	@Autowired
	protected Cookie cookie;

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

	/**
	 * Instantiated FormElement keyword bean
	 */
	@Autowired
	protected FormElement formElement;

	/**
	 * Instantiated JavaScript keyword bean
	 */
	@Autowired
	protected JavaScript javaScript;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	/**
	 * Instantiated RunOnFailure keyword bean
	 */
	@Autowired
	protected RunOnFailure runOnFailure;

	/**
	 * Instantiated Screenshot keyword bean
	 */
	@Autowired
	protected Screenshot screenshot;

	/**
	 * Instantiated SelectElement keyword bean
	 */
	@Autowired
	protected SelectElement selectElement;

	/**
	 * Instantiated TableElement keyword bean
	 */
	@Autowired
	protected TableElement tableElement;

	/**
	 * Instantiated Waiting keyword bean
	 */
	@Autowired
	protected Waiting waiting;

	// ##############################
	// Getter / Setter
	// ##############################

	public BrowserManagement getBrowserManagement() {
		return browserManagement;
	}

	public Cookie getCookie() {
		return cookie;
	}

	public Element getElement() {
		return element;
	}

	public FormElement getFormElement() {
		return formElement;
	}

	public JavaScript getJavaScript() {
		return javaScript;
	}

	public Logging getLogging() {
		return logging;
	}

	public RunOnFailure getRunOnFailure() {
		return runOnFailure;
	}

	public Screenshot getScreenshot() {
		return screenshot;
	}

	public SelectElement getSelectElement() {
		return selectElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}

	public Waiting getWaiting() {
		return waiting;
	}

	// ##############################
	// Public Methods
	// ##############################

	@Override
	public Object runKeyword(String keywordName, Object[] args) {
		return super.runKeyword(keywordName, toStrings(args));
	}

	@Override
	public String getKeywordDocumentation(String keywordName) {
		String keywordDocumentation = JAVADOC_2_LIBDOC.getKeywordDocumentation(keywordName);
		if (keywordDocumentation == null) {
			try {
				return super.getKeywordDocumentation(keywordName);
			} catch (NullPointerException e) {
				return "";
			}
		}
		return keywordDocumentation;
	}

	public static Selenium2Library getLibraryInstance() throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
		engine.put("library", "Selenium2Library");
		engine.eval("from robot.libraries.BuiltIn import BuiltIn");
		engine.eval("instance = BuiltIn().get_library_instance(library)");
		return (Selenium2Library) engine.get("instance");
	}

	// ##############################
	// Internal Methods
	// ##############################

	/**
	 * Convert all arguments in the object array to string
	 */
	protected Object[] toStrings(Object[] args) {
		Object[] newArgs = new Object[args.length];
		for (int i = 0; i < newArgs.length; i++) {
			if (args[i].getClass().isArray()) {
				newArgs[i] = args[i];
			} else {
				newArgs[i] = args[i].toString();
			}
		}
		return newArgs;
	}
}
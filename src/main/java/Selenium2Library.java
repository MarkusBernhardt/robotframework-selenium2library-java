import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

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
import com.github.markusbernhardt.xmldoclet.xjc.AnnotationInstance;
import com.github.markusbernhardt.xmldoclet.xjc.Class;
import com.github.markusbernhardt.xmldoclet.xjc.Method;
import com.github.markusbernhardt.xmldoclet.xjc.MethodParameter;
import com.github.markusbernhardt.xmldoclet.xjc.ObjectFactory;
import com.github.markusbernhardt.xmldoclet.xjc.Package;
import com.github.markusbernhardt.xmldoclet.xjc.Root;
import com.github.markusbernhardt.xmldoclet.xjc.TagInfo;

public class Selenium2Library extends AnnotationLibrary {

	/**
	 * The list of keyword patterns for the AnnotationLibrary
	 */
	public static final String KEYWORD_PATTERN = "com/github/markusbernhardt/selenium2library/keywords/**/*.class";

	/**
	 * The root node of the parsed javadoc
	 */
	public static final Map<String, String> KEYWORD_DOCUMENTATION_MAP = loadKeywordDocumentationMap();

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

	private static Map<String, String> loadKeywordDocumentationMap() {
		Map<String, String> keywordDocumentation = new HashMap<String, String>();
		Root root = loadJavadocRoot();
		for (Package packageNode : root.getPackage()) {
			for (Class classNode : packageNode.getClazz()) {
				for (Method methodNode : classNode.getMethod()) {
					for (AnnotationInstance annotationInstanceNode : methodNode.getAnnotation()) {
						if (annotationInstanceNode.getName().equals("RobotKeyword")) {
							keywordDocumentation.put(methodNode.getName(), formatComment(methodNode));
							break;
						}
					}
				}
			}
		}
		return keywordDocumentation;
	}

	private static Root loadJavadocRoot() {
		try {
			JAXBContext context = JAXBContext.newInstance(Root.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Root) unmarshaller.unmarshal(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(Selenium2Library.class.getName().replace(".", File.separator) + ".javadoc"));
		} catch (JAXBException e) {
			return new ObjectFactory().createRoot();
		}
	}

	private static String formatComment(Method methodNode) {
		StringBuilder stringBuilder = new StringBuilder();

		if (methodNode.getComment() != null) {
			stringBuilder.append(methodNode.getComment());
		}

		if (methodNode.getParameter().size() != 0) {
			stringBuilder.append("<p>");
			stringBuilder.append("<b>Parameters:</b>");
			stringBuilder.append("<blockquote>");
			for (MethodParameter methodParameter : methodNode.getParameter()) {
				stringBuilder.append("<b>" + methodParameter.getName() + "</b>");
				String paramTextStart = methodParameter.getName() + "\n";
				for(TagInfo tagInfo : methodNode.getTag()){
					if(!tagInfo.getName().equals("@param")) {
						continue;
					}
					if(!tagInfo.getText().startsWith(paramTextStart)) {
						continue;
					}		
					stringBuilder.append("  ");
					stringBuilder.append(tagInfo.getText().substring(paramTextStart.length()).trim());
				}
				stringBuilder.append("<br>");
			}
			stringBuilder.append("</blockquote>");
		}

		return stringBuilder.toString();
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

	public Selenium2Library(String timeout, String implicitWait, String runOnFailureKeyword) {
		super();
		addKeywordPattern(KEYWORD_PATTERN);
		createKeywordFactory(); // => init annotations
		browserManagement.setSeleniumTimeout(timeout);
		browserManagement.setSeleniumImplicitWait(implicitWait);
		runOnFailure.registerKeywordToRunOnFailure(runOnFailureKeyword);
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
		String keywordDocumentation = KEYWORD_DOCUMENTATION_MAP.get(keywordName);
		if (keywordDocumentation == null) {
			keywordDocumentation = "";
		}
		return keywordDocumentation;
		/*
		 * if (keywordName.equals("__intro__")) { return this.docIntro; } else
		 * if (keywordName.equals("__init__")) { return this.docInit; } try {
		 * String keywordDocumentation =
		 * super.getKeywordDocumentation(keywordName); if
		 * (keywordDocumentation.length() == 0) {
		 * System.out.println(KEYWORD_DOCUMENTATION_MAP.get(keywordName)); }
		 * return keywordDocumentation; } catch (NullPointerException e) {
		 * e.printStackTrace(); } return "";
		 */
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

	protected final String docIntro = "Selenium2Library is a web testing library for Robot Framework.\n\n"

	+ "It uses the Selenium 2 (WebDriver) libraries internally to control a web browser. See "
			+ "http://seleniumhq.org/docs/03_webdriver.html for more information on Selenium 2 and " + "WebDriver.\n\n"

			+ "Selenium2Library runs tests in a real browser instance. It should work in most modern "
			+ "browsers and can be used with both Python and Jython interpreters.\n\n"

			+ "*Before running tests*\n\n"

			+ "Prior to running test cases using Selenium2Library, Selenium2Library must be imported "
			+ "into your Robot test suite (see importing section), and the `Open Browser` keyword must "
			+ "be used to open a browser to the desired location.\n\n"

			+ "*Locating elements*\n\n"

			+ "All keywords in Selenium2Library that need to find an element on the page take an "
			+ "argument, locator. By default, when a locator value is provided, it is matched against "
			+ "the key attributes of the particular element type. For example, id and name are key "
			+ "attributes to all elements, and locating elements is easy using just the id as a locator. "
			+ "For example::\n\n"

			+ "| Click Element | my_element |\n\n"

			+ "It is also possible to specify the approach Selenium2Library should take to find an element "
			+ "by specifying a lookup strategy with a locator prefix. Supported strategies are:\n\n"

			+ "| Strategy | Example | Description |\n"
			+ "| identifier | Click Element|identifier=my_element | Matches by @id or @name " + "attribute |\n"
			+ "| id | Click Element|id=my_element | Matches by @id attribute |\n"
			+ "| name | Click Element|name=my_element | Matches by @name attribute |\n"
			+ "| xpath | Click Element|xpath=//div[@id='my_element'] | Matches with arbitrary XPath "
			+ "expression |\n"
			+ "| dom | Click Element|dom=document.images[56] | Matches with arbitrary DOM express |\n"
			+ "| link | Click Element|link=My Link | Matches anchor elements by their link text |\n"
			+ "| css | Click Element|css=div.my_class | Matches by CSS selector |\n"
			+ "| jquery | Click Element|jquery=div.my_class | Matches by jQuery/sizzle selector |\n"
			+ "| sizzle | Click Element|sizzle=div.my_class | Matches by jQuery/sizzle selector |\n"
			+ "| tag | Click Element|tag=div | Matches by HTML tag name |\n\n"

			+ "Table related keywords, such as `Table Should Contain`, work differently. By default, "
			+ "when a table locator value is provided, it will search for a table with the specified "
			+ "id attribute. For example:\n\n"

			+ "| Table Should Contain | my_table text |\n\n"

			+ "More complex table lookup strategies are also supported:\n" + "| Strategy | Example | Description |\n"
			+ "| css | Table Should Contain|css=table.my_class|text | Matches by @id or @name " + "attribute |\n"
			+ "| xpath | Table Should Contain|xpath=//table/[@name=\"my_table\"]|text | Matches "
			+ "by @id or @name attribute |\n\n"

			+ "*Timeouts*\n\n"

			+ "There are several Wait ... keywords that take timeout as an argument. All of these timeout "
			+ "arguments are optional. The timeout used by all of them can be set globally using the `Set "
			+ "Selenium Timeout keyword`.\n\n"

			+ "All timeouts can be given as numbers considered seconds (e.g. 0.5 or 42) or in Robot "
			+ "Framework's time syntax (e.g. '1.5 seconds' or '1 min 30 s'). For more information about the "
			+ "time syntax see:\n"
			+ "http://robotframework.googlecode.com/svn/trunk/doc/userguide/RobotFrameworkUserGuide.html"
			+ "#time-format.\n\n";

	protected final String docInit = "Selenium2Library can be imported with optional arguments.\n\n"

			+ "`timeout` is the default timeout used to wait for all waiting actions. "
			+ "It can be later set with `Set Selenium Timeout`.\n\n"

			+ "'implicit_wait' is the implicit timeout that Selenium waits when "
			+ "looking for elements. "
			+ "It can be later set with `Set Selenium Implicit Wait`. "
			+ "See `WebDriver: Advanced Usage`__ section of the SeleniumHQ documentation "
			+ "for more information about WebDriver's implicit wait functionality.\n\n"

			+ "__ http://seleniumhq.org/docs/04_webdriver_advanced.html#explicit-and-implicit-waits\n\n"

			+ "`run_on_failure` specifies the name of a keyword (from any available "
			+ "libraries) to execute when a Selenium2Library keyword fails. By default "
			+ "`Capture Page Screenshot` will be used to take a screenshot of the current page. "
			+ "Using the value \"Nothing\" will disable this feature altogether. See "
			+ "`Register Keyword To Run On Failure` keyword for more information about this "
			+ "functionality.\n\n"

			+ "Examples:\n"
			+ "| Library `|` Selenium2Library `|` 15                                    | # Sets default timeout to 15 seconds                                       |\n"
			+ "| Library `|` Selenium2Library `|` 0 `|` 5                               | # Sets default timeout to 0 seconds and default implicit_wait to 5 seconds |\n"
			+ "| Library `|` Selenium2Library `|` 0 `|` 5 `|` run_on_failure=Log Source | # Sets default timeout to 0 seconds, default implicit_wait to 5 seconds and runs `Log Source` on failure|\n"
			+ "| Library `|` Selenium2Library `|` 0 `|` 5 `|` run_on_failure=Nothing    | # Sets default timeout to 0 seconds, default implicit_wait to 5 seconds and does nothing on failure|\n";

}

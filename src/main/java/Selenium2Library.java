import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.robotframework.javalib.library.AnnotationLibrary;

public class Selenium2Library extends
	org.robotframework.javalib.library.AnnotationLibrary {
	
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
    public static Selenium2Library instance;

	/**
	 * The actual version of this library. Loaded from maven project.
	 */
	public static String ROBOT_LIBRARY_VERSION;

	static {
		/**
		 * Load the version from file
		 */
		try {
			ROBOT_LIBRARY_VERSION = ResourceBundle.getBundle(
					Selenium2Library.class.getCanonicalName().replace(".",
							File.separator)).getString("version");
		} catch (RuntimeException e) {
			ROBOT_LIBRARY_VERSION = "unknown";
		}
	}

    static List<String> keywordPatterns = new ArrayList<String>() {{
        add("com/github/markusbernhardt/selenium2library/keywords/**/*.class");
      }};
      
	private final AnnotationLibrary annotationLibrary = new AnnotationLibrary(keywordPatterns);
	
	public Selenium2Library() {
		super(keywordPatterns);
        instance = this;
	}
	
	public Object runKeyword(String keywordName, Object[] args) {
        return annotationLibrary.runKeyword(keywordName, toStrings(args));
    }

    public String[] getKeywordArguments(String keywordName) {
        return annotationLibrary.getKeywordArguments(keywordName);
    }

    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__")) {
            return this.docIntro;
        }
        return annotationLibrary.getKeywordDocumentation(keywordName);
    }

    public String[] getKeywordNames() {
        return annotationLibrary.getKeywordNames();
    }

    private Object[] toStrings(Object[] args) {
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
    
    private final String docIntro = "Selenium2Library is a web testing library for Robot Framework.\n\n"

		+ "It uses the Selenium 2 (WebDriver) libraries internally to control a web browser. See "
		+ "http://seleniumhq.org/docs/03_webdriver.html for more information on Selenium 2 and "
		+ "WebDriver.\n\n"
		
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
		+ "| identifier | Click Element|identifier=my_element | Matches by @id or @name "
		+ "attribute |\n"
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
		
		+ "More complex table lookup strategies are also supported:\n"
		+ "| Strategy | Example | Description |\n"
		+ "| css | Table Should Contain|css=table.my_class|text | Matches by @id or @name "
		+ "attribute |\n"
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
		+ "#time-format.\n\n"
		
		+ "*Importing*\n\n"
		
		+ "| Arguments | Documentation |\n"
		+ "| timeout=5.0, implicit_wait=0.0, run_on_failure=Capture Page Screenshot  | Selenium2Library "
		+ "can be imported with optional arguments. | \n\n"
		
		+ "_timeout_ is the default timeout used to wait for all waiting actions. It can be later set "
		+ "with `Set Selenium Timeout`.\n\n"
		
		+ "_implicit_wait_ is the implicit timeout that Selenium waits when looking for elements. It "
		+ "can be later set with `Set Selenium Implicit Wait`. See WebDriver: Advanced Usage__ section of "
		+ "the SeleniumHQ documentation for more information about WebDriver's implicit wait "
		+ "functionality.\n"
		+ "__ http://seleniumhq.org/docs/04_webdriver_advanced.html#explicit-and-implicit-waits \n\n"
		
		+ "_run_on_failure_ specifies the name of a keyword (from any available libraries) to execute "
		+ "when a Selenium2Library keyword fails. By default `Capture Page Screenshot` will be used to take "
		+ "a screenshot of the current page. Using the value \"Nothing\" will disable this feature altogether. "
		+ "See `Register Keyword To Run On Failure` keyword for more information about this functionality.\n\n"
		
		+ "Examples:\n"
		+ "| Library | Selenium2Library|15|# Sets default timeout to 15 seconds |\n"
		+ "| Library | Selenium2Library|0|5 | # Sets default timeout to 0 seconds and default "
		+ "implicit_wait to 5 seconds |\n"
		+ "| Library | Selenium2Library|5|run_on_failure=Log Source | # Sets default timeout to 5 "
		+ "seconds and runs Log Source on failure |\n"
		+ "| Library | Selenium2Library|implicit_wait=5|run_on_failure=Log Source| # Sets default "
		+ "implicit_wait to 5 seconds and runs Log Source on failure |\n"
		+ "| Library | Selenium2Library|timeout=10|run_on_failure=Nothing | # Sets default timeout "
		+ "to 10 seconds and does nothing on failure |\n |\n";
}

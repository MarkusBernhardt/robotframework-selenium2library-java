import java.util.ArrayList;
import java.util.List;

import org.robotframework.javalib.library.AnnotationLibrary;

public class Selenium2Library extends
	org.robotframework.javalib.library.AnnotationLibrary {
	
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
    public static Selenium2Library instance;

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
            return "";
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
}

public class SeleniumLibrary extends com.github.markusbernhardt.seleniumlibrary.SeleniumLibrary {

	public SeleniumLibrary(String timeout, String implicitWait, String runOnFailure) {
		super(timeout, implicitWait, runOnFailure);
	}

	public SeleniumLibrary(String timeout, String implicitWait) {
		super(timeout, implicitWait);
	}

	public SeleniumLibrary(String timeout) {
		super(timeout);
	}

	public SeleniumLibrary() {
		super();
	}

}

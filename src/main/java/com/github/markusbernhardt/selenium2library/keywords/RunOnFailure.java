package com.github.markusbernhardt.selenium2library.keywords;

import org.python.util.PythonInterpreter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;

@RobotKeywords
public class RunOnFailure extends RunOnFailureKeywordsAdapter {

	/**
	 * The keyword to run an failure
	 */
	protected String runOnFailureKeyword = "Capture Page Screenshot";

	/**
	 * Only run keyword on failure if true
	 */
	protected boolean runningOnFailureRoutine;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	// ##############################
	// Keywords
	// ##############################

	/**
	 * Sets the actual and returns the previous keyword to execute when a
	 * Selenium2Library keyword fails.<br>
	 * <br>
	 * The <b>keyword</b> is the name of a keyword (from any available
	 * libraries) that will be executed, if a Selenium2Library keyword fails. It
	 * is not possible to use a keyword that requires arguments. Using the value
	 * <b>Nothing</b> will disable this feature altogether.<br>
	 * <br>
	 * The initial keyword to use is set at importing the library and the
	 * keyword that is used by default is `Capture Page Screenshot`. Taking a
	 * screenshot when something failed is a very useful feature, but notice
	 * that it can slow down the execution.<br>
	 * <br>
	 * This keyword returns the name of the previously registered failure
	 * keyword. It can be used to restore the original value later.<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Register Keyword To Run On Failure</td>
	 * <td>Log Source</td>
	 * <td></td>
	 * <td># Run `Log Source` on failure.</td>
	 * </tr>
	 * <tr>
	 * <td>${previous kw}=</td>
	 * <td>Register Keyword To Run On Failure</td>
	 * <td>Nothing</td>
	 * <td># Disable run-on-failure functionality and stors the previous kw name
	 * in a variable.</td>
	 * </tr>
	 * <tr>
	 * <td>Register Keyword To Run On Failure</td>
	 * <td>${previous kw}</td>
	 * <td></td>
	 * <td># Restore to the previous keyword.</td>
	 * </tr>
	 * </table>
	 * 
	 * @param keyword
	 *            The keyword to execute on failure
	 * @return The previous keyword
	 */
	@RobotKeyword
	@ArgumentNames({ "keyword" })
	public String registerKeywordToRunOnFailure(String keyword) {
		String oldKeyword = runOnFailureKeyword;
		String oldKeywordText = oldKeyword != null ? oldKeyword : "No keyword";

		String newKeyword = !keyword.trim().toLowerCase().equals("nothing") ? keyword : null;
		String newKeywordText = newKeyword != null ? newKeyword : "No keyword";

		runOnFailureKeyword = newKeyword;
		logging.info(String.format("%s will be run on failure.", newKeywordText));

		return oldKeywordText;
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected static ThreadLocal<PythonInterpreter> runOnFailurePythonInterpreter = new ThreadLocal<PythonInterpreter>() {

		@Override
		protected PythonInterpreter initialValue() {
			PythonInterpreter pythonInterpreter = new PythonInterpreter();
			pythonInterpreter.exec("from robot.libraries import BuiltIn; BUILTIN = BuiltIn.BuiltIn();");
			return pythonInterpreter;
		}
	};

	public void runOnFailure() {
		if (runOnFailureKeyword == null) {
			return;
		}
		if (runningOnFailureRoutine) {
			return;
		}
		runningOnFailureRoutine = true;
		try {
			runOnFailurePythonInterpreter.get().exec(
					String.format("BUILTIN.run_keyword('%s')",
							runOnFailureKeyword.replace("'", "\\'").replace("\n", "\\n")));
		} catch (RuntimeException r) {
			logging.warn(String.format("Keyword '%s' could not be run on failure%s", runOnFailureKeyword,
					r.getMessage() != null ? " '" + r.getMessage() + "'" : ""));
		} finally {
			runningOnFailureRoutine = false;
		}
	}

}

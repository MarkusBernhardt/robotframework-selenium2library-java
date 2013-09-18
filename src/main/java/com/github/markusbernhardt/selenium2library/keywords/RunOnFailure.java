package com.github.markusbernhardt.selenium2library.keywords;

import org.python.util.PythonInterpreter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public abstract class RunOnFailure extends Logging {

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Sets the keyword to execute when a Selenium2Library keyword fails.\n\n"

			+ "_keywordname_ is the name of a keyword (from any available libraries) that will be "
			+ "executed if a Selenium2Library keyword fails. It is not possible to use a keyword "
			+ "that requires arguments. Using the value _Nothing_ will disable this feature "
			+ "altogether.\n\n"

			+ "The initial keyword to use is set in importing, and the keyword that is used by "
			+ "default is `Capture Page Screenshot`. Taking a screenshot when something failed is "
			+ "a very useful feature, but notice that it can slow down the execution.\n\n"

			+ "This keyword returns the name of the previously registered failure keyword. It can "
			+ "be used to restore the original value later.\n\n"

			+ "Example:\n"
			+ "| Register Keyword To Run On Failure | Log Source | # Run Log Source on failure. |\n"
			+ "| ${previous kw}= | Register Keyword To Run On Failure | Nothing | # Disables "
			+ "run-on-failure functionality and stores the previous kw name in a variable. |\n"
			+ "| Register Keyword To Run On Failure | ${previous kw} | # Restore to the previous "
			+ "keyword. |\n\n"

			+ "This run-on-failure functionality only works when running tests on Python/Jython 2.4 "
			+ "or newer and it does not work on IronPython at all.")
	@ArgumentNames({ "keyword" })
	public String registerKeywordToRunOnFailure(String keyword) {
		String oldKeyword = runOnFailureKeyword;
		String oldKeywordText = oldKeyword != null ? oldKeyword : "No keyword";

		String newKeyword = !keyword.trim().toLowerCase().equals("nothing") ? keyword
				: null;
		String newKeywordText = newKeyword != null ? newKeyword : "No keyword";

		runOnFailureKeyword = newKeyword;
		info(String.format("%s will be run on failure.", newKeywordText));

		return oldKeywordText;
	}

	// ##############################
	// Internal Methods
	// ##############################


	protected static ThreadLocal<PythonInterpreter> runOnFailurePythonInterpreter = new ThreadLocal<PythonInterpreter>() {

		@Override
		protected PythonInterpreter initialValue() {
			PythonInterpreter pythonInterpreter = new PythonInterpreter();
			pythonInterpreter
					.exec("from robot.libraries import BuiltIn; BUILTIN = BuiltIn.BuiltIn();");
			return pythonInterpreter;
		}
	};

	protected void runOnFailure() {
		if (runOnFailureKeyword == null) {
			return;
		}
		if (runningOnFailureRoutine) {
			return;
		}
		runningOnFailureRoutine = true;
		try {
			runOnFailurePythonInterpreter.get().exec(
					String.format(
							"BUILTIN.run_keyword('%s')",
							runOnFailureKeyword.replace("'", "\\'").replace(
									"\n", "\\n")));
		} catch (RuntimeException r) {
			warn(String.format("Keyword '%s' could not be run on failure%s",
					runOnFailureKeyword,
					r.getMessage() != null ? " '" + r.getMessage() + "'" : ""));
		} finally {
			runningOnFailureRoutine = false;
		}
	}

}

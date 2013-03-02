package com.github.markusbernhardt.selenium2library.keywords;

import org.python.util.PythonInterpreter;

public abstract class RunOnFailure extends Logging {

	// ##############################
	// Keywords
	// ##############################

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

	/**
	 * The keyword to run an failure
	 */
	protected String runOnFailureKeyword = "Capture Page Screenshot";
	protected boolean runningOnFailureRoutine;

	protected ThreadLocal<PythonInterpreter> runOnFailurePythonInterpreter = new ThreadLocal<PythonInterpreter>() {

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

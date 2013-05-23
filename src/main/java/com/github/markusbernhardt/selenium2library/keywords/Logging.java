package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.utils.Python;

public abstract class Logging extends JavaScript {

	protected final static Set<String> VALID_LOG_LEVELS;

	static {
		VALID_LOG_LEVELS = new HashSet<String>();
		VALID_LOG_LEVELS.add("debug");
		VALID_LOG_LEVELS.add("html");
		VALID_LOG_LEVELS.add("info");
		VALID_LOG_LEVELS.add("trace");
		VALID_LOG_LEVELS.add("warn");
	}

	// ##############################
	// Internal Methods
	// ##############################

	@Override
	protected void trace(String msg) {
		log0(msg, "trace");
	}

	@Override
	protected void debug(String msg) {
		log0(msg, "debug");
	}

	@Override
	protected void info(String msg) {
		log0(msg, "info");
	}

	@Override
	protected void html(String msg) {
		log0(msg, "html");
	}

	@Override
	protected void warn(String msg) {
		log0(msg, "warn");
	}

	@Override
	protected void log(String msg, String logLevel) {
		if (VALID_LOG_LEVELS.contains(logLevel.toLowerCase())) {
			log0(msg, logLevel);
		} else {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Given log level %s is invalid.", logLevel));
		}
	}

	protected void log0(String msg, String logLevel) {
		if (msg.length() > 1024) {
			// Message is too large.
			// There is a hard limit of 100k in the Jython source code parser
			try {
				// Write message to temp file
				File tempFile = File
						.createTempFile("Selenium2Library-", ".log");
				tempFile.deleteOnExit();
				FileWriter writer = new FileWriter(tempFile);
				writer.write(msg);
				writer.close();

				// Read the message in Python back and log it.
				loggingPythonInterpreter
						.get()
						.exec("from __future__ import with_statement\n\nwith open('"
								+ tempFile.getAbsolutePath()
								+ "', 'r') as msg_file:\n    msg = msg_file.read()\n    logger."
								+ logLevel + "(msg)");

			} catch (IOException e) {
				throw new Selenium2LibraryNonFatalException(
						"Error in handling temp file for long log message.", e);
			}
		} else {
			// Message is small enough to get parsed by Jython
			loggingPythonInterpreter.get().exec(
					String.format("logger.info('%s');", msg.replace("'", "\\'")
							.replace("\n", "\\n")));
		}
	}

	@Override
	protected List<String> logList(List<String> items) {
		return logList(items, "item");
	}

	@Override
	protected List<String> logList(List<String> items, String what) {
		List<String> msg = new ArrayList<String>();
		msg.add(String.format("Altogether %d %s%s.\n", items.size(), what,
				items.size() == 1 ? "" : "s"));
		for (int index = 0; index < items.size(); index++) {
			msg.add(String.format("%d: %s", index + 1, items.get(index)));
		}
		info(Python.join("\n", msg));
		return items;
	}

	@Override
	protected File getLogDir() {
		PyString logDirName = (PyString) loggingPythonInterpreter.get().eval(
				"GLOBAL_VARIABLES['${LOG FILE}']");
		if (logDirName != null) {
			return new File(logDirName.asString()).getParentFile();
		}
		logDirName = (PyString) loggingPythonInterpreter.get().eval(
				"GLOBAL_VARIABLES['${OUTPUTDIR}']");
		return new File(logDirName.asString()).getParentFile();
	}

	protected static ThreadLocal<PythonInterpreter> loggingPythonInterpreter = new ThreadLocal<PythonInterpreter>() {

		@Override
		protected PythonInterpreter initialValue() {
			PythonInterpreter pythonInterpreter = new PythonInterpreter();
			pythonInterpreter
					.exec("from robot.variables import GLOBAL_VARIABLES; from robot.api import logger;");
			return pythonInterpreter;
		}
	};
}

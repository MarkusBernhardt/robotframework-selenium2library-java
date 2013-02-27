package org.robotframework.selenium2library.keywords;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public abstract class Logging extends JavaScript {

	@Override
	protected void trace(String msg) {
		loggingPythonInterpreter.get().exec(
				String.format("logger.trace('%s');", msg.replace("'", "\\'")
						.replace("\n", "\\n")));
	}

	@Override
	protected void debug(String msg) {
		loggingPythonInterpreter.get().exec(
				String.format("logger.debug('%s');", msg.replace("'", "\\'")
						.replace("\n", "\\n")));
	}

	@Override
	protected void info(String msg) {
		loggingPythonInterpreter.get().exec(
				String.format("logger.info('%s');", msg.replace("'", "\\'")
						.replace("\n", "\\n")));
	}

	@Override
	protected void html(String msg) {
		loggingPythonInterpreter.get().exec(
				String.format("logger.info('%s', True, False);",
						msg.replace("'", "\\'").replace("\n", "\\n")));
	}

	@Override
	protected void warn(String msg) {
		loggingPythonInterpreter.get().exec(
				String.format("logger.warn('%s');", msg.replace("'", "\\'")
						.replace("\n", "\\n")));
	}

	@Override
	protected void log(String msg, String logLevel) {
		logLevel = logLevel.toLowerCase();
		if (logLevel.equals("trace")) {
			trace(msg);
		} else if (logLevel.equals("debug")) {
			debug(msg);
		} else if (logLevel.equals("info")) {
			info(msg);
		} else if (logLevel.equals("html")) {
			html(msg);
		} else if (logLevel.equals("warn")) {
			warn(msg);
		} else {
			throw new IllegalArgumentException(String.format(
					"Given log level %s is invalid.", logLevel));
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
		info(join("\n", msg));
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

	private ThreadLocal<PythonInterpreter> loggingPythonInterpreter = new ThreadLocal<PythonInterpreter>() {

		@Override
		protected PythonInterpreter initialValue() {
			PythonInterpreter pythonInterpreter = new PythonInterpreter();
			pythonInterpreter
					.exec("from robot.variables import GLOBAL_VARIABLES; from robot.api import logger;");
			return pythonInterpreter;
		}
	};

}

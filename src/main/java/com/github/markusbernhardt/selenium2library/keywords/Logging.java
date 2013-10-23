package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;

@RobotKeywords
public class Logging extends RunOnFailureKeywordsAdapter {

	protected final static Map<String, String[]> VALID_LOG_LEVELS;
	protected static String logDir = null;

	static {
		VALID_LOG_LEVELS = new HashMap<String, String[]>();
		VALID_LOG_LEVELS.put("debug", new String[] { "debug", "" });
		VALID_LOG_LEVELS.put("html", new String[] { "info", ", True, False" });
		VALID_LOG_LEVELS.put("info", new String[] { "info", "" });
		VALID_LOG_LEVELS.put("trace", new String[] { "trace", "" });
		VALID_LOG_LEVELS.put("warn", new String[] { "warn", "" });
	}

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeywordOverload
	public List<String> logWindowIdentifiers() {
		return logWindowIdentifiers("INFO");
	}

	/**
	 * Logs and returns the id attributes of all windows known to the current
	 * browser instance.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return List of window id attributes.
	 * 
	 * @see BrowserManagement#getWindowIdentifiers
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public List<String> logWindowIdentifiers(String logLevel) {
		List<String> windowIdentifiers = browserManagement.getWindowIdentifiers();
		for (String windowIdentifier : windowIdentifiers) {
			log(windowIdentifier, logLevel);
		}
		return windowIdentifiers;
	}

	@RobotKeywordOverload
	public List<String> logWindowNames() {
		return logWindowNames("INFO");
	}

	/**
	 * Logs and returns the names of all windows known to the current browser
	 * instance.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return List of windows names.
	 * 
	 * @see BrowserManagement#getWindowNames
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public List<String> logWindowNames(String logLevel) {
		List<String> windowIdentifiers = browserManagement.getWindowNames();
		for (String windowIdentifier : windowIdentifiers) {
			log(windowIdentifier, logLevel);
		}
		return windowIdentifiers;
	}

	@RobotKeywordOverload
	public List<String> logWindowTitles() {
		return logWindowTitles("INFO");
	}

	/**
	 * Logs and returns the titles of all windows known to the current browser
	 * instance.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return List of window titles.
	 * 
	 * @see BrowserManagement#getWindowTitles
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public List<String> logWindowTitles(String logLevel) {
		List<String> windowIdentifiers = browserManagement.getWindowTitles();
		for (String windowIdentifier : windowIdentifiers) {
			log(windowIdentifier, logLevel);
		}
		return windowIdentifiers;
	}

	@RobotKeywordOverload
	public String logLocation() {
		return logLocation("INFO");
	}

	/**
	 * Logs and returns the location of the current browser instance.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return The current location.
	 * 
	 * @see BrowserManagement#getLocation
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public String logLocation(String logLevel) {
		String actual = browserManagement.getLocation();
		log(actual, logLevel);
		return actual;
	}

	@RobotKeywordOverload
	public String logSource() {
		return logSource("INFO");
	}

	/**
	 * Logs and returns the entire html source of the current page or frame.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return The entire html source.
	 * 
	 * @see BrowserManagement#getSource
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public String logSource(String logLevel) {
		String actual = browserManagement.getSource();
		log(actual, logLevel);
		return actual;
	}

	@RobotKeywordOverload
	public String logTitle() {
		return logTitle("INFO");
	}

	/**
	 * Logs and returns the title of current page.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return The page title.
	 * 
	 * @see BrowserManagement#getSource
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public String logTitle(String logLevel) {
		String actual = browserManagement.getTitle();
		log(actual, logLevel);
		return actual;
	}

	@RobotKeywordOverload
	public String logSystemInfo() {
		return logSystemInfo("INFO");
	}

	/**
	 * Logs and returns basic system information about the execution
	 * environment.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return System information.
	 * 
	 * @see BrowserManagement#getSystemInfo
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public String logSystemInfo(String logLevel) {
		String actual = browserManagement.getSystemInfo();
		log(actual, logLevel);
		return actual;
	}

	@RobotKeywordOverload
	public String logRemoteCapabilities() {
		return logRemoteCapabilities("INFO");
	}

	/**
	 * Logs and returns the actually supported capabilities of the remote
	 * browser instance.<br>
	 * <br>
	 * Not all server implementations will support every WebDriver feature.
	 * Therefore, the client and server should use JSON objects with the
	 * properties listed below when describing which features a user requests
	 * that a session support. <b>If a session cannot support a capability that
	 * is requested in the desired capabilities, no error is thrown;</b> a
	 * read-only capabilities object is returned that indicates the capabilities
	 * the session actually supports. For more information see: <a href=
	 * "http://code.google.com/p/selenium/wiki/DesiredCapabilities"
	 * >DesiredCapabilities</a><br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return The capabilities of the remote node.
	 * 
	 * @see BrowserManagement#getRemoteCapabilities
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public String logRemoteCapabilities(String logLevel) {
		String actual = browserManagement.getRemoteCapabilities();
		log(actual, logLevel);
		return actual;
	}

	@RobotKeywordOverload
	public String logRemoteSessionId() {
		return logRemoteSessionId("INFO");
	}

	/**
	 * Logs and returns the session id of the remote browser instance.<br>
	 * <br>
	 * See `Introduction` for details about the <b>loglevel</b>.<br>
	 * 
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return The remote session id.
	 * 
	 * @see BrowserManagement#getRemoteSessionId
	 */
	@RobotKeyword
	@ArgumentNames({ "logLevel=INFO" })
	public String logRemoteSessionId(String logLevel) {
		String actual = browserManagement.getRemoteSessionId();
		log(actual, logLevel);
		return actual;
	}

	@RobotKeyword
	@ArgumentNames({ "logDirectory" })
	public void setLogDirectory(String logDirectory) throws Exception {
		File file = new File(logDirectory);

		if (file.exists() && file.isDirectory() && file.canWrite()) {
			Logging.setLogDir(file.getAbsolutePath());
		} else {
			throw new Exception("Location given as parameter: " + logDirectory
					+ " must exist and must be a writeable directory!");
		}
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected void trace(String msg) {
		log(msg, "trace");
	}

	protected void debug(String msg) {
		log(msg, "debug");
	}

	protected void info(String msg) {
		log(msg, "info");
	}

	protected void html(String msg) {
		log(msg, "html");
	}

	protected void warn(String msg) {
		log(msg, "warn");
	}

	protected void log(String msg, String logLevel) {
		String[] methodParameters = VALID_LOG_LEVELS.get(logLevel.toLowerCase());
		if (methodParameters != null) {
			log0(msg, methodParameters[0], methodParameters[1]);
		} else {
			throw new Selenium2LibraryNonFatalException(String.format("Given log level %s is invalid.", logLevel));
		}
	}

	protected void log0(String msg, String methodName, String methodArguments) {
		if (msg.length() > 1024) {
			// Message is too large.
			// There is a hard limit of 100k in the Jython source code parser
			try {
				// Write message to temp file
				File tempFile = File.createTempFile("Selenium2Library-", ".log");
				tempFile.deleteOnExit();
				FileWriter writer = new FileWriter(tempFile);
				writer.write(msg);
				writer.close();

				// Read the message in Python back and log it.
				loggingPythonInterpreter.get().exec(
						String.format("from __future__ import with_statement\n" + "\n"
								+ "with open('%s', 'r') as msg_file:\n" + "    msg = msg_file.read()\n"
								+ "    logger.%s(msg%s)", tempFile.getAbsolutePath().replace("\\", "\\\\"), methodName,
								methodArguments));

			} catch (IOException e) {
				throw new Selenium2LibraryNonFatalException("Error in handling temp file for long log message.", e);
			}
		} else {
			// Message is small enough to get parsed by Jython
			loggingPythonInterpreter.get().exec(
					String.format("logger.%s('%s'%s)", methodName, msg.replace("'", "\\'").replace("\n", "\\n"),
							methodArguments));
		}
	}

	protected File getLogDir() {
		if (logDir == null) {
			PyString logDirName = (PyString) loggingPythonInterpreter.get().eval("GLOBAL_VARIABLES['${LOG FILE}']");
			if (logDirName != null && !(logDirName.asString().toUpperCase().equals("NONE"))) {
				return new File(logDirName.asString()).getParentFile();
			}
			logDirName = (PyString) loggingPythonInterpreter.get().eval("GLOBAL_VARIABLES['${OUTPUTDIR}']");
			return new File(logDirName.asString()).getParentFile();
		} else {
			return new File(logDir);
		}
	}

	public static void setLogDir(String logDirectory) {
		logDir = logDirectory;
	}

	protected static ThreadLocal<PythonInterpreter> loggingPythonInterpreter = new ThreadLocal<PythonInterpreter>() {

		@Override
		protected PythonInterpreter initialValue() {
			PythonInterpreter pythonInterpreter = new PythonInterpreter();
			pythonInterpreter.exec("from robot.variables import GLOBAL_VARIABLES; from robot.api import logger;");
			return pythonInterpreter;
		}
	};
}

package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.utils.Python;

@RobotKeywords
public abstract class JavaScript extends FormElement {

	// ##############################
	// Keywords
	// ##############################

	@RobotKeywordOverload
	public void alertShouldBePresent() {
		alertShouldBePresent("");
	}

	@RobotKeyword("Verifies an alert is present and dismisses it.\n\n"
			
			+ "If _text_ is a non-empty string, then it is also verified that the message "
			+ "of the alert equals to _text_.\n\n"
			
			+ "Will fail if no alert is present. Note that following keywords will fail "
			+ "unless the alert is dismissed by this keyword or another like _Get Alert Message_.\n")
	@ArgumentNames({"text=NONE"})
	public void alertShouldBePresent(String text) {
		String alertText = getAlertMessage();
		if (text != null && !alertText.equals(text)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Alert text should have been '%s' but was '%s'", text,
					alertText));
		}
	}
	
	@RobotKeyword("Cancel will be selected the next time _Confirm Action_ is used.\n")
	public void chooseCancelOnNextConfirmation() {
		cancelOnNextConfirmation = true;
	}

	@RobotKeyword("Undo the effect of using keywords _Choose Cancel On Next Confirmation_. Note that "
			+ "Selenium's overridden _window.confirm()_ function will normally automatically return "
			+ "true, as if the user had manually clicked OK, so you shouldn't need to use this "
			+ "command unless for some reason you need to change your mind prior to the next "
			+ "confirmation\n\n. "
			
			+ "After any confirmation, Selenium will resume using the default behavior for future "
			+ "confirmations, automatically returning true (OK) unless/until you explicitly use "
			+ "_Choose Cancel On Next Confirmation_ for each confirmation.\n\n"
			
			+ "Note that every time a confirmation comes up, you must consume it by using a keywords "
			+ "such as _Get Alert Message_, or else the following selenium operations will fail.")
	public void chooseOkOnNextConfirmation() {
		cancelOnNextConfirmation = false;
	}

	@RobotKeyword("Dismisses currently shown confirmation dialog and returns its message.\n\n"

			+ "By default, this keyword chooses 'OK' option from the dialog. If 'Cancel' needs to be "
			+ "chosen, keyword _Choose Cancel On Next Confirmation_ must be called before the action "
			+ "that causes the confirmation dialog to be shown.\n\n"

			+ "Examples:\n"
			+ "| Click Button | Send | # Shows a confirmation dialog |\n"
			+ "| ${message}= | Confirm Action | # Chooses Ok |\n"
			+ "| Should Be Equal | ${message} | Are your sure? |\n"
			+ "| Choose Cancel On Next Confirmation |\n"
			+ "| Click Button | Send | # Shows a confirmation dialog |\n"
			+ "| Confirm Action | # Chooses Cancel |\n")
	public String confirmAction() {
		String text = closeAlert(!cancelOnNextConfirmation);
		cancelOnNextConfirmation = false;
		return text;
	}

	@RobotKeyword("Executes the given JavaScript code.\n\n"

			+ "_code_ may contain multiple lines of code but must contain a return statement (with the "
			+ "value to be returned) at the end.\n\n"
		
			+ "_code_ may be divided into multiple cells in the test data. In that case, the parts are "
			+ "catenated together without adding spaces.\n"
			+ "If _code_ is an absolute path to an existing file, the JavaScript to execute will be read "
			+ "from that file. Forward slashes work as a path separator on all operating systems.\n\n"
		
			+ "Note that, by default, the code will be executed in the context of the Selenium object "
			+ "itself, so this will refer to the Selenium object. Use _window_ to refer to the window of "
			+ "your application, e.g. _window.document.getElementById('foo')_.\n\n"
		
			+ "Example:\n"
			+ "| Execute JavaScript | window.my_js_function('arg1', 'arg2') |\n"
			+ "| Execute JavaScript | ${CURDIR}/js_to_execute.js |\n")
	@ArgumentNames({"*code"})
	public Object executeJavascript(String... code) {
		String js = getJavascriptToExecute(Python.join("", Arrays.asList(code)));
		String.format("Executing JavaScript:\n%s", js);
		return ((JavascriptExecutor) webDriverCache.getCurrent())
				.executeScript(js);
	}

	@RobotKeyword("Executes asynchronous JavaScript code.\n\n"

			+ "_code_ may contain multiple lines of code but must contain a return statement (with the "
			+ "value to be returned) at the end.\n\n"
		
			+ "_code_ may be divided into multiple cells in the test data. In that case, the parts are "
			+ "catenated together without adding spaces.\n"
			+ "If _code_ is an absolute path to an existing file, the JavaScript to execute will be read "
			+ "from that file. Forward slashes work as a path separator on all operating systems.\n\n"
		
			+ "Note that, by default, the code will be executed in the context of the Selenium object "
			+ "itself, so this will refer to the Selenium object. Use _window_ to refer to the window of "
			+ "your application, e.g. _window.document.getElementById('foo')_.\n\n"
		
			+ "Example:\n"
			+ "| Execute Async JavaScript | window.my_js_function('arg1', 'arg2') |\n"
			+ "| Execute Async JavaScript | ${CURDIR}/js_to_execute.js |\n")
	@ArgumentNames({"*code"})
	public Object executeAsyncJavascript(String... code) {
		String js = getJavascriptToExecute(Python.join("", Arrays.asList(code)));
		String.format("Executing JavaScript:\n%s", js);
		return ((JavascriptExecutor) webDriverCache.getCurrent())
				.executeAsyncScript(js);
	}

	@RobotKeyword("Returns the text of current JavaScript alert.\n\n"
			
			+ "This keyword will fail if no alert is present. Note that following keywords will fail "
			+ "unless the alert is dismissed by this keyword or another like _Get Alert Message_.\n")
	public String getAlertMessage() {
		return closeAlert();
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected boolean cancelOnNextConfirmation = false;

	protected String closeAlert() {
		return closeAlert(false);
	}

	protected String closeAlert(boolean confirm) {
		Alert alert = null;
		try {
			alert = webDriverCache.getCurrent().switchTo().alert();
			String text = alert.getText().replace("\n", "");
			if (!confirm) {
				alert.dismiss();
			} else {
				alert.accept();
			}
			return text;
		} catch (WebDriverException wde) {
			throw new Selenium2LibraryNonFatalException("There were no alerts");
		}
	}

	protected static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

	protected String getJavascriptToExecute(String code) {
		String codepath = code.replace('/', File.separatorChar);
		if (!new File(codepath).isFile()) {
			return code;
		}
		html(String.format(
				"Reading JavaScript from file <a href=\"file://%s\">%s</a>.",
				codepath.replace(File.separatorChar, '/'), codepath));
		try {
			return readFile(codepath);
		} catch (IOException e) {
			throw new Selenium2LibraryNonFatalException(
					"Cannot read JavaScript file: " + codepath);
		}
	}

}

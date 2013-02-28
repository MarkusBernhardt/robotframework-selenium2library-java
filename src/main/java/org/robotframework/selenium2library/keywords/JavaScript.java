package org.robotframework.selenium2library.keywords;

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
import org.robotframework.selenium2library.Selenium2LibraryNonFatalException;
import org.robotframework.selenium2library.utils.Python;

public abstract class JavaScript extends FormElement {

	// ##############################
	// Keywords
	// ##############################

	public void alertShouldBePresent() {
		alertShouldBePresent("");
	}

	public void alertShouldBePresent(String text) {
		String alertText = getAlertMessage();
		if (text != null && !alertText.equals(text)) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Alert text should have been '%s' but was '%s'", text,
					alertText));
		}
	}

	public void chooseCancelOnNextConfirmation() {
		cancelOnNextConfirmation = true;
	}

	public void chooseOkOnNextConfirmation() {
		cancelOnNextConfirmation = false;
	}

	public String confirmAction() {
		String text = closeAlert(!cancelOnNextConfirmation);
		cancelOnNextConfirmation = false;
		return text;
	}

	public Object executeJavascript(String... code) {
		String js = getJavascriptToExecute(Python.join("", Arrays.asList(code)));
		String.format("Executing JavaScript:\n%s", js);
		return ((JavascriptExecutor) webDriverCache.getCurrent())
				.executeScript(js);
	}

	public Object executeAsyncJavascript(String... code) {
		String js = getJavascriptToExecute(Python.join("", Arrays.asList(code)));
		String.format("Executing JavaScript:\n%s", js);
		return ((JavascriptExecutor) webDriverCache.getCurrent())
				.executeAsyncScript(js);
	}

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
			throw new Selenium2LibraryNonFatalException("Cannot read JavaScript file: "
					+ codepath);
		}
	}

}

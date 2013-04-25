package com.github.markusbernhardt.selenium2library.keywords;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.github.markusbernhardt.selenium2library.utils.Robotframework;

public abstract class Screenshot extends RunOnFailure {

	// ##############################
	// Keywords
	// ##############################

	public void capturePageScreenshot() {
		capturePageScreenshot(null);
	}

	public void capturePageScreenshot(String filename) {
		File logdir = getLogDir();
		File path = new File(logdir, normalizeFilename(filename));
		String link = Robotframework.getLinkPath(path, logdir);

		TakesScreenshot takesScreenshot = ((TakesScreenshot) webDriverCache
				.getCurrent());
		if (takesScreenshot == null) {
			warn("Can't take screenshot. No open browser found");
			return;
		}

		byte[] png = takesScreenshot.getScreenshotAs(OutputType.BYTES);
		writeScreenshot(path, png);

		html(String
				.format("</td></tr><tr><td colspan=\"3\"><a href=\"%s\"><img src=\"%s\" width=\"800px\"></a>",
						link, link));
	}

	// ##############################
	// Internal Methods
	// ##############################

	private int screenshotIndex = 0;

	private void writeScreenshot(File path, byte[] png) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(png);
			fos.flush();
		} catch (IOException e) {
			warn(String.format("Can't write screenshot '%s'",
					path.getAbsolutePath()));
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					warn("Can't even close stream");
				}
			}
		}
	}

	private String normalizeFilename(String filename) {
		if (filename == null) {
			screenshotIndex++;
			filename = String.format("selenium-screenshot-%d.png",
					screenshotIndex);
		} else {
			filename = filename.replace('/', File.separatorChar);
		}
		return filename;
	}

}

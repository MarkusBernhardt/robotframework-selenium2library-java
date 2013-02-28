package org.robotframework.selenium2library.keywords;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.robotframework.selenium2library.utils.Robotframework;

public abstract class Screenshot extends RunOnFailure {

	// ##############################
	// Keywords
	// ##############################

	public void capturePageScreenshot() {
		capturePageScreenshot(null);
	}

	public void capturePageScreenshot(String filename) {
		String normalizedFilename = normalizeFilename(filename);
		File logdir = getLogDir();
		File path = new File(logdir, normalizedFilename);

		byte[] png = ((TakesScreenshot) webDriverCache.getCurrent())
				.getScreenshotAs(OutputType.BYTES);
		writeScreenshot(path, png);

		String link = Robotframework.getLinkPath(path, logdir);
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
		}

		else {
			filename = filename.replace('/', File.separatorChar);
		}
		return filename;
	}

}

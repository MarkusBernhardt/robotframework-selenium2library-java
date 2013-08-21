package com.github.markusbernhardt.selenium2library.keywords;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.TableElementFinder;

@RobotKeywords
public abstract class TableElement extends SelectElement {

	// ##############################
	// Keywords
	// ##############################
	
	@RobotKeywordOverload
	public String getTableCell(String tableLocator, int row, int column) {
		return getTableCell(tableLocator, row, column, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"locator, row, column, loglevel=INFO"})
	public String getTableCell(String tableLocator, int row, int column,
			String loglevel) {
		int rowIndex = row - 1;
		int columnIndex = column - 1;
		WebElement table = TableElementFinder.find(webDriverCache.getCurrent(),
				tableLocator);
		if (table != null) {
			List<WebElement> rows = table.findElements(By.xpath("./thead/tr"));
			if (rowIndex >= rows.size()) {
				rows.addAll(table.findElements(By.xpath("./tbody/tr")));
			}
			if (rowIndex >= rows.size()) {
				rows.addAll(table.findElements(By.xpath("./tfoot/tr")));
			}
			if (rowIndex < rows.size()) {
				List<WebElement> columns = rows.get(rowIndex).findElements(
						By.tagName("th"));
				if (columnIndex >= columns.size()) {
					columns.addAll(rows.get(rowIndex).findElements(
							By.tagName("td")));
				}
				if (columnIndex < columns.size()) {
					return columns.get(columnIndex).getText();
				}
			}
		}
		logSource(loglevel);
		throw new Selenium2LibraryNonFatalException(
				String.format(
						"Cell in table %s in row #%d and column #%d could not be found.",
						tableLocator, row, column));
	}

	@RobotKeywordOverload
	public void tableCellShouldContain(String tableLocator, int row,
			int column, String expected) {
		tableCellShouldContain(tableLocator, row, column, expected, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"locator, row, column, expected, loglevel=INFO"})
	public void tableCellShouldContain(String tableLocator, int row,
			int column, String expected, String loglevel) {
		String message = String
				.format("Cell in table '%s' in row #%d and column #%d should have contained text '%s'.",
						tableLocator, row, column, expected);

		String content = "";
		try {
			content = getTableCell(tableLocator, row, column, loglevel);
		} catch (AssertionError err) {
			info(err.getMessage());
			throw new Selenium2LibraryNonFatalException(message);
		}

		info(String.format("Cell contains %s.", content));
		if (!content.contains(expected)) {
			logSource(loglevel);
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void tableColumnShouldContain(String tableLocator, int col,
			String expected) {
		tableColumnShouldContain(tableLocator, col, expected, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"locator, col, expected, loglevel=INFO"})
	public void tableColumnShouldContain(String tableLocator, int col,
			String expected, String loglevel) {
		WebElement element = TableElementFinder.findByCol(
				webDriverCache.getCurrent(), tableLocator, col, expected);
		if (element == null) {
			logSource(loglevel);
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Column #%d in table identified by '%s' should have contained text '%s'.",
							col, tableLocator, expected));
		}
	}

	@RobotKeywordOverload
	public void tableFooterShouldContain(String tableLocator, String expected) {
		tableFooterShouldContain(tableLocator, expected, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"tableLocator, expected, loglevel=INFO"})
	public void tableFooterShouldContain(String tableLocator, String expected,
			String loglevel) {
		WebElement element = TableElementFinder.findByFooter(
				webDriverCache.getCurrent(), tableLocator, expected);
		if (element == null) {
			logSource(loglevel);
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Footer in table identified by '%s' should have contained text '%s'.",
							tableLocator, expected));
		}
	}

	@RobotKeywordOverload
	public void tableHeaderShouldContain(String tableLocator, String expected) {
		tableHeaderShouldContain(tableLocator, expected, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"tableLocator, expected, loglevel=INFO"})
	public void tableHeaderShouldContain(String tableLocator, String expected,
			String loglevel) {
		WebElement element = TableElementFinder.findByHeader(
				webDriverCache.getCurrent(), tableLocator, expected);
		if (element == null) {
			logSource(loglevel);
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Header in table identified by '%s' should have contained text '%s'.",
							tableLocator, expected));
		}
	}

	@RobotKeywordOverload
	public void tableRowShouldContain(String tableLocator, int row,
			String expected) {
		tableRowShouldContain(tableLocator, row, expected, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"tableLocator, row, expected, loglevel=INFO"})
	public void tableRowShouldContain(String tableLocator, int row,
			String expected, String loglevel) {
		WebElement element = TableElementFinder.findByRow(
				webDriverCache.getCurrent(), tableLocator, row, expected);
		if (element == null) {
			logSource(loglevel);
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Row #%d in table identified by '%s' should have contained text '%s'.",
							row, tableLocator, expected));
		}
	}

	@RobotKeywordOverload
	public void tableShouldContain(String tableLocator, String expected) {
		tableShouldContain(tableLocator, expected, "INFO");
	}

	@RobotKeyword
	@ArgumentNames({"tableLocator, expected, loglevel=INFO"})
	public void tableShouldContain(String tableLocator, String expected,
			String loglevel) {
		WebElement element = TableElementFinder.findByContent(
				webDriverCache.getCurrent(), tableLocator, expected);
		if (element == null) {
			logSource(loglevel);
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Table identified by '%s' should have contained text '%s'.",
							tableLocator, expected));
		}
	}

}

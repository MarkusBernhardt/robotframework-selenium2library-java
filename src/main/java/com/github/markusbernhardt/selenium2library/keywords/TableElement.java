package com.github.markusbernhardt.selenium2library.keywords;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.locators.TableElementFinder;

@RobotKeywords
public class TableElement extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeywordOverload
	public String getTableCell(String tableLocator, int row, int column) {
		return getTableCell(tableLocator, row, column, "INFO");
	}

	/**
	 * Returns the content of the table cell at the coordinates <b>row</b> and
	 * <b>column</b> of the table identified by <b>tableLocator</b>.<br>
	 * <br>
	 * Row and column number start from 1. Header and footer rows are included
	 * in the count. That way also cell content from header or footer rows can
	 * be obtained with this keyword.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param row
	 *            The table row.
	 * @param column
	 *            The table column.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 * @return The table cell content.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "row", "column", "logLevel=INFO" })
	public String getTableCell(String tableLocator, int row, int column, String logLevel) {
		int rowIndex = row - 1;
		int columnIndex = column - 1;
		WebElement table = TableElementFinder.find(browserManagement.getCurrentWebDriver(), tableLocator);
		if (table != null) {
			List<WebElement> rows = table.findElements(By.xpath("./thead/tr"));
			if (rowIndex >= rows.size()) {
				rows.addAll(table.findElements(By.xpath("./tbody/tr")));
			}
			if (rowIndex >= rows.size()) {
				rows.addAll(table.findElements(By.xpath("./tfoot/tr")));
			}
			if (rowIndex < rows.size()) {
				List<WebElement> columns = rows.get(rowIndex).findElements(By.tagName("th"));
				if (columnIndex >= columns.size()) {
					columns.addAll(rows.get(rowIndex).findElements(By.tagName("td")));
				}
				if (columnIndex < columns.size()) {
					return columns.get(columnIndex).getText();
				}
			}
		}
		logging.logSource(logLevel);
		throw new Selenium2LibraryNonFatalException(String.format(
				"Cell in table %s in row #%d and column #%d could not be found.", tableLocator, row, column));
	}

	@RobotKeywordOverload
	public void tableCellShouldContain(String tableLocator, int row, int column, String text) {
		tableCellShouldContain(tableLocator, row, column, text, "INFO");
	}

	/**
	 * Verify the content of the table cell at the coordinates <b>row</b> and
	 * <b>column</b> of the table identified by <b>tableLocator</b> contains
	 * <b>text</b>.<br>
	 * <br>
	 * Row and column number start from 1. Header and footer rows are included
	 * in the count. That way also cell content from header or footer rows can
	 * be obtained with this keyword.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param row
	 *            The table row.
	 * @param column
	 *            The table column.
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "row", "column", "text", "logLevel=INFO" })
	public void tableCellShouldContain(String tableLocator, int row, int column, String text, String logLevel) {
		String message = String.format("Cell in table '%s' in row #%d and column #%d should have contained text '%s'.",
				tableLocator, row, column, text);

		String content = "";
		try {
			content = getTableCell(tableLocator, row, column, logLevel);
		} catch (AssertionError err) {
			logging.info(err.getMessage());
			throw new Selenium2LibraryNonFatalException(message);
		}

		logging.info(String.format("Cell contains %s.", content));
		if (!content.contains(text)) {
			logging.logSource(logLevel);
			throw new Selenium2LibraryNonFatalException(message);
		}
	}

	@RobotKeywordOverload
	public void tableColumnShouldContain(String tableLocator, int col, String text) {
		tableColumnShouldContain(tableLocator, col, text, "INFO");
	}

	/**
	 * Verify the content of any table cells of the table <b>column</b> of the
	 * table identified by <b>tableLocator</b> contains <b>text</b>.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * <br>
	 * The first leftmost column is column number 1. If the table contains cells
	 * that span multiple columns, those merged cells count as a single column.
	 * For example both tests below work, if in one row columns A and B are
	 * merged with colspan="2", and the logical third column contains "C".<br>
	 * <br>
	 * Example:
	 * <table border="1" cellspacing="0">
	 * <tr>
	 * <td>Table Column Should Contain</td>
	 * <td>tableId</td>
	 * <td>3</td>
	 * <td>C</td>
	 * </tr>
	 * <tr>
	 * <td>Table Column Should Contain</td>
	 * <td>tableId</td>
	 * <td>2</td>
	 * <td>C</td>
	 * </tr>
	 * </table>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param col
	 *            The table column.
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "col", "text", "logLevel=INFO" })
	public void tableColumnShouldContain(String tableLocator, int col, String text, String logLevel) {
		WebElement element = TableElementFinder.findByCol(browserManagement.getCurrentWebDriver(), tableLocator, col,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Column #%d in table identified by '%s' should have contained text '%s'.", col, tableLocator, text));
		}
	}

	@RobotKeywordOverload
	public void tableFooterShouldContain(String tableLocator, String text) {
		tableFooterShouldContain(tableLocator, text, "INFO");
	}

	/**
	 * Verify the content of any table footer cells of the table identified by
	 * <b>tableLocator</b> contains <b>text</b>.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "text", "logLevel=INFO" })
	public void tableFooterShouldContain(String tableLocator, String text, String logLevel) {
		WebElement element = TableElementFinder.findByFooter(browserManagement.getCurrentWebDriver(), tableLocator,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Footer in table identified by '%s' should have contained text '%s'.", tableLocator, text));
		}
	}

	@RobotKeywordOverload
	public void tableHeaderShouldContain(String tableLocator, String text) {
		tableHeaderShouldContain(tableLocator, text, "INFO");
	}

	/**
	 * Verify the content of any table header cells of the table identified by
	 * <b>tableLocator</b> contains <b>text</b>.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "text", "logLevel=INFO" })
	public void tableHeaderShouldContain(String tableLocator, String text, String logLevel) {
		WebElement element = TableElementFinder.findByHeader(browserManagement.getCurrentWebDriver(), tableLocator,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Header in table identified by '%s' should have contained text '%s'.", tableLocator, text));
		}
	}

	@RobotKeywordOverload
	public void tableRowShouldContain(String tableLocator, int row, String text) {
		tableRowShouldContain(tableLocator, row, text, "INFO");
	}

	/**
	 * Verify the content of any table cells of the table <b>row</b> of the
	 * table identified by <b>tableLocator</b> contains <b>text</b>.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * <br>
	 * The uppermost row is row number 1. For tables that are structured with
	 * thead, tbody and tfoot, only the tbody section is searched. Please use
	 * Table Header Should Contain or Table Footer Should Contain for tests
	 * against the header or footer content.<br>
	 * <br>
	 * If the table contains cells that span multiple rows, a match only occurs
	 * for the uppermost row of those merged cells.<br>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param row
	 *            The table row.
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "row", "text", "logLevel=INFO" })
	public void tableRowShouldContain(String tableLocator, int row, String text, String logLevel) {
		WebElement element = TableElementFinder.findByRow(browserManagement.getCurrentWebDriver(), tableLocator, row,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Row #%d in table identified by '%s' should have contained text '%s'.", row, tableLocator, text));
		}
	}

	@RobotKeywordOverload
	public void tableShouldContain(String tableLocator, String text) {
		tableShouldContain(tableLocator, text, "INFO");
	}

	/**
	 * Verify the content of any table cells of the table identified by
	 * <b>tableLocator</b> contains <b>text</b>.<br>
	 * <br>
	 * Key attributes for tables are id and name. See `Introduction` for details
	 * about locators and log levels.<br>
	 * 
	 * @param tableLocator
	 *            The locator to locate the table.
	 * @param text
	 *            The text to verify.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "tableLocator", "text", "logLevel=INFO" })
	public void tableShouldContain(String tableLocator, String text, String logLevel) {
		WebElement element = TableElementFinder.findByContent(browserManagement.getCurrentWebDriver(), tableLocator,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new Selenium2LibraryNonFatalException(String.format(
					"Table identified by '%s' should have contained text '%s'.", tableLocator, text));
		}
	}

}

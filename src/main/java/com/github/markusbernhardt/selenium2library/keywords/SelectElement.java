package com.github.markusbernhardt.selenium2library.keywords;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.selenium2library.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.selenium2library.Selenium2LibraryNonFatalException;
import com.github.markusbernhardt.selenium2library.utils.Python;

@RobotKeywords
public class SelectElement extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;

	// ##############################
	// Keywords
	// ##############################

	/**
	 * Returns the values in the select list identified by <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @return The select list values
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public List<String> getListItems(String locator) {
		List<WebElement> options = getSelectListOptions(locator);

		return getLabelsForOptions(options);
	}

	/**
	 * Returns the visible label of the first selected element from the select
	 * list identified by <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @return The first visible select list label
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public String getSelectedListLabel(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Returns the visible labels of the first selected elements as a list from
	 * the select list identified by <b>locator</b>.<br>
	 * <br>
	 * Fails if there is no selection.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @return The list of visible select list labels
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public List<String> getSelectedListLabels(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Select list with locator '%s' does not have any selected values.", locator));
		}

		return getLabelsForOptions(options);
	}

	/**
	 * Returns the value of the first selected element from the select list
	 * identified by <b>locator</b>.<br>
	 * <br>
	 * The return value is read from the value attribute of the selected
	 * element.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @return The first select list value
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public String getSelectedListValue(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getAttribute("value");
	}

	/**
	 * Returns the values of the first selected elements as a list from the
	 * select list identified by <b>locator</b>.<br>
	 * <br>
	 * Fails if there is no selection. The return values are read from the value
	 * attribute of the selected element.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @return The list of select list values
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public List<String> getSelectedListValues(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Select list with locator '%s' does not have any selected values.", locator));
		}

		return getValuesForOptions(options);
	}

	/**
	 * Verify the selection of the select list identified by <b>locator</b>is
	 * exactly <b>*items</b>.<br>
	 * <br>
	 * If you want to verify no option is selected, simply give no items.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @param items
	 *            The list of items to verify
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*items" })
	public void listSelectionShouldBe(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "no options";
		logging.info(String.format("Verifying list '%s' has %s selected.", locator, itemList));

		pageShouldContainList(locator);

		List<WebElement> options = getSelectListOptionsSelected(locator);
		List<String> selectedLabels = getLabelsForOptions(options);
		String message = String.format("List '%s' should have had selection [ %s ] but it was [ %s ].", locator,
				Python.join(" | ", items), Python.join(" | ", selectedLabels));
		if (items.length != options.size()) {
			throw new Selenium2LibraryNonFatalException(message);
		} else {
			List<String> selectedValues = getValuesForOptions(options);

			for (String item : items) {
				if (!selectedValues.contains(item) && !selectedLabels.contains(item)) {
					throw new Selenium2LibraryNonFatalException(message);
				}
			}
		}
	}

	/**
	 * Verify the select list identified by <b>locator</b>has no selections.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void listShouldHaveNoSelections(String locator) {
		logging.info(String.format("Verifying list '%s' has no selection.", locator));

		List<WebElement> options = getSelectListOptionsSelected(locator);
		if (!options.equals(null)) {
			List<String> selectedLabels = getLabelsForOptions(options);
			String items = Python.join(" | ", selectedLabels);
			throw new Selenium2LibraryNonFatalException(String.format(
					"List '%s' should have had no selection (selection was [ %s ]).", locator, items.toString()));
		}
	}

	@RobotKeywordOverload
	public void pageShouldContainList(String locator) {
		pageShouldContainList(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldContainList(String locator, String message) {
		pageShouldContainList(locator, message, "INFO");
	}

	/**
	 * Verify the select list identified by <b>locator</b> is found on the
	 * current page.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators and log levels.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldContainList(String locator, String message, String logLevel) {
		element.pageShouldContainElement(locator, "list", message, logLevel);
	}

	@RobotKeywordOverload
	public void pageShouldNotContainList(String locator) {
		pageShouldNotContainList(locator, "");
	}

	@RobotKeywordOverload
	public void pageShouldNotContainList(String locator, String message) {
		pageShouldNotContainList(locator, message, "INFO");
	}

	/**
	 * Verify the select list identified by <b>locator</b> is not found on the
	 * current page.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators and log levels.<br>
	 * 
	 * @param locator
	 *            The locator to locate the select list.
	 * @param message
	 *            Default=NONE. Optional custom error message.
	 * @param logLevel
	 *            Default=INFO. Optional log level.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldNotContainList(String locator, String message, String logLevel) {
		element.pageShouldNotContainElement(locator, "list", message, logLevel);
	}

	/**
	 * Select all values of the multi-select list identified by <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 */
	@RobotKeyword
	@ArgumentNames({ "locator" })
	public void selectAllFromList(String locator) {
		logging.info(String.format("Selecting all options from list '%s'.", locator));

		Select select = getSelectList(locator);
		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Select all from list' works only for multiselect lists.");
		}

		for (int i = 0; i < select.getOptions().size(); i++) {
			select.selectByIndex(i);
		}
	}

	/**
	 * Select the given <b>*items</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param items
	 *            The list of items to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*items" })
	public void selectFromList(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "all options";
		logging.info(String.format("Selecting %s from list '%s'.", itemList, locator));

		Select select = getSelectList(locator);

		// If no items given, select all values (of in case of single select
		// list, go through all values)
		if (items.length == 0) {
			for (int i = 0; i < select.getOptions().size(); i++) {
				select.selectByIndex(i);
			}
		}

		for (String item : items) {
			try {
				select.selectByValue(item);
			} catch (NoSuchElementException e1) {
				try {
					select.selectByVisibleText(item);
				} catch (NoSuchElementException e2) {
					continue;
				}
			}
		}
	}

	/**
	 * Select the given <b>*indexes</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Tries to select by value AND by label. It's generally faster to use 'by
	 * index/value/label' keywords.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param indexes
	 *            The list of indexes to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*indexes" })
	public void selectFromListByIndex(String locator, String... indexes) {
		if (indexes.length == 0) {
			throw new Selenium2LibraryNonFatalException("No index given.");
		}

		List<String> tmp = new ArrayList<String>();
		for (String index : indexes) {
			tmp.add(index);
		}
		String items = String.format("index(es) '%s'", Python.join(", ", tmp));
		logging.info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String index : indexes) {
			select.selectByIndex(Integer.parseInt(index));
		}
	}

	/**
	 * Select the given <b>*values</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param values
	 *            The list of values to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*values" })
	public void selectFromListByValue(String locator, String... values) {
		if (values.length == 0) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String.format("value(s) '%s'", Python.join(", ", values));
		logging.info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String value : values) {
			select.selectByValue(value);
		}
	}

	/**
	 * Select the given <b>*labels</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param labels
	 *            The list of labels to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*labels" })
	public void selectFromListByLabel(String locator, String... labels) {
		if (labels.length == 0) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String.format("label(s) '%s'", Python.join(", ", labels));
		logging.info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String label : labels) {
			select.selectByVisibleText(label);
		}
	}

	/**
	 * Unselect the given <b>*items</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * As a special case, giving an empty *items list will remove all
	 * selections.<br>
	 * <br>
	 * Tries to unselect by value AND by label. It's generally faster to use 'by
	 * index/value/label' keywords.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param items
	 *            The list of items to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*items" })
	public void unselectFromList(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "all options";
		logging.info(String.format("Unselecting %s from list '%s'.", itemList, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		if (items.length == 0) {
			select.deselectAll();

			return;
		}

		for (String item : items) {
			select.deselectByValue(item);
			select.deselectByVisibleText(item);
		}
	}

	/**
	 * Unselect the given <b>*indexes</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param indexes
	 *            The list of indexes to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*indexes" })
	public void unselectFromListByIndex(String locator, Integer... indexes) {
		if (indexes.equals(null)) {
			throw new Selenium2LibraryNonFatalException("No index given.");
		}

		List<String> tmp = new ArrayList<String>();
		for (Integer index : indexes) {
			tmp.add(index.toString());
		}
		String items = String.format("index(es) '%s'", Python.join(", ", tmp));
		logging.info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (int index : indexes) {
			select.deselectByIndex(index);
		}
	}

	/**
	 * Unselect the given <b>*values</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param values
	 *            The list of values to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*values" })
	public void unselectFromListByValue(String locator, String... values) {
		if (values.equals(null)) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String.format("value(s) '%s'", Python.join(", ", values));
		logging.info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (String value : values) {
			select.deselectByValue(value);
		}
	}

	/**
	 * Unselect the given <b>*labels</b> of the multi-select list identified by
	 * <b>locator</b>.<br>
	 * <br>
	 * Select list keywords work on both lists and combo boxes. Key attributes
	 * for select lists are id and name. See `Introduction` for details about
	 * locators.<br>
	 * 
	 * @param locator
	 *            The locator to locate the multi-select list.
	 * @param labels
	 *            The list of labels to select
	 */
	@RobotKeyword
	@ArgumentNames({ "locator", "*labels" })
	public void unselectFromListByLabel(String locator, String... labels) {
		if (labels.equals(null)) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String.format("label(s) '%s'", Python.join(", ", labels));
		logging.info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (String label : labels) {
			select.deselectByVisibleText(label);
		}
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected List<String> getLabelsForOptions(List<WebElement> options) {
		List<String> labels = new ArrayList<String>();

		for (WebElement option : options) {
			labels.add(option.getText());
		}

		return labels;
	}

	protected Select getSelectList(String locator) {
		List<WebElement> webElements = element.elementFind(locator, true, true, "select");

		return new Select(webElements.get(0));
	}

	protected List<WebElement> getSelectListOptions(Select select) {
		return new ArrayList<WebElement>(select.getOptions());
	}

	protected List<WebElement> getSelectListOptions(String locator) {
		Select select = getSelectList(locator);

		return getSelectListOptions(select);
	}

	protected List<WebElement> getSelectListOptionsSelected(String locator) {
		Select select = getSelectList(locator);

		return new ArrayList<WebElement>(select.getAllSelectedOptions());
	}

	protected List<String> getValuesForOptions(List<WebElement> options) {
		ArrayList<String> labels = new ArrayList<String>();

		for (WebElement option : options) {
			labels.add(option.getAttribute("value"));
		}

		return labels;
	}

	protected boolean isMultiselectList(Select select) {
		return select.isMultiple();
	}

}
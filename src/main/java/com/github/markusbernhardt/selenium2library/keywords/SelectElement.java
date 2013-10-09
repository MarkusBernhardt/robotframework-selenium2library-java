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
	private Element element;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	private Logging logging;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Returns the values in the select list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.\n")
	@ArgumentNames({ "locator" })
	public List<String> getListItems(String locator) {
		List<WebElement> options = this.getSelectListOptions(locator);

		return getLabelsForOptions(options);
	}

	@RobotKeyword("Returns the visible label of the selected element from the select list "
			+ "identified by _locator_.\n\n"

			+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.\n")
	@ArgumentNames({ "locator" })
	public String getSelectedListLabel(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getText();
	}

	@RobotKeyword("Returns the visible labels of selected elements (as a list) from the select "
			+ "list identified by _locator_.\n\n"

			+ "Fails if there is no selection.\n\n"

			+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.\n")
	@ArgumentNames({ "locator" })
	public List<String> getSelectedListLabels(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Select list with locator '%s' does not have any selected values.", locator));
		}

		return getLabelsForOptions(options);
	}

	@RobotKeyword("Returns the value of the selected element from the select list identified " + "by _locator_.\n\n"

	+ "Return value is read from value attribute of the selected element.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.\n")
	@ArgumentNames({ "locator" })
	public String getSelectedListValue(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getAttribute("value");
	}

	@RobotKeyword("Returns the values of selected elements (as a list) from the select list "
			+ "identified by _locator_.\n\n"

			+ "Fails if there is no selection.\n\n"

			+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.\n")
	@ArgumentNames({ "locator" })
	public List<String> getSelectedListValues(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new Selenium2LibraryNonFatalException(String.format(
					"Select list with locator '%s' does not have any selected values.", locator));
		}

		return getValuesForOptions(options);
	}

	@RobotKeyword("Verifies the selection of select list identified by _locator_ is exactly _*items_.\n\n"

	+ "If you want to test that no option is selected, simply give no _items_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.\n")
	@ArgumentNames({ "locator", "*items" })
	public void listSelectionShouldBe(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "no options";
		logging.info(String.format("Verifying list '%s' has %s selected.", locator, itemList));

		this.pageShouldContainList(locator);

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

	@RobotKeyword("Verifies select list identified by _locator_ has no selections.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for "
			+ "select lists are id and name. See `Introduction` for details about locating " + "elements.")
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

	@RobotKeyword("Verifies select list identified by _locator_ is found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_" + " arguments.\n\n"

	+ "Key attributes for lists are id and name. See `Introduction` for details about " + "locating elements.")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
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

	@RobotKeyword("Verifies select list identified by _locator_ is not found on current page.\n\n"

	+ "See `Page Should Contain Element` for explanation about _message_ and _loglevel_" + " arguments.\n\n"

	+ "Key attributes for lists are id and name. See `Introduction` for details about " + "locating elements.\n")
	@ArgumentNames({ "locator", "message=NONE", "loglevel=INFO" })
	public void pageShouldNotContainList(String locator, String message, String logLevel) {
		element.pageShouldNotContainElement(locator, "list", message, logLevel);
	}

	@RobotKeyword("Selects all values from multi-select list identified by _locator_.\n\n"

	+ "Key attributes for lists are id and name. See `Introduction` for details about " + "locating elements.")
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

	@RobotKeyword("Selects all values from multi-select list identified by _locator_.\n\n"

	+ "Key attributes for lists are id and name. See `Introduction` for details about " + "locating elements.")
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

	@RobotKeyword("Selects _*indexes_ from list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Selects _*values_ from list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Selects _*labels_ from list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Unselects given values from select list identified by _locator_.\n\n"

	+ "As a special case, giving empty list as _*items_ will remove all selections.\n\n"

	+ "_*items_ try to unselect by value AND by label.\n\n"

	+ "It's faster to use 'by index/value/label' functions.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Unselects _*indexes_ from list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Unselects _*values_ from list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	@RobotKeyword("Unselects _*labels_ from list identified by _locator_.\n\n"

	+ "Select list keywords work on both lists and combo boxes. Key attributes for select "
			+ "lists are id and name. See `Introduction` for details about locating elements.")
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

	private List<String> getLabelsForOptions(List<WebElement> options) {
		List<String> labels = new ArrayList<String>();

		for (WebElement option : options) {
			labels.add(option.getText());
		}

		return labels;
	}

	private Select getSelectList(String locator) {
		List<WebElement> webElements = element.elementFind(locator, true, true, "select");

		return new Select(webElements.get(0));
	}

	private List<WebElement> getSelectListOptions(Select select) {
		return new ArrayList<WebElement>(select.getOptions());
	}

	private List<WebElement> getSelectListOptions(String locator) {
		Select select = getSelectList(locator);

		return getSelectListOptions(select);
	}

	private List<WebElement> getSelectListOptionsSelected(String locator) {
		Select select = getSelectList(locator);

		return new ArrayList<WebElement>(select.getAllSelectedOptions());
	}

	private List<String> getValuesForOptions(List<WebElement> options) {
		ArrayList<String> labels = new ArrayList<String>();

		for (WebElement option : options) {
			labels.add(option.getAttribute("value"));
		}

		return labels;
	}

	private boolean isMultiselectList(Select select) {
		return select.isMultiple();
	}

}
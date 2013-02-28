package org.robotframework.selenium2library.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.robotframework.selenium2library.Selenium2LibraryNonFatalException;
import org.robotframework.selenium2library.utils.Python;

public abstract class SelectElement extends Screenshot {

	// ##############################
	// Keywords
	// ##############################

	public List<String> getListItems(String locator) {
		List<WebElement> options = this.getSelectListOptions(locator);

		return getLabelsForOptions(options);
	}

	public String getSelectedListLabel(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getAttribute("label");
	}

	public List<String> getSelectedListLabels(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Select list with locator '%s' does not have any selected values.",
							locator));
		}

		return getLabelsForOptions(options);
	}

	public String getSelectedListValue(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getAttribute("value");
	}

	public List<String> getSelectedListValues(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"Select list with locator '%s' does not have any selected values.",
							locator));
		}

		return getValuesForOptions(options);
	}

	public void listSelectionShouldBe(String locator, List<String> items) {
		String itemList = items.size() != 0 ? String.format("option(s) [ %s ]",
				Python.join(" | ", items)) : "no options";
		info(String.format("Verifying list '%s' has %s selected.", locator,
				itemList));

		this.pageShouldContainList(locator);

		List<WebElement> options = getSelectListOptionsSelected(locator);
		if (items.size() > 0 || options.size() > 0) {
			List<String> selectedValues = getValuesForOptions(options);
			List<String> selectedLabels = getLabelsForOptions(options);

			String message = String
					.format("List '%s' should have had selection [ %s ] but it was [ %s ].",
							locator, Python.join(" | ", items),
							Python.join(" | ", selectedLabels));

			for (String item : items) {
				if (!selectedValues.contains(item)
						|| !selectedLabels.contains(item)) {
					throw new Selenium2LibraryNonFatalException(message);
				}
			}

			Map<String, String> map = Python
					.zip(selectedValues, selectedLabels);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (!items.contains(entry.getKey())
						|| !items.contains(entry.getValue())) {
					throw new Selenium2LibraryNonFatalException(message);
				}
			}
		}
	}

	public void listShouldHaveNoSelections(String locator) {
		info(String.format("Verifying list '%s' has no selection.", locator));

		List<WebElement> options = getSelectListOptionsSelected(locator);
		if (!options.equals(null)) {
			List<String> selectedLabels = getLabelsForOptions(options);
			String items = Python.join(" | ", selectedLabels);
			throw new Selenium2LibraryNonFatalException(
					String.format(
							"List '%s' should have had no selection (selection was [ %s ]).",
							locator, items.toString()));
		}
	}

	public void pageShouldContainList(String locator) {
		pageShouldContainList(locator, "");
	}

	public void pageShouldContainList(String locator, String message) {
		pageShouldContainList(locator, message, "INFO");
	}

	public void pageShouldContainList(String locator, String message,
			String logLevel) {
		this.pageShouldContainElement(locator, "list", message, logLevel);
	}

	public void pageShouldNotContainList(String locator) {
		pageShouldNotContainList(locator, "");
	}

	public void pageShouldNotContainList(String locator, String message) {
		pageShouldNotContainList(locator, message, "INFO");
	}

	public void pageShouldNotContainList(String locator, String message,
			String logLevel) {
		this.pageShouldNotContainElement(locator, "list", message, logLevel);
	}

	public void selectAllFromList(String locator) {
		info(String.format("Selecting all options from list '%s'.", locator));

		Select select = getSelectList(locator);
		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Select all from list' works only for multiselect lists.");
		}

		for (int i = 0; i < select.getOptions().size(); i++) {
			select.selectByIndex(i);
		}
	}

	public void selectFromList(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]",
				Python.join(" | ", items)) : "all options";
		info(String.format("Selecting %s from list '%s'.", itemList, locator));

		Select select = getSelectList(locator);

		if (items.length != 0) {
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

	public void selectFromListByIndex(String locator, String... indexes) {
		if (indexes.length == 0) {
			throw new Selenium2LibraryNonFatalException("No index given.");
		}

		List<String> tmp = new ArrayList<String>();
		for (String index : indexes) {
			tmp.add(index);
		}
		String items = String.format("index(es) '%s'", Python.join(", ", tmp));
		info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String index : indexes) {
			select.selectByIndex(Integer.parseInt(index));
		}
	}

	public void selectFromListByValue(String locator, String... values) {
		if (values.length == 0) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String
				.format("value(s) '%s'", Python.join(", ", values));
		info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String value : values) {
			select.selectByValue(value);
		}
	}

	public void selectFromListByLabel(String locator, String... labels) {
		if (labels.length == 0) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String
				.format("label(s) '%s'", Python.join(", ", labels));
		info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String label : labels) {
			select.selectByVisibleText(label);
		}
	}

	public void unselectFromList(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]",
				Python.join(" | ", items)) : "all options";
		info(String.format("Unselecting %s from list '%s'.", itemList, locator));

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

	public void unselectFromListByIndex(String locator, Integer... indexes) {
		if (indexes.equals(null)) {
			throw new Selenium2LibraryNonFatalException("No index given.");
		}

		List<String> tmp = new ArrayList<String>();
		for (Integer index : indexes) {
			tmp.add(index.toString());
		}
		String items = String.format("index(es) '%s'", Python.join(", ", tmp));
		info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (int index : indexes) {
			select.deselectByIndex(index);
		}
	}

	public void unselectFromListByValue(String locator, String... values) {
		if (values.equals(null)) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String
				.format("value(s) '%s'", Python.join(", ", values));
		info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new Selenium2LibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (String value : values) {
			select.deselectByValue(value);
		}
	}

	public void unselectFromListByLabel(String locator, String... labels) {
		if (labels.equals(null)) {
			throw new Selenium2LibraryNonFatalException("No value given.");
		}

		String items = String
				.format("label(s) '%s'", Python.join(", ", labels));
		info(String.format("Unselecting %s from list '%s'.", items, locator));

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
		List<WebElement> elements = elementFind(locator, true, true, "select");

		return new Select(elements.get(0));
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
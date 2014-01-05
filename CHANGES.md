CHANGES
=======

unreleased
----------
* Update selenium to 2.39.0
* Update phantomjsdriver to 1.1.0
* DesiredCapabilities can be specified as complex JSON strings now
* Removed the ffProfileDir arguments from Open Browser

1.4.0.1
-------

* Fixed wrong separator in Javadoc2Libdoc. See #40
* Fixed Page Should Contain Button fails to find buttons with the button tag. See #43

1.4.0.0
-------

* Porting recent changes in the master branch of the Python Selenium2Library
  from id b4a3e500 until cf971d91 to this Java port. This contains roughly:
  * Added 'Get Window Size' and 'Set Window Size' keywords matching the Selenium functionality.
  * Added new keyword 'Click Element At Coordinates'.
  * Added keywords for verifying text entered into textarea elements.
    * 'Textarea Should Contain'
    * 'Textarea Should Not Contain'
    * 'Textarea Value Should Be'
    * 'Textarea Value Should Not Be'
  * 'Mouse Up' doesn't click any more on the element.
  * Raise exception in selecting non-existing item in list. Error handling varies
    between single-select and multi-select lists. See keyword documentation for
    more information.
* Back-port recent changes from version 1.3 and 1.4 of Python library. See #35
* Jump Version number to 1.4.0.0 to reflect the new version of the Python library.

1.2.0.14
--------

* Fixed an ArrayIndexOutOfBoundsException in the Select Window keyword. See #27
* Added the possibility to set logging directory. See #28.
* Refactoring of the library to a JavaLibCore AnnotationLibrary. See #28.
* Added keyword documentation from library for e.g. Ride. See #16, #28.
* Added keyword missing documentation for new keywords. See #32.
* Added access to the current WebDriver instance from custom libraries. #30
* Generating libdoc from javadoc.

1.2.0.13
--------

* Fixed a NullPointerException in Capture Page Screenshot when Log File is set to NONE. See #24
* Fixed that library can't be instrumented with JaCoCo. See #22
* Update robotframework to 2.8.1
* Update robotframework-maven-plugin to 1.2
* Update aspectj to 1.7.3
* Update java.version to 1.7

1.2.0.12
--------

* Fixed a NullPointerException in Close Browser. See #23

1.2.0.11
--------

* Fixed a NullPointerException in WebDriverCache.getCurrent. See #21

1.2.0.10
--------

* Fixed a TypeError in List Selection Should Be. See #18.
* Fixed a problem with logging a string longer then 1024 characters on Windows systems. See #17.
* Porting recent changes in the master branch of the Python Selenium2Library
  from id 6793340d until b4a3e500 to this Java port. This contains roughly:
  * Keyword 'Current Frame Should Not Contain'
  * Tag 'option' also marks an form element
  

1.2.0.9
-------

* Updated Selenium to 2.33.0
* Updated Robot Framework to 2.8
* Fixing a bug in "Select From List" that leads to traversing all list entries before selecting 
  the correct one. See issue #12. 
* New keywords:
  * Get Remote Capabilities
  * Log Remote Capabilities
  * Get System Info
  * Log System Info

1.2.0.8
-------

* More sophisticated algorithm to create new session ids at Open Browser. See issue #11 and #14.

1.2.0.7
-------

* Fixing a ConcurrentModificationException when using the keyword 'Close All Browsers'. See issue #11.

1.2.0.6
-------

* Fixing a IllegalArgumentException when using the keyword 'Select From List'. See issue #8.
* Renamed keyword "Textfield Should Be" to "Textfield Value Should Be" to be in sync with Python library. See issue #9.
* New keywords "Get Remote Session Id" and "Log Remote Session Id". See issue #10.
  
1.2.0.5
-------

* Fixing stupid bugs in the new logging functionality

1.2.0.4
-------

* Log messages larger than 1kB are now written to a temp file on the Java side and then read back
  on the Python side. This avoids long messages to get parsed by the Jython source code parser.
  See issue #7.

1.2.0.3
-------

* Rerelease of 1.2.0.2, because the upload to maven central was corrupt

1.2.0.2
-------

* Support for iPhone, iPad and Android started. See issue #6.

1.2.0.1
-------

* Added a delimiter to custom locator strategies to enable multiple arguments. See issue #3.

1.2.0.0
-------

* Upgrading to version 1.2.0 of the Python Selenium2Library
* Porting recent changes in the master branch of the Python Selenium2Library
  from id 2d3adce8 until 6793340d to this Java port. This contains roughly:
  * Add Cookie keyword
  * Fixed 'Get Selected List Label' under IE7 or IE8.

1.1.0.7
-------

* Fixing a problem at opening a Safari, Opera or PhantomJS browser that caused a Selenium2LibraryFatalException("... is not a supported browser.")
* Fixing a problem with screenshots not showing in the log
* Added a jar with all required dependencies to the deployed packages   

1.1.0.6
-------

* ArrayIndexOutOfBoundsException at locators without prefix fixed.
* Default Selenium dependency updated to 2.32.0
* Support for Safari browser
* Porting recent changes in the master branch of the Python Selenium2Library
  from id 966a4c5f until 2d3adce8 to this Java port. This contains roughly:
  * Support for PhantomJS browser
  * jquery/sizzle locator
  

1.1.0.5
-------

* Added the following keyword:
  * Add Location Strategy
  
    This keyword is somewhat analogous to the SeleniumLibrary (RC) keyword. 
    It can be used to register a JavaScript function as locator. 
* Changed all ThreadLocal<PythonInterpreter> to static


1.1.0.4
-------

* Added proxy handling for RemoteWebDriver
* Minor fixes


1.1.0.3
-------

* Changed the wrongly set artifactId from selenium2library to 
  robotframework-selenium2library-java.


1.1.0.2
-------

* Extended com.github.markusbernhardt.selenium2library.keywords.Waiting.waitUntil
  to keep waiting, when a Throwable is thrown.
* Added the following keyword:
  * Wait Until Element Is Successfully Clicked
  
    In a AJAX site it sometimes happens, that an element disappears between
    a call to Wait Until Element Is Visible and Click Element. This keyword
    simply tries to click on the element until it worked or the timeout
    occurs. 
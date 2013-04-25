CHANGES
=======

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
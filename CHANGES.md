CHANGES
=======

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
  
    In a AJAX site it sometimes happen, that an element disappears between
    a call to Wait Until Element Is Visible and Click Element. This keyword
    simply tries to click on the element until it worked or the timeout
    occurs. 
Java port of the Selenium 2 (WebDriver) Python library for Robot Framework
==========================================================================

Introduction
------------

This Java port of the existing Selenium2Library was created to enable
the usage of a Selenium 2 library with Jython.
* Python Selenium2Library needs Python 2.6 upwards
* The latests stable release of Jython is 2.5.3
* Jython 2.7a2 is a alpha version and missing essential features
* There seems to be no further development of a stable Jython 2.7

Usage
-----

This library is a direct replacement to the Python Selenium2Library.
There are almost no changes necesssary to the existing code. You 
can execute the same testcases and keywords with Python and Jython.

If you are using the robotframework-maven-plugin you can (in the 
short future) use this library by adding the following dependency to 
your pom.xml:

    <dependency>
        <groupId>org.robotframework</groupId>
        <artifactId>robotframework-selenium2library-java</artifactId>
        <version>1.1.0.0</version>
        <scope>test</scope>
    </dependency>

Small differences
-----------------

* Robotframework does not support providing arguments to Java libraries

  With Python it is possible to provide arguments to libraries
  
        Library    Selenium2Library    15    30    run_on_failure=Log Source
  
  This is not possible with a Java Library. But it can be done in a way
  that works with Java and Python
  
  > *** Settings ***
  > Library    Selenium2Library
  >
  > *** Keywords ***
  > Open
  >     Open Browser    <url>    <browser>
  >     Set Selenium Timeout    15
  >     Set Selenium Implicit Wait    30
  >     Register Keyword To Run On Failure    Log Source

* Robotframework does not support named arguments with Java libraries

  It is not possible to use named arguments. See User Guide 2.2.2
  
* Selenium Speed not implemented

  Setting the Selenium Speed is deprecated several years and not
  implemented in WebDriver. The Python Selenium2Library tries to
  emulate the old behavior. I have not implemented this emulation
  for the following reasons.
  
  * As far as I understand the emulation is broken and only works
    with RemoteWebDriver
  * I do not know how to implement that in a correct way with Java 
  * There is a reason, why this is not implemented in WebDriver.
    It's a bad idea.

Enhancements
------------

Our application is heavily using AJAX. So much more waiting keywords
are needed. The list of new keywords:

* Element Should Be Clickable
* Element Should Not Be Clickable
* Element Should Be Selected
* Element Should Not Be Selected
* Element Should Not Contain
* Element Text Should Not Be
* Wait Until Element Is Clickable
* Wait Until Element Is Not Clickable
* Wait Until Element Is Selected
* Wait Until Element Is Not Selected
* Wait Until Element Is Visible
* Wait Until Element Is Not Visible
* Wait Until Page Not Contains
* Wait Until Page Not Contains Element
* Wait Until Title Is
* Wait Until Title Is Not
* Wait Until Title Contains
* Wait Until Title Not Contains

Demo
----

This is a maven project. If you have firefox installed,
you can execute the unit tests with:

    mvn integration-test
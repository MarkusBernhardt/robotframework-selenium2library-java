Java port of the Selenium 2 (WebDriver) Python library for Robot Framework
==========================================================================

Introduction
------------

Selenium2Library is a web testing library for Robot Framework that leverages
the [Selenium 2 (WebDriver)](http://docs.seleniumhq.org/docs/03_webdriver.jsp)
libraries from the [Selenium](http://docs.seleniumhq.org) project.

It is modeled after (and forked from) the Robot Framework
[SeleniumLibrary](http://code.google.com/p/robotframework-seleniumlibrary/)
library, but re-implemented to use Selenium 2 and WebDriver technologies.

More information about this library can be found in the
[Keyword Documentation](http://search.maven.org/remotecontent?filepath=com/github/markusbernhardt/robotframework-selenium2library-java/1.2.0.14/robotframework-selenium2library-java-1.2.0.14-libdoc.html).

For keyword completion in RIDE you can download this
[Library Specs](http://search.maven.org/remotecontent?filepath=com/github/markusbernhardt/robotframework-selenium2library-java/1.2.0.14/robotframework-selenium2library-java-1.2.0.14-libdoc.xml)
and place it in your PYTHONPATH.

This Java port of the existing Selenium2Library was created to enable
the usage of a Selenium 2 library with Jython.
* Python Selenium2Library needs Python 2.6 upwards
* The latests stable release of Jython is 2.5.3
* Jython 2.7b2 is a beta version
* There seems to be only slow development of a stable Jython 2.7

Usage
-----

This library is a direct replacement to the Python Selenium2Library.
There are almost no changes necesssary to the existing code. You 
can execute the same testcases and keywords with Python and Jython.

If you are using the robotframework-maven-plugin you can
use this library by adding the following dependency to 
your pom.xml:

    <dependency>
        <groupId>com.github.markusbernhardt</groupId>
        <artifactId>robotframework-selenium2library-java</artifactId>
        <version>1.2.0.14</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the
[jar-with-dependencies](http://search.maven.org/remotecontent?filepath=com/github/markusbernhardt/robotframework-selenium2library-java/1.2.0.14/robotframework-selenium2library-java-1.2.0.14-jar-with-dependencies.jar),
which contains all required libraries.

If you want more control and feel adventurous you could you use this
[jar](http://search.maven.org/remotecontent?filepath=com/github/markusbernhardt/robotframework-selenium2library-java/1.2.0.14/robotframework-selenium2library-java-1.2.0.14.jar)
and provide all required libraries from this [list](DEPENDENCIES.md) on your own.

Differences
-----------

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

Demo
----

This is a maven project. If you have firefox installed,
you can execute the unit tests with:

    mvn integration-test

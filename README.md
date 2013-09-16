Java port of the Selenium 2 (WebDriver) Python library for Robot Framework
==========================================================================

Introduction
------------

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
        <version>1.2.0.13</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the [jar-with-dependencies](http://search.maven.org/remotecontent?filepath=com/github/markusbernhardt/robotframework-selenium2library-java/1.2.0.13/robotframework-selenium2library-java-1.2.0.13-jar-with-dependencies.jar), which contains all required libraries.

If you want more control and feel adventurous you could you use this [jar](http://search.maven.org/remotecontent?filepath=com/github/markusbernhardt/robotframework-selenium2library-java/1.2.0.13/robotframework-selenium2library-java-1.2.0.13.jar) and provide all required libraries from this [list](DEPENDENCIES.md) on your own.

Road map
--------

Because I had very few time the last 2 months to take care of the project here my road map of the upcoming topics:
* (Done) Make a release with Java 7
* Fix Issues 
  * (Done) #27
  * #28
  * #30
  * #31
* Back-port recent changes from version 1.3 and 1.4 of python library
* Switch to javaLibCore
* Fix Issues
  * #16
  * #26

Differences
-----------

* Robotframework does not support providing arguments to Java libraries

  With Python it is possible to provide arguments to libraries
  
        *** Settings ***
        Library    Selenium2Library    15    30    run_on_failure=Log Source
  
  This is not possible with a Java Library. But it can be done in a way
  that works with Java and Python
  
        *** Settings ***
        Library    Selenium2Library
        
        *** Keywords ***
        Open
            Open Browser    <url>    <browser>
            Set Selenium Timeout    15
            Set Selenium Implicit Wait    30
            Register Keyword To Run On Failure    Log Source

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

<dl>
  <dt>More browser supported</dt>
<dd>
<ul>
<li>ipad</li>
<li>safari</li>
</ul>
</dd>

  <dt>Custom JavaScript Locators</dt>
<dd>We added the keyword Add Location Strategy. This keyword is somewhat
analogous to the SeleniumLibrary (RC) keyword. It can be used to register
a JavaScript function as locator with the specified strategy name. The 
registered function has to return a WebElement, a List of WebElements or
null. This feature was requested in issue #1.

Since 1.2.0.1 an optional delimiter can be specified. See #3 for more info.
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Add Location Strategy</td><td>strategy_name, function_definition, delimiter=NONE</td></tr>
</table>
</dd>
</dl>

A small example:

        Selenium2Library.Add Location Strategy    elementById    return window.document.getElementById(arguments[0]);
        Input Text    elementById=firstName    Max

A small example with delimiter:

        Selenium2Library.Add Location Strategy    elementByClassname    return window.document.getElementsByClassName(arguments[0])[arguments[1]];    ,
        Input Text    elementByClassname=inputField,0    Max
        Input Text    elementByClassname=inputField,1    Mustermann

  
<dl>
  <dt>Proxy Handling</dt>
<dd>We added proper proxy handling for RemoteWebDriver instances. This is
needed, if you want to connect to an external Selenium grid through a
local HTTP proxy. Our implementation handles BASIC, DIGEST and NTLM
based authentication schemes correctly. We added the following keyword.
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Set Remote Web Driver Proxy</td><td>host,port,user=NONE,password=NONE,domain=NONE,workstation=NONE</td></tr>
</table>
Some additional info:
<ul>
<li>If you set a proxy, it will be used for all subsequent calls of Open Browser</li>
<li>You can remove the proxy by calling: Set Remote Web Driver Proxy    ${EMPTY}    ${EMPTY}</li>
<li>If you provide no username, we are looking for a username at the Java property http.proxyUser and the environment variables HTTP_PROXY and http_proxy. If we find a username, it is only used, if the host and port also match.</li>
<li>If you provide no password, we are looking for a password at the Java property http.proxyUser and the environment variables HTTP_PROXY and http_proxy. If we find a password, it is only used, if the host, port and username also match.</li>
<li>If you provide a domain, we use NTLM based authentication
<li>If you provide no workstation and we use NTLM based authentication, we use the hostname as workstation name
</ul>
</dd>

  <dt>Waiting</dt>
<dd>Our application is heavily using AJAX. So much more waiting keywords
are needed. The list of new keywords:
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Element Should Be Clickable</td><td>locator,message=NONE</td></tr>
  <tr><td>Element Should Not Be Clickable</td><td>locator,message=NONE</td></tr>
  <tr><td>Element Should Be Selected</td><td>locator,message=NONE</td></tr>
  <tr><td>Element Should Not Be Selected</td><td>locator,message=NONE</td></tr>
  <tr><td>Element Should Not Contain</td><td>locator,expected,message=NONE</td></tr>
  <tr><td>Element Should Text Should Not Be</td><td>locator,expected,message=NONE</td></tr>
  <tr><td>Wait Until Element Is Clickable</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Element Is Successfully Clicked</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Element Is Not Clickable</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Element Is Selected</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Element Is Not Selected</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Element Is Not Visible</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Page Not Contains</td><td>condition,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Page Not Contains Element</td><td>locator,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Title Is</td><td>title,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Title Is Not</td><td>title,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Title Contains</td><td>title,timeout=NONE,message=NONE</td></tr>
  <tr><td>Wait Until Title Not Contains</td><td>title,timeout=NONE,message=NONE</td></tr>
</table>
</dd>

  <dt>XPath Count</dt>
<dd>We extended the following keywords to be called with a
xpath statement that optionally can start with "xpath=".
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Get Matching Xpath Count</td><td>xpath</td></tr>
  <tr><td>Xpath Should Match X Times</td><td>xpath, expected_xpath_count, message=NONE, loglevel=INFO</td></tr>
</table>
</dd>

  <dt>Remote Session Id</dt>
<dd>We introduced two keywords to get the session id from the
RemoteWebDriver. This is mainly needed to identify the test 
session at external grids like SauceLabs.
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Get Remote Session Id</td><td></td></tr>
  <tr><td>Log Remote Session Id</td><td></td></tr>
</table>
</dd>

  <dt>Remote Capabilities</dt>
<dd>According to the Javadoc no error is thrown, if a session cannot support a capability that is requested
in the desired capabilities. Therefore it can happen that you are silently running your tests on the wrong
environment. With the following two keywords you can control what capabilities the actual session supports.
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Get Remote Capabilities</td><td></td></tr>
  <tr><td>Log Remote Capabilities</td><td></td></tr>
</table>
</dd>

  <dt>System Info</dt>
<dd>The following two keywords are outputting the current system info as in the WebDriverException.
<table>
  <tr><th>Keyword</th><th>Arguments</th></tr>
  <tr><td>Get System Info</td><td></td></tr>
  <tr><td>Log System Info</td><td></td></tr>
</table>
</dd>
</dl>

Demo
----

This is a maven project. If you have firefox installed,
you can execute the unit tests with:

    mvn integration-test

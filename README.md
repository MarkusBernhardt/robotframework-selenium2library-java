Java port of the Python based SeleniumLibrary for Robot Framework
==========================================================================

Introduction
------------

SeleniumLibrary is a web testing library for Robot Framework that leverages
the Selenium libraries from the [Selenium](http://docs.seleniumhq.org) project.

Version number follows Selenium's version numbers (first 3 numbers), and 4th number is reserved for e.g. issue fixes.
* More information about this library can be found in the
  [Keyword Documentation](http://search.maven.org/remotecontent?filepath=com/github/hi_fi/robotframework-seleniumlibrary-java/2.53.1.0/robotframework-seleniumlibrary-java-2.53.1.0-libdoc.html).
* For keyword completion in RIDE you can download this
  [Library Specs](http://search.maven.org/remotecontent?filepath=com/github/hi_fi/robotframework-seleniumlibrary-java/2.53.1.0/robotframework-seleniumlibrary-java-2.53.1.0-libdoc.xml)
  and place it in your PYTHONPATH.

This Java version of SeleniumLibrary exists because of easier dependency management when Robot Framework is used in Java-projects.

Usage
-----

This library is a quite direct alternative to the Python SeleniumLibrary.

If you are using the robotframework-maven-plugin you can
use this library by adding the following dependency to 
your pom.xml:

    <dependency>
        <groupId>com.github.hi_fi</groupId>
        <artifactId>robotframework-seleniumlibrary-java</artifactId>
        <version>2.53.1.0</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the
[jar-with-dependencies](http://search.maven.org/remotecontent?filepath=com/github/hi_fi/robotframework-seleniumlibrary-java/2.53.1.0/robotframework-seleniumlibrary-java-2.53.1.0-jar-with-dependencies.jar),
which contains all required libraries.

If you want more control and feel adventurous you could you use this
[jar](http://search.maven.org/remotecontent?filepath=com/github/hi_fi/robotframework-seleniumlibrary-java/2.53.1.0/robotframework-seleniumlibrary-java-2.53.1.0.jar)
and provide all required libraries from this [list](DEPENDENCIES.md) on your own.

Differences
-----------

* Some keyword differences between this and [Python version](https://github.com/robotframework/SeleniumLibrary) exists. (Same) keywords should be aligned in upcoming versions.

Demo
----

This is a maven project. If you have firefox installed,
you can execute the unit tests with:

    mvn integration-test

Getting Help
------------

The [user group for Robot Framework](https://groups.google.com/forum/#!forum/robotframework-users)
is the best place to get help. Consider including in the post:
* Full description of what you are trying to do and expected outcome
* Version number of SeleniumLibrary, Robot Framework, and Selenium
* StackTraces or other debug output containing error information
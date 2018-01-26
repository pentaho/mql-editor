# MQL Editor #


#### Pre-requisites for building the project:
* Maven, version 3+
* Java JDK 1.8
* This [settings.xml](https://github.com/pentaho/maven-parent-poms/blob/master/maven-support-files/settings.xml) in your <user-home>/.m2 directory

#### Building it


```
$ mvn clean install
```

To include assembly and generate the package add the `mqlPackage` property.
```
$ mvn clean install -DmqlPackage
```

#### Running the tests

__Unit tests__

This will run all tests in the project (and sub-modules).
```
$ mvn test
```

If you want to remote debug a single java unit test (default port is 5005):
```
$ cd impl
$ mvn test -Dtest=<<YourTest>> -Dmaven.surefire.debug
```

__Deprecations__

Note that the pentaho-mql-editor-debug module was removed during mavenization because it was no longer being used
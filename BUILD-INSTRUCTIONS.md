## IMPORTANT LIB

Serposcope rely on these libraries (among many other).

* The web framework [ninjaframework](http://www.ninjaframework.org/).
* [Guice](https://github.com/google/guice) for dependency injection.
* The query builder [querydsl](http://querydsl.com) allows autocompletion of SQL query, prevents SQL injections, etc.

You don't need to know these libraries to contribute to serposcope (you'll learn!) but they worth a check :)

## QUERYBUILDER & SQL

If first build, initialize the querybuilder database : 

`./core/scripts/sqlcodegen.sh` (or `.\core\scripts\sqlcodegen.bat`)

This script will create the structure of the database, it is sourced by [querydsl](http://www.querydsl.com/) to generate querybuilder classes.

You only need to run this script on first build or each time you change the structure of the database.

If you plan to edit SQL, table name must be in uppercase (allow compatibility of .sql files between h2 and MySQL).

## SUPERDEVMODE

When you code, you want to stay in [superdevmode](http://www.ninjaframework.org/documentation/basic_concepts/super_dev_mode.html). It allows hot reloading of the classes when you edit code in your IDE (so you don't have to restart serposcope to see changes).

To start serposcope in superdevmode go in the **serposcope/web/** folder and run the following command : 

```
mvn ninja:run \
-Dninja.mainClass=serposcope.lifecycle.Daemon \
-Dninja.jvmArgs="-Dlogback.configurationFile=dev/logback-stdout.xml -Dserposcope.datadir=/var/tmp/serposcope -Dserposcope.db.options=;AUTO_SERVER=TRUE"
```

## BUILD FOR PROD

To build the package for production (compiled javascripts and single jar) : 

`mvn -Dsinglejar=true -Dminify=true -DskipTests clean install`

Assuming no errors occurred, the .jar file will be located at **web\target\serposcope.jar**.

### UPGRADING

This fork does not contain installation files, so install from 2.15.0 (if required) and then do the following to upgrade.

* If Serposcope is already running, stop it. For example:

`systemctl stop serposcope`

* Overwrite the original .jar file with the newly built file. For example:

`cp serposcope.jar /usr/share/serposcope/serposcope.jar`

* Restart Serposcope. For example:

`systemctl start serposcope`

## RUNNING INTEGRATION TESTS

**AS OF 2.16.0, TESTS ARE NO LONGER USED BUT ARE BEING KEPT HERE FOR POSTERITY**

To run integration tests : 

```
cd serposcope/core; mvn -Dtest=all failsafe:integration-test
cd serposcope/scraper; mvn -Dtest=all failsafe:integration-test
cd serposcope/web; mvn -Dtest=all -Dninja.standalone.class=serposcope.lifecycle.MyNinjaJetty failsafe:integration-test
```

Check also for testconfig.DEFAULT.properties in test resources if you want to run extensive integration tests relying on credentials or whatever

Ripple-Rest-Client-PluginTest
=============================

Test project for the Grails Ripple Rest Client plugin


## Basic Usage - Running the tests

Edit `Config.groovy` and replace serverUrl with your server url. The [ripple rest](https://github.com/ripple/ripple-rest) server must be running for tests to pass 
This is **required**:

    rippleRest{
              api{
                serverUrl = "http://localhost:5990/v1/"
                 }
              }

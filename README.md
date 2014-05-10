Ripple-Rest-Client-PluginTest
=============================

Test project for the Grails Ripple Rest Client plugin


## Basic Usage - Running the tests
Run

`grails install-plugin grails-ripple-rest-client-api-0.2.zip `

Edit `Config.groovy` and replace serverUrl with your server url. The [ripple rest](https://github.com/ripple/ripple-rest) server must be running for tests to pass 
This is **required**:

    rippleRest{
              api{
                serverUrl = "http://localhost:5990/v1/"
                 }
              }

##Run the integration tests 

###Replace "Pleas add" with the correct values 

        
      def rippleRestClientService
    def testAddressSecret = "PLEASE_ADD"
    def testAddress = "PLEASE_ADD"
    def testAddressTransactionHash = "PLEASE_ADD"
    def testAddressPaymentHash = "PLEASE_ADD"

    def testDestAddress = "PLEASE_ADD"
    
##Also run the grails project locally and navigate to http://localhost:8080/RippleRestPluginTEst/testRipple

###To see the client being used on a running grails application


###

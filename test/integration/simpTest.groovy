import groovy.json.JsonSlurper
import org.junit.Test
import sim.entity.*
import sim.rest.RippleRequestException


/**
 * Created by simeon gbolo on 5/5/14.
 */
class simpTest {

    def rippleRestClientService
    def testAddressSecret = "snYtdhR1U8zom65BVzTrBKNrdgtNc"
    def testAddress = "rU4oGkzf2Hh82X4Aj2s4dz4ev33ezTzNTq"
    def testAddressTransactionHash = "A2EE00F548F0E52DD0AFF6A061F2ECAD422B05F41B74D6BC233FD1C5CC930CE7"
    def testAddressPaymentHash = "6E812934DCA7F4B81F8AE1DC9EA550D816DF97D08DD8A5A85D8A9A5974B3920C"
    def testDestAddress = "rLGaDmicKt96YguCvnCyWq9n7RjjarTWWb"
    def response

    @Test
    public void letsTestHappyPath(){

        //get account settings
        Account acct =  rippleRestClientService.getAccountSettings("rU4oGkzf2Hh82X4Aj2s4dz4ev33ezTzNTq")

        //Post new account settings
        Account newAccountSettings = rippleRestClientService.postAccountSettings(testAddressSecret,testAddress){
            transfer_rate = 0
            password_spent =  false
            require_destination_tag = false
            require_authorization = false
            disallow_xrp = false
            disable_master =  false
        }

        assert newAccountSettings.success == true
        assert newAccountSettings.settings.transfer_rate == 0
        assert newAccountSettings.settings.require_destination_tag == false
        assert  newAccountSettings.settings.disallow_xrp == false

        println "Testing rippleRestClientService.getAccountSettings\n"
        assert acct.success == true
        println "The Hash is: " +  acct.hash +"\n"
        println "The address is: " +  acct.address+"\n"


        //Test posting a payment
        PaymentResponse  paymentResponse =  rippleRestClientService.postPayment(testAddressSecret){
            source_account =testAddress
            destination_account =testDestAddress
            destination_amount =  new Amount(value: ".0001",currency: "XRP" ,issuer: "")
        }

        println "\n Testing rippleRestClientService.postPayment full Response\n"
        assert paymentResponse.success == true
        println "The Client Resouce id is: " +  paymentResponse.client_resource_id +"\n"
        println "The Status Url is: " +  paymentResponse.status_url  + "\n"



        //testing getting a - returns a list of balances
        def balancesResponse = rippleRestClientService.getBalances(testAddress)
        println "\nTesting rippleRestClientService.getBalances\n"

        balancesResponse.each{ it ->
            println  "The balance is "+it.value
            println  "The Currency is " + it.currency
            assert    it.currency == "XRP" || it.currency == "USD"
            println "the Counter Party  is " + it?.counterparty
            if(it.currency == "USD")
            assert it.counterparty == "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B"
        }

        //Test getting a notification
        NotificationResponse notificationResponseResponse = rippleRestClientService.getNotification(testAddress,testAddressTransactionHash)
        println "\nTesting rippleRestClientService.getNotification\n"

        println "The Direction is " + notificationResponseResponse.direction +"\n"
        assert notificationResponseResponse.direction == "outgoing"
        println "The Hash is " + notificationResponseResponse.hash +"\n"
        println "The state is " + notificationResponseResponse.state +"\n"
        println "The type is " + notificationResponseResponse.type +"\n"
        println "The timestamp is " + notificationResponseResponse.timestamp +"\n"
        println "The account is " + notificationResponseResponse.account +"\n"
        println "The Ledger  is " + notificationResponseResponse.ledger +"\n"
        println "The next hash  is " + notificationResponseResponse.next_hash
        println "The next notification url  is " + notificationResponseResponse.next_notification_url
        println "The previous  is " + notificationResponseResponse.previous_hash



        //Test getting paths   - returns a list of payment opjects
        def payments = rippleRestClientService.getPaths("rDdwBhw5ypG7jgg2HTD8g3ntw3vrXq8ssQ",testAddress){
            value = ".10"
            currency = "XRP"
            sourceCurrencies = ["USD","BTC"]
        }
        println "Test for rippleRestClientService.getPaths\n"

        payments.each {p ->
            assert p.source_account != null
            println p.destination_amount
            println p.destination_tag
            println p.direction

        }


        //Test Getting a specific payment for a given hash
        Payment thePayment = rippleRestClientService.getPayment(testAddress){
            hash = testAddressPaymentHash
        }
        println "Test for rippleRestClientService.getPayment\n"
        assert thePayment.result == "tesSUCCESS"
        println "The Direction is " + thePayment.direction +"\n"
        println "The Hash is " + thePayment.hash +"\n"
        println "The state is " + thePayment.state +"\n"
        println "The timestamp is " + thePayment.timestamp +"\n"
        println "The account is " + thePayment.source_account +"\n"
        println "The Ledger  is " + thePayment.ledger +"\n"


        //Test getting a payment with query params  -  returns a list of payments
        def payments2  = rippleRestClientService.getPaymentQuery(testAddress){
            earliest_first = true
            direction = "incoming"
        }

        println "Test For rippleRestClientService.getPaymentQuery\n"
        payments2.each{it ->
            println "====Payent info===="
            assert it.result == "tesSUCCESS"
            println "The Direction is " + it.direction +"\n"
            println "The Hash is " + it.hash +"\n"
            println "The state is " + it.state +"\n"
            println "The timestamp is " + it.timestamp +"\n"
            println "The account is " + it.source_account +"\n"
            println "The fee  is " + it.fee +"\n"
            println "The invoice_id  is " + it.invoice_id +"\n"
            println "The destination_account  is " + it.destination_account +"\n"
            println "The partial_payment  is " + it.partial_payment +"\n"

        }



        //Test Transactions
        response = rippleRestClientService.getTransaction(testAddressTransactionHash)
        println "Test for rippleRestClientService.getTransaction\n"
        assert response.success == true

        def transaction = response.transaction
        println "The Amount is " + transaction.Amount +"\n"
        println "The Hash is " + transaction.hash +"\n"
        println "The fee is " + transaction.fee +"\n"
        println "The date is " + transaction.date +"\n"
        println "The timestamp is " + transaction.timestamp +"\n"
        println "The account is " + transaction.account +"\n"
        println "The Ledger  is " + transaction.ledger +"\n"


        //test for trustlines - returns a list of trust lines
        def trustLines  = rippleRestClientService.getTrustLines(testAddress){
            currency = "USD"
        }


        println "Test for rippleRestClientService.getTrustLines\n"
        trustLines.each {tl->
            println "The limit is " + tl.limit
            println "The account is " + tl.account
            println "The currency is " + tl.currency
            println "The counter party is " + tl.counterparty
        }



        //granting a trustline
        TrustLineGrant trustLine = rippleRestClientService.grantTrustLine(testAddressSecret,testAddress){
            limit = 5
            currency ="USD"
            counterparty = "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B"
            account_allows_rippling = true
        }
        println "Test For rippleRestClientService.grantTrustLine\n"
        assert   trustLine.counterparty != null
        println "The limit is " + trustLine.limit +"\n"
        println "The account is " + trustLine.account +"\n"
        println "The currency is " + trustLine.currency +"\n"
        println "The counter party is " + trustLine.counterparty +"\n"

        response = rippleRestClientService.getConnectionStatus()
        println "\nGet status Test for Connection status rippleRestClientService.getConnectionStatus\n"+response.toString()
        assert response.success == true
        assert response.connected == true

        response = rippleRestClientService.getServerInfo()
        println "\n Test for server info rippleRestClientService.getServerInfo \n"+response.toString()
        assert response.success == true

        println rippleRestClientService.getUuid()





    }

    /**
     * Test for the alternate path of sending a bad request,
     * The ripple server should respond with an error message
     */
    @Test
    public void testAlternatePath(){

        testAddressSecret = "BAD"
        testAddress = "BAD"
        testAddressTransactionHash = "BAD"
        testAddressPaymentHash = "BAD"

        //Get the server status and ensure the ripple server is running
        response = rippleRestClientService.getConnectionStatus()
        assert response.success == true

        try{
            println "rippleRestClientService.getBalances"
            response =  rippleRestClientService.postPayment(testAddressSecret){
            source_account =testAddress
            destination_account ="BAD"
            destination_amount =  new Amount(value: ".0001",currency: "XRP" ,issuer: "")
        }
           //fail the test if no exception is thrown
           assert false
            }catch(RippleRequestException e){
            println e.message
              assert e.message == "There was an error processing your request: " +
                      "Invalid parameter: source_account. Must be a valid Ripple address"
            }


        try {
            println "rippleRestClientService.getBalances"
            response = rippleRestClientService.getBalances(testAddress)
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }


        try {
            println "rippleRestClientService.getNotification"
            response = rippleRestClientService.getNotification(testAddress,testAddressTransactionHash)
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }



        try{
            println "rippleRestClientService.getPaths"
            response = rippleRestClientService.getPaths("BAD","BAD"){
            value = ".10"
            currency = "XRP"
            sourceCurrencies = ["USD","CHF","BTC"]
        }
            //fail the test in no exception is thrown
            assert false

        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }


        try {
        //no source currencies
        println "rippleRestClientService.getPaths"
        response = rippleRestClientService.getPaths("rU4oGkzf2Hh82X4Aj2s4dz4ev33ezTzNTq","rLGaDmicKt96YguCvnCyWq9n7RjjarTWWb"){
            value = ".10"
            currency = "XRPPP"
        }
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Invalid parameter: destination_amount. Must be an object of the form { value: '1', currency: 'XRP', issuer: ' }"
        }


        try {
        println "rippleRestClientService.getPayment"
        response = rippleRestClientService.getPayment(testAddress){
            hash = testAddressPaymentHash
        }
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }


        try {
        println "rippleRestClientService.getPaymentQuery"
        response = rippleRestClientService.getPaymentQuery(testAddress){
            earliest_first = false
            direction = "incoming"
        }
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }

        try {
        println "rippleRestClientService.getTrustLines"
        response = rippleRestClientService.getTrustLines("BAD"){

        }
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }

        try {
        println "rippleRestClientService.grantTrustline"
        response = rippleRestClientService.grantTrustLine(testAddressSecret,testAddress){
            limit = 5
            currency ="USD"
            counterparty = "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B"
            account_allows_rippling = true
        }
            //fail the test in no exception is thrown
            assert false
        } catch (RippleRequestException e) {
            assert e.message =="There was an error processing your request: Specified address is invalid: account"
        }




    }
}

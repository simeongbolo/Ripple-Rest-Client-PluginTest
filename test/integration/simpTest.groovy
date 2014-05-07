import org.junit.Test
import sim.entity.Amount

/**
 * Created by simeon gbolo on 5/5/14.
 */
class simpTest {

    def rippleRestClientService
    def testAddressSecret = "PLEASE_ADD"
    def testAddress = "PLEASE_ADD"
    def testAddressTransactionHash = "PLEASE_ADD"
    def testAddressPaymentHash = "PLEASE_ADD"

    def testDestAddress = "PLEASE_ADD"
    def response

    @Test
    public void letsTestHappyPath(){

         response =  rippleRestClientService.postPayment(testAddressSecret){
            source_account =testAddress
            destination_account ="testDestAddress"
            destination_amount =  new Amount(value: ".0001",currency: "XRP" ,issuer: "")
        }

        println "\nrippleRestClientService.postPayment\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getBalances(testAddress)
        println "\nrippleRestClientService.getBalances\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getNotification(testAddress,testAddressTransactionHash)
        println "\nrippleRestClientService.getNotification\n"+response.toString()
        assert response.success == true


        response = rippleRestClientService.getPaths("testAddress","testDestAddress"){
               value = ".10"
            currency = "XRP"
            sourceCurrencies = ["USD","CHF","BTC"]
        }
        println "\nrippleRestClientService.getPaths\n"+response.toString()
        assert response.success == true
//        response.payments.source_amount.value
        //no source currencies
        response = rippleRestClientService.getPaths("testAddress","testAddress"){
            value = ".10"
            currency = "XRP"
        }
        println "\nrippleRestClientService.getPaths\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getPayment(testAddress){
            hash = testAddressPaymentHash
        }
                println "\nrippleRestClientService.getPayment\n"+response.toString()
        assert response.success == true


        response = rippleRestClientService.getPaymentQuery(testAddress){
            earliest_first = true
            direction = "incoming"
        }
        println "\nrippleRestClientService.getPaymentQuery\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getTransaction(testAddressTransactionHash)
        println "\nrippleRestClientService.getTransaction\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getTrustLines(testAddress){

        }
        println "\nrippleRestClientService.getTrustLines\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getAllTrustLines(testAddress)
        println "\nrippleRestClientService.getTrustLines\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.grantTrustLine(testAddressSecret,testAddress){
            limit = 5
            currency ="USD"
            counterparty = "PLEASE_ADD"
            allows_rippling = true
        }
        println "\nrippleRestClientService.grantTrustLine\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getConnectionStatus()
        println "\nrippleRestClientService.getConnectionStatus\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getServerInfo()
        println "\nrippleRestClientService.getServerInfo \n"+response.toString()
        assert response.success == true

        println rippleRestClientService.getUuid()


//        response = rippleRestClientService.postAccountSettings(testAddressSecret,testAddress){
//            transfer_rate = 0
//            password_spent =  false
//            require_destination_tag = false
//            require_authorization = false
//            disallow_xrp = false
//            disable_master =  false
//        }
//
//                println "rippleRestClientService.postAccountSettings \n"+response.toString()

    }

    @Test
    public void testAlternatePath(){

        testAddressSecret = "BAD"
        testAddress = "BAD"
        testAddressTransactionHash = "BAD"
        testAddressPaymentHash = "BAD"

        println rippleRestClientService.getUuid()
        response =  rippleRestClientService.postPayment(testAddressSecret){
            source_account =testAddress
            destination_account ="PLEASE_ADD"
            destination_amount =  new Amount(value: ".0001",currency: "XRP" ,issuer: "")
        }

        println "\nrippleRestClientService.postPayment\n"+response.toString()
        assert response.success == false

        response = rippleRestClientService.getBalances(testAddress)
        println "\nrippleRestClientService.getBalances\n"+response.toString()
        assert response.success == false

        response = rippleRestClientService.getNotification(testAddress,testAddressTransactionHash)
        println "\nrippleRestClientService.getNotification\n"+response.toString()
        assert response.success == false


        response = rippleRestClientService.getPaths("BAD","BAD"){
            value = ".10"
            currency = "XRP"
            sourceCurrencies = ["USD","CHF","BTC"]
        }
        println "\nrippleRestClientService.getNotification\n"+response.toString()
        assert response.success == false

        //no source currencies
        response = rippleRestClientService.getPaths("BAD","BAD"){
            value = ".10"
            currency = "XRP"
        }
        println "\nrippleRestClientService.getNotification\n"+response.toString()
        assert response.success == false

        response = rippleRestClientService.getPayment(testAddress){
            hash = testAddressPaymentHash
        }
        println "\nrippleRestClientService.getPayment\n"+response.toString()
        assert response.success == false


        response = rippleRestClientService.getPaymentQuery(testAddress){
            earliest_first = false
            direction = "incoming"
        }
        println "\nrippleRestClientService.getPaymentQuery\n"+response.toString()
        assert response.success == false

        response = rippleRestClientService.getTrustLines("BAD"){

        }
        println "\nrippleRestClientService.getTrustLines\n"+response.toString()
        assert response.success == false

        response = rippleRestClientService.grantTrustLine(testAddressSecret,testAddress){
            limit = 5
            currency ="USD"
            counterparty = "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B"
            allows_rippling = true
        }
        println "\nrippleRestClientService.grantTrustLine\n"+response.toString()
        assert response.success == false

        response = rippleRestClientService.getConnectionStatus()
        println "\nrippleRestClientService.getConnectionStatus\n"+response.toString()
        assert response.success == true

        response = rippleRestClientService.getServerInfo()
        println "\nrippleRestClientService.getServerInfo \n"+response.toString()
        assert response.success == true
    }
}

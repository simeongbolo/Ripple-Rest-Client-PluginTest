package ripplerestplugintest

class TestRippleController {

    def rippleRestClientService


    def index() {
        def uuid = rippleRestClientService.getUuid()
        render "<h1>Got the uuid from the server = $uuid </h1>"
    }
}

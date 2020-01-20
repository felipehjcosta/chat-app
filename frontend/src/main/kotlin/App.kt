import react.RBuilder
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch

fun RBuilder.app() =
        browserRouter {
            switch {
                route("/", exact = true) {
                    welcome()
                }
            }
        }


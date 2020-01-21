import react.RBuilder
import react.RProps
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch

interface UsernameProps : RProps {
    var username: String
}

fun RBuilder.app() =
        browserRouter {
            switch {
                route("/", exact = true) {
                    welcome()
                }
                route<UsernameProps>("/chat/:username") { props ->
                    chat(props.match.params.username)
                }
            }
        }

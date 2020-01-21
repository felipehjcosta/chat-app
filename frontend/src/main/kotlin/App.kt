import react.RBuilder
import react.RProps
import react.dom.div
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
                    div("App") {
                        div(classes = "container") {
                            div(classes = "row") {
                                div(classes = "col-auto") {
                                    div(classes = "card") {
                                        div(classes = "card-body") {
                                            chat(props.match.params.username)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

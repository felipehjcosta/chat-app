package chat

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h2

class Chat : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        h2 { +"Hello Chat" }
    }
}

fun RBuilder.chat() = child(Chat::class) {}
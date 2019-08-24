import react.RProps
import react.ReactElement

typealias empty = Empty

object Empty : ReactElement {
    override val props = object : RProps {}
}

import react.RProps
import react.ReactElement

object empty : ReactElement {
    override val props = object : RProps {}
}

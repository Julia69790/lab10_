package component

import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import kotlinx.html.title
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.input

interface LinkProps : RProps {
    var onClick: () -> Unit
}

class Link(props: LinkProps) : RComponent<LinkProps, RState>(props) {
    override fun RBuilder.render() {
        button {
            attrs.onClickFunction = { props.onClick() }
            children()
        }
    }
}
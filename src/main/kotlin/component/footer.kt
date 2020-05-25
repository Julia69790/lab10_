package component

import container.filterLink
import enums.VisibilityFilter
import react.RBuilder
import react.dom.div
import react.dom.span

fun RBuilder.footer() =
    div {
        span { +"Show: " }
        filterLink {
            attrs.filter = VisibilityFilter.SHOW_ALL
            +"All"
        }
        filterLink {
            attrs.filter = VisibilityFilter.SHOW_PRESENCE
            +"Presence"
        }
        filterLink {
            attrs.filter = VisibilityFilter.SHOW_ABSENCE
            +"Absence"
        }
    }
package container

import component.Link
import component.LinkProps
import data.State
import enums.VisibilityFilter
import react.RClass
import react.RProps
import react.invoke
import react.redux.rConnect
import redux.SetVisibilityFilter
import redux.WrapperAction

interface FilterLinkProps : RProps {
    var filter: VisibilityFilter
}

private interface LinkStateProps : RProps {
    var present: Boolean
}

private interface LinkDispatchProps : RProps {
    var onClick: () -> Unit
}

val filterLink: RClass<FilterLinkProps> =
    rConnect<
            State,
            SetVisibilityFilter,
            WrapperAction,
            FilterLinkProps,
            LinkStateProps,
            LinkDispatchProps,
            LinkProps>(
        { state, ownProps ->
            present = state.visibilityFilter == ownProps.filter
        },
        { dispatch, ownProps ->
            onClick = { dispatch(SetVisibilityFilter(ownProps.filter)) }
        }
    )(Link::class.js.unsafeCast<RClass<LinkProps>>())
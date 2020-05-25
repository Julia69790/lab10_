package container

import component.*
import data.*
import enums.VisibilityFilter
import hoc.withDisplayName
import org.w3c.dom.events.Event
import react.*
import redux.*
import react.redux.rConnect

interface AnyFullDispatchProps : RProps {
    var onClick: (Int) -> (Event) -> Unit
}

interface AnyFullStateProps<O, S> : RProps {
    var subobjs: Map<Int, S>
    var presents: Map<Int, Boolean>?
}

interface AnyFullOwnProps<O> : RProps {
    var obj: Pair<Int, O>
}

private fun <O> getVisibleObjects(objects:Map <Int,O>, presents:Map<Int,Boolean>?,filter: VisibilityFilter): Map<Int, O>
        = when (filter) {
    VisibilityFilter.SHOW_ALL -> objects
    VisibilityFilter.SHOW_ABSENCE ->  {
        val absentObj = objects.toMutableMap()
        absentObj.clear()
        if (presents != null) {
            presents.filter { !it.value }.map{
                absentObj[it.key] = objects.getValue(it.key)
            }
        }
        absentObj
    }
    VisibilityFilter.SHOW_PRESENCE ->  {
        val presentObj = objects.toMutableMap()
        presentObj.clear()
        if (presents != null) {
            presents.filter { it.value }.map{
                presentObj[it.key] = objects.getValue(it.key)
            }
        }
        presentObj
    }
}

val lessonFullContainer =
    rConnect<
            State,
            RAction,
            WrapperAction,
            AnyFullOwnProps<Lesson>,
            AnyFullStateProps<Lesson, Student>,
            AnyFullDispatchProps,
            AnyFullProps<Lesson, Student>>(
        { state, ownProps ->
            presents = state.presents[ownProps.obj.first]
            subobjs = getVisibleObjects(state.students,presents,state.visibilityFilter)
        },
        { dispatch, ownProps ->
            onClick =
                { index ->
                    {
                        dispatch(ChangePresent(ownProps.obj.first, index))
                    }
                }
        }
        
    )(
        withDisplayName(
            "LessonFull",
            fAnyFull<Lesson, Student>(RBuilder::student)
        )
            .unsafeCast<RClass<AnyFullProps<Lesson, Student>>>()
    )

val studentFullContainer =
    rConnect<
            State,
            RAction,
            WrapperAction,
            AnyFullOwnProps<Student>,
            AnyFullStateProps<Student, Lesson>,
            AnyFullDispatchProps,
            AnyFullProps<Student, Lesson>>(
        { state, ownProps ->
            subobjs = getVisibleObjects(state.lessons, state.presentsStudent(ownProps.obj.first), state.visibilityFilter)
            presents = state.presentsStudent(ownProps.obj.first)
        },
        { dispatch, ownProps ->
            onClick = { index ->
                {
                    dispatch(ChangePresent(index, ownProps.obj.first))
                }
            }
        }
    )(
        withDisplayName(
            "StudentFull",
            fAnyFull<Student, Lesson>(RBuilder::lesson)
        )
            .unsafeCast<RClass<AnyFullProps<Student, Lesson>>>()
    )
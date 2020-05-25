# lab10_
## Задание
Доработайте приложение, добавив в списки фильтры, которые выводят либо всех студентов (уроки), либо только присутствующих, либо только отсутствующих.
## Выполнение 
1. Создана директория enums и enum class VisibilityFilter:<br>
        
        enum class VisibilityFilter {
        SHOW_ALL,
        SHOW_PRESENCE,
        SHOW_ABSENCE
        }
     
2. Дополнены reducers и actions.<br>
actions:<br>
        
        class SetVisibilityFilter(val filter: VisibilityFilter) : RAction
        
reducers:<br>
        
        fun visibilityReducer(
        state: VisibilityFilter,
        action: RAction
        ) = when (action) {
        is SetVisibilityFilter -> action.filter
        else -> state
        }
        
3. Созданы компоненты link и footer. <br>link:<br>
        
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
        
footer: <br>
        
        fun RBuilder.footer() =
        div {
        span { +"Show: " }
        filterLink {
            attrs.filter = VisibilityFilter.SHOW_ALL
            +"All"
        }
        filterLink {
            attrs.filter = VisibilityFilter.SHOW_PRESENCE
            +"Present"
        }
        filterLink {
            attrs.filter = VisibilityFilter.SHOW_ABSENCE
            +"Absence"
          }
        }
        
4. Создан контейнер FilterLink:<br>
        
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
        
5. В контейнер anyFullContainer добавлена функция getVisibleObjects:<br>
        
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
        
Открыт урок, отмечено присутствие студентов, выведены все студенты:<br>
![](/screen10/ОтметилиПрисутствие.png)<br>
Применен фильтр, отображаются только студенты, присутствовавшие на уроке:<br>
![](/screen10/УрокПрисутствие.png)<br>
Применен фильтр, отображаются только студенты, отсутствовавшие на уроке:<br>
![](/screen10/УрокОтстутствие.png)<br>
Открыта страница со студентом, показаны все уроки:<br>
![](/screen10/СтудентВсе.png)<br>
Применен фильтр, показаны только те уроки, на которых студент присутствовал:<br>
![](/screen10/СтудентПрисутствие.png)<br>
Применен фильтр, показаны только те уроки, на которых студент отсутствовал:<br>
![](/screen10/СтудентОтсутствие.png)<br>

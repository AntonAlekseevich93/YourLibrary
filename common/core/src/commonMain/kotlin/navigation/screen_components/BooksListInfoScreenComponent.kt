package navigation.screen_components

import com.arkivanov.decompose.ComponentContext

interface BooksListInfoScreenComponent {

}

class DefaultBooksListInfoScreenComponent(
    componentContext: ComponentContext,
) : BooksListInfoScreenComponent, ComponentContext by componentContext {

}
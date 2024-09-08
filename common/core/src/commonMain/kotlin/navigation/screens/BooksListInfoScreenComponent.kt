package navigation.screens

import com.arkivanov.decompose.ComponentContext

interface BooksListInfoScreenComponent {

}

class DefaultBooksListInfoScreenComponent(
    componentContext: ComponentContext,
) : BooksListInfoScreenComponent, ComponentContext by componentContext {

}
package models

import BaseEventScope

interface BooksListInfoScope<BooksListInfoScreenEvent> : BaseEventScope<BooksListInfoScreenEvent> {
    override fun sendEvent(event: BooksListInfoScreenEvent)
}
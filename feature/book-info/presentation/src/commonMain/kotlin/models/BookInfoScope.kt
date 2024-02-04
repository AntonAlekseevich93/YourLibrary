package models

import BaseEventScope

interface BookInfoScope<BookScreenEvent>: BaseEventScope<BookScreenEvent> {
    override fun sendEvent(event: BookScreenEvent)
}
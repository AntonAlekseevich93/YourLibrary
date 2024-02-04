interface BaseEventScope<BaseEvent> {
    fun sendEvent(event: BaseEvent)
}
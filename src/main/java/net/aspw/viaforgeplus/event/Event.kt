package net.aspw.viaforgeplus.event

open class Event

open class CancellableEvent : Event() {
    var isCancelled: Boolean = false
        private set

    fun cancelEvent() {
        isCancelled = true
    }
}

enum class EventState {
    PRE, POST
}
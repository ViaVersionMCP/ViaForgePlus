package net.aspw.viaforgeplus.event

class MotionEvent : Event() {
    var eventState: EventState = EventState.PRE
}

class PushOutEvent : CancellableEvent()
class UpdateEvent : Event()
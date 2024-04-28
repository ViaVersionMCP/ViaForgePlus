package net.aspw.viaforgeplus.event

class MotionEvent(
    var x: Double,
    var y: Double,
    var z: Double,
    var yaw: Float,
    var pitch: Float,
    var onGround: Boolean
) : Event() {
    var eventState: EventState = EventState.PRE
}

class PushOutEvent : CancellableEvent()
class UpdateEvent : Event()
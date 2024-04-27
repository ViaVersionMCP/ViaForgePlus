package net.aspw.viaforgeplus.event

import net.minecraft.network.Packet

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

class PacketEvent(val packet: Packet<*>) : CancellableEvent()
class PushOutEvent : CancellableEvent()
class TextEvent(var text: String?) : Event()
package net.aspw.viaforgeplus

import net.aspw.viaforgeplus.api.McUpdatesHandler
import net.aspw.viaforgeplus.api.PacketManager
import net.aspw.viaforgeplus.event.EventManager
import net.aspw.viaforgeplus.inventorytabs.EnchantItems
import net.aspw.viaforgeplus.inventorytabs.ModItems
import net.aspw.viaforgeplus.inventorytabs.StackItems
import net.aspw.viaforgeplus.network.APIConnecter

object ProtocolInject {

    const val modVersion = "1.0.3"

    lateinit var eventManager: EventManager

    fun init() {
        ProtocolBase.init(ProtocolMod.PLATFORM)

        APIConnecter.checkData()

        eventManager = EventManager()

        eventManager.registerListener(PacketManager())
        eventManager.registerListener(McUpdatesHandler())

        ModItems()
        StackItems()
        EnchantItems()
    }
}
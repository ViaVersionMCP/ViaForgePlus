package net.aspw.viaforgeplus.event

class EventManager {

    private val registry = hashMapOf<Class<out Event>, MutableList<EventHook>>()

    fun registerListener(listener: Listenable) {
        for (method in listener.javaClass.declaredMethods) {
            if (method.isAnnotationPresent(EventTarget::class.java) && method.parameterTypes.size == 1) {
                if (!method.isAccessible)
                    method.isAccessible = true

                val eventClass = method.parameterTypes[0] as Class<out Event>
                val eventTarget = method.getAnnotation(EventTarget::class.java)

                val invokableEventTargets = registry.getOrElse(eventClass, { arrayListOf<EventHook>() })
                try {
                    invokableEventTargets.add(EventHook(listener, method, eventTarget.priority, eventTarget))
                } catch (e: Exception) {
                    e.printStackTrace()
                    invokableEventTargets.add(EventHook(listener, method, eventTarget))
                }
                invokableEventTargets.sortBy { it.priority }
                registry.put(eventClass, invokableEventTargets)
            }
        }
    }

    fun callEvent(event: Event) {
        val targets = registry.get(event.javaClass) ?: return

        targets.filter { it.eventClass.handleEvents() || it.isIgnoreCondition }.forEach {
            try {
                it.method.invoke(it.eventClass, event)
            } catch (_: Throwable) {
            }
        }
    }
}

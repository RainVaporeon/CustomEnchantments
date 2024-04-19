package io.github.rainvaporeon.customenchantments.internal;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.fishutils.utils.eventbus.EventBus;
import io.github.rainvaporeon.fishutils.utils.eventbus.events.Event;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Level;

public class EventManager {
    private static final EventBus bus = new EventBus();
    // 100 is an arbitrarily chosen value, may scale if needed.
    private static final Queue<Event> queuedEvents = new ArrayDeque<>(100);

    /**
     * Whether the current manager (and event bus) is paused
     */
    private static volatile boolean paused = false;
    /**
     * Whether to hold onto events while paused, does nothing
     * if not paused
     */
    private static volatile boolean holdEvents = false;

    static {
        bus.setErrorHandler(t -> {
            CustomEnchantments.PLUGIN.getLogger().log(Level.SEVERE, "An exception had occurred whilst handling bus events: ", t);
        });
    }

    public static void subscribe(Object source) {
        bus.subscribe(source);
    }

    public static void unsubscribe(Object source) {
        bus.unsubscribe(source);
    }

    public static void fire(Event event) {
        if (paused) {
            if (holdEvents) queuedEvents.add(event);
            return;
        }
        bus.fire(event);
    }

    public static void pause(boolean holdEvents) {
        paused = true;
        EventManager.holdEvents = holdEvents;
    }

    public static void resume(boolean releaseEvents) {
        paused = false;
        if (releaseEvents) {
            queuedEvents.forEach(EventManager::fire);
        }
        holdEvents = false;
    }
}

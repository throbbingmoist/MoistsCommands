package com.moist.moistscommands.helper;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraft.world.World;

import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class DelayedEventHelper {

    public final LinkedBlockingQueue<Event> queue;
    public static DelayedEventHelper INSTANCE = new DelayedEventHelper();

    public DelayedEventHelper() {
        queue = new LinkedBlockingQueue<Event>();
    }

    public void scheduleEvent(Runnable runnable, int targetTick) {
        queue.add(new Event(targetTick, runnable));
    }

    // Basically, this is meant to run every four world ticks or so.
    // Lag can probably cause a skipped tick, but it's probably not a good idea to iterate every tick lmfao.
    // So yeah.
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.world.isRemote) return;
        World world = event.world;
        int currentTick = Math.toIntExact(world.getTotalWorldTime());
        if (currentTick % 4 != 0) return;
        Iterator<DelayedEventHelper.Event> queueIterator = DelayedEventHelper.INSTANCE.queue.iterator();
        while (queueIterator.hasNext()) { // this shit's fucked up
            DelayedEventHelper.Event tickEvent = queueIterator.next();
            if (currentTick >= tickEvent.target_tick()) {
                tickEvent.run();
                queueIterator.remove();
            }
        }
    }

    @Desugar
    public record Event(int target_tick, Runnable runnable) {

        public void run() {
            this.runnable.run();
        }
    }
}

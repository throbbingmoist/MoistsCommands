package com.moist.moistscommands.helper;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class DelayedEventHelper {

    public final LinkedBlockingQueue<Event> queue;
    public final ConcurrentHashMap<UUID, CooldownHelper> cooldowns;
    public static DelayedEventHelper INSTANCE = new DelayedEventHelper();

    public DelayedEventHelper() {
        queue = new LinkedBlockingQueue<Event>();
        cooldowns = new ConcurrentHashMap<UUID, CooldownHelper>();
    }

    public static void scheduleEvent(Runnable runnable, int targetTick) {
        INSTANCE.queue.add(new Event(targetTick, runnable));
    }

    public static void addCommandCooldown(EntityPlayerMP player, String command, long targetTick) {
        INSTANCE.cooldowns.putIfAbsent(player.getPersistentID(), new CooldownHelper());
        INSTANCE.cooldowns.get(player.getPersistentID())
            .put(command, targetTick);

    }

    public static boolean checkCommandCooldown(EntityPlayerMP player, String command, long currentTick) {
        INSTANCE.cooldowns.putIfAbsent(player.getPersistentID(), new CooldownHelper());
        return currentTick <= INSTANCE.cooldowns.get(player.getPersistentID())
            .getOrDefault(command);
    }

    public static Long commandCooldownTime(EntityPlayerMP player, String command) {
        INSTANCE.cooldowns.putIfAbsent(player.getPersistentID(), new CooldownHelper());
        return INSTANCE.cooldowns.get(player.getPersistentID())
            .getOrDefault(command) - player.worldObj.getTotalWorldTime();
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

    @Desugar

    public static class CooldownHelper {

        private final ConcurrentHashMap<String, Long> commandCooldowns = new ConcurrentHashMap<String, Long>();

        public CooldownHelper() {}

        private void put(String command, Long targetTick) {
            commandCooldowns.put(command, targetTick);
        }

        private Long get(String command) {
            return commandCooldowns.get(command);
        }

        private Long getOrDefault(String command) {
            return commandCooldowns.getOrDefault(command, 0L);
        }
    }
}

package com.pm.aiost.misc.utils.scheduler;

import java.util.List;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;

public class AiostScheduler {

	private static final List<Runnable> TASKS = new UnorderedIdentityArrayList<>();
	private static final List<Runnable> REMOVED_TASKS = new UnorderedIdentityArrayList<>();
	private static final TimeBuffer TIMED_BUFFER = new TimeBuffer();

	public static void update() {
		if (!REMOVED_TASKS.isEmpty()) {
			for (Runnable runnable : REMOVED_TASKS) {
				if (TASKS.remove(runnable))
					Logger.warn("AiostScheduler: Tried to remove task that did not exist!");
			}
		}
		TIMED_BUFFER.update(NMS.getMinecraftServerTick());
		for (int i = 0; i < TASKS.size(); i++)
			TASKS.get(i).run();
	}

	public static void runTask(Runnable task) {
		TASKS.add(task);
	}

	public static void stopTask(Runnable task) {
		REMOVED_TASKS.add(task);
	}

	// TODO TIMED_BUFFER has some bug!
	public static void runTaskLater(int time, Runnable task) {
		TIMED_BUFFER.add(NMS.getMinecraftServerTick() + time, task);
	}

	@FunctionalInterface
	public interface AiostTask extends Runnable {

		public default void start() {
			runTask(this);
		}

		public default void stop() {
			stopTask(this);
		}
	}
}
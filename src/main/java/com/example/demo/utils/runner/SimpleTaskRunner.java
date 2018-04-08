package com.example.demo.utils.runner;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class SimpleTaskRunner implements ITaskRunner {

	private static final SimpleTaskRunner timerSingleton = initTaskRunner();
	private Timer timer;
	private TimerTask prevTask;

	public static SimpleTaskRunner getInstance() {
		// lazy initialization of static field may not be ThreadSafe
		// see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
		return timerSingleton;
	}

	private static SimpleTaskRunner initTaskRunner() {
		SimpleTaskRunner simpleTaskRunner = new SimpleTaskRunner();
		simpleTaskRunner.setTimer(new Timer());

		return simpleTaskRunner;
	}

	@Override
	public void schedule(Date date, TimerTask task) {
		if (prevTask != null) {
			timer.purge();
			prevTask.cancel();
		}

		prevTask = task;
		timer.schedule(task ,date);
	}

	private void setTimer(Timer timer) {
		this.timer = timer;
	}
}
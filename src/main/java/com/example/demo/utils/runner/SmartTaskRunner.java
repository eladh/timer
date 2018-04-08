package com.example.demo.utils.runner;

import javafx.util.Pair;

import java.util.*;

/**
 *
 */
public class SmartTaskRunner implements ITaskRunner {

	private static final SmartTaskRunner smartRunnerInstance = initTaskRunner();
	private List<Pair<Date ,TimerTask>> tasks;
	private SimpleTaskRunner taskRunner;

	public static SmartTaskRunner getInstance() {
		// lazy initialization of static field may not be ThreadSafe
		// see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
		return smartRunnerInstance;
	}

	@Override
	public void schedule(Date date, TimerTask task) {
		tasks.add(new Pair<>(date ,task));
		tasks.sort(Comparator.comparing(Pair::getKey));

		Pair<Date, TimerTask> taskDetails = tasks.get(0);
		taskRunner.schedule(taskDetails.getKey(), new TimerTaskJob());
	}

	private static SmartTaskRunner initTaskRunner() {
		SmartTaskRunner result = new SmartTaskRunner();
		result.setTasks(new ArrayList<>(0));
		result.setTaskRunner(SimpleTaskRunner.getInstance());

		return result;
	}

	private List<Pair<Date, TimerTask>> getTasks() {
		return tasks;
	}

	private void setTasks(List<Pair<Date, TimerTask>> tasks) {
		this.tasks = tasks;
	}

	private SimpleTaskRunner getTaskRunner() {
		return taskRunner;
	}

	private void setTaskRunner(SimpleTaskRunner taskRunner) {
		this.taskRunner = taskRunner;
	}


	private static class TimerTaskJob extends TimerTask {

		@Override
		public void run() {
			List<Pair<Date, TimerTask>> tasks = smartRunnerInstance.getTasks();

			Pair<Date, TimerTask> taskDetails = tasks.get(0);
			tasks.remove(0);
			taskDetails.getValue().run();

			if (tasks.isEmpty()) {
				return;
			}
			Pair<Date, TimerTask> nextTaskDetails = tasks.get(0);
			smartRunnerInstance.getTaskRunner().schedule(nextTaskDetails.getKey(), new TimerTaskJob());
		}
	}
}
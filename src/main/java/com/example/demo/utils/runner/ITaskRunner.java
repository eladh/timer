package com.example.demo.utils.runner;

import java.util.Date;
import java.util.TimerTask;

public interface ITaskRunner {
	void schedule(Date date , TimerTask task);
}
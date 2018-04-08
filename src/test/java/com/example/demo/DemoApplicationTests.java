package com.example.demo;

import com.example.demo.utils.runner.SimpleTaskRunner;
import com.example.demo.utils.runner.SmartTaskRunner;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoApplicationTests {


	@Test
	public void testSimpleRunner() throws InterruptedException {

		final CountDownLatch latch = new CountDownLatch(1);
		final AtomicInteger tasksExecuted = new AtomicInteger();

		SimpleTaskRunner instance = SimpleTaskRunner.getInstance();

		instance.schedule(generateDelayedDate(1), new TimerTask() {
			@Override
			public void run() {
				System.out.println("run task 1");
				tasksExecuted.incrementAndGet();
				latch.countDown();
			}
		});

		instance.schedule(generateDelayedDate(3), new TimerTask() {
			@Override
			public void run() {
				System.out.println("run task 2");
				tasksExecuted.incrementAndGet();
				latch.countDown();
			}
		});

		latch.await();
		Assert.assertEquals(1 ,tasksExecuted.get());
	}

	@Test
	public void testSmartRunner() throws InterruptedException {

		final CountDownLatch latch = new CountDownLatch(3);
		final List<Integer> executionOrder = new ArrayList<>(3);

		SmartTaskRunner instance = SmartTaskRunner.getInstance();

		instance.schedule(generateDelayedDate(1), new TimerTask() {
			@Override
			public void run() {
				System.out.println("run task 1");
				executionOrder.add(1);
				latch.countDown();
			}
		});

		instance.schedule(generateDelayedDate(7), new TimerTask() {
			@Override
			public void run() {
				System.out.println("run task 2");
				executionOrder.add(2);
				latch.countDown();
			}
		});


		instance.schedule(generateDelayedDate(4), new TimerTask() {
			@Override
			public void run() {
				System.out.println("run task 3");
				executionOrder.add(3);
				latch.countDown();
			}
		});

		latch.await();
		Assert.assertEquals(Arrays.asList(1 ,3 ,2) ,executionOrder);
	}

	private Date generateDelayedDate(Integer seconds) {
		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

}

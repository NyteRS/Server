package com.server2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Rene
 * 
 */
public class Executor {

	private static final ExecutorService workService = Executors
			.newSingleThreadExecutor();

	public static void submitWork(final Runnable runnable) {
		workService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (final Throwable t) {
					t.printStackTrace();
				}
			}
		});
	}

}
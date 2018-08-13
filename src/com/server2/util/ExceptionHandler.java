package com.server2.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private static final Logger logger = Logger
			.getLogger(ExceptionHandler.class.getName());

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.log(Level.WARNING, "Unhandled exception!", e);
		e.printStackTrace();
	}

}
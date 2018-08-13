// Decompiled by:		 Fernflower v0.6
// Date:					 15.07.2011 00:19:32
// Copyright:			  2008-2009, Stiver
// Home page:			  http://www.reversed-java.com

package com.server2.util;

public class SimpleTimer {

	private long cachedTime;

	public SimpleTimer() {
		reset();
	}

	public long elapsed() {
		return System.currentTimeMillis() - cachedTime;
	}

	public void reset() {
		cachedTime = System.currentTimeMillis();
	}
}

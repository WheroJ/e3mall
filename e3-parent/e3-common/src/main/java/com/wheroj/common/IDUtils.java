package com.wheroj.common;

import java.util.Random;

public class IDUtils {
	
	public static long getId() {
		long mills = System.currentTimeMillis();
		int randomInt = new Random().nextInt(999);
		String format = String.format("%d%d", mills, randomInt);
		return Long.parseLong(format);
	}
}

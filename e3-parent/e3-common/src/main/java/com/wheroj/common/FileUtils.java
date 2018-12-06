package com.wheroj.common;

public class FileUtils {
	public static String getExtName(String fileName) {
		if (fileName == null) {
			return null;
		}
		
		int lastIndexOf = fileName.lastIndexOf(".");
		if (lastIndexOf != -1 && lastIndexOf != fileName.length() - 1) {
			return fileName.substring(lastIndexOf + 1);
		}
		return null;
	}
}

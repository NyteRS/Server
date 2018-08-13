package com.server2.util;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Rene
 * 
 */
public class LineCounter {

	public interface Filter<T> {
		public boolean accept(T t);
	}

	public static int lineCount(File file) {
		int count = 0;
		try {
			final LineNumberReader ln = new LineNumberReader(new FileReader(
					file));
			while (true) {
				final String line = ln.readLine();
				if (line == null)
					break;
				if (!line.trim().equals(""))
					count++;
			}
			ln.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public static LinkedList<File> listRecursive(File file, Filter<File> filter) {
		final LinkedList<File> files = new LinkedList<File>();
		for (final File f : file.listFiles())
			if (f.isDirectory())
				files.addAll(listRecursive(f, filter));
			else if (filter.accept(f))
				files.add(f);
		return files;
	}

	public static void main(String[] args) {
		System.out.println("Loading files...");
		final List<File> files = listRecursive(new File("./src/"),
				new Filter<File>() {
					@Override
					public boolean accept(File t) {
						return t.getName().endsWith(".java");
					}
				});
		System.out.println("Parsing line count...");
		int lineCount = 0;
		for (final File file : files)
			lineCount += lineCount(file);
		System.out.println("server2 now has " + lineCount
				+ " Lines of code in " + files.size() + " files");
	}
}
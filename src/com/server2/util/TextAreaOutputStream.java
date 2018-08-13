package com.server2.util;

import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.swing.JTextArea;

public class TextAreaOutputStream extends OutputStream {

	@SuppressWarnings("rawtypes")
	private LinkedList lineLengths;
	private JTextArea textArea;
	private int maxLines;
	private int curLength;
	private byte[] oneByte;

	static private byte[] LINE_SEP = System.getProperty("line.separator", "\n")
			.getBytes();

	public TextAreaOutputStream(JTextArea ta) {
		this(ta, 1000);
	}

	@SuppressWarnings("rawtypes")
	public TextAreaOutputStream(JTextArea ta, int ml) {
		if (ml < 1)
			try {
				System.out
						.println("Maximum lines of "
								+ ml
								+ " in TextAreaOutputStream constructor is not permitted");
			} catch (final Exception e) {
				System.out.println(e);

			}
		textArea = ta;
		maxLines = ml;
		lineLengths = new LinkedList();
		curLength = 0;
		oneByte = new byte[1];
	}

	private boolean bytesEndWith(byte[] ba, int str, int len, byte[] ew) {
		if (len < LINE_SEP.length)
			return false;
		for (int xa = 0, xb = str + len - LINE_SEP.length; xa < LINE_SEP.length; xa++, xb++)
			if (LINE_SEP[xa] != ba[xb])
				return false;
		return true;
	}

	@SuppressWarnings("rawtypes")
	public synchronized void clear() {
		lineLengths = new LinkedList();
		curLength = 0;
		textArea.setText("");
	}

	@Override
	public void close() {
		if (textArea != null) {
			textArea = null;
			lineLengths = null;
			oneByte = null;
		}
	}

	@Override
	public void flush() {
	}

	public synchronized int getMaximumLines() {
		return maxLines;
	}

	public synchronized void setMaximumLines(int val) {
		maxLines = val;
	}

	@Override
	public void write(byte[] ba) {
		write(ba, 0, ba.length);
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized void write(byte[] ba, int str, int len) {
		try {
			curLength += len;
			if (bytesEndWith(ba, str, len, LINE_SEP)) {
				lineLengths.addLast(new Integer(curLength));
				curLength = 0;
				if (lineLengths.size() > maxLines)
					textArea.replaceRange(null, 0,
							((Integer) lineLengths.removeFirst()).intValue());
			}
			for (int xa = 0; xa < 10; xa++)
				try {
					textArea.append(new String(ba, str, len));
					break;
				} catch (final Throwable thr) {
					if (xa == 9)
						thr.printStackTrace();
					else
						Thread.sleep(200);
				}
		} catch (final Throwable thr) {
			final CharArrayWriter caw = new CharArrayWriter();
			thr.printStackTrace(new PrintWriter(caw, true));
			textArea.append(System.getProperty("line.separator", "\n"));
			textArea.append(caw.toString());
		}
	}

	@Override
	public void write(int val) {
		oneByte[0] = (byte) val;
		write(oneByte, 0, 1);
	}
}
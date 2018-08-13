package com.server2.model;

/**
 * Object definition
 * 
 * @author Graham
 */
public class ObjectDefinition {

	private final int type;
	private final String name;
	private final String description;
	private final int xsize;
	private final int ysize;

	public ObjectDefinition(int type, String name, String description,
			int xsize, int ysize) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.xsize = xsize;
		this.ysize = ysize;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public int getXSize() {
		return xsize;
	}

	public int getYSize() {
		return ysize;
	}

}

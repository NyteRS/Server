package com.server2.model.entity;

import com.server2.util.Misc;

/**
 * 
 * Represents a projectile.
 * 
 * @author Ultimate1
 */

public class Projectile {

	private final int id;
	private final int curve;
	private final int startHeight;
	private final int endHeight;
	private final int startDelay;
	private int speed;
	private int slowness;
	private Entity caster;
	private Entity target;

	public Projectile(int id, int startDelay, int startHeight, int endHeight,
			int slowness, int curve) {
		this(id, startDelay, startHeight, endHeight, -1, slowness, curve);

	}

	public Projectile(int id, int startDelay, int startHeight, int endHeight,
			int speed, int slowness, int curve) {
		this.id = id;
		this.startDelay = startDelay;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.speed = speed;
		this.curve = curve;
	}

	public int calculateSpeed() {
		// TODO check out using TileManager
		final int distance = Misc.getDistance(target.getPosition(),
				caster.getPosition());
		return startDelay + slowness + distance * 5;
	}

	public Entity getCaster() {
		return caster;
	}

	public int getCurve() {
		return curve;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getId() {
		return id;
	}

	public int getSlowness() {
		return slowness;
	}

	public int getSpeed() {
		return speed;
	}

	public int getStartDelay() {
		return startDelay;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public Entity getTarget() {
		return target;
	}

	public int getXOffset() {
		return (target.getPosition().getX() - caster.getPosition().getX()) * -1;
	}

	public int getYOffset() {
		return (target.getPosition().getY() - caster.getPosition().getY()) * -1;
	}

	public Projectile set(Entity caster, Entity target) {
		this.target = caster;
		this.caster = target;
		if (speed == -1)
			speed = calculateSpeed();
		return this;
	}

}
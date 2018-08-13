package com.server2.world.map;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import com.server2.Constants;
import com.server2.model.entity.Entity;

public class Region {

	public static final int SOLID_TILE = 131328; // Is that right?

	public static int[][] blockSpawns = { { 3426, 3555 }, { 3427, 3555 },
			{ 3445, 3554 }, { 2326, 3802 }, { 2328, 3804 }, { 2328, 3805 },
			{ 3096, 3498 }, { 2753, 2775 }, { 3093, 3957 }, { 3095, 3957 },
			{ 3426, 3555 }, { 3427, 3555 }, { 3445, 3554 }, { 2326, 3802 },
			{ 2328, 3804 }, { 3041, 4997 }, { 3041, 4999 }, { 2328, 3805 },
			{ 3096, 3498 }, { 2753, 2775 }, { 3093, 3957 }, { 3095, 3957 },
			{ 2409, 9503 }, { 2401, 9494 }, { 2391, 9501 }, { 2400, 9512 },
			{ 3426, 3555 }, { 3427, 3555 }, { 3445, 3554 }, { 2326, 3802 },
			{ 2328, 3804 }, { 2328, 3805 }, { 3096, 3498 }, { 2753, 2775 },
			{ 3093, 3957 }, { 3095, 3957 }, { 3426, 3555 }, { 3427, 3555 },
			{ 3445, 3554 }, { 2326, 3802 }, { 2328, 3804 }, { 2328, 3805 },
			{ 3096, 3498 }, { 2753, 2775 }, { 3093, 3957 }, { 3095, 3957 },
			{ 2409, 9503 }, { 2401, 9494 }, { 2391, 9501 }, { 2400, 9512 },
			{ 1860, 4616 }, { 1863, 4613 }, { 1863, 4665 }, { 1860, 4662 },
			{ 3232, 9311 }, { 3228, 9311 }, { 3228, 9315 }, { 3228, 9319 },
			{ 3234, 9314 }, { 3232, 9314 }, { 3233, 9314 }, { 3238, 9311 },
			{ 3238, 9315 }, { 3238, 9319 }, { 3232, 9294 }, { 3445, 3554 },
			{ 3445, 3555 }, { 2736, 3477 }, { 2737, 3477 }, { 2737, 3476 },
			{ 2736, 3476 }, { 3008, 3849 }, { 3008, 3850 }, { 3007, 3849 },
			{ 3007, 3850 }, { 2656, 2585 }, { 2657, 2585 }, { 2656, 2584 },
			{ 2657, 2584 }, { 2671, 2592 }, { 2671, 2593 }, { 2670, 2592 },
			{ 2670, 2593 }, { 2643, 2593 }, { 2643, 2592 }, { 2642, 2592 },
			{ 2642, 2593 }, { 2924, 9803 }, { 2923, 9803 },

	};

	private static Region[] regions;

	public static void addClipping(int x, int y, int height, int shift) {
		for (final int[] blockSpawn : blockSpawns)
			if (blockSpawn[0] == x && blockSpawn[1] == y)
				return;
		final int regionX = x >> 3;
		final int regionY = y >> 3;
		final int regionId = (regionX / 8 << 8) + regionY / 8;
		for (final Region r : regions)
			if (r.id() == regionId) {
				r.addClip(x, y, height, shift);
				break;
			}
	}

	private static void addClippingForSolidObject(int x, int y, int height,
			int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag)
			clipping += 0x20000;
		for (int i = x; i < x + xLength; i++)
			for (int i2 = y; i2 < y + yLength; i2++)
				addClipping(i, i2, height, clipping);
	}

	private static void addClippingForVariableObject(int x, int y, int height,
			int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2)
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		if (flag) {
			if (type == 0)
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2)
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
		}
	}

	public static void addObject(int objectId, int x, int y, int height,
			int type, int direction) {
		if (objectId == 733 || objectId == 4058 || objectId == 154)
			return;
		if (objectId == -1) {
			removeClipping(x, y, height, type);
			return;
		}
		final ObjectDefinition def = ObjectDefinition.getObjectDef(objectId);
		if (def == null)
			return;
		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.aBoolean767())
				addClipping(x, y, height, 0x200000);
		} else if (type >= 9) {
			if (def.aBoolean767())
				addClippingForSolidObject(x, y, height, xLength, yLength,
						def.solid());
		} else if (type >= 0 && type <= 3)
			if (def.aBoolean767())
				addClippingForVariableObject(x, y, height, type, direction,
						def.solid());
	}

	public static boolean blockedEast(int x, int y, int z) {
		return (getClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public static boolean blockedNorth(int x, int y, int z) {
		return (getClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public static boolean blockedNorthEast(int x, int y, int z) {
		return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public static boolean blockedNorthWest(int x, int y, int z) {
		return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public static boolean blockedSouth(int x, int y, int z) {
		return (getClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public static boolean blockedSouthEast(int x, int y, int z) {
		return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public static boolean blockedSouthWest(int x, int y, int z) {
		return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	public static boolean blockedWest(int x, int y, int z) {
		return (getClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public static boolean canMove(int startX, int startY, int endX, int endY,
			int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;

		final int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ++ii) {
			final int currentX = endX - diffX;
			final int currentY = endY - diffY;
			for (int i = 0; i < xLength; ++i)
				for (int i2 = 0; i2 < yLength; ++i2)
					if (diffX < 0 && diffY < 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 - 1,
								height) & 19398926) != 0
								|| (getClipping(currentX + i - 1,
										currentY + i2, height) & 19398920) != 0
								|| (getClipping(currentX + i,
										currentY + i2 - 1, height) & 19398914) != 0)
							return false;
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1,
								height) & 19399136) != 0
								|| (getClipping(currentX + i + 1,
										currentY + i2, height) & 19399040) != 0
								|| (getClipping(currentX + i,
										currentY + i2 + 1, height) & 19398944) != 0)
							return false;
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 + 1,
								height) & 19398968) != 0
								|| (getClipping(currentX + i - 1,
										currentY + i2, height) & 19398920) != 0
								|| (getClipping(currentX + i,
										currentY + i2 + 1, height) & 19398944) != 0)
							return false;
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 - 1,
								height) & 19399043) != 0
								|| (getClipping(currentX + i + 1,
										currentY + i2, height) & 19399040) != 0
								|| (getClipping(currentX + i,
										currentY + i2 - 1, height) & 19398914) != 0)
							return false;
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2,
								height) & 19399040) != 0)
							return false;
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping(currentX + i - 1, currentY + i2,
								height) & 19398920) != 0)
							return false;
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1,
								height) & 19398944) != 0)
							return false;
					} else if (diffX == 0
							&& diffY < 0
							&& (getClipping(currentX + i, currentY + i2 - 1,
									height) & 19398914) != 0)
						return false;
			if (diffX < 0)
				++diffX;
			else if (diffX > 0)
				--diffX;
			if (diffY < 0)
				++diffY;
			else if (diffY > 0)
				--diffY;
		}
		return true;
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists())
			return null;
		byte[] buffer = new byte[(int) f.length()];
		final DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		final byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		final GZIPInputStream gzip = new GZIPInputStream(
				new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out
						.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			final int readByte = gzip.read(gzipInputBuffer, bufferlength,
					gzipInputBuffer.length - bufferlength);
			if (readByte == -1)
				break;
			bufferlength += readByte;
		} while (true);
		final byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if (buffer.length < 10)
			return null;
		return buffer;
	}

	public static int getClipping(int x, int y, int height) {
		if (x > 2307 && x < 2370 && y > 3780 && y < 3868)
			return 0x200000;

		final int regionX = x >> 3;
		final int regionY = y >> 3;
		final int regionId = (regionX / 8 << 8) + regionY / 8;
		for (final Region r : regions)
			if (r.id() == regionId)
				return r.getClip(x, y, height);
		return 0x200000;
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX,
			int moveTypeY) {
		try {
			height %= 4;
			final int checkX = x + moveTypeX;
			final int checkY = y + moveTypeY;
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x128010e) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0;
			else if (moveTypeX == 1 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280183) == 0
						&& (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0;
			else if (moveTypeX == -1 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280138) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0;
			else if (moveTypeX == 1 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x12801e0) == 0
						&& (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0;
			else {
				System.out.println("[FATAL ERROR]: At getClipping: " + x + ", "
						+ y + ", " + height + ", " + moveTypeX + ", "
						+ moveTypeY);
				return false;
			}
		} catch (final Exception e) {
			return true;
		}
	}

	public static int[] getNextStep(int baseX, int baseY, int toX, int toY,
			int height, int xLength, int yLength) {
		int moveX = 0;
		int moveY = 0;
		if (baseX - toX > 0)
			--moveX;
		else if (baseX - toX < 0)
			++moveX;
		if (baseY - toY > 0)
			--moveY;
		else if (baseY - toY < 0)
			++moveY;
		return canMove(baseX, baseY, baseX + moveX, baseY + moveY, height,
				xLength, yLength) ? new int[] { baseX + moveX, baseY + moveY }
				: moveX != 0
						&& canMove(baseX, baseY, baseX + moveX, baseY, height,
								xLength, yLength) ? new int[] { baseX + moveX,
						baseY } : moveY != 0
						&& canMove(baseX, baseY, baseX, baseY + moveY, height,
								xLength, yLength) ? new int[] { baseX,
						baseY + moveY } : new int[] { baseX, baseY };
	}

	public static boolean isMembers(int x, int y, int height) {
		if (x >= 3272 && x <= 3320 && y >= 2752 && y <= 2809)
			return false;
		if (x >= 2640 && x <= 2677 && y >= 2638 && y <= 2679)
			return false;
		final int regionX = x >> 3;
		final int regionY = y >> 3;
		final int regionId = (regionX / 8 << 8) + regionY / 8;
		for (final Region r : regions)
			if (r.id() == regionId)
				return r.members();
		return false;
	}

	public static void load() { // Yes, we do have those.
		try {
			final File f = new File(Constants.DATA_DIRECTORY
					+ "cache/map_index");
			final byte[] buffer = new byte[(int) f.length()];
			final DataInputStream dis = new DataInputStream(
					new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			final ByteStream in = new ByteStream(buffer);
			final int size = in.length() / 7;
			regions = new Region[size];
			final int[] regionIds = new int[size];
			final int[] mapGroundFileIds = new int[size];
			final int[] mapObjectsFileIds = new int[size];
			final boolean[] isMembers = new boolean[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = in.getUShort();
				mapGroundFileIds[i] = in.getUShort();
				mapObjectsFileIds[i] = in.getUShort();
				in.getUByte();
			}
			for (int i = 0; i < size; i++)
				regions[i] = new Region(regionIds[i], isMembers[i]);
			for (int i = 0; i < size; i++) {
				final byte[] file1 = getBuffer(new File(
						Constants.DATA_DIRECTORY + "cache/map/"
								+ mapObjectsFileIds[i] + ".gz"));
				final byte[] file2 = getBuffer(new File(
						Constants.DATA_DIRECTORY + "cache/map/"
								+ mapGroundFileIds[i] + ".gz"));
				if (file1 == null || file2 == null)
					continue;
				try {
					loadMaps(regionIds[i], new ByteStream(file1),
							new ByteStream(file2));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		for (int i = 3429; i < 3436; i++)
			removeClipping(2474, i, 0, Integer.MAX_VALUE);
		for (int i = 2477; i < 2483; i++)
			removeClipping(i, 3420, 2, Integer.MAX_VALUE);
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		final int absX = (regionId >> 8) * 64;
		final int absY = (regionId & 0xff) * 64;
		final int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++)
			for (int i2 = 0; i2 < 64; i2++)
				for (int i3 = 0; i3 < 64; i3++)
					while (true) {
						final int v = str2.getUByte();
						if (v == 0)
							break;
						else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49)
							str2.skip(1);
						else if (v <= 81)
							someArray[i][i2][i3] = v - 49;
					}
		for (int i = 0; i < 4; i++)
			for (int i2 = 0; i2 < 64; i2++)
				for (int i3 = 0; i3 < 64; i3++)
					if ((someArray[i][i2][i3] & 1) == 1) { // is walkable?
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2)
							// with bridges
							height--;
						if (height >= 0 && height <= 3)
							addClipping(absX + i2, absY + i3, height, 0x200000);
					}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				final int localX = location >> 6 & 0x3f;
				final int localY = location & 0x3f;
				int height = location >> 12;
				final int objectData = str1.getUByte();
				final int type = objectData >> 2;
				final int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64)
					continue;
				if ((someArray[1][localX][localY] & 2) == 2)
					height--;
				if (height >= 0 && height <= 3)
					addObject(objectId, absX + localX, absY + localY, height,
							type, direction);
			}
		}
	}

	public static boolean pathBlocked(Entity attacker, Entity victim) {

		double offsetX = Math.abs(attacker.getAbsX() - victim.getAbsX());
		double offsetY = Math.abs(attacker.getAbsY() - victim.getAbsY());

		int distance = TileControl.calculateDistance(attacker, victim);

		if (distance == 0)
			return true;

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		final int[][] path = new int[distance][5];

		int curX = attacker.getAbsX();
		int curY = attacker.getAbsY();
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > victim.getAbsX()) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < victim.getAbsX()) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > victim.getAbsY()) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < victim.getAbsY()) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = attacker.getHeightLevel();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++)
			if (!Region.getClipping(path[i][0], path[i][1], path[i][2],
					path[i][3], path[i][4]))
				return true;
		return false;
	}

	public static void removeClipping(int x, int y, int height, int shift) {
		final int regionX = x >> 3;
		final int regionY = y >> 3;
		final int regionId = (regionX / 8 << 8) + regionY / 8;
		for (final Region r : regions)
			if (r.id() == regionId) {
				r.removeClip(x, y, height, shift);
				break;
			}
	}

	private final int id;

	private int clips[][][];

	private boolean members = false;

	public Region(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	private void addClip(int x, int y, int height, int shift) {

		final int regionAbsX = (id >> 8) * 64;
		final int regionAbsY = (id & 0xff) * 64;
		if (clips == null)
			clips = new int[8000][][];
		if (clips[height] == null)
			clips[height] = new int[64][64];
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	public int getClip(int x, int y, int height) {
		final int regionAbsX = (id >> 8) * 64;
		final int regionAbsY = (id & 0xff) * 64;
		if (height < 0 || x - regionAbsX < 0 || y - regionAbsY < 0
				|| clips == null || clips[height] == null)
			return 0;
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public int id() {
		return id;
	}

	public boolean members() {
		return members;
	}

	private void removeClip(int x, int y, int height, int shift) {
		final int regionAbsX = (id >> 8) * 64;
		final int regionAbsY = (id & 0xff) * 64;
		if (clips == null)
			return;
		if (clips[height] == null)
			return;
		clips[height][x - regionAbsX][y - regionAbsY] &= 16777215 - shift;
	}

}
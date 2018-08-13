package com.server2.world.map;

import com.server2.Constants;

public final class ObjectDefinition {

	public static MemoryArchive getArchive() {
		return archive;
	}

	public static byte[] getBuffer(String s) {
		try {
			final java.io.File f = new java.io.File(Constants.DATA_DIRECTORY
					+ "cache/object/" + s);
			if (!f.exists())
				return null;
			final byte[] buffer = new byte[(int) f.length()];
			final java.io.DataInputStream dis = new java.io.DataInputStream(
					new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch (final Exception e) {
		}
		return null;
	}

	public static ObjectDefinition[] getCache() {
		return cache;
	}

	public static int getCacheIndex() {
		return cacheIndex;
	}

	public static ObjectDefinition getObjectDef(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].type == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 20;
		final ObjectDefinition class46 = cache[cacheIndex];
		class46.type = i;
		class46.setDefaults();
		final byte[] buffer = archive.get(i);
		if (buffer != null && buffer.length > 0)
			class46.readValues(new ByteStreamExt(buffer));
		return class46;
	}

	public static boolean isLowMem() {
		return lowMem;
	}

	public static void load() {
		archive = new MemoryArchive(new ByteStream(getBuffer("loc.dat")),
				new ByteStream(getBuffer("loc.idx")));
		cache = new ObjectDefinition[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new ObjectDefinition();

	}

	public static void setArchive(MemoryArchive archive) {
		ObjectDefinition.archive = archive;
	}

	public static void setCache(ObjectDefinition[] cache) {
		ObjectDefinition.cache = cache;
	}

	public static void setCacheIndex(int cacheIndex) {
		ObjectDefinition.cacheIndex = cacheIndex;
	}

	public static void setLowMem(boolean lowMem) {
		ObjectDefinition.lowMem = lowMem;
	}

	public boolean aBoolean736;

	private byte aByte737;

	private int anInt738;

	public String name;

	private int anInt740;
	private byte aByte742;
	public int anInt744;

	private int anInt745;

	public int anInt746;

	private int[] originalModelColors;

	private int anInt748;

	public int anInt749;

	public static boolean lowMem;

	public int type;

	public boolean aBoolean757;

	public int anInt758;

	public int childrenIDs[];

	private int anInt760;

	public int anInt761;

	public boolean aBoolean762;

	public boolean aBoolean764;

	public boolean aBoolean767;

	public int anInt768;

	private static int cacheIndex;

	private int anInt772;

	private int[] anIntArray773;

	public int anInt774;

	public int anInt775;

	private int[] anIntArray776;

	public byte description[];

	public boolean hasActions;

	public boolean aBoolean779;

	public int anInt781;

	private static ObjectDefinition[] cache;

	private int anInt783;

	private int[] modifiedModelColors;

	public String actions[];

	private static MemoryArchive archive;

	private ObjectDefinition() {
		type = -1;
	}

	public boolean aBoolean767() {
		return aBoolean767;
	}

	public byte getaByte737() {
		return aByte737;
	}

	public byte getaByte742() {
		return aByte742;
	}

	public String[] getActions() {
		return actions;
	}

	public int getAnInt738() {
		return anInt738;
	}

	public int getAnInt740() {
		return anInt740;
	}

	public int getAnInt744() {
		return anInt744;
	}

	public int getAnInt745() {
		return anInt745;
	}

	public int getAnInt746() {
		return anInt746;
	}

	public int getAnInt748() {
		return anInt748;
	}

	public int getAnInt749() {
		return anInt749;
	}

	public int getAnInt758() {
		return anInt758;
	}

	public int getAnInt760() {
		return anInt760;
	}

	public int getAnInt761() {
		return anInt761;
	}

	public int getAnInt768() {
		return anInt768;
	}

	public int getAnInt772() {
		return anInt772;
	}

	public int getAnInt774() {
		return anInt774;
	}

	public int getAnInt775() {
		return anInt775;
	}

	public int getAnInt781() {
		return anInt781;
	}

	public int getAnInt783() {
		return anInt783;
	}

	public int[] getAnIntArray773() {
		return anIntArray773;
	}

	public int[] getAnIntArray776() {
		return anIntArray776;
	}

	public int getBiggestSize() {
		if (anInt761 > anInt744)
			return anInt761;
		return anInt744;
	}

	public int[] getChildrenIDs() {
		return childrenIDs;
	}

	public byte[] getDescription() {
		return description;
	}

	public int[] getModifiedModelColors() {
		return modifiedModelColors;
	}

	public String getName() {
		return name;
	}

	public int[] getOriginalModelColors() {
		return originalModelColors;
	}

	public int getType() {
		return type;
	}

	public boolean hasActions() {
		return hasActions;
	}

	public boolean hasName() {
		return name != null && name.length() > 1;
	}

	public boolean isaBoolean736() {
		return aBoolean736;
	}

	public boolean isaBoolean757() {
		return aBoolean757;
	}

	public boolean isaBoolean762() {
		return aBoolean762;
	}

	public boolean isaBoolean764() {
		return aBoolean764;
	}

	public boolean isaBoolean767() {
		return aBoolean767;
	}

	public boolean isaBoolean779() {
		return aBoolean779;
	}

	public boolean isHasActions() {
		return hasActions;
	}

	private void readValues(ByteStreamExt stream) {
		@SuppressWarnings("unused")
		final int flag = -1;
		do {
			final int type = stream.readUnsignedByte();
			if (type == 0)
				break;
			if (type == 1) {
				final int len = stream.readUnsignedByte();
				if (len > 0)
					if (anIntArray773 == null || lowMem) {
						anIntArray776 = new int[len];
						anIntArray773 = new int[len];
						for (int k1 = 0; k1 < len; k1++) {
							anIntArray773[k1] = stream.readUnsignedWord();
							anIntArray776[k1] = stream.readUnsignedByte();
						}
					} else
						stream.currentOffset += len * 3;
			} else if (type == 2)
				name = stream.readNewString();
			else if (type == 5) {
				final int len = stream.readUnsignedByte();
				if (len > 0)
					if (anIntArray773 == null || lowMem) {
						anIntArray776 = null;
						anIntArray773 = new int[len];
						for (int l1 = 0; l1 < len; l1++)
							anIntArray773[l1] = stream.readUnsignedWord();
					} else
						stream.currentOffset += len * 2;
			} else if (type == 14)
				anInt744 = stream.readUnsignedByte();
			else if (type == 15)
				anInt761 = stream.readUnsignedByte();
			else if (type == 17)
				aBoolean767 = false;
			else if (type == 18)
				aBoolean757 = false;
			else if (type == 19)
				hasActions = stream.readUnsignedByte() == 1;
			else if (type == 21)
				aBoolean762 = true;
			else if (type == 22) {
			} else if (type == 23)
				aBoolean764 = true;
			else if (type == 24) {
				anInt781 = stream.readUnsignedWord();
				if (anInt781 == 65535)
					anInt781 = -1;
			} else if (type == 27)
				continue;
			else if (type == 28)
				anInt775 = stream.readUnsignedByte();
			else if (type == 29)
				aByte737 = stream.readSignedByte();
			else if (type == 39)
				aByte742 = stream.readSignedByte();
			else if (type >= 30 && type < 39) {
				if (actions == null)
					actions = new String[5];
				actions[type - 30] = stream.readNewString();
				hasActions = true;
				if (actions[type - 30].equalsIgnoreCase("hidden"))
					actions[type - 30] = null;
			} else if (type == 40) {
				final int i1 = stream.readUnsignedByte();
				modifiedModelColors = new int[i1];
				originalModelColors = new int[i1];
				for (int i2 = 0; i2 < i1; i2++) {
					modifiedModelColors[i2] = stream.readUnsignedWord();
					originalModelColors[i2] = stream.readUnsignedWord();
				}
			} else if (type == 41) {
				final int l = stream.readUnsignedByte();
				stream.skip(l * 4);
			} else if (type == 42) {
				final int l = stream.readUnsignedByte();
				stream.skip(l);
			} else if (type == 60)
				anInt746 = stream.readUnsignedWord();
			else if (type == 62) {
			} else if (type == 64)
				aBoolean779 = false;
			else if (type == 65)
				anInt748 = stream.readUnsignedWord();
			else if (type == 66)
				anInt772 = stream.readUnsignedWord();
			else if (type == 67)
				anInt740 = stream.readUnsignedWord();
			else if (type == 68)
				anInt758 = stream.readUnsignedWord();
			else if (type == 69)
				anInt768 = stream.readUnsignedByte();
			else if (type == 70)
				setAnInt738(stream.readSignedWord());
			else if (type == 71)
				anInt745 = stream.readSignedWord();
			else if (type == 72)
				anInt783 = stream.readSignedWord();
			else if (type == 73)
				aBoolean736 = true;
			else if (type == 74) {
			} else if (type == 75)
				anInt760 = stream.readUnsignedByte();
			else if (type == 77 || type == 92) {
				anInt774 = stream.readUnsignedWord();
				if (anInt774 == 65535)
					anInt774 = -1;
				anInt749 = stream.readUnsignedWord();
				if (anInt749 == 65535)
					anInt749 = -1;
				int endChild = -1;
				if (type == 92) {
					endChild = stream.readUnsignedWord();
					if (endChild == 65535)
						endChild = -1;
				}
				final int j1 = stream.readUnsignedByte();
				childrenIDs = new int[j1 + 2];
				for (int j2 = 0; j2 <= j1; j2++) {
					childrenIDs[j2] = stream.readUnsignedWord();
					if (childrenIDs[j2] == 65535)
						childrenIDs[j2] = -1;
				}
				childrenIDs[j1 + 1] = endChild;
			} else if (type == 78)
				stream.skip(3);
			else if (type == 79) {
				stream.skip(5);
				final int l = stream.readUnsignedByte();
				stream.skip(l * 2);
			} else if (type == 81)
				stream.skip(1);
			else if (type == 82 || type == 88 || type == 89 || type == 90
					|| type == 91 || type == 94 || type == 95 || type == 96
					|| type == 97)
				continue;
			else if (type == 93)
				stream.skip(2);
			else if (type == 249) {
				final int l = stream.readUnsignedByte();
				for (int ii = 0; ii < l; ii++) {
					final boolean b = stream.readUnsignedByte() == 1;
					stream.skip(3);
					if (b)
						stream.readNewString();
					else
						stream.skip(4);
				}
			} else
				System.out.println("Unknown config: " + type);
		} while (true);
	}

	public void setaBoolean736(boolean aBoolean736) {
		this.aBoolean736 = aBoolean736;
	}

	public void setaBoolean757(boolean aBoolean757) {
		this.aBoolean757 = aBoolean757;
	}

	public void setaBoolean762(boolean aBoolean762) {
		this.aBoolean762 = aBoolean762;
	}

	public void setaBoolean764(boolean aBoolean764) {
		this.aBoolean764 = aBoolean764;
	}

	public void setaBoolean767(boolean aBoolean767) {
		this.aBoolean767 = aBoolean767;
	}

	public void setaBoolean779(boolean aBoolean779) {
		this.aBoolean779 = aBoolean779;
	}

	public void setaByte737(byte aByte737) {
		this.aByte737 = aByte737;
	}

	public void setaByte742(byte aByte742) {
		this.aByte742 = aByte742;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public void setAnInt738(int anInt738) {
		this.anInt738 = anInt738;
	}

	public void setAnInt740(int anInt740) {
		this.anInt740 = anInt740;
	}

	public void setAnInt744(int anInt744) {
		this.anInt744 = anInt744;
	}

	public void setAnInt745(int anInt745) {
		this.anInt745 = anInt745;
	}

	public void setAnInt746(int anInt746) {
		this.anInt746 = anInt746;
	}

	public void setAnInt748(int anInt748) {
		this.anInt748 = anInt748;
	}

	public void setAnInt749(int anInt749) {
		this.anInt749 = anInt749;
	}

	public void setAnInt758(int anInt758) {
		this.anInt758 = anInt758;
	}

	public void setAnInt760(int anInt760) {
		this.anInt760 = anInt760;
	}

	public void setAnInt761(int anInt761) {
		this.anInt761 = anInt761;
	}

	public void setAnInt768(int anInt768) {
		this.anInt768 = anInt768;
	}

	public void setAnInt772(int anInt772) {
		this.anInt772 = anInt772;
	}

	public void setAnInt774(int anInt774) {
		this.anInt774 = anInt774;
	}

	public void setAnInt775(int anInt775) {
		this.anInt775 = anInt775;
	}

	public void setAnInt781(int anInt781) {
		this.anInt781 = anInt781;
	}

	public void setAnInt783(int anInt783) {
		this.anInt783 = anInt783;
	}

	public void setAnIntArray773(int[] anIntArray773) {
		this.anIntArray773 = anIntArray773;
	}

	public void setAnIntArray776(int[] anIntArray776) {
		this.anIntArray776 = anIntArray776;
	}

	public void setChildrenIDs(int[] childrenIDs) {
		this.childrenIDs = childrenIDs;
	}

	private void setDefaults() {
		anIntArray773 = null;
		anIntArray776 = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		anInt744 = 1;
		anInt761 = 1;
		aBoolean767 = true;
		aBoolean757 = true;
		hasActions = false;
		aBoolean762 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		setAnInt738(0);
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}

	public void setDescription(byte[] description) {
		this.description = description;
	}

	public void setHasActions(boolean hasActions) {
		this.hasActions = hasActions;
	}

	public void setModifiedModelColors(int[] modifiedModelColors) {
		this.modifiedModelColors = modifiedModelColors;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOriginalModelColors(int[] originalModelColors) {
		this.originalModelColors = originalModelColors;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean solid() {
		return aBoolean779;
	}

	public int xLength() {
		return anInt744;
	}

	public int yLength() {
		return anInt761;
	}

}
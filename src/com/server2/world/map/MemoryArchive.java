package com.server2.world.map;

public class MemoryArchive {

	private final ByteStream cache;
	private final ByteStream index;
	private static final int INDEX_DATA_CHUNK_SIZE = 12;

	public MemoryArchive(ByteStream cache, ByteStream index) {
		this.cache = cache;
		this.index = index;
	}

	public int contentSize() {
		return index.length() / 12;
	}

	public byte[] get(int dataIndex) {
		try {
			if (index.length() < dataIndex * INDEX_DATA_CHUNK_SIZE)
				return null;
			index.setOffset(dataIndex * INDEX_DATA_CHUNK_SIZE);
			final long fileOffset = index.getLong();
			final int fileSize = index.getInt();
			cache.setOffset(fileOffset);
			final byte[] buffer = cache.read(fileSize);
			return buffer;
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

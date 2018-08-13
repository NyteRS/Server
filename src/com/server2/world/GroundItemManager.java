package com.server2.world;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.server2.content.TradingConstants;
import com.server2.model.Item;
import com.server2.model.entity.GroundItem;
import com.server2.model.entity.Location;
import com.server2.model.entity.player.Player;
import com.server2.util.Misc;

/**
 * 
 * @author Rene
 * 
 */
public final class GroundItemManager {

	private static GroundItemManager INSTANCE = new GroundItemManager();

	private static final int[][] brokenBarrows = { { 4708, 4860 },
			{ 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 },
			{ 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 },
			{ 4732, 4932 }, { 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 },
			{ 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 },
			{ 4745, 4956 }, { 4747, 4962 }, { 4749, 4968 }, { 4751, 4974 },
			{ 4753, 4980 }, { 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public static GroundItemManager getInstance() {
		return INSTANCE;
	}

	private final ConcurrentLinkedQueue<GroundItem> items = new ConcurrentLinkedQueue<GroundItem>();

	public void createGlobalItem(Location pos, Item item) {
		final GroundItem groundItem = new GroundItem(item, pos, -1, false,
				"false");
		getGroundItemList().add(groundItem);
		createGroundItemForAll(groundItem);
	}

	public GroundItem createGroundItem(long encodedName, Player player,
			Item item, Location position) {
		return this.createGroundItem(encodedName, player, item, position, "");
	}

	public GroundItem createGroundItem(long encodedName, Player player,
			Item item, Location position, String pked) {
		int id = item.getId();
		if (id > 23000 || id < 0)
			return null;
		if (id >= 2412 && id <= 2414) {
			if (player != null)
				player.sendMessage("The cape vanishes as it touches the ground.");
			return null;
		}
		if (id > 4705 && id < 4760)
			for (final int[] brokenBarrow : GroundItemManager.brokenBarrows)
				if (brokenBarrow[0] == id) {
					id = brokenBarrow[1];
					break;
				}
		if (Item.itemStackable[item.getId()])
			for (GroundItem groundItem : getGroundItemList()) {
				if (groundItem.isVisible())
					continue;
				if (groundItem.getEncodedName() != encodedName)
					continue;
				if (!position.equals(groundItem.getPosition()))
					continue;
				if (id == groundItem.getItem().getId()) {
					final int oldAmount = groundItem.getItem().getCount();
					final int maximum = Integer.MAX_VALUE - oldAmount;
					if (item.getCount() > maximum) {
						groundItem.increaseAmount(maximum);
						groundItem = new GroundItem(Item.create(item.getId(),
								item.getCount() - maximum), position,
								encodedName, player == null, pked);
						getGroundItemList().add(groundItem);
						if (player != null)
							player.getGroundItemDistributor()
									.getGroundItemQueue().add(groundItem);
					} else
						groundItem.increaseAmount(item.getCount());
					if (player != null)
						player.getActionSender().replaceGroundItem(groundItem,
								oldAmount, groundItem.getItem().getCount());
					return groundItem;
				}
			}
		final GroundItem groundItem = new GroundItem(item, position,
				encodedName, player == null, pked);

		getGroundItemList().add(groundItem);
		if (player != null)
			player.getGroundItemDistributor().getGroundItemQueue()
					.add(groundItem);
		return groundItem;
	}

	public GroundItem createGroundItem(Player player, Item item) {
		return this.createGroundItem(player, item, player.getPosition());
	}

	public GroundItem createGroundItem(Player player, Item item,
			Location position) {
		return this
				.createGroundItem(player.encodedName, player, item, position);
	}

	public GroundItem createGroundItem(Player player, Item item,
			Location position, String pked) {
		return this.createGroundItem(player.encodedName, player, item,
				position, pked);
	}

	public void createGroundItemForAll(GroundItem item) {
		if (item == null)
			return;
		if (TradingConstants.isUntradable(item.getItem().getId()))
			return;
		for (final Player player : PlayerManager.getSingleton().getPlayers()) {
			if (player == null)
				continue;

			final Location playerPosition = player.getPosition();
			final Location itemPosition = item.getPosition();
			if (playerPosition.getZ() == itemPosition.getZ()
					&& Misc.distanceTo(playerPosition, itemPosition, 1) <= 60)
				player.getGroundItemDistributor().getGroundItemQueue()
						.add(item);
		}
	}

	public void deleteGroundItem(Player player, int id, int x, int y) {
		final Iterator<GroundItem> iterator = getGroundItemList().iterator();
		for (; iterator.hasNext();) {
			final GroundItem item = iterator.next();
			if (id != item.getItem().getId())
				continue;
			final Location position = item.getPosition();
			if (x != position.getX())
				continue;
			if (y != position.getY())
				continue;
			if (player.getPosition().getZ() != position.getZ())
				continue;
			if (!item.isVisible(player))
				continue;
			if (item.isVisible())
				removeGroundItemForAll(item);
			else
				player.getGroundItemDistributor().getDeletionQueue().add(item);
			iterator.remove();
			return;
		}
	}

	public synchronized GroundItem getGroundItem(Player player, int id, int x,
			int y) {
		final Iterator<GroundItem> iterator = getGroundItemList().iterator();
		for (; iterator.hasNext();) {
			final GroundItem item = iterator.next();
			if (id != item.getItem().getId())
				continue;
			final Location position = item.getPosition();
			if (x != position.getX())
				continue;
			if (y != position.getY())
				continue;
			if (player.getHeightLevel() != position.getZ())
				continue;
			if (!item.isVisible(player))
				continue;
			return item;
		}
		return null;
	}

	public final ConcurrentLinkedQueue<GroundItem> getGroundItemList() {
		synchronized (items) {
			return items;
		}
	}

	public boolean itemExists(Player player, int id, int x, int y) {
		for (final GroundItem groundItem : getGroundItemList()) {
			if (id != groundItem.getItem().getId())
				continue;
			if (!groundItem.isVisible(player))
				continue;
			if (groundItem.getPosition().getZ() != player.getPosition().getZ())
				continue;
			if (!groundItem.getPosition().matches(x, y))
				continue;
			return true;
		}
		return false;
	}

	public void loadRegion(Player player) {
		final Location playerPosition = player.getPosition();
		for (final GroundItem item : getGroundItemList()) {
			final Location itemPosition = item.getPosition();
			if (playerPosition.getZ() != itemPosition.getZ()
					|| Misc.distanceTo(playerPosition, itemPosition, 1) > 60)
				continue;
			if (!item.isVisible(player))
				continue;
			player.getGroundItemDistributor().getDeletionQueue().add(item);
			player.getGroundItemDistributor().getGroundItemQueue().add(item);
		}
	}

	public boolean removeGroundItem(GroundItem item) {
		item.setRemoved();
		synchronized (items) {
			return items.remove(item);
		}
	}

	public void removeGroundItemForAll(GroundItem item) {
		for (final Player player : PlayerManager.getSingleton().getPlayers()) {
			if (player == null)
				continue;

			final Location playerPosition = player.getPosition();
			final Location itemPosition = item.getPosition();
			if (playerPosition.getZ() == itemPosition.getZ()
					&& Misc.distanceTo(playerPosition, itemPosition, 1) <= 60)
				player.getGroundItemDistributor().getDeletionQueue().add(item);
		}
	}

	public int size() {
		return getGroundItemList().size();
	}

	public void tick() {
		final Iterator<GroundItem> iterator = getGroundItemList().iterator();
		for (; iterator.hasNext();) {
			final GroundItem item = iterator.next();
			item.tick();
			if (item.isRemoved()) {
				iterator.remove();
				removeGroundItemForAll(item);
			} else if (item.makeVisible()) {
				final Player p = PlayerManager.getSingleton()
						.getPlayerByNameLong(item.getEncodedName());
				if (p != null)
					p.getGroundItemDistributor().getDeletionQueue().add(item);
				createGroundItemForAll(item);
			}
		}
	}

}
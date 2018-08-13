package com.server2.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.server2.Constants;
import com.server2.content.anticheat.StringCheating;
import com.server2.engine.cycle.CycleEvent;
import com.server2.engine.cycle.CycleEventContainer;
import com.server2.model.entity.player.Player;
import com.server2.util.BanProcessor;
import com.server2.util.LogHandler;
import com.server2.util.Misc;
import com.server2.util.NameUtils;
import com.server2.util.SpamFilter;
import com.server2.util.XStreamLibrary;

/**
 * 
 * @author Rene
 * 
 */

public class Clan {

	/**
	 * Represents a member of the clan.
	 * 
	 * @author Ultimate1
	 */
	public static class ClanMember {

		/**
		 * An enum of clan ranks.
		 * 
		 * @author Ultimate1
		 */
		public enum Rank {
			GUEST, RECRUIT, LUITENENT, OWNER
		}

		/**
		 * The slot of the member.
		 */
		private int slot;

		/**
		 * The name of the member.
		 */
		private final String name;

		/**
		 * The clan the member is in.
		 */
		@XStreamOmitField
		private String clanOwner;

		/**
		 * The rank of the member.
		 */
		@XStreamOmitField
		private Rank rank = Rank.RECRUIT;;

		/**
		 * Initialise the clan member.
		 * 
		 * @param slot
		 * @param name
		 * @param owner
		 */
		private ClanMember(int slot, String name, String clanOwner) {
			this.slot = slot;
			this.name = name;
			this.clanOwner = clanOwner;
		}

		/**
		 * Initiate the clan member.
		 * 
		 * @param name
		 * @return
		 * @return
		 */
		public ClanMember(String name) {
			this(-1, name, null);
		}

		/**
		 * @return the clan member as a player.
		 */
		public Player asPlayer() {
			return PlayerManager.getSingleton().getPlayerByName(name);
		}

		/**
		 * @return the clan the member is in.
		 */
		public Clan getClan() {
			return Clan.forOwner(clanOwner);
		}

		/**
		 * @return the name of the member.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the position of the member on the clan list.
		 */
		public int getPosition() {
			return 18143 + slot;
		}

		/**
		 * Get the member prefix
		 * 
		 * @return
		 */
		public char getPrefix() {
			return rank.toString().charAt(0);
		}

		/**
		 * @return the rank of the clan member.
		 */
		public Rank getRank() {
			return rank;
		}

		/**
		 * @return the slot of the member.
		 */
		public int getSlot() {
			return slot;
		}

		/**
		 * Check if the clan member is the specified rank.
		 * 
		 * @param rank
		 * @return
		 */
		public boolean isRank(Rank rank) {
			return this.rank.equals(rank);
		}

	}

	/**
	 * Maps clan names to clans,
	 */
	private static Map<String, Clan> clans = new HashMap<String, Clan>();

	/**
	 * Authenticates a clan join.
	 * 
	 * @param player
	 */
	public static void authenticateJoin(String password, Player player) {
		final Clan targetClan = Clan.forOwner(player.clanToJoin);
		if (targetClan != null)
			if (password.equals(targetClan.password))
				Clan.join(targetClan.owner, false, false, player);
			else
				player.getActionSender().sendMessage(
						"@red@You have entered an invalid password!");
	}

	/**
	 * Changes the name of the clan.
	 * 
	 * @param name
	 * @param player
	 */
	public static void changeName(String name, Player player) {
		if (name.length() == 0)
			return;
		final ClanMember clanMember = player.getClanDetails();
		if (clanMember == null) {
			// player.getActionSender().sendMessage("You are not in a clan.");
		} else {
			final Clan targetClan = clanMember.getClan();
			if (targetClan == null)
				return;
			targetClan.name = NameUtils.formatDisplayName(name);
			for (final ClanMember member : targetClan.members) {
				if (member == null)
					continue;
				final Player p = member.asPlayer();
				if (p != null)
					targetClan.updateClanInformation(p);
			}
			targetClan.saveRequired = true;
		}
	}

	/**
	 * Changes the password for the clan.
	 * 
	 * @param password
	 * @param player
	 */
	public static void changePassword(String password, Player player) {
		if (password.length() == 0)
			return;
		final ClanMember clanMember = player.getClanDetails();
		if (clanMember == null) {
			// player.getActionSender().sendMessage("You are not in a clan.");
		} else {
			final Clan targetClan = clanMember.getClan();
			if (targetClan == null)
				return;
			if (password.equals(targetClan.password))
				player.getActionSender().sendMessage(
						"Your new password is the same as your old one.");
			else {
				player.getActionSender()
						.sendMessage(
								"The password for your clan is now '"
										+ password + "'.");
				targetClan.password = password;
			}
			targetClan.saveRequired = true;
		}
	}

	/**
	 * Creates a new clan.
	 * 
	 * @param name
	 * @param owner
	 * @return
	 */
	public static Clan create(String name, String owner) {
		return new Clan(name, owner, null);
	}

	/**
	 * Gets the clan for the specified owner.
	 * 
	 * @param owner
	 * @return
	 */
	public static Clan forOwner(String owner) {
		return clans.get(owner);
	}

	/**
	 * @return the map of clans.
	 */
	public static Map<String, Clan> getClans() {
		return clans;
	}

	/**
	 * Handles clan dialogue options.
	 * 
	 * @param option
	 * @param player
	 */
	public static void handleDialogueOption(String option, Player player) {
		if (option.equals("Enter Password"))
			player.setExpectedInput("EnterPassword", true);
		else {
			final ClanMember clanMember = player.getClanDetails();
			if (clanMember == null) {
				// player.getActionSender().sendMessage("You are not in a clan.");
			} else {
				final Clan targetClan = clanMember.getClan();
				if (targetClan == null)
					return;
				if (option.equals("Change Clan Name"))
					player.setExpectedInput("NewClanName", true);
				else if (option.equals("Create/Change Password"))
					player.setExpectedInput("NewClanPassword", true);
				else if (option.equals("Remove Password")) {
					player.getActionSender().sendWindowsRemoval();
					if (targetClan.password == null)
						player.getActionSender().sendMessage(
								"You clan does not have a password.");
					else {
						player.getActionSender()
								.sendMessage(
										"The clan password has been successfully removed.");
						targetClan.password = null;
						targetClan.saveRequired = true;
					}
				}
			}
		}
	}

	/**
	 * Handles a moderation command.
	 * 
	 * @param command
	 * @param nameOfTarget
	 * @param player
	 */
	public static void handleModerationCommands(String command,
			String nameOfTarget, Player player) {
		final ClanMember clanMember = player.getClanDetails();
		if (clanMember == null) {
			// player.getActionSender().sendMessage("You are not in a clan.");
		} else {
			boolean refresh = false;
			final Clan targetClan = clanMember.getClan();
			if (targetClan == null)
				return;
			final ClanMember target = targetClan.getMember(nameOfTarget);
			if (target == null && !command.equals("unban"))
				player.getActionSender().sendMessage(
						"The specified player is not in your clan.");
			else if (nameOfTarget.equalsIgnoreCase(targetClan.owner)
					|| nameOfTarget.equalsIgnoreCase(player.getUsername()))
				player.getActionSender().sendMessage(
						"You cannot do this to yourself");
			else if (command.startsWith("unban"))
				targetClan.unban(nameOfTarget, player);
			else if (command.startsWith("promote")) {
				if (target.isRank(ClanMember.Rank.LUITENENT))
					player.getActionSender().sendMessage(
							NameUtils.formatDisplayName(nameOfTarget)
									+ " cannot be promoted any furthur.");
				else
					targetClan.updateRank(target, ClanMember.Rank.LUITENENT,
							false, player);
			} else if (command.startsWith("demote")) {
				if (target.isRank(ClanMember.Rank.RECRUIT))
					player.getActionSender().sendMessage(
							NameUtils.formatDisplayName(nameOfTarget)
									+ " cannot be demoted any furthur.");
				else
					targetClan.updateRank(target, ClanMember.Rank.RECRUIT,
							true, player);
			} else {
				final Player other = target.asPlayer();
				if (command.startsWith("ban")) {
					if (other != null) {
						other.getActionSender().clearClanChatInterface(
								targetClan.getMemberCount() + 1, true);
						other.getActionSender().sendMessage(
								"You have been banned from "
										+ NameUtils
												.formatDisplayName(targetClan
														.getOwner())
										+ "'s clan.");
						other.resetClan();
					}
					targetClan.ban(target.name, player);
					targetClan.remove(target);
					refresh = true;
				} else if (command.startsWith("kick")) {
					if (other != null) {
						other.getActionSender().clearClanChatInterface(
								targetClan.getMemberCount() + 1, true);
						other.getActionSender().sendMessage(
								"You have been kicked from "
										+ NameUtils
												.formatDisplayName(targetClan
														.getOwner())
										+ "'s clan.");
						other.resetClan();
					}
					targetClan.remove(target);
					refresh = true;
				}
			}

			if (refresh)
				for (final ClanMember member : targetClan.members) {
					if (member == null)
						continue;
					final Player p = member.asPlayer();
					if (p != null) {
						p.getActionSender().clearClanChatInterface(
								targetClan.getMemberCount() + 1, false);
						for (final ClanMember other : targetClan.members) {
							if (other == null)
								continue;
							targetClan.update(other.name, p);
						}
					}
				}

		}
	}

	/**
	 * Join the clan.
	 * 
	 * @param clanOwner
	 * @param player
	 * @param createIfAbsent
	 * @param authenticate
	 */
	public static void join(String clanOwner, boolean createIfAbsent,
			boolean authenticate, final Player player) {
		final Clan targetClan = Clan.forOwner(clanOwner);
		final ClanMember clanMember = new ClanMember(player.getUsername());

		player.getActionSender().sendMessage("Attempting to join channel...");
		if (createIfAbsent)
			if (targetClan == null) {
				if (!clanOwner.equalsIgnoreCase(player.getUsername()))
					player.getActionSender().sendMessage(
							NameUtils.formatDisplayName(clanOwner)
									+ " does not have a clan chat channel.");
				else
					try {
						clans.put(clanOwner.toLowerCase(),
								Clan.create("Not set", clanOwner));
					} finally {
						Clan.join(clanOwner, false, true, player);
					}
				return;
			}

		if (targetClan.isBanned(player.getUsername())) {
			player.getActionSender()
					.sendMessage(
							"You have been banned from joining this clan chat channel.");
			return;
		}

		if (authenticate)
			if (!targetClan.getOwner().equalsIgnoreCase(player.getUsername())
					&& targetClan.isPrivate()) {
				player.clanToJoin = targetClan.owner;
				player.getActionSender()
						.sendMessage(
								"@red@This clan has a password, please enter it when the screen pops up.");
				player.getPlayerEventHandler().addEvent(new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						player.setExpectedInput("EnterPassword", true);
						container.stop();
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub

					}
				}, 4);
				return;
			}

		if (!targetClan.add(clanMember))
			player.getActionSender().sendMessage(
					"This clan chat is full, please try again later!");
		else {
			player.setClanDetails(clanMember);
			targetClan.add(player.getClanDetails());
			targetClan.updateClanInformation(player);
			for (final ClanMember member : targetClan.members) {
				if (member == null)
					continue;
				final Player other = member.asPlayer();
				if (other != null)
					targetClan.update(player.getUsername(), other);
				targetClan.update(member.name, player);
			}
			targetClan.sendWelcomeMessage(player);
		}
	}

	/**
	 * Leave the current clan chat channel.
	 * 
	 * @param player
	 */
	public static void leave(Player player) {
		final ClanMember clanMember = player.getClanDetails();
		if (clanMember == null) {
			// player.getActionSender().sendMessage("You are not in a clan.");
		} else {
			final Clan targetClan = clanMember.getClan();
			if (targetClan == null)
				return;
			player.getActionSender().clearClanChatInterface(
					targetClan.getMemberCount() + 1, true);
			targetClan.remove(clanMember);
			for (final ClanMember member : targetClan.members) {
				if (member == null)
					continue;
				final Player p = member.asPlayer();
				if (p != null) {
					p.getActionSender().clearClanChatInterface(
							targetClan.getMemberCount() + 1, false);
					for (final ClanMember other : targetClan.members) {
						if (other == null)
							continue;
						targetClan.update(other.name, p);
					}
				}
			}
			player.getActionSender().sendMessage("You have left the channel.");
			player.resetClan();
		}
	}

	/**
	 * Load the clan handler.
	 */
	public static void load() {
		final File[] files = new File(Constants.DATA_DIRECTORY + "clans/")
				.listFiles();
		for (final File file : files) {
			Clan loadedClan = null;
			try {
				loadedClan = (Clan) XStreamLibrary.load(file.getAbsolutePath());
			} catch (final FileNotFoundException e) {
				return;
			}
			loadedClan.members = new ClanMember[100];
			clans.put(loadedClan.getOwner(), loadedClan);
		}
	}

	/**
	 * Handle player login.
	 * 
	 * @param player
	 */
	public static void login(Player player) {
		Clan targetClan = null;

		if (player.loadedClanKey.length() == 0)
			targetClan = clans.get("help");
		else if (player.loadedClanKey.length() > 0)
			targetClan = clans.get(player.loadedClanKey);
		final int linesToRefresh = targetClan == null ? 100 : targetClan
				.getMemberCount() + 1;
		player.getActionSender().clearClanChatInterface(linesToRefresh, true);
		if (targetClan != null) {
			player.getActionSender().sendMessage(
					"Attempting to join channel...");
			final ClanMember clanMember = new ClanMember(player.username);
			if (!targetClan.add(clanMember)) {
				player.getActionSender().sendMessage(
						"This clan chat is currently full.");
				return;
			}
			player.setClanDetails(clanMember);
			targetClan.updateClanInformation(player);
			for (final ClanMember member : targetClan.members) {
				if (member == null)
					continue;
				final Player other = member.asPlayer();
				if (other != null && other != player)
					targetClan.update(player.getUsername(), other);
				targetClan.update(member.name, player);
			}
			targetClan.sendWelcomeMessage(player);
		}
	}

	/**
	 * Sends a message to all members of the clan.
	 * 
	 * @param message
	 */
	public static void sendMessage(String message, Player player) {
		final ClanMember clanMember = player.getClanDetails();
		if (clanMember == null) {
			// player.getActionSender().sendMessage("You are not in a clan.");
		} else {
			final Clan targetClan = clanMember.getClan();
			if (targetClan == null || message.length() <= 1)
				return;
			if (player.isMuted()) {
				player.getActionSender().sendMessage(
						"You cannot do this while muted.");
				return;
			}
			message = message.trim();
			if (StringCheating.isFake(message) && player.getPrivileges() != 3) {
				player.getActionSender().sendMessage(
						"You cannot use color codes in the clan chat.");
				return;
			}
			if (!SpamFilter.canSend(message.toLowerCase())) {
				player.warnings++;
				player.getActionSender().sendMessage(
						"You are not allowed to mention websites, warning "
								+ player.warnings + ".");
				if (player.warnings >= 5) {
					try {
						BanProcessor.writeBanRecord(player.getUsername(), 0,
								player.getUsername(), 2);
						LogHandler.logMute(player.getUsername(),
								player.getUsername());
						player.getActionSender().sendMessage(
								"You have been automuted!");
					} catch (final Exception e) {
						e.printStackTrace();
					}
					player.setMuted(true);
				}
				return;
			}

			for (final ClanMember member : targetClan.members) {
				if (member == null)
					continue;
				final Player p = member.asPlayer();
				if (p != null)
					if (p.getPrivileges() == 8)
						p.getActionSender().sendMessage(
								"[@red@"
										+ targetClan.name
										+ "@bla@] [@whi@S@bla@] "
										+ Misc.capitalizeFirstLetter(player
												.getUsername()) + ": @dre@"
										+ Misc.capitalizeFirstLetter(message));
					else
						p.getActionSender().sendMessage(
								"[@red@"
										+ targetClan.name
										+ "@bla@] "
										+ Misc.capitalizeFirstLetter(player
												.getUsername()) + ": @dre@"
										+ Misc.capitalizeFirstLetter(message));
			}
		}
	}

	/**
	 * The name of the name.
	 */
	private String name;

	/**
	 * The owner of the clan.
	 */
	private final String owner;

	/**
	 * The password required to enter the clan.
	 */
	private String password;

	/**
	 * A flag stating weather or not a save of the clan file is required.
	 */
	@XStreamOmitField
	private boolean saveRequired;

	/**
	 * A list of clan staff.
	 */
	private final Map<String, ClanMember.Rank> staff = new HashMap<String, ClanMember.Rank>();

	/**
	 * A list of banned clan members.
	 */
	private final List<String> banned = new ArrayList<String>();

	/**
	 * An array of clan members.
	 */
	@XStreamOmitField
	private ClanMember[] members = new ClanMember[200];

	/**
	 * The time the clan started waiting.s
	 */
	@XStreamOmitField
	private long waitStartedAt;

	/**
	 * The time the game started.
	 */
	@XStreamOmitField
	private long gameStartedAt;

	/**
	 * Game in progress flag.
	 */
	@XStreamOmitField
	private boolean gameInProgress;

	/**
	 * Initialises the clan.
	 * 
	 * @param name
	 * @param owner
	 * @param password
	 */
	private Clan(String name, String owner, String password) {
		this.name = name;
		this.owner = owner;
		this.password = password;
		saveRequired = true;
	}

	/**
	 * Adds a clan member to the clan.
	 * 
	 * @param member
	 * @return
	 */
	public boolean add(ClanMember member) {
		final int slot = getFreeSlot();
		if (slot == -1 || getMember(member.name) != null)
			return false;
		member.slot = slot;
		member.clanOwner = owner;
		member.rank = getRank(member.name);
		members[member.slot] = member;
		saveRequired = true;
		return true;
	}

	/**
	 * Adds a player to the ban list.
	 * 
	 * @param target
	 * @param player
	 * @return
	 */
	public boolean ban(String target, Player player) {
		if (isBanned(target))
			return false;
		banned.add(target.toLowerCase());
		player.getActionSender().sendMessage(
				"You have successfully banned "
						+ NameUtils.formatDisplayName(target) + ".");
		saveRequired = true;
		return true;
	}

	/**
	 * Gets a free slot in the members array.
	 * 
	 * @return
	 */
	public int getFreeSlot() {
		for (int i = 1; i < members.length; i++)
			if (members[i] == null)
				return i;
		return -1;
	}

	/**
	 * @return the time the game started.
	 */
	public long getGameStartedAt() {
		return gameStartedAt;
	}

	/**
	 * Gets a clan member using thier name.
	 * 
	 * @param name
	 * @return
	 */
	public ClanMember getMember(String name) {
		for (final ClanMember member : members) {
			if (member == null)
				continue;
			if (member.getName().equalsIgnoreCase(name))
				return member;
		}
		return null;
	}

	/**
	 * @return the amount of members in the clan.
	 */
	public int getMemberCount() {
		int count = 0;
		for (final ClanMember member : members)
			if (member != null)
				count++;
		return count;
	}

	/**
	 * @return the clan members.
	 */
	public ClanMember[] getMembers() {
		return members;
	}

	/**
	 * @return the name of the clan.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the owner of the clan.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Gets the rank of the specified clan member.
	 * 
	 * @param member
	 * @return
	 */
	public ClanMember.Rank getRank(String member) {
		final ClanMember.Rank rank = staff.get(member.toLowerCase());
		if (rank != null)
			return rank;
		if (member.equalsIgnoreCase(owner))
			return ClanMember.Rank.OWNER;
		return ClanMember.Rank.RECRUIT;
	}

	/**
	 * @return the time we started waiting.
	 */
	public long getWaitStartedAt() {
		return waitStartedAt;
	}

	/**
	 * Checks if the ban list contains the target member.
	 * 
	 * @param target
	 * @return
	 */
	public boolean isBanned(String target) {
		return banned.contains(target.toLowerCase());
	}

	/**
	 * @return true if there is a game in progress.
	 */
	public boolean isGameInProgess() {
		return gameInProgress;
	}

	/**
	 * @return true if this is a private clan.
	 */
	public boolean isPrivate() {
		return password != null;
	}

	/**
	 * @return the saveRequired flag.
	 */
	public boolean isSaveRequired() {
		return saveRequired;
	}

	/**
	 * Removes a clan member from the clan.
	 * 
	 * @param remove
	 */
	public void remove(ClanMember remove) {
		for (int i = remove.slot; i < members.length - 1; i++) {
			members[i] = members[i + 1];
			if (members[i] != null)
				members[i].slot = i;
		}
		saveRequired = true;
	}

	/**
	 * Sends the clan welcome message.
	 * 
	 * @param player
	 */
	public void sendWelcomeMessage(Player player) {
		player.getActionSender().sendMessage(
				"Now talking in friends chat channel " + name + "@bla@.");
		player.getActionSender().sendMessage(
				"To talk start each line of chat with the / symbol.");
		player.getActionSender().sendString("Manage", 18135);
		player.getActionSender().sendSidebar(7, 18128);
	}

	/**
	 * Sets the game in progress flag.
	 * 
	 * @param gameInProgress
	 */
	public void setGameInProgress(boolean gameInProgress) {
		this.gameInProgress = gameInProgress;
	}

	/**
	 * Set the time the game started.
	 * 
	 * @param gameStartedAt
	 */
	public void setGameStartedAt(long gameStartedAt) {
		this.gameStartedAt = gameStartedAt;
	}

	/**
	 * Sets the save required flag.
	 * 
	 * @param saveRequired
	 */
	public void setSaveRequired(boolean saveRequired) {
		this.saveRequired = saveRequired;
	}

	/**
	 * Set the time we started waiting.
	 * 
	 * @param waitStartedAt
	 */
	public void setWaitStartedAt(long waitStartedAt) {
		this.waitStartedAt = waitStartedAt;
	}

	/**
	 * Removes a player from the ban list.
	 * 
	 * @param target
	 * @param player
	 * @return
	 */
	public boolean unban(String target, Player player) {
		if (!isBanned(target))
			return false;
		banned.remove(target.toLowerCase());
		player.getActionSender().sendMessage(
				"You have successfully un-banned "
						+ NameUtils.formatDisplayName(target) + ".");
		saveRequired = true;
		return true;
	}

	/**
	 * Updates a specific clan member.
	 * 
	 * @param target
	 * @param player
	 */
	public void update(String target, Player player) {
		final ClanMember member = getMember(target);
		if (member != null)
			player.getActionSender().sendString(
					"@bla@[@whi@" + member.getPrefix() + "@bla@] "
							+ (member.asPlayer() != null ? "@gre@" : "@red@")
							+ Misc.capitalizeFirstLetter(member.getName()),
					member.getPosition());
	}

	/**
	 * Updates the clan information.
	 * 
	 * @param player
	 */
	public void updateClanInformation(Player player) {
		player.getActionSender().sendString("Talking in: @yel@" + name, 18139);
		player.getActionSender().sendString(
				"Owner: " + NameUtils.formatDisplayName(owner), 18140);
	}

	/**
	 * Promotes a member of the clan.
	 * 
	 * @param target
	 * @param rank
	 * @param demote
	 * @param player
	 */
	public void updateRank(ClanMember target, ClanMember.Rank rank,
			boolean demote, Player player) {
		if (demote)
			staff.remove(target.name.toLowerCase());
		else
			staff.put(target.name.toLowerCase(), rank);
		target.rank = rank;
		player.getActionSender().sendMessage(
				NameUtils.formatDisplayName(target.name)
						+ " has been successfully "
						+ (demote ? "demoted" : "promoted") + ".");
		for (final ClanMember member : members) {
			if (member == null)
				continue;
			final Player p = member.asPlayer();
			if (p != null)
				update(target.name, p);
		}
		saveRequired = true;
	}

}
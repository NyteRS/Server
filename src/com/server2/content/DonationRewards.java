package com.server2.content;

import java.sql.ResultSet;

import com.server2.Settings;
import com.server2.model.Item;
import com.server2.model.entity.player.Player;
import com.server2.world.World;

/**
 * 
 * @author Lukas Pinckers & Rene Roosen
 * 
 */
public class DonationRewards {

	/**
	 * Instances the Rewards Class.
	 */
	public static DonationRewards INSTANCE = new DonationRewards();

	/**
	 * Gets the instance instanced earlier.
	 * 
	 * @return INSTANCE
	 */
	public static DonationRewards getInstance() {
		return INSTANCE;
	}

	/**
	 * A string containing the items[] which is fetched from the DB.
	 */
	private String items[];
	/**
	 * Do we need to re-add some of the items to the DB due to lack of space?
	 */
	private boolean someThingLeft;
	/**
	 * Which items are new?
	 */
	private String newItems = "";
	/**
	 * Outprints the result of str which gets the string from the DB.
	 */
	private String str;
	/**
	 * The price of the item purchased.
	 */
	private double price = 0;

	/**
	 * Looks which item needs to be given and trims the string.
	 * 
	 * @param client
	 * @param item
	 */
	public void giveItem(Player client, String item) {
		final String donationItem = item.trim();
		if (donationItem.equalsIgnoreCase("itemrandombox"))
			client.getActionSender().addItem(6199, 1);
		else if (donationItem.equalsIgnoreCase("item4151"))
			client.getActionSender().addItem(4151, 1);
		else if (donationItem.equalsIgnoreCase("item11694"))
			client.getActionSender().addItem(11694, 1);
		else if (donationItem.equalsIgnoreCase("item14484"))
			client.getActionSender().addItem(14484, 1);
		else if (donationItem.equalsIgnoreCase("item19784"))
			client.getActionSender().addItem(19784, 1);
		else if (donationItem.equalsIgnoreCase("item13899"))
			client.getActionSender().addItem(13899, 1);
		else if (donationItem.equalsIgnoreCase("itemskillerbox"))
			client.getActionSender().addItem(18768, 1);
		else if (donationItem.equalsIgnoreCase("item18353"))
			client.getActionSender().addItem(18353, 1);
		else if (donationItem.equalsIgnoreCase("item18349"))
			client.getActionSender().addItem(18349, 1);
		else if (donationItem.equalsIgnoreCase("item18351"))
			client.getActionSender().addItem(18351, 1);
		else if (donationItem.equalsIgnoreCase("item18355"))
			client.getActionSender().addItem(18355, 1);
		else if (donationItem.equalsIgnoreCase("item18357"))
			client.getActionSender().addItem(18357, 1);
		else if (donationItem.equalsIgnoreCase("item15486"))
			client.getActionSender().addItem(15486, 1);
		else if (donationItem.equalsIgnoreCase("item13740"))
			client.getActionSender().addItem(13740, 1);
		else if (donationItem.equalsIgnoreCase("item13738"))
			client.getActionSender().addItem(13738, 1);
		else if (donationItem.equalsIgnoreCase("item18335"))
			client.getActionSender().addItem(18335, 1);
		else if (donationItem.equalsIgnoreCase("item6585"))
			client.getActionSender().addItem(6585, 1);
		else if (donationItem.equalsIgnoreCase("item6737"))
			client.getActionSender().addItem(6737, 1);
		else if (donationItem.equalsIgnoreCase("item21787"))
			client.getActionSender().addItem(21787, 1);
		else if (donationItem.equalsIgnoreCase("item21793"))
			client.getActionSender().addItem(21793, 1);
		else if (donationItem.equalsIgnoreCase("item21790"))
			client.getActionSender().addItem(21790, 1);
		else if (donationItem.equalsIgnoreCase("item13887"))
			client.getActionSender().addItem(13887, 1);
		else if (donationItem.equalsIgnoreCase("item13893"))
			client.getActionSender().addItem(13893, 1);
		else if (donationItem.equalsIgnoreCase("item11724"))
			client.getActionSender().addItem(11724, 1);
		else if (donationItem.equalsIgnoreCase("item11726"))
			client.getActionSender().addItem(11726, 1);
		else if (donationItem.equalsIgnoreCase("item11235"))
			client.getActionSender().addItem(11235, 1);
		else if (donationItem.equalsIgnoreCase("item18359"))
			client.getActionSender().addItem(18359, 1);
		else if (donationItem.equalsIgnoreCase("item18363"))
			client.getActionSender().addItem(18363, 1);
		else if (donationItem.equalsIgnoreCase("item18361"))
			client.getActionSender().addItem(18361, 1);
		else if (donationItem.equalsIgnoreCase("item10551"))
			client.getActionSender().addItem(10551, 1);
		else if (donationItem.equalsIgnoreCase("itemrocktail"))
			client.getActionSender().addItem(15273, 5000);
		else if (donationItem.equalsIgnoreCase("item11212"))
			client.getActionSender().addItem(11212, 5000);
		else if (donationItem.equalsIgnoreCase("item9244"))
			client.getActionSender().addItem(9244, 2000);
		else if (donationItem.equalsIgnoreCase("item3025"))
			client.getActionSender().addItem(3025, 1000);
		else if (donationItem.equalsIgnoreCase("item6686"))
			client.getActionSender().addItem(6686, 1000);
		else if (donationItem.equalsIgnoreCase("itemsuperset")) { /* Superset */
			client.getActionSender().addItem(2437, 200);
			client.getActionSender().addItem(2441, 200);
			client.getActionSender().addItem(2443, 200);
		} else if (donationItem.equalsIgnoreCase("item1746"))
			client.getActionSender().addItem(1746, 1000);
		else if (donationItem.equalsIgnoreCase("item1516"))
			client.getActionSender().addItem(1516, 1000); /* Yew logs */
		else if (donationItem.equalsIgnoreCase("item1514"))
			client.getActionSender().addItem(1514, 1000); /* Magic Logs */
		else if (donationItem.equalsIgnoreCase("item1518"))
			client.getActionSender().addItem(1518, 1000); /* Maple logs */
		else if (donationItem.equalsIgnoreCase("item2362"))
			client.getActionSender().addItem(2362, 1000);
		else if (donationItem.equalsIgnoreCase("item454"))
			client.getActionSender().addItem(454, 1000);
		else if (donationItem.equalsIgnoreCase("item6694"))
			client.getActionSender().addItem(6694, 1000);
		else if (donationItem.equalsIgnoreCase("itemrunebars"))
			client.getActionSender().addItem(2364, 500);
		else if (donationItem.equalsIgnoreCase("itemfbones"))
			client.getActionSender().addItem(18831, 100);
		else if (donationItem.equalsIgnoreCase("item4698"))
			client.getActionSender().addItem(4698, 1000);
		else if (donationItem.equalsIgnoreCase("item868"))
			client.getActionSender().addItem(new Item(868, 5000));
		else if (donationItem.equalsIgnoreCase("weekItem1"))
			client.getActionSender().addItem(1050, 1);
		else if (donationItem.equalsIgnoreCase("handcannon+shots")) { /*
																	 * Hand
																	 * cannon +
																	 * 1k shots
																	 */
			client.getActionSender().addItem(15241, 1);
			client.getActionSender().addItem(15243, 1000);
		} else if (donationItem.equalsIgnoreCase("shots"))
			client.getActionSender().addItem(15243, 1000);
		else if (donationItem.equalsIgnoreCase("santa"))
			client.getActionSender().addItem(1050, 1);
		else if (donationItem.equalsIgnoreCase("ghmask"))
			client.getActionSender().addItem(1053, 1);
		else if (donationItem.equalsIgnoreCase("item392"))
			client.getActionSender().addItem(15273, 5000);
		else if (donationItem.equalsIgnoreCase("coins"))
			client.getActionSender().addItem(995, 100000000);
		else if (donationItem.equalsIgnoreCase("itemvine"))
			client.getActionSender().addItem(21371, 1);
		else if (donationItem.equalsIgnoreCase("bhmask"))
			client.getActionSender().addItem(1055, 1);
		else if (donationItem.equalsIgnoreCase("rhmask"))
			client.getActionSender().addItem(1057, 1);
		else if (donationItem.equalsIgnoreCase("pphat"))
			client.getActionSender().addItem(1046, 1);
		else if (donationItem.equalsIgnoreCase("yphat"))
			client.getActionSender().addItem(1040, 1);
		else if (donationItem.equalsIgnoreCase("gphat"))
			client.getActionSender().addItem(1044, 1);
		else if (donationItem.equalsIgnoreCase("rphat"))
			client.getActionSender().addItem(1038, 1);
		else if (donationItem.equalsIgnoreCase("wphat"))
			client.getActionSender().addItem(1048, 1);
		else if (donationItem.equalsIgnoreCase("ccape"))
			client.getActionSender().addItem(9790, 1);
		else if (donationItem.equalsIgnoreCase("3agemelee")) { /* whip vine */
			client.getActionSender().addItem(10350, 1);
			client.getActionSender().addItem(10348, 1);
			client.getActionSender().addItem(10346, 1);
			client.getActionSender().addItem(10352, 1);
		} else if (donationItem.equalsIgnoreCase("3agerange")) { /* whip vine */
			client.getActionSender().addItem(10336, 1);
			client.getActionSender().addItem(10334, 1);
			client.getActionSender().addItem(10332, 1);
			client.getActionSender().addItem(10330, 1);
		} else if (donationItem.equalsIgnoreCase("3agedruidic")) { /* whip vine */
			client.getActionSender().addItem(19308, 1);
			client.getActionSender().addItem(19311, 1);
			client.getActionSender().addItem(19314, 1);
			client.getActionSender().addItem(19317, 1);
			client.getActionSender().addItem(19320, 1);
		} else if (donationItem.equalsIgnoreCase("3agemagic")) { /* whip vine */
			client.getActionSender().addItem(10344, 1);
			client.getActionSender().addItem(10342, 1);
			client.getActionSender().addItem(10340, 1);
			client.getActionSender().addItem(10338, 1);
		} else if (donationItem.equalsIgnoreCase("sspack")) { /* whip vine */
			client.getActionSender().addItem(13738, 1);
			client.getActionSender().addItem(13740, 1);
			client.getActionSender().addItem(13742, 1);
			client.getActionSender().addItem(13744, 1);
		} else if (donationItem.equalsIgnoreCase("cwpack")) { /* whip vine */
			client.getActionSender().addItem(18349, 1);
			client.getActionSender().addItem(18351, 1);
			client.getActionSender().addItem(18353, 1);
			client.getActionSender().addItem(18355, 1);
			client.getActionSender().addItem(18357, 1);
		} else if (donationItem
				.equalsIgnoreCase("upack68s412f8965e7r6f65r7y4jh2t")) { /*
																		 * ultimate
																		 * set
																		 */
			client.getActionSender().addItem(1038, 1);
			client.getActionSender().addItem(1040, 1);
			client.getActionSender().addItem(1044, 1);
			client.getActionSender().addItem(1046, 1);
			client.getActionSender().addItem(1048, 1);
			client.getActionSender().addItem(1042, 1);
			client.getActionSender().addItem(20135, 1);
			client.getActionSender().addItem(20139, 1);
			client.getActionSender().addItem(20143, 1);
			client.getActionSender().addItem(20147, 1);
			client.getActionSender().addItem(20151, 1);
			client.getActionSender().addItem(20155, 1);
			client.getActionSender().addItem(20159, 1);
			client.getActionSender().addItem(20163, 1);
			client.getActionSender().addItem(20167, 1);
			client.getActionSender().addItem(20171, 1);
			client.getActionSender().addItem(1050, 1);
			client.getActionSender().addItem(1053, 1);
			client.getActionSender().addItem(1055, 1);
			client.getActionSender().addItem(1057, 1);
			client.getActionSender().addItem(1961, 1);
			client.getActionSender().addItem(981, 1);
			client.getActionSender().addItem(962, 1);
			client.getActionSender().addItem(995, 2147000000);
		} else if (donationItem.equalsIgnoreCase("nexpack")) { /* whip vine */
			client.getActionSender().addItem(20135, 1);
			client.getActionSender().addItem(20139, 1);
			client.getActionSender().addItem(20143, 1);
			client.getActionSender().addItem(20147, 1);
			client.getActionSender().addItem(20151, 1);
			client.getActionSender().addItem(20155, 1);
			client.getActionSender().addItem(20159, 1);
			client.getActionSender().addItem(20163, 1);
			client.getActionSender().addItem(20167, 1);
			client.getActionSender().addItem(20171, 1);

		} else if (donationItem.equalsIgnoreCase("phatpack")) { /* whip vine */
			client.getActionSender().addItem(1038, 1);
			client.getActionSender().addItem(1040, 1);
			client.getActionSender().addItem(1044, 1);
			client.getActionSender().addItem(1046, 1);
			client.getActionSender().addItem(1048, 1);
			client.getActionSender().addItem(1042, 1);

		}

		else if (donationItem.equalsIgnoreCase("evoidpack")) { /* whip vine */
			client.getActionSender().addItem(19712, 1);
			client.getActionSender().addItem(19785, 1);
			client.getActionSender().addItem(19786, 1);
			client.getActionSender().addItem(8841, 1);
			client.getActionSender().addItem(8842, 1);
			client.getActionSender().addItem(11663, 1);
			client.getActionSender().addItem(11664, 1);
			client.getActionSender().addItem(11665, 1);

		}

		else if (donationItem.equalsIgnoreCase("deflector"))
			client.getActionSender().addItem(19712, 1);
		else if (donationItem.equalsIgnoreCase("tokhaar"))
			client.getActionSender().addItem(19111, 1);
		else if (donationItem.equalsIgnoreCase("coinbox"))
			client.getActionSender().addItem(3062, 1);
		else if (donationItem.equalsIgnoreCase("potluckbox"))
			client.getActionSender().addItem(10025, 1);
		else if (donationItem.equalsIgnoreCase("godbox"))
			client.getActionSender().addItem(6183, 1);
		else if (donationItem.equalsIgnoreCase("torva")) { /* whip vine */
			client.getActionSender().addItem(20135, 1);
			client.getActionSender().addItem(20139, 1);
			client.getActionSender().addItem(20143, 1);

		} else if (donationItem.equalsIgnoreCase("pernix")) { /* whip vine */
			client.getActionSender().addItem(20147, 1);
			client.getActionSender().addItem(20151, 1);
			client.getActionSender().addItem(20155, 1);

		} else if (donationItem.equalsIgnoreCase("virtus")) { /* whip vine */
			client.getActionSender().addItem(20159, 1);
			client.getActionSender().addItem(20163, 1);
			client.getActionSender().addItem(20167, 1);

		} else if (donationItem.equalsIgnoreCase("royal")) { /* whip vine */
			client.getActionSender().addItem(15503, 1);
			client.getActionSender().addItem(15505, 1);
			client.getActionSender().addItem(15507, 1);
			client.getActionSender().addItem(15509, 1);
			client.getActionSender().addItem(15511, 1);

		} else if (donationItem.equalsIgnoreCase("weekitme74d86sf7468sf746ds")) { /*
																				 * marshall
																				 * set
																				 */
			client.getActionSender().addItem(15039, 1);
			client.getActionSender().addItem(15040, 1);
			client.getActionSender().addItem(15041, 1);
			client.getActionSender().addItem(15042, 1);
			client.getActionSender().addItem(15043, 1);
			client.getActionSender().addItem(15044, 1);

		}

		else if (donationItem.equalsIgnoreCase("weekitemgrenwall"))
			client.getActionSender().addItem(12539, 1000);
	}

	/**
	 * Gives the reward to the user and checks for timing and initiates the SQL
	 * connection.
	 * 
	 * @param client
	 */
	public void giveRewardl(final Player client) {
		if (client == null)
			return;
		World.getWorld().submit(new Runnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - client.lastDonateCheck < 30000) {
					client.getDM()
							.sendNpcChat2(
									"You can only check your donations every 30 seconds.",
									"", 3805, "Postie Pete");
					return;
				}

				if (client.getActionAssistant().freeSlots() < 20) {
					client.getActionSender()
							.sendMessage(
									"You need atleast 20 free inventory slots to check your donations.");
					client.getActionSender().sendWindowsRemoval();
					return;
				}

				try {
					client.lastDonateCheck = System.currentTimeMillis();

					final ResultSet rs = World.getGameDatabase().executeQuery(
							"SELECT * FROM donationitems789 WHERE userid = '"
									+ client.getUsername() + "'");

					if (rs.next()) {
						str = rs.getString("items");

						items = str.split(",");

						if (str == null || str == "" || str.length() < 2) {
							client.getDM()
									.sendNpcChat2(
											"You haven't got any items waiting for delivery.",
											"If this is a bug, email to support@"
													+ Settings
															.getString("sv_site")
													+ ".", 3805, "Postie Pete");
							rs.close();
							return;
						}

					} else {
						client.getDM()
								.sendNpcChat2(
										"You haven't got any items waiting for delivery.",
										"If this is a bug, email to support@"
												+ Settings.getString("sv_site")
												+ ".", 3805, "Postie Pete");
						rs.close();
						return;
					}
					for (int i = 0; i < items.length; i++)
						if (client.getActionAssistant().freeSlots() > 0
								&& items[i] != "itemsuperset"
								|| client.getActionAssistant().freeSlots() > 2
								&& items[i] == "itemsuperset") {
							if (items[i] != "" && items[i].length() >= 2) {
								giveItem(client, items[i]);
								items[i] = "";
							}
						} else {
							client.getDM()
									.sendNpcChat2(
											"You do not have anymore inventory space, please bank some items.",
											"", 3805, "Postie Pete");
							client.getActionSender().sendWindowsRemoval();
							setSomeThingLeft(true);
							break;
						}
					newItems = "";
					if (someThingLeft)
						for (final String item : items)
							if (item != "" || item != null)
								if (item.length() >= 2)
									newItems = item + ", ";

					World.getGameDatabase().executeQuery(
							"UPDATE donationitems789 SET items='" + newItems
									+ "' WHERE userid='" + client.getUsername()
									+ "'");
					client.getDM().sendNpcChat2(
							"Thank you for your donation, remember, donations keep "
									+ Settings.getString("sv_name")
									+ " running!", "", 3805, "Postie Pete");

					final ResultSet rs2 = World.getGameDatabase().executeQuery(
							"SELECT * FROM donationitems789 WHERE userid = '"
									+ client.getUsername() + "'");

					if (rs2.next()) {
						price = Double.valueOf(rs2.getString("price"));
						client.amountDonated = price;
					}

					rs2.close();

					if (client.amountDonated >= 5 && client.amountDonated < 50) {
						if (client.donatorRights != 1) {
							client.donatorRights = 1;
							client.saved = false;
							if (client.getPrivileges() != 1
									&& client.getPrivileges() != 2
									&& client.getPrivileges() != 3)
								client.setPrivileges(7);
							client.getActionSender()
									.sendMessage(
											"Your account has been upgraded to : @red@Donator.");
							client.getActionSender().sendWindowsRemoval();
						}
					} else if (client.amountDonated >= 50
							&& client.amountDonated < 250) {
						if (client.donatorRights != 2) {
							client.donatorRights = 2;
							client.saved = false;
							if (client.getPrivileges() != 1
									&& client.getPrivileges() != 2
									&& client.getPrivileges() != 3)
								client.setPrivileges(5);
							client.getActionSender()
									.sendMessage(
											"Your account has been upgraded to : @blu@Super Donator.");
							client.getActionSender().sendWindowsRemoval();
						}
					} else if (client.amountDonated >= 250)
						if (client.donatorRights != 3) {
							client.donatorRights = 3;
							client.saved = false;
							if (client.getPrivileges() != 1
									&& client.getPrivileges() != 2
									&& client.getPrivileges() != 3)
								client.setPrivileges(6);
							client.getActionSender()
									.sendMessage(
											"Your account has been upgraded to : @gre@Extreme Donator.");
							client.getActionSender().sendWindowsRemoval();
						}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void handleDialogue(Player client, int stage) {
		if (stage == 0) {
			client.getDM().sendNpcChat2("Postie pete right here!",
					"How can i help you today?", 3805, "Postie Pete");
			client.nDialogue = 53;
		}
	}

	public boolean isSomeThingLeft() {
		return someThingLeft;
	}

	public void setSomeThingLeft(boolean someThingLeft) {
		this.someThingLeft = someThingLeft;
	}

}

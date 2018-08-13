package com.server2.content.skills.hunter;

import com.server2.content.skills.Skill;
import com.server2.model.entity.player.Player;
import com.server2.model.entity.player.PlayerConstants;
import com.server2.util.Misc;

public class ImplingHunting {

	public enum Butterfly {

		RUBY_HARVEST("Ruby Harvest", 10010, 5085, 10020, 150, 30), Sapphire_Glacialis(
				"Sapphire Glacialis", 10010, 5084, 10018, 200, 45), Snowy_Knight(
				"Snowy Knight", 10010, 5083, 10016, 250, 75), Black_Warlock(
				"Black Warlock", 10010, 5082, 10014, 325, 85);

		public static Butterfly forId(int npcId) {
			for (final Butterfly butterfly : Butterfly.values())
				if (butterfly.getNpcId() == npcId)
					return butterfly;
			return null;
		}

		private String name = "";
		private final int Net, npcId, itemId, AmtExp, Req;

		private Butterfly(String npcName, int Net, int npcId, int itemId,
				int AmtExp, int Req) {
			name = npcName;
			this.Net = Net;
			this.npcId = npcId;
			this.itemId = itemId;
			this.AmtExp = AmtExp;
			this.Req = Req;
		}

		public int getExp() {
			return AmtExp;
		}

		public int getItemId() {
			return itemId;
		}

		public String getName() {
			return name;
		}

		public int getNet() {
			return Net;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getReq() {
			return Req;
		}
	}

	public enum Impling {

		Baby_Impling("Baby Impling", 10010, 6055, 11238, 20, 1), Young_Impling(
				"Young Impling", 10010, 6056, 11240, 48, 20), Gourmet_Impling(
				"Gourmet Impling", 10010, 6057, 11242, 82, 20), Earth_Impling(
				"Earth Impling", 10010, 6058, 11244, 126, 34), Essence_Impling(
				"Essence Impling", 10010, 6059, 11246, 160, 40), Electric_Impling(
				"Electric Impling", 10010, 6060, 11248, 205, 50), Nature_Impling(
				"Nature Impling", 10010, 6061, 11250, 250, 58), Magpie_Impling(
				"Magpie Impling", 10010, 6062, 11252, 289, 65), Ninja_Impling(
				"Ninja Impling", 10010, 6063, 11254, 339, 74), Dragon_Impling(
				"Dragon Impling", 10010, 6064, 11256, 390, 90);

		public static Impling forId(int npcId) {
			for (final Impling impling : Impling.values())
				if (impling.getNpcId() == npcId)
					return impling;
			return null;
		}

		private String name = "";
		private final int Net, npcId, itemId, AmtExp, Req;

		private Impling(String npcName, int Net, int npcId, int itemId,
				int AmtExp, int Req) {
			name = npcName;
			this.Net = Net;
			this.npcId = npcId;
			this.itemId = itemId;
			this.AmtExp = AmtExp;
			this.Req = Req;
		}

		public int getExp() {
			return AmtExp;
		}

		public String getImpName() {
			return name;
		}

		public int getItemId() {
			return itemId;
		}

		public int getNet() {
			return Net;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getReq() {
			return Req;
		}
	}

	public static void CatchButterfly(Player c, int npcId) {
		final Butterfly butterfly = Butterfly.forId(npcId);
		if (butterfly == null)
			return;
		final int equipped = c.playerEquipment[3];
		if (System.currentTimeMillis() - c.lastCatch > 1500) {
			if (c.playerLevel[Skill.HUNTER] >= butterfly.getReq()) {
				if (equipped == butterfly.getNet()) {
					if (c.getActionAssistant().playerHasItem(10012, 1)) {
						if (c.getActionAssistant().freeSlots() > 2) {
							c.getActionAssistant().deleteItem(10012, 1);
							HandleCatch(c, butterfly.getItemId(),
									butterfly.getExp(), butterfly.getReq(),
									butterfly.getName());
						} else {
							c.getActionSender().sendMessage(
									"Not enough inventory space.");
							return;
						}
					} else {
						c.getActionSender().sendMessage(
								"you need a impling jar to catch this");
						return;
					}
				} else {
					c.getActionSender().sendMessage(
							"You need to be wearing a butterfly net!");
					return;
				}
			} else {
				c.getActionSender().sendMessage(
						"You need at least " + butterfly.getReq()
								+ " Hunter To catch that Butterfly!");
				return;
			}
			c.lastCatch = System.currentTimeMillis();
		}
	}

	public static void CatchImpling(Player c, int npcId) {
		final Impling imp = Impling.forId(npcId);
		if (imp == null)
			return;
		final int equipped = c.playerEquipment[3];
		if (System.currentTimeMillis() - c.lastCatch >= 1500) {
			if (c.playerLevel[Skill.HUNTER] >= imp.getReq()) {
				if (equipped == imp.getNet()) {
					if (c.getActionAssistant().playerHasItem(11260, 1)) {
						if (c.getActionAssistant().freeSlots() > 0) {
							c.getActionAssistant().deleteItem(11260, 1);
							HandleCatch(c, imp.getItemId(), imp.getExp(),
									imp.getReq(), imp.getImpName());
						} else {
							c.getActionSender().sendMessage(
									"Not enough inventory space.");
							return;
						}
					} else {
						c.getActionSender().sendMessage(
								"you need a impling jar to catch this");
						return;
					}
				} else {
					c.getActionSender().sendMessage(
							"You need to equip a butterfly net!");
					return;
				}
			} else {
				c.getActionSender().sendMessage(
						"You need atleast " + imp.getReq()
								+ " Hunter To catch that Impling!");
				return;
			}
			c.lastCatch = System.currentTimeMillis();
		}
	}

	public static void HandleCatch(Player c, int ID, int XP, int Req,
			String Name) {
		if (c.playerLevel[Skill.HUNTER] + Misc.random(10) > Misc.random(20)
				+ Req) {
			c.getActionSender().sendMessage("You caught a " + Name + "!");
			c.getActionSender().addItem(ID, 1);
			c.getActionAssistant().startAnimation(6999);
			c.getActionAssistant().addSkillXP(
					XP * PlayerConstants.SKILL_EXPERIENCE_MULTIPLIER, 21);
		} else {
			c.getActionSender().sendMessage(
					"You Failed To Catch The " + Name + ".");
			c.getActionAssistant().startAnimation(6999);
		}
	}
}
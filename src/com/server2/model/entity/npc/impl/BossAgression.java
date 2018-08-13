package com.server2.model.entity.npc.impl;

/**
 * 
 * @author Lukas Pinckers
 * 
 */
public class BossAgression {

	/*
	 * public void agression(Entity attacker, int id, int whichOne){ Player
	 * client=null; if(attacker.getTarget()!=null){ return; }
	 * if(((NPC)attacker).isDeath()){
	 * 
	 * return; } boolean canGo=false;
	 * 
	 * if(attacker.target==null){ int distance=1000;
	 * 
	 * for(int i = 0; i < Settings.getInt("sv_maxclients"); i++) { Player player
	 * = PlayerManager.getSingleton().getPlayers()[i]; if (player == null)
	 * continue; if (!player.isActive || player.disconnected) continue;
	 * 
	 * if(player.getHeightLevel()==attacker.getHeightLevel()){
	 * if(getInRoom(whichOne, player.getAbsX(), player.getAbsY(),
	 * player.getHeightLevel() )){
	 * 
	 * if(attacker.getTarget()!=null){ return; }
	 * if(TileManager.calculateDistance(attacker, player) <distance){
	 * distance=TileManager.calculateDistance(attacker, player);
	 * client=(Player)player;
	 * 
	 * canGo=true; } }
	 * 
	 * } } if(canGo){ attacker.target = client; attacker.following =
	 * (Entity)client; // System.out.println("bossagression ID: "+client.ID +
	 * "  "+whichOne); attacker.setInCombatWith(client);
	 * 
	 * 
	 * 
	 * 
	 * } } if(attacker.target!=null){ if(!getInRoom(whichOne,
	 * attacker.target.getAbsX(), attacker.target.getAbsY(),
	 * attacker.getHeightLevel())){ attacker.target = null; attacker.following =
	 * null;
	 * 
	 * attacker.setInCombatWith(null); return; } } }
	 */
	public static BossAgression INSTANCE = new BossAgression();

	public static BossAgression getInstance() {
		return INSTANCE;
	}

	public static boolean nexRoom(int x, int y, int z) {
		if (x >= 2451 && x <= 2478 && y >= 4766 && y <= 4792)
			return true;
		return false;
	}

	public boolean armaRoom(int x, int y, int z) {
		if (x >= 2824 && y <= 5308 && x <= 2842 && y >= 5296)
			return true;
		return false;
	}

	public boolean bandosRoom(int x, int y, int z) {
		if (x >= 2864 && y >= 5351 && x <= 2876 && y <= 5369)
			return true;
		return false;
	}

	public boolean corpRoom(int x, int y, int z) {
		if (x >= 2620 && x <= 2680 && y <= 4610 && y >= 4540 && z == 1)
			return true;
		return false;
	}

	public boolean dagannothKingsRoom(int x, int y, int z) {
		if (x >= 2890 && y <= 4470 && x <= 2945 && y >= 4420)
			return true;
		return false;
	}

	public boolean getInRoom(int whichOne, int x, int y, int z) {
		if (whichOne == 1)
			return armaRoom(x, y, z);
		if (whichOne == 2)
			return bandosRoom(x, y, z);
		if (whichOne == 3)
			return saraRoom(x, y, z);
		if (whichOne == 4)
			return zamaRoom(x, y, z);
		if (whichOne == 5)
			return nexRoom(x, y, z);
		if (whichOne == 6)
			return corpRoom(x, y, z);
		if (whichOne == 7)
			return kbdRoom(x, y, z);
		/*
		 * if(whichOne==8){ return nomadRoom(x, y, z); }
		 */
		if (whichOne == 9)
			return dagannothKingsRoom(x, y, z);
		return false;

	}

	public boolean kbdRoom(int x, int y, int z) {
		if (x >= 2695 && y <= 9838 && x <= 2745 && y >= 9800)
			return true;
		return false;
	}

	public boolean saraRoom(int x, int y, int z) {
		if (x >= 2889 && y >= 5258 && x <= 2907 && y <= 5276)
			return true;
		return false;
	}

	public boolean zamaRoom(int x, int y, int z) {
		if (x >= 2918 && y >= 5318 && x <= 2936 && y <= 5331)
			return true;
		return false;
	}

}

package com.server2;

import java.io.File;
import java.math.BigInteger;

public class Constants {

	public static boolean TRADE = true;
	public static boolean DROP = true;
	public static boolean DUEL = true;
	public static boolean CWARS = true;
	public static boolean MINIGAME = true;
	public static boolean NPC = true;
	public static boolean YELL = true;
	public static boolean COMMANDS = true;
	public static boolean RIOTWARS = true;
	public static boolean DOUBLEDROPS = false;

	public static final int PET_TYPE = 6873; // YAK
	public static final int MAX_NPCS = 81920; // TODO: use this
	public static final int PLAYER_INACTIVITY_THRESHOLD = 5; // TODO: use this
	public static final char[] XLATE_TABLE = { ' ', 'e', 't', 'a', 'o', 'i',
			'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g',
			'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(',
			')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$',
			'%', '"', '[', ']' };
	public static final int[] PACKET_SIZES = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 4, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 0, 0, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};
	// public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1,
	// 1, -1, 0, 1 };
	// public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0,
	// 0, -1, -1, -1 };
	public static final String CHARACTER_DIRECTORY;
	public static final String CHARACTER_DATA_DIRECTORY;
	public static final String CHARACTER_EXTRA_DATA_1_DIRECTORY;
	public static final String CHARACTER_EXTRA_DATA_2_DIRECTORY;
	public static final String DATA_DIRECTORY;
	public static final String CREDENTIAL_DIRECTORY;

	static {
		if (Server.isRunningOnProtosPC()) {
			DATA_DIRECTORY = "C:/Users/Rajasoo/Desktop/work on this until it fucking works/Server/data/";
			CHARACTER_DIRECTORY = "/bin/characters/";
			CHARACTER_DATA_DIRECTORY = "";
			CHARACTER_EXTRA_DATA_1_DIRECTORY = "";
			CHARACTER_EXTRA_DATA_2_DIRECTORY = "";
			CREDENTIAL_DIRECTORY = "";
		} else {
			String finalPath = "";
			String[] testPaths = { "./data/", "../data/", "../../data/",
					"./bin/data/", "../../Server/bin/data/" };

			for (final String path : testPaths) {
				final File dir = new File(path);
				final File settings = new File(dir.getAbsolutePath()
						+ "/world/xml/settings.xml");

				if (dir.exists() && dir.isDirectory() && settings.exists()) {
					finalPath = dir.getAbsolutePath();
					break;
				}
			}
			if (finalPath.isEmpty())
				System.err
						.println("WARNING - EMPTY DATA DIRECTORY PATH - WARNING");

			DATA_DIRECTORY = finalPath + System.getProperty("file.separator");
			System.out.println("DATA_DIRECTORY = " + DATA_DIRECTORY);

			testPaths = new String[] { "C:/characters/",
					DATA_DIRECTORY + "characters" };
			finalPath = "";

			for (final String path : testPaths) {
				final File dir = new File(path);
				final File minutes = new File(dir.getAbsolutePath()
						+ "/minutes.log");

				if (dir.exists() && dir.isDirectory() && minutes.exists()) {
					finalPath = dir.getAbsolutePath();
					break;
				}
			}

			if (finalPath.isEmpty())
				System.err
						.println("WARNING - EMPTY CHARACTER DATA DIRECTORY PATH - WARNING");

			CHARACTER_DIRECTORY = finalPath
					+ System.getProperty("file.separator");
			CHARACTER_DATA_DIRECTORY = CHARACTER_DIRECTORY + "data"
					+ System.getProperty("file.separator");
			CHARACTER_EXTRA_DATA_1_DIRECTORY = CHARACTER_DIRECTORY
					+ "extra_data" + System.getProperty("file.separator");
			CHARACTER_EXTRA_DATA_2_DIRECTORY = CHARACTER_DIRECTORY
					+ "extra_data_noInteger"
					+ System.getProperty("file.separator");
			CREDENTIAL_DIRECTORY = CHARACTER_DIRECTORY + "extra_data_login"
					+ System.getProperty("file.separator");
			System.out.println("CHARACTER_DIRECTORY = " + CHARACTER_DIRECTORY);
			System.out.println("CHARACTER_DATA_DIRECTORY = "
					+ CHARACTER_DATA_DIRECTORY);
			System.out.println("CHARACTER_EXTRA_DATA_1_DIRECTORY = "
					+ CHARACTER_EXTRA_DATA_1_DIRECTORY);
			System.out.println("CHARACTER_EXTRA_DATA_2_DIRECTORY = "
					+ CHARACTER_EXTRA_DATA_2_DIRECTORY);
			System.out
					.println("CREDENTIAL_DIRECTORY = " + CREDENTIAL_DIRECTORY);
		}
	}

	public static int indexOfSaga;

	public static BigInteger PRIVATE_RSA_MODULUS = new BigInteger(
			"4521021184426385204894010098929444659631361353890121019419147328881857133204880250224578492901849180769308963372017862583936239718341556794043934450936293168220635609434933510032577695796354697245890046963111216858623128094501927468050625941283909948634550065128438581645051791523880236499322181415770616454562570010201230193785893438641516834505385108594063672739590691674294690791019551277193954094556281955551537058437947102613073495990232114403612739909055832543864385211764134303387720346795290630196820062743766347677756135101683557892892406743375190506620266452470604071024742011500170359566989308796094727923"); // i'm
																																																																																																																																																															// changing
																																																																																																																																																															// this
																																																																																																																																																															// in
																																																																																																																																																															// a
																																																																																																																																																															// minute
	public static BigInteger PRIVATE_RSA_EXPONENT = new BigInteger(
			"4172098817659083430837310782812058586292401289682479044409274322954145559898957118167329640847321918973206536701043201721376929702695866660771520082673544326423483238827156320143739665594515094293821568736469525373341290630138426710716599879501801879601888298043896378828922399554034405347246688280580299869497361530464918058221611254214438533365426752281028738753052203534036459942638454084504944979870949166057042956829441046239121172758558954131475249013282913142607097439010018902246646858847114679572744376851635391164542387871867927606089648873803231219443678618496307174626173426443683331319117157829416266009");
}
package com.server2.util;

/**
 * 
 * @author Rene Roosen
 * 
 */
public class SpamFilter {

	/**
	 * The not allowed strings
	 */
	private static String[] checks = {
			".tk",
			". tk",
			".com",
			".c0m",
			". com",
			". c0m",
			".n3t",
			". n3t",
			".n33t",
			".c 0 m",
			".c o m",
			". c 0 m",
			". c o m",
			".tk",
			".t k",
			".t  k",
			". t  k",
			".us",
			".u s",
			". us",
			". u s",
			". u  s",
			".u  s",
			".net",
			". net",
			".n e t",
			". n e t",
			". n et",
			". ne t",
			".ne t",
			".n et",
			".org",
			".biz",
			".biz",
			". biz",
			".  biz",
			".b iz",
			". b iz",
			".  b iz",
			".bi z",
			". bi z",
			".  bi z",
			".  b i z",
			".  b  i  z",
			".info",
			".nl",
			".ee",
			".ru",
			".r u",
			".e e",
			".n l",
			".i n f o",
			".b i z",
			".org",
			". org",
			". o r g",
			".o r g",
			". o rg",
			". or g",
			".  o rg",
			".  or g",
			"server2x",
			".  o r g",
			".n e t",
			".u k",
			".t k",
			". t k",
			". u k",
			". n e t",
			". b i z",
			". i n f o",
			". n l",
			". nl",
			". info",
			". biz",
			"6ia", // A top-level domain name used for ArpaNet sites".
			".arpa", // A top-level domain name used for ArpaNet sites".
			".asia", // A top-level domain name used for ArpaNet sites".
			".cat", // A top-level domain name used for ArpaNet sites".
			".info", // A top-level domain name used for ArpaNet sites".
			".int", // A top-level domain name used for ArpaNet sites".
			".name", // A top-level domain name used for ArpaNet sites".
			".org", // A top-level domain name used for ArpaNet sites".
			".coop", // A top-level domain name used for ArpaNet sites".
			".biz", // A top-level domain name used for ArpaNet sites".
			"no.ip", // A top-level domain name used for ArpaNet sites".
			".com", // Pronounced
					// "dot com"." A top-level domain name used for commercial Internet sites".
			".edu", // A top-level domain name used for educational sites ".
			".firm", // An ending of an address for an Internet site for a
						// business".
			".gov", // A top-level domain name used for a U".S". government site
					// on the Internet".
			".int", // A top-level domain name used for international
					// institutions".
			".mil", // A top-level domain name for a U".S". military site on the
					// Internet".
			".mobi", // A top-level domain name for a sites made for mobile
						// phones".
			".nato", // A top-level domain name used for NATO sites".
			".net", // A top-level domain name used for Internet administrative
					// sites".
			".nom", // An ending of an address for a personal site on the
					// Internet".
			".org", // A top-level domain name for organizational Internet
					// sites".
			".store", // An ending of an address for an Internet site that is
						// for a retail business".
			".web", // An ending of an address for an Internet site that is
					// about the World Wide Web".
			// //".ac", //Top-level domain name for an educational network (same
			// as ".edu)".
			// //".ad", //Andorra
			".ae", // United Arab Emirates
			// ".af", //Afghanistan
			".ag", // Antigua and Barbuda
			".ai", // Anguilla
			// ".al", //Albania
			// ".am", //Armenia
			// ".an", //Netherlands Antilles
			".ao", // Angola
			".aq", // Antarctica
			// ".ar", //Argentina
			// ".as", //American Samoa
			// ".at", //Austria
			".au", // Australia
			".aw", // Aruba
			".az", // Azerbaijan
			// ".ba", //Bosnia/Herzegovinia
			// ".bb", //Barbados
			// ".bd", //Bangladesh
			// ".be", //Belgium
			".bf", // Burkina Faso
			".bg", // Bulgaria
			// ".bh", //Bahrain
			".bi", // Burundi
			".bj", // Benin
			".bm", // Bermuda
			".bn", // Brunei Darussalam
			// ".bo", //Bolivia
			".br", // Brazil
			// ".bs", //Bahamas
			".bt", // Bhutan
			".bv", // Bouvet Island
			".bw", // Botswana
			// ".by", //1". Belarus 2". Byelorussia
			".bz", // Belize
			".ca", // Canada
			".cc", // Cocos Islands - Keelings
			".cf", // Central African Republic
			".cg", // Congo
			// ".ch", //Switzerland
			".ci", // Cote D&#226;??????Ivoire, or Ivory Coast
			".ck", // Cook Islands
			".cl", // Chile
			".cm", // Cameroon
			".cn", // China
			".co", // Colombia
			// ".cr", //Costa Rica
			".cs", // Czechoslovakia (former)
			// ".cu", //Cuba
			".cv", // Cape Verde
			".cx", // Christmas Island
			".cy", // Cyprus
			".cz", // Czech Republic
			// ".de", //Germany
			".dj", // Djibouti
			".dk", // Denmark
			".dm", // Dominica
			// ".do", //Dominican Republic
			".dz", // Algeria
			// ".ec", //Ecuador
			// ".ee", //Estonia
			".eg", // Egypt
			// ".eh", //Western Sahara
			// ".er", //Eritrea
			// ".es", //Spain
			".et", // Ethiopia
			".eu", // European Union
			".fi", // Finland
			".fj", // Fiji
			// ".fk", //Falkland Islands/Malvinas
			".fm", // Micronesia
			".fo", // Faroe Islands
			".fr", // France
			".fx", // Metropolitan France
			".ga", // Gabon
			".gb", // Great Britain
			".gd", // Grenada
			// ".ge", //Georgia
			".gf", // French Guiana
			".gh", // Ghana
			".gi", // Gibraltar
			".gl", // Greenland
			".gm", // Gambia
			".gn", // Guinea
			".gp", // Guadeloupe
			".gq", // Equatorial Guinea
			".gr", // Greece
			".gs", // South Georgia and South Sandwich Islands
			".gt", // Guatemala
			".gu", // Guam
			".gw", // Guinea-Bissau
			".gy", // Guyana
			".hk", // Hong Kong
			// ".hm", //Heard and McDonald Islands
			".hn", // Honduras
			".hr", // Croatia/Hrvatska
			".ht", // Haiti
			".hu", // Hungary
			".id", // Indonesia
			// ".ie", //Ireland
			// ".il", //Israel
			// ".in", //India
			".io", // British Indian Ocean Territory
			".iq", // Iraq
			".ir", // Iran
			// ".is", //Iceland
			// ".it", //Italy
			".jm", // Jamaica
			// ".jo", //Jordan
			".jp", // Japan
			".ke", // Kenya
			".kg", // Kyrgyzstan
			".kh", // Cambodia
			".ki", // Kiribati
			".km", // Comoros
			".kn", // Saint Kitts and Nevis
			".kp", // North Korea
			".kr", // South Korea
			".kw", // Kuwait
			".ky", // Cayman Islands
			".kz", // Kazakhstan
			// ".la", //Laos
			".lb", // Lebanon
			".lc", // Saint Lucia
			// ".li", //Liechtenstein
			".lk", // Sri Lanka
			".lr", // Liberia
			".ls", // Lesotho
			".lt", // Lithuania
			".lu", // Luxembourg
			".lv", // Latvia
			".ly", // Libya
			// ".ma", //Morocco
			".mc", // Monaco
			".md", // Moldova
			".mg", // Madagascar
			".mh", // Marshall Islands
			".mk", // Macedonia
			".ml", // Mali
			// ".mm", //Myanmar
			".mn", // Mongolia
			// ".mo", //Macau
			".mp", // Northern Mariana Islands
			".mq", // Martinique
			".mr", // Mauritania
			".ms", // Montserrat
			".mt", // Malta
			// ".mu", //Mauritius
			".mv", // Maldives
			".mw", // Malawi
			".mx", // Mexico
			// ".my", //Malaysia
			".mz", // Mozambique
			// ".na", //Namibia
			".nc", // New Caledonia
			// ".ne", //Niger
			".nf", // Norfolk Island
			".ng", // Nigeria
			// ".ni", //Nicaragua
			".nl", // Netherlands
			// ".no", //Norway
			// ".np", //Nepal
			// ".nr", //Nauru
			".nt", // Neutral Zone
			".nu", // Niue
			".nz", // New Zealand (Aotearoa)
			// ".om", //Oman
			// ".pa", //Panama
			// ".pe", //Peru
			".pf", // French Polynesia
			".pg", // Papua New Guinea
			".ph", // Philippines
			".pk", // Pakistan
			".pl", // Poland
			// ".pm", //St". Pierre and Miquelon
			".pn", // Pitcairn
			".pr", // Puerto Rico
			".pt", // Portugal
			// ".pw", //Palau
			".py", // Paraguay
			".qa", // Qatar
			// ".re", //Reunion
			// ".ro", //Romania
			// ".ru", //Russian Federation
			".rw", // Rwanda
			// ".sa", //Saudi Arabia
			".sb", // Solomon Islands
			".sc", // Seychelles
			".sd", // Sudan
			// ".se", //Sweden
			".sg", // Singapore
			".sh", // Saint Helena
			// ".si", //Slovenia
			".sj", // Svalbard and Jan Mayen Islands
			".sk", // Slovakia
			".sl", // Sierra Leone
			// ".sm", //San Marino
			// ".sn", //Senegal
			// ".so", //Somalia
			// ".sr", //Suriname
			// ".st", //Sao Torme and Principe
			// ".su", //Former USSR
			".sv", // El Salvador
			".sy", // Syria
			".sz", // Swaziland
			".tc", // Turks and Caicos Islands
			".td", // Chad
			".tf", // French Southern Territory
			".tg", // Togo
			// ".th", //Thailand
			".tj", // Tajikistan
			".tk", // Tokelau
			".tm", // Turkmenistan
			".tn", // Tunisia
			// ".to", //Tonga
			".tp", // East Timor
			".tr", // Turkey
			".tt", // Trinidad and Tobago
			".tv", // Tuvalu
			".tw", // Taiwan
			".tz", // Tanzania
			".ua", // Ukraine
			// ".ug", //Uganda
			".uk", // United Kingdom
			// ".um", //U".S". Minor Outlying Islands
			".us", // United States
			".uy", // Uruguay
			".uz", // Uzbekistan
			// ".va", //Vatican City State
			".vc", // Saint Vincent and the Grenadines
			".ve", // Venezuela
			".vg", // British Virgin Islands
			// ".vi", //U".S". Virgin Islands
			".vn", // Viet Nam
			// ".vu", //Vanuatu
			".wf", // Wallis and Futuna Islands
			".ws", // Samoa
			// ".ye", //Yemen
			".yt", // Mayotte
			// ".yu", //Yugoslavia
			".za", // South Africa
			".zm", // Zambia
			".zr", // Zaire
			".zw", // Zimbabwe
			". net", ". org", ". 0rg", ". 0 r g", "c[]m", "server2x.tk",
			"server2x,tk", ".tk", "ooowebhost", "2ip.jp", "ata.jp", "about.gs",
			"about.tc", "aboutus.gs", "aboutus.ms", "aboutus.tc", "aboutus.vg",
			"about.vg", "band.io", "beijing.am", "biografi.biz",
			"biografi.info", "biografi.org", "biografi.us", "blogs.io",
			"boke.am", "church.io", "clan.io", "clubs.io", "consensus.jp",
			"datadiri.biz", "datadiri.cc", "datadiri.com", "datadiri.info",
			"datadiri.net", "datadiri.org", "datadiri.tv", "datadiri.us",
			"desyo.jp", "dif.jp", "ecv.gs", "ecv.ms", "ecv.tc", "ecv.vg",
			"eprofile.us", "faith.io", "fans.io", "food.io", "freekong.cn",
			"go2net.ws", "groups.io", "hello.cn.com", "hello.io", "hits.io",
			"hostingweb.us", "hub.io", "ide.am", "inc.io", "incn.in",
			"indo.bz", "indo.cc", "indo.gs", "indo.ms", "indo.tc", "indo.vg",
			"infinitehosting.net", "infinites.net", "invited.at", "jushige.cn",
			"jushige.com", "kids.io", "kongjian.in", "lan.io", "learn.ac",
			"learn.io", "llc.nu", "maimai.in", "max.io", "musician.io",
			"mycv.bz", "mycv.nu", "mycv.tv", "myfam.io", "myweb.io",
			"ourprofile.biz", "ourprofile.info", "ourprofile.net",
			"ourprofile.org", "ourprofile.us", "pastels.jp", "powerblogger.jp",
			"profil.bz", "profil.cc", "profil.cn", "profil.gs", "profil.in",
			"profil.ms", "profil.tc", "profil.tv", "profil.vg", "qiye.in",
			"registered.md", "site.io", "tips.io", "trips.io", "vippers.jp",
			"wan.io", "web-cam.ws", "webpages.jp", "webs.io", "xixu.cc",
			"zaici.am", "zip.io", "zuzhi.in", ".tk", "co.nr", "tiny.cc",
			"tinyurl.com", "url2cut.com", "tinyurl.co.uk", "9f.com", "7p.com",
			"4t.com", "8m.com", "8m.net", "8k.com", "s5.com", "itgo.com",
			"iwarp.com", "4mg.com", "gq.nu", "faithweb.com", "tvheaven.com",
			"freehosting.net", "htmlplanet.com", "scriptmania.com", "uni.cc",
			"afraid.org", "fadlan.com", "c-o.in", "c-o.cc", "coz.in", "cq.bz",
			"net.tc", "eu.tc", "us.tc", "pro.tc", "de.tc", "at.tc", "co.at.tc",
			"it.tc", "es.tc", "ru.tc", "se.tc", "dk.tc", "be.tc", "no.tc",
			"int.tc", "pl.tc", "bg.tc", "cz.tc", "mx.tc", "br.tc", "hk.tc",
			"kr.tc", "th.tc", "ph.tc", "net.tf", "eu.tf", "us.tf", "int.tf",
			"ca.tf", "de.tf", "at.tf", "ch.tf", "edu.tf", "ru.tf", "pl.tf",
			"cz.tf", "bg.tf", "sg.tf", "waaa.ws", "net.ms", "us.ms", "info.ms",
			"shop.ms", "au.ms", "de.ms", "fr.ms", "br.ms", "cn.ms", "hk.ms",
			"CY.eu.org", "GR.eu.org", "IL.eu.org", "NL.eu.org", "PT.eu.org",
			"DK.eu.org", "IE.eu.org", "co.cc", "DNIC.ORG", "soulsplit.com" };

	/**
	 * Checks if a string can be sent
	 * 
	 * @param check
	 * @return
	 */
	public static boolean canSend(String check) {
		for (final String check2 : checks) {
			if (check.contains(check2))
				return false;
			if (check.contains("," + check2.substring(1)))
				return false;
		}
		return true;
	}
}

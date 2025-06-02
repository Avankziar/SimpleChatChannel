package main.java.me.avankziar.scc.general.database;

import java.util.LinkedHashMap;

public class Language
{
	//See https://en.wikipedia.org/wiki/List_of_ISO_639-2_codes, and if a * appears, than use this.
	public enum ISO639_2B
	{
		AAR, ABK, ACE, ACH, ADA, ADY, AFA, AFH, AFR, AIN, AKA, AKK, ALB, ALE, ALG, ALT, AMH, ANG, ANP, APA,
		ARA, ARC, ARG, ARM, ARN, ARP, ART, ARW, ASM, AST, ATH, AUS, AVA, AVE, AWA, AYM, AZE, BAD, BAI, BAK,
		BAL, BAM, BAN, BAQ, BAS, BAT, BEJ, BEL, BEM, BEN, BER, BHO, BIH, BIK, BIN, BIS, BLA, BNT, BOS, BRA,
		BRE, BTK, BUA, BUG, BUL, BUR, BYN, CAD, CAI, CAR, CAT, CAU, CEB, CEL, CES, CHA, CHB, CHE, CHG, CHI,
		CHK, CHM, CHN, CHO, CHP, CHR, CHU, CHV, CHY, CMC, CNR, COP, COR, COS, CPE, CPF, CPP, CRE, CRH, CRP,
		CSB, CUS, CYM, CZE, DAK, DAN, DAR, DAY, DEL, DEN, DGR, DIN, DIV, DOL, DRA, DSB, DUA, DUM, DUT, DYU,
		DZO, EFI, EGY, EKA, ELX, ENG, ENM, EPO, EST, EWE, EWO, FAN, FAO, FAT, FIJ, FIL, FIN, FIU, FON, FRE,
		FRM, FRO, FRR, FRS, FRY, FUL, FUR, GAA, GAY, GBA, GEM, GEO, GER, GEZ, GIL, GLA, GLE, GLG, GLV, GMH,
		GOH, GON, GOR, GRB, GRC, GRE, GRN, GSW, GUJ, GWI, HAI, HAT, HAU, HAW, HEB, HER, HIL, HIM, HIN, HIT,
		HMN, HMO, HRV, HSB, HUN, HUP, IBA, IBO, ICE, IDO, III, IJO, IKU, ILE, ILO, INO, INC, IND, INE, INH,
		IPK, IRA, IRO, ITA, JAV, JBO, JPN, JPR, JRB, KAA, KAB, KAC, KAL, KAM, KAN, KAS, KAU, KAW, KAZ, KBD,
		KHA, KHI, KHM, KHO, KIK, KIN, KIR, KMB, KOK, KOM, KON, KOR, KOS, KPE, KRC, KRL, KRO, KRU, KUA, KUM,
		KUR, KUT, LAD, LAH, LAM, LAO, LAT, LAV, LEZ, LIM, LIN, LIT, LIL, LOZ, LTZ, LUA, LUB, LUG, LUI, LUN,
		LUO, LUS, MAC, MAD, MAG, MAH, MAI, MAK, MAL, MAN, MAO, MAP, MAR, MAS, MAY, MDF, MDR, MEN, MGA, MIC,
		MIN, MIS, MKH, MLG, MLT, MNC, MNI, MNO, MOH, MON, MOS, MUL, MUN, MUS, MWL, MWR, MYN, MYV, NAH, NAI,
		NAP, NAU, NAV, NBI, NDE, NDO, NDS, NEP, NEW, NIA, NIC, NIU, NNO, NOB, NOG, NON, NOR, NQO, NSO, NUB,
		NWC, NYA, NYM, NYN, NYO, NZI, OCI, OJI, ORI, ORM, OSA, OSS, OTA, OTO, PAA, PAG, PAL, PAM, PAN, PAP,
		PAU, PEO, PER, PHI, PHN, PLI, POL, PON, POR, PRA, PRO, PUS, QUE, RAJ, RAP, RAR, ROA, ROH, RUM, RUN,
		RUP, RUS, SAD, SAG, SAH, SAI, SAL, SAM, SAN, SAS, SAT, SCN, SCO, SEL, SEM, SGA, SGN, SHN, SID, SIN,
		SIO, SIT, SLA, SLO, SLV, SMA, SME, SMI, SMJ, SMN, SMO, SMS, SNA, SND, SNK, SOG, SOM, SON, SOT, SPA,
		SRD, SRN, SRP, SRR, SAA, SSW, SUK, SUN, SUS, SUX, SWA, SWE, SYC, SYR, TAH, TAI, TAM, TAT, TEL, TEM,
		TER, TET, TGK, TGL, THA, TIB, TIG, TIR, TIV, TKI, TLH, TLI, TMH, TOG, TON, TPI, TSI, TSN, TSO, TUK,
		TUM, TUP, TUR, TUT, TVL, TWL, TYV, UDM, UGA, UIG, UKR, UMB, UND, URD, UZB, VAI, VEN, VIE, VOL, VOT, 
		WAK, WAL, WAR, WAS, WEL, WEN, WLN, WOL, XAL, XHO, YAO, YAP, YID, YOR, YPK, ZAP, UBL, ZEN, ZGH, ZHA,
		ZHO, ZND, ZUL, ZUN, ZZA;
	}
	
	public LinkedHashMap<ISO639_2B, Object[]> languageValues = new LinkedHashMap<>();
	
	public Language(ISO639_2B[] languages, Object[] values)
	{
		if(languages.length == values.length)
		{
			for(int i = 0; i < languages.length; i++)
			{
				if(languages[i] != null && values[i] != null)
				{
					languageValues.put(languages[i], new Object[] {values[i]});
				}				
			}
		} else if(values.length % languages.length == 0)
		{
			int multiply = values.length / languages.length;
			for(int i = 0; i < languages.length; i++)
			{
				Object[] valuesArray = new String[multiply];
				for(int j = 0; j < multiply; j++)
				{
					valuesArray[j] = values[i*multiply+j];
				}
				languageValues.put(languages[i], valuesArray);
			}
		}
	}
	/**
	 * Return the ISO639_2B from the locale of the Player locale.<br>
	 * All ISO639_3 Constructed Language return ENG.
	 * @param localeCountry
	 * @return
	 */
	public static ISO639_2B convertLocale(String localeCountry)
	{
		String locale = localeCountry.split("_")[0];
		switch(locale)
		{
		default: return ISO639_2B.ENG;
		case "af": return ISO639_2B.AFR;
		case "ar": return ISO639_2B.ARA;
		case "ast": return ISO639_2B.AST;
		case "az": return ISO639_2B.AZE;
		case "ba": return ISO639_2B.RUS;
		case "bar": return ISO639_2B.GER;
		case "be": return ISO639_2B.BEL;
		case "bg": return ISO639_2B.BUL;
		case "br": return ISO639_2B.FRE;
		case "brb": return ISO639_2B.DUT;
		case "bs": return ISO639_2B.BOS;
		case "ca": return ISO639_2B.SPA;
		case "cs": return ISO639_2B.CZE;
		case "cy": return ISO639_2B.ENG;
		case "da": return ISO639_2B.DAN;
		case "de": return ISO639_2B.GER;
		case "el": return ISO639_2B.GRE;
		case "en": return ISO639_2B.ENG;
		case "enp": return ISO639_2B.ENG;
		case "enws": return ISO639_2B.ENG;
		case "eo": return ISO639_2B.ENG;
		case "es": return ISO639_2B.SPA;
		case "esan": return ISO639_2B.SPA;
		case "et": return ISO639_2B.EST;
		case "eu": return ISO639_2B.BAQ;
		case "fa": return ISO639_2B.PER;
		case "fi": return ISO639_2B.FIN;
		case "fil": return ISO639_2B.FIL;
		case "fo": return ISO639_2B.FAO;
		case "fr": return ISO639_2B.FRE;
		case "fra": return ISO639_2B.FRE;
		case "fur": return ISO639_2B.ITA;
		case "fy": return ISO639_2B.DUT;
		case "ga": return ISO639_2B.GLE;
		case "gd": return ISO639_2B.GLA;
		case "gl": return ISO639_2B.SPA;
		case "haw": return ISO639_2B.ENG;
		case "he": return ISO639_2B.HEB;
		case "hi": return ISO639_2B.HIN;
		case "hr": return ISO639_2B.HRV;
		case "hu": return ISO639_2B.HUN;
		case "hy": return ISO639_2B.ARM;
		case "id": return ISO639_2B.IND;
		case "ig": return ISO639_2B.IBO;
		case "io": return ISO639_2B.IDO;
		case "is": return ISO639_2B.ICE;
		case "isv": return ISO639_2B.ENG;
		case "it": return ISO639_2B.ITA;
		case "ja": return ISO639_2B.JPN;
		case "jbo": return ISO639_2B.ENG;
		case "ka": return ISO639_2B.GEO;
		case "kk": return ISO639_2B.KAZ;
		case "kn": return ISO639_2B.KAN;
		case "ko": return ISO639_2B.KOR;
		case "ksh": return ISO639_2B.GER;
		case "kw": return ISO639_2B.ENG;
		case "la": return ISO639_2B.LAT;
		case "lb": return ISO639_2B.LTZ;
		case "li": return ISO639_2B.DUT;
		case "lmo": return ISO639_2B.ITA;
		case "lo": return ISO639_2B.LAO;
		case "lol": return ISO639_2B.ENG;
		case "lt": return ISO639_2B.LIT;
		case "lv": return ISO639_2B.LAV;
		case "lzh": return ISO639_2B.CHI;
		case "mk": return ISO639_2B.MAC;
		case "mn": return ISO639_2B.MON;
		case "ms": return ISO639_2B.MAY;
		case "mt": return ISO639_2B.MLT;
		case "nds": return ISO639_2B.GER;
		case "nl": return ISO639_2B.DUT;
		case "nn": return ISO639_2B.NOR;
		case "no": return ISO639_2B.NOR;
		case "nb": return ISO639_2B.NOR;
		case "oc": return ISO639_2B.FRE;
		case "ovd": return ISO639_2B.SWE;
		case "pl": return ISO639_2B.POL;
		case "pt": return ISO639_2B.POR;
		case "qya": return ISO639_2B.ENG;
		case "ro": return ISO639_2B.RUM;
		case "rpr": return ISO639_2B.RUS;
		case "ru": return ISO639_2B.RUS;
		case "ry": return ISO639_2B.SRP;
		case "sah": return ISO639_2B.RUS;
		case "se": return ISO639_2B.NOR;
		case "sk": return ISO639_2B.SLO;
		case "sl": return ISO639_2B.SLV;
		case "so": return ISO639_2B.SOM;
		case "sq": return ISO639_2B.ALB;
		case "sr": return ISO639_2B.SRP;
		case "sv": return ISO639_2B.SWE;
		case "sxu": return ISO639_2B.GER;
		case "szl": return ISO639_2B.POL;
		case "ta": return ISO639_2B.TAM;
		case "th": return ISO639_2B.THA;
		case "tl": return ISO639_2B.TGL;
		case "tlh": return ISO639_2B.ENG;
		case "tok": return ISO639_2B.ENG;
		case "tr": return ISO639_2B.TUR;
		case "tt": return ISO639_2B.RUS;
		case "uk": return ISO639_2B.UKR;
		case "val": return ISO639_2B.SPA;
		case "vec": return ISO639_2B.ITA;
		case "vi": return ISO639_2B.VIE;
		case "yi": return ISO639_2B.ENG;
		case "yo": return ISO639_2B.YOR;
		case "zh": return ISO639_2B.CHI;
		case "zim": return ISO639_2B.MAY;
		}
	}
}
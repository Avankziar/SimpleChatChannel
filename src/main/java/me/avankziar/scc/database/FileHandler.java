package main.java.me.avankziar.scc.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class FileHandler
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
		MIN, MIS, MKH, MLG, MIT, MNC, MNI, MNO, MOH, MON, MOS, MUL, MUN, MUS, MWL, MWR, MYN, MYV, NAH, NAI,
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
	
	public static File initFile(File pluginDataFolder, String filename, String additionalDirectory)
	{
		File directory = new File(pluginDataFolder+"/"+additionalDirectory+"/");
		directory.mkdir();
		File file = new File(directory.getPath(), filename);
		try
		{
			if(file.createNewFile())
			{
				System.out.println("Create "+filename+" in "+pluginDataFolder.getPath()+"...");
			} else
			{
				System.out.println(filename+" already exist in "+pluginDataFolder.getPath()+"...");
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}
	
	public static File initFile(File pluginDataFolder, String filename)
	{
		pluginDataFolder.mkdir();
		File file = new File(pluginDataFolder.getPath(),filename);
		try
		{
			if(file.createNewFile())
			{
				System.out.println("Create "+filename+" in "+pluginDataFolder.getPath()+"...");
			} else
			{
				System.out.println(filename+" already exist in "+pluginDataFolder.getPath()+"...");
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}
	
	public static ArrayList<String> readFile(File file)
	{
		ArrayList<String> list = new ArrayList<>();
		try 
		{
			String line;
			BufferedReader f = new BufferedReader(new FileReader(file.getPath()));
			while ((line = f.readLine()) != null) 
			{
				list.add(line);
			}
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void writeFile(File file, ArrayList<String> actualLines, ArrayList<String> defaultLines)
	{
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			Writer writer = new BufferedWriter(outputStreamWriter);
			if(actualLines.isEmpty() || actualLines.size() <= 1)
			{
				/*
				 * New File
				 */
				for(String dline : defaultLines)
				{
					writer.write(dline + "\n");
					//System.out.println(dline);
				}
			} else
			{
				int i = 0;
				int j = 0;
				while(i < actualLines.size())
				{
					if(j >= defaultLines.size())
					{
						/*
						 * If the default at the end, but the actual not.
						 */
						//System.out.println(actualLines.get(i));
						writer.write(actualLines.get(i) + "\n");
						i++;
					} else if(actualLines.get(i).contains(":")
							&& defaultLines.get(j).contains(":"))
					{
						/*
						 * If both are a path
						 */
						String[] akey = actualLines.get(i).split(":");
						String[] dkey = defaultLines.get(j).split(":");
						if(akey[0].equals(dkey[0]))
						{
							/*
							 * If both key are equal, use the actualline
							 */
							//System.out.println(actualLines.get(i));
							writer.write(actualLines.get(i) + "\n");
							i++;
							j++;
						} else
						{
							/*
							 * If the not equal, use the defaultline
							 */
							//System.out.println(defaultLines.get(j));
							writer.write(defaultLines.get(j) + "\n");
							j++;
						}
					} else if(actualLines.get(i).trim().startsWith("-")
							&& defaultLines.get(j).trim().startsWith("-"))
					{
						/*
						 * If both are a list value
						 */
						if(actualLines.get(i).trim().equals(defaultLines.get(j).trim()))
						{
							/*
							 * If both values are equal, use the actualline
							 */
							//System.out.println(actualLines.get(i));
							writer.write(actualLines.get(i) + "\n");
							i++;
							j++;
						} else
						{
							/*
							 * If not equal, use the actualline
							 */
							//System.out.println(actualLines.get(i));
							writer.write(actualLines.get(i) + "\n");
							i++;
						}
					} else if(actualLines.get(i).trim().startsWith("#")
							&& defaultLines.get(j).trim().startsWith("#"))
					{
						/*
						 * If both values are a emtpy line
						 */
						//System.out.println(defaultLines.get(j));
						writer.write(defaultLines.get(j) + "\n");
						i++;
						j++;
					} else if(actualLines.get(i).trim().isEmpty()
							&& defaultLines.get(j).trim().isEmpty())
					{
						/*
						 * If both values are a emtpy line
						 */
						//System.out.println(actualLines.get(i));
						writer.write(actualLines.get(i) + "\n");
						i++;
						j++;
					} else if((actualLines.get(i).trim().startsWith("-") //21 Multiple list values from the admin
							&& defaultLines.get(j).trim().contains(":"))
							||
							(actualLines.get(i).trim().startsWith("-") //24 Multiple list values from the admin
							&& defaultLines.get(j).trim().isEmpty())
							||
							(actualLines.get(i).trim().startsWith("#") //31 Note from the admin
							&& defaultLines.get(j).trim().contains(":"))
							||
							(actualLines.get(i).trim().isEmpty() //41 Empty line in the actual file
									&& defaultLines.get(j).trim().contains(":"))
							||
							(actualLines.get(i).trim().startsWith("-") //23 Multiple list values from the admin
									&& defaultLines.get(j).trim().startsWith("#"))
							||
							(actualLines.get(i).trim().startsWith("#") //34 Note from the admin
									&& defaultLines.get(j).trim().isEmpty())
							||
							(actualLines.get(i).trim().startsWith("#") //34 Empty Line in actual file
									&& defaultLines.get(j).trim().isEmpty()))
					{
						//System.out.println(actualLines.get(i));
						writer.write(actualLines.get(i) + "\n");
						i++;
					} else if((actualLines.get(i).trim().contains(":") //12 Multiple list values from default. (Trotzdem!)
							&& defaultLines.get(j).trim().startsWith("-"))
							||
							(actualLines.get(i).trim().isEmpty() //42 Multiple list values from default. (Trotzdem!)
									&& defaultLines.get(j).trim().startsWith("-"))
							||
							(actualLines.get(i).trim().startsWith("#") //32 Note from the admin
									&& defaultLines.get(j).trim().startsWith("-"))) 
					{
						//System.out.println(actualLines.get(i));
						writer.write(actualLines.get(i) + "\n");
						i++;
						j++;
					} else if((actualLines.get(i).trim().contains(":") //13 Note from the dev
							&& defaultLines.get(j).trim().startsWith("#"))
							||
							(actualLines.get(i).trim().contains(":") //14 Empty line from dev
									&& defaultLines.get(j).trim().isEmpty())
							)
					{
						//System.out.println(defaultLines.get(i));
						writer.write(defaultLines.get(j) + "\n");
						j++;
					} else
					{
						System.out.println(" > ELSE < |"+actualLines.get(i)+"|"+defaultLines.get(j));
						i++;
						j++;
					}
				}
				while(j < defaultLines.size())
				{
					/*
					 * If the actual at the end, but the default not.
					 */
					//System.out.println(defaultLines.get(j));
					writer.write(defaultLines.get(j) + "\n");
					j++;
				}
			}
			writer.close();
			outputStreamWriter.close();
			fileOutputStream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
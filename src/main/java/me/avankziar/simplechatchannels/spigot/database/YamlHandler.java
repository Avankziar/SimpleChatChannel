package main.java.me.avankziar.simplechatchannels.spigot.database;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	private File wordfilter = null;
	private YamlConfiguration wfi = new YamlConfiguration();
	private File arabic = null;
	private YamlConfiguration ara = new YamlConfiguration();
	private File dutch = null;
	private YamlConfiguration dut = new YamlConfiguration();
	private File english = null;
	private YamlConfiguration eng = new YamlConfiguration();
	private File french = null;
	private YamlConfiguration fre = new YamlConfiguration();
	private File german = null;
	private YamlConfiguration ger = new YamlConfiguration();
	private File hindi = null;
	private YamlConfiguration hin = new YamlConfiguration();
	private File italian = null;
	private YamlConfiguration ita = new YamlConfiguration();
	private File japanese = null;
	private YamlConfiguration jap = new YamlConfiguration();
	private File mandarin = null;
	private YamlConfiguration mad = new YamlConfiguration();
	private File russian = null;
	private YamlConfiguration rus = new YamlConfiguration();
	private File spanish = null;
	private YamlConfiguration spa = new YamlConfiguration();
	private YamlConfiguration lang = null;
	private String languages;
	private String symbolglobal;
	private String symboltrade;
	private String symbolauction;
	private String symbolevent;
	private String symbollocal;
	private String symbolworld;
	private String symbolsupport;
	private String symbolteam;
	private String symboladmin;
	private String symbolpm;
	private String symbolpmre;
	private String symbolgroup;
	private String symboltemp;
	private String symbolperma;
	
	public YamlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		if(!mkdirGeneralFiles())
		{
			return false;
		}
		if(!loadGeneralFiles())
		{
			return false;
		}
		languages = cfg.getString("Language", "English");
		if(!mkdir())
		{
			return false;
		}
		if(!loadYamls())
		{
			return false;
		}
		initGetL();
		symbolglobal = lang.getString("ChannelSymbol.Global", "");
		symboltrade = lang.getString("ChannelSymbol.Trade", "$");
		symbolauction = lang.getString("ChannelSymbol.Auction", "!");
		symbolevent = lang.getString("ChannelSymbol.Event", "%");
		symbollocal = lang.getString("ChannelSymbol.Local", ",");
		symbolworld = lang.getString("ChannelSymbol.World", "°");
		symbolsupport = lang.getString("ChannelSymbol.Support", "?");
		symbolteam = lang.getString("ChannelSymbol.Team", "+");
		symboladmin = lang.getString("ChannelSymbol.Admin", "#");
		symbolpm = lang.getString("ChannelSymbol.PrivateMessage", "@");
		symbolpmre = lang.getString("ChannelSymbol.PrivateMessageRe", "@r");
		symbolgroup = lang.getString("ChannelSymbol.Group", "@*");
		symboltemp = lang.getString("ChannelSymbol.Temp", ";");
		symbolperma = lang.getString("ChannelSymbol.Perma", ".");
		return true;
	}
	
	public YamlConfiguration get()
	{
		return cfg;
	}
	
	public YamlConfiguration getCom()
	{
		return com;
	}
	
	public YamlConfiguration getWord()
	{
		return wfi;
	}
	
	public YamlConfiguration getL()
	{
		return lang;
	}
	
	public boolean saveWordfilter() 
	{
	    try 
	    {
	    	SimpleChatChannels.log.info("Save wordfilter.yml...");
	        wfi.save(wordfilter);
	    } catch (IOException e) 
	    {
	    	SimpleChatChannels.log.severe("Could not save the wordfilter.yml! Error: " + e.getMessage());
			e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	public void initGetL()
	{
		if(languages.equalsIgnoreCase("Arabic"))
		{
			lang = ara;
		} else if(languages.equalsIgnoreCase("Dutch"))
		{
			lang = dut;
		} else if(languages.equalsIgnoreCase("French"))
		{
			lang = fre;
		} else if(languages.equalsIgnoreCase("German"))
		{
			lang = ger;
		} else if(languages.equalsIgnoreCase("Hindi"))
		{
			lang = hin;
		} else if(languages.equalsIgnoreCase("Italian"))
		{
			lang = ita;
		} else if(languages.equalsIgnoreCase("Japanese"))
		{
			lang = jap;
		} else if(languages.equalsIgnoreCase("Mandarin"))
		{
			lang = mad;
		} else if(languages.equalsIgnoreCase("Russian"))
		{
			lang = rus;
		} else if(languages.equalsIgnoreCase("Spanish"))
		{
			lang = spa;
		} else
		{
			lang = eng;
		}
	}
	
	public boolean mkdirGeneralFiles()
	{
		config = new File(plugin.getDataFolder(), "spigotconfig.yml");
		if(!config.exists()) 
		{
			SimpleChatChannels.log.info("Create spigotconfig.yml...");
			plugin.saveResource("spigotconfig.yml", false);
		}
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			SimpleChatChannels.log.info("Create commands.yml...");
			plugin.saveResource("commands.yml", false);
		}
		wordfilter = new File(plugin.getDataFolder(), "wordfilter.yml");
		if(!wordfilter.exists()) 
		{
			SimpleChatChannels.log.info("Create wordfilter.yml...");
			plugin.saveResource("wordfilter.yml", false);
		}
		return true;
	}
	
	private boolean loadGeneralFiles()
	{
		if(!loadYamlTask(config, cfg, "spigotconfig.yml"))
		{
			return false;
		}
		if(!loadYamlTask(commands, com, "commands.yml"))
		{
			return false;
		}
		if(!loadYamlTask(wordfilter, wfi, "wordfilter.yml"))
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdir()
	{
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		arabic = new File(directory.getPath(), "arabic.yml");
		if(!arabic.exists()) 
		{
			SimpleChatChannels.log.info("Create arabic.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("arabic.yml"), arabic);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		dutch = new File(directory.getPath(), "dutch.yml");
		if(!dutch.exists()) 
		{
			SimpleChatChannels.log.info("Create dutch.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("dutch.yml"), dutch);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		english = new File(directory.getPath(), "english.yml");
		if(!english.exists()) 
		{
			SimpleChatChannels.log.info("Create english.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("english.yml"), english);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		french = new File(directory.getPath(), "french.yml");
		if(!french.exists())
		{
			SimpleChatChannels.log.info("Create french.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("french.yml"), french);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		german = new File(directory.getPath(), "german.yml");
		if(!german.exists()) 
		{
			SimpleChatChannels.log.info("Create german.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("german.yml"), german);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		hindi = new File(directory.getPath(), "hindi.yml");
		if(!hindi.exists()) 
		{
			SimpleChatChannels.log.info("Create hindi.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("hindi.yml"), hindi);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		italian = new File(directory.getPath(), "italian.yml");
		if(!italian.exists()) 
		{
			SimpleChatChannels.log.info("Create italian.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("italian.yml"), italian);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		japanese = new File(directory.getPath(), "japanese.yml");
		if(!japanese.exists()) 
		{
			SimpleChatChannels.log.info("Create japanese.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("japanese.yml"), japanese);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mandarin = new File(directory.getPath(), "mandarin.yml");
		if(!mandarin.exists()) 
		{
			SimpleChatChannels.log.info("Create mandarin.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("mandarin.yml"), mandarin);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		russian = new File(directory.getPath(), "russian.yml");
		if(!russian.exists()) 
		{
			SimpleChatChannels.log.info("Create russian.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("russian.yml"), russian);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		spanish = new File(directory.getPath(), "spanish.yml");
		if(!spanish.exists()) 
		{
			SimpleChatChannels.log.info("Create spanish.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("spanish.yml"), spanish);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean loadYamls()
	{
		if(!loadYamlTask(arabic, ara, "arabic.yml"))
		{
			return false;
		}
		if(!loadYamlTask(dutch, dut, "dutch.yml"))
		{
			return false;
		}
		if(!loadYamlTask(english, eng, "english.yml"))
		{
			return false;
		}
		if(!loadYamlTask(french, fre, "french.yml"))
		{
			return false;
		}
		if(!loadYamlTask(german, ger, "german.yml"))
		{
			return false;
		}
		if(!loadYamlTask(hindi, hin, "hindi.yml"))
		{
			return false;
		}
		if(!loadYamlTask(italian, ita, "italian.yml"))
		{
			return false;
		}
		if(!loadYamlTask(japanese, jap, "japanese.yml"))
		{
			return false;
		}
		if(!loadYamlTask(mandarin, mad, "mandarin.yml"))
		{
			return false;
		}
		if(!loadYamlTask(russian, rus, "russian.yml"))
		{
			return false;
		}
		if(!loadYamlTask(spanish, spa, "spanish.yml"))
		{
			return false;
		}
		return true;
	}
	
	private boolean loadYamlTask(File file, YamlConfiguration yaml, String filename)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			SimpleChatChannels.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", filename)
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getChannel(String channelwithoutsymbol, String msg)
	 {
		 String cws = channelwithoutsymbol;
		 if(!cws.equalsIgnoreCase("Global"))
		 {
			 if(msg.startsWith(symbolglobal))
			 {
				 return "Global";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Trade"))
		 {
			 if(msg.startsWith(symboltrade))
			 {
				 return "Trade";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Auction"))
		 {
			 if(msg.startsWith(symbolauction))
			 {
				 return "Auction";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Event"))
		 {
			 if(msg.startsWith(symbolevent))
			 {
				 return "Event";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Local"))
		 {
			 if(msg.startsWith(symbollocal))
			 {
				 return "Local";
			 }
		 }
		 if(!cws.equalsIgnoreCase("World"))
		 {
			 if(msg.startsWith(symbolworld))
			 {
				 return "World";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Support"))
		 {
			 if(msg.startsWith(symbolsupport))
			 {
				 return "Support";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Team"))
		 {
			 if(msg.startsWith(symbolteam))
			 {
				 return "Team";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Admin"))
		 {
			 if(msg.startsWith(symboladmin))
			 {
				 return "Admin";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Temp"))
		 {
			 if(msg.startsWith(symboltemp))
			 {
				 return "Temp";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Perma"))
		 {
			 if(msg.startsWith(symbolperma))
			 {
				 return "Perma";
			 }
		 }
		 if(!cws.equalsIgnoreCase("Group"))
		 {
			 if(msg.startsWith(symbolgroup))
			 {
				 return "Group";
			 }
		 }
		 if(!cws.equalsIgnoreCase("PrivateMessageRe"))
		 {
			 if(msg.startsWith(symbolpmre))
			 {
				 return "PrivateMessageRe";
			 }
		 }
		 if(!cws.equalsIgnoreCase("PrivateMessage"))
		 {
			 if(msg.startsWith(symbolpm))
			 {
				 return "PrivateMessage";
			 }
		 }
		return cws;
	 }
	 
	 public String getSymbol(String channel)
	 {
		if(channel.equals("Global"))
		{
			return symbolglobal;
		} else if(channel.equals("Trade"))
		{
			return symboltrade;
		} else if(channel.equals("Auction"))
		{
			return symbolauction;
		} else if(channel.equals("Event"))
		{
			return symbolevent;
		} else if(channel.equals("Local"))
		{
			return symbollocal;
		} else if(channel.equals("World"))
		{
			return symbolworld;
		} else if(channel.equals("Support"))
		{
			 return symbolsupport;
		} else if(channel.equals("Team"))
		{
			 return symbolteam;
		} else if(channel.equals("Admin"))
		{
			 return symboladmin;
		} else if(channel.equals("Temp"))
		{
			 return symboltemp;
		} else if(channel.equals("Perma"))
		{
			 return symbolperma;
		} else if(channel.equals("Group"))
		{
			return symbolgroup;
		} else if(channel.equals("PrivateMessageRe"))
		{
			 return symbolpmre;
		} else if(channel.equals("PrivateMessage"))
		{
			 return symbolpm;
		}
		return symbolglobal;
	 }
}

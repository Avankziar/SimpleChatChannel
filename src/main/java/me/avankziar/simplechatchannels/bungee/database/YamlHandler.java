package main.java.me.avankziar.simplechatchannels.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private Configuration cfg = null;
	private Configuration com = null;
	private Configuration wfi = null;
	private Configuration ara = null;
	private Configuration dut = null;
	private Configuration eng = null;
	private Configuration fre = null;
	private Configuration ger = null;
	private Configuration hin = null;
	private Configuration ita = null;
	private Configuration jap = null;
	private Configuration mad = null;
	private Configuration rus = null;
	private Configuration spa = null;
	private Configuration lang = null;
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
		if(!mkdir())
		{
			return false;
		}
		if(!loadYamls())
		{
			return false;
		}
		languages = cfg.getString("Language", "English");
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
	 
	public Configuration get()
	{
		return cfg;
	}
	
	public Configuration getCom()
	{
		return com;
	}
	
	public Configuration getWord()
	{
		return wfi;
	}
	
	public Configuration getL()
	{
		return lang;
	}
	
	public boolean saveWordfilter() 
	{
		try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(wfi, new File(plugin.getDataFolder(), "wordfilter.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	}
	
	public boolean saveCommands() 
	{
		try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(com, new File(plugin.getDataFolder(), "commands.yml"));
		 } catch (IOException e) 
		 {
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
		File config = new File(plugin.getDataFolder(), "bungeeconfig.yml");  
	    if (!config.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("bungeeconfig.yml")) 
	    	{       
	    		Files.copy(in, config.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File commands = new File(plugin.getDataFolder(), "commands.yml");  
	    if (!commands.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("commands.yml")) 
	    	{       
	    		Files.copy(in, commands.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File wordfilter = new File(plugin.getDataFolder(), "wordfilter.yml");  
	    if (!wordfilter.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("wordfilter.yml")) 
	    	{       
	    		Files.copy(in, wordfilter.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
		return true;
	}
	
	private boolean loadGeneralFiles()
	{
		try 
		{
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "bungeeconfig.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			com = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "commands.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			wfi = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "wordfilter.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
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
		File arabic = new File(directory.toPath().toString(), "arabic.yml");  
	    if (!arabic.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("arabic.yml")) 
	    	{       
	    		Files.copy(in, arabic.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File dutch = new File(directory.toPath().toString(), "dutch.yml");  
	    if (!dutch.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("dutch.yml")) 
	    	{       
	    		Files.copy(in, dutch.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File english = new File(directory.toPath().toString(), "english.yml");  
	    if (!english.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("english.yml")) 
	    	{       
	    		Files.copy(in, english.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File french = new File(directory.toPath().toString(), "french.yml");  
	    if (!french.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("french.yml")) 
	    	{       
	    		Files.copy(in, french.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File german = new File(directory.toPath().toString(), "german.yml");  
	    if (!german.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("german.yml")) 
	    	{       
	    		Files.copy(in, german.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File hindi = new File(directory.toPath().toString(), "hindi.yml");  
	    if (!hindi.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("hindi.yml")) 
	    	{       
	    		Files.copy(in, hindi.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File italian = new File(directory.toPath().toString(), "italian.yml");  
	    if (!italian.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("italian.yml")) 
	    	{       
	    		Files.copy(in, italian.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File japanese = new File(directory.toPath().toString(), "japanese.yml");  
	    if (!japanese.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("japanese.yml")) 
	    	{       
	    		Files.copy(in, japanese.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File mandarin = new File(directory.toPath().toString(), "mandarin.yml");  
	    if (!mandarin.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("mandarin.yml")) 
	    	{       
	    		Files.copy(in, mandarin.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File russian = new File(directory.toPath().toString(), "russian.yml");  
	    if (!russian.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("russian.yml")) 
	    	{       
	    		Files.copy(in, russian.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File spanish = new File(directory.toPath().toString(), "spanish.yml");  
	    if (!spanish.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("spanish.yml")) 
	    	{       
	    		Files.copy(in, spanish.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
		return true;
	}
	
	public boolean loadYamls()
	{
		try 
		{
			ara = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "arabic.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			dut = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "dutch.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			eng = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "english.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			fre = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "french.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			ger = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "german.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			hin = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "hindi.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			ita = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "italian.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			jap = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "japanese.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			mad = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "mandarin.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			rus = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "russian.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			spa = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "spanish.yml"));
		} catch (IOException e) 
		{
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

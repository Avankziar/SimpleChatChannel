package main.java.me.avankziar.simplechatchannels.spigot.database;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	private File language = null;
	private YamlConfiguration lgg = new YamlConfiguration();
	private String languages;
	private String symbolglobal;
	private String symboltrade;
	private String symbolauction;
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
		if(!mkdir())
		{
			return false;
		}
		if(!loadYamls())
		{
			return false;
		}
		languages = cfg.getString("Language", "English");
		symbolglobal = lgg.getString(languages+".ChannelSymbol.Global", "");
		symboltrade = lgg.getString(languages+".ChannelSymbol.Trade", "$");
		symbolauction = lgg.getString(languages+".ChannelSymbol.Auction", "!");
		symbollocal = lgg.getString(languages+".ChannelSymbol.Local", ",");
		symbolworld = lgg.getString(languages+".ChannelSymbol.World", "°");
		symbolsupport = lgg.getString(languages+".ChannelSymbol.Support", "?");
		symbolteam = lgg.getString(languages+".ChannelSymbol.Team", "+");
		symboladmin = lgg.getString(languages+".ChannelSymbol.Admin", "#");
		symbolpm = lgg.getString(languages+".ChannelSymbol.PrivateMessage", "@");
		symbolpmre = lgg.getString(languages+".ChannelSymbol.PrivateMessageRe", "@r");
		symbolgroup = lgg.getString(languages+".ChannelSymbol.Group", "@*");
		symboltemp = lgg.getString(languages+".ChannelSymbol.Temp", ";");
		symbolperma = lgg.getString(languages+".ChannelSymbol.Perma", ".");
		return true;
	}
	
	public YamlConfiguration get()
	{
		return cfg;
	}
	
	public YamlConfiguration getL()
	{
		return lgg;
	}
	
	public boolean mkdir() 
	{
		config = new File(plugin.getDataFolder(), "spigotconfig.yml");
		if(!config.exists()) 
		{
			SimpleChatChannels.log.info("Create config.yml...");
			plugin.saveResource("spigotconfig.yml", false);
		}
		language = new File(plugin.getDataFolder(), "language.yml");
		if(!language.exists()) 
		{
			SimpleChatChannels.log.info("Create language.yml...");
			plugin.saveResource("language.yml", false);
		}
		return true;
	}
	
	public boolean saveConfig() 
	{
	    try 
	    {
	    	SimpleChatChannels.log.info("Save spigotconfig.yml...");
	        cfg.save(config);
	    } catch (IOException e) 
	    {
	    	SimpleChatChannels.log.severe("Could not save the spigotconfig.yml! Error: " + e.getMessage());
			e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	public boolean saveLanguage() 
	{
	    try 
	    {
	    	SimpleChatChannels.log.info("Save language.yml...");
	        lgg.save(language);
	    } catch (IOException e) 
	    {
	    	SimpleChatChannels.log.severe("Could not save the language.yml! Error: " + e.getMessage());
			e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	public boolean loadYamls() 
	{
		try 
		{
			SimpleChatChannels.log.info("Load spigotconfig.yml...");
			cfg.load(config);
		} catch (IOException | InvalidConfigurationException e) {
			SimpleChatChannels.log.severe("Could not load the spigotconfig file! You need to regenerate the spigotconfig! Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		try 
		{
			lgg.load(language);
		} catch (IOException | InvalidConfigurationException e) 
		{
			SimpleChatChannels.log.severe("Could not load the language file! You need to regenerate the language! Error: " + e.getMessage());
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

	public String getLanguages()
	{
		return languages;
	}
}

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
	private String symbolcustom;
	
	public YamlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		mkdir();
		loadYamls();
		languages = cfg.getString("language");
		symbolglobal = lgg.getString(languages+".channelsymbol.global");
		symboltrade = lgg.getString(languages+".channelsymbol.trade");
		symbolauction = lgg.getString(languages+".channelsymbol.auction");
		symbollocal = lgg.getString(languages+".channelsymbol.local");
		symbolworld = lgg.getString(languages+".channelsymbol.world");
		symbolsupport = lgg.getString(languages+".channelsymbol.support");
		symbolteam = lgg.getString(languages+".channelsymbol.team");
		symboladmin = lgg.getString(languages+".channelsymbol.admin");
		symbolpm = lgg.getString(languages+".channelsymbol.message");
		symbolpmre = lgg.getString(languages+".channelsymbol.messagere");
		symbolgroup = lgg.getString(languages+".channelsymbol.group");
		symbolcustom = lgg.getString(languages+".channelsymbol.custom");
	}
	
	public void reload()
	{
		languages = cfg.getString("language");
		symbolglobal = lgg.getString(languages+".channelsymbol.global");
		symboltrade = lgg.getString(languages+".channelsymbol.trade");
		symbolauction = lgg.getString(languages+".channelsymbol.auction");
		symbollocal = lgg.getString(languages+".channelsymbol.local");
		symbolworld = lgg.getString(languages+".channelsymbol.world");
		symbolsupport = lgg.getString(languages+".channelsymbol.support");
		symbolteam = lgg.getString(languages+".channelsymbol.team");
		symboladmin = lgg.getString(languages+".channelsymbol.admin");
		symbolpm = lgg.getString(languages+".channelsymbol.message");
		symbolpmre = lgg.getString(languages+".channelsymbol.messagere");
		symbolgroup = lgg.getString(languages+".channelsymbol.group");
		symbolcustom = lgg.getString(languages+".channelsymbol.custom");
	}
	
	public YamlConfiguration get()
	{
		return cfg;
	}
	
	public YamlConfiguration getL()
	{
		return lgg;
	}
	
	public void mkdir() 
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
	}
	
	public void saveConfig() 
	{
	    try 
	    {
	    	SimpleChatChannels.log.info("Save spigotconfig.yml...");
	        cfg.save(config);
	    } catch (IOException e) 
	    {
	    	SimpleChatChannels.log.severe("Could not save the spigotconfig.yml! Error: " + e.getMessage());
			e.printStackTrace();
	    }
	}
	
	public void saveLanguage() 
	{
	    try 
	    {
	    	SimpleChatChannels.log.info("Save language.yml...");
	        lgg.save(language);
	    } catch (IOException e) 
	    {
	    	SimpleChatChannels.log.severe("Could not save the language.yml! Error: " + e.getMessage());
			e.printStackTrace();
	    }
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
		 if(!cws.equalsIgnoreCase("global"))
		 {
			 if(msg.startsWith(symbolglobal))
			 {
				 return "global";
			 }
		 }
		 if(!cws.equalsIgnoreCase("trade"))
		 {
			 if(msg.startsWith(symboltrade))
			 {
				 return "trade";
			 }
		 }
		 if(!cws.equalsIgnoreCase("auction"))
		 {
			 if(msg.startsWith(symbolauction))
			 {
				 return "auction";
			 }
		 }
		 if(!cws.equalsIgnoreCase("local"))
		 {
			 if(msg.startsWith(symbollocal))
			 {
				 return "local";
			 }
		 }
		 if(!cws.equalsIgnoreCase("world"))
		 {
			 if(msg.startsWith(symbolworld))
			 {
				 return "world";
			 }
		 }
		 if(!cws.equalsIgnoreCase("support"))
		 {
			 if(msg.startsWith(symbolsupport))
			 {
				 return "support";
			 }
		 }
		 if(!cws.equalsIgnoreCase("team"))
		 {
			 if(msg.startsWith(symbolteam))
			 {
				 return "team";
			 }
		 }
		 if(!cws.equalsIgnoreCase("admin"))
		 {
			 if(msg.startsWith(symboladmin))
			 {
				 return "admin";
			 }
		 }
		 if(!cws.equalsIgnoreCase("custom"))
		 {
			 if(msg.startsWith(symbolcustom))
			 {
				 return "custom";
			 }
		 }
		 if(!cws.equalsIgnoreCase("group"))
		 {
			 if(msg.startsWith(symbolgroup))
			 {
				 return "group";
			 }
		 }
		 if(!cws.equalsIgnoreCase("pmre"))
		 {
			 if(msg.startsWith(symbolpmre))
			 {
				 return "pmre";
			 }
		 }
		 if(!cws.equalsIgnoreCase("pm"))
		 {
			 if(msg.startsWith(symbolpm))
			 {
				 return "pm";
			 }
		 }
		return cws;
	 }
	 
	 public String getSymbol(String channel)
	 {
		if(channel.equals("global"))
		{
			return symbolglobal;
		} else if(channel.equals("trade"))
		{
			return symboltrade;
		} else if(channel.equals("auction"))
		{
			return symbolauction;
		} else if(channel.equals("local"))
		{
			return symbollocal;
		} else if(channel.equals("world"))
		{
			return symbolworld;
		} else if(channel.equals("support"))
		{
			 return symbolsupport;
		} else if(channel.equals("team"))
		{
			 return symbolteam;
		} else if(channel.equals("admin"))
		{
			 return symboladmin;
		} else if(channel.equals("custom"))
		{
			 return symbolcustom;
		} else if(channel.equals("group"))
		{
			return symbolgroup;
		} else if(channel.equals("pmre"))
		{
			 return symbolpmre;
		} else if(channel.equals("pm"))
		{
			 return symbolpm;
		}
		return symbolglobal;
	 }
}

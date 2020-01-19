package main.java.de.avankziar.simplechatchannels.spigot.database;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;

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
	private String symbolgroup;
	private String symbolcustom;
	
	public YamlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		mkdir();
		loadYamls();
		languages = plugin.getYamlHandler().get().getString("language");
		symbolglobal = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.global");
		symboltrade = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.trade");
		symbolauction = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.auction");
		symbollocal = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.local");
		symbolworld = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.world");
		symbolsupport = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.support");
		symbolteam = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.team");
		symboladmin = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.admin");
		symbolpm = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.pm");
		symbolgroup = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.group");
		symbolcustom = plugin.getYamlHandler().getL().getString(languages+".channelsymbol.custom");
	}
	
	public YamlConfiguration get()
	{
		return cfg;
	}
	
	public YamlConfiguration getL()
	{
		return lgg;
	}
	
	private void mkdir() 
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
	
	public void loadYamls() 
	{
		try 
		{
			SimpleChatChannels.log.info("Load spigotconfig.yml...");
			cfg.load(config);
		} catch (IOException | InvalidConfigurationException e) {
			SimpleChatChannels.log.severe("Could not load the spigotconfig file! You need to regenerate the spigotconfig! Error: " + e.getMessage());
			e.printStackTrace();
		}
		try 
		{
			lgg.load(language);
		} catch (IOException | InvalidConfigurationException e) 
		{
			SimpleChatChannels.log.severe("Could not load the language file! You need to regenerate the language! Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getSymbol(String channel)
	 {
		 switch(channel)
		 {
		 default:
			 return symbolglobal;
		 case "global":
			 return symbolglobal;
		 case "trade":
			 return symboltrade;
		 case "auction":
			 return symbolauction;
		 case "local":
			 return symbollocal;
		 case "world":
			 return symbolworld;
		 case "support":
			 return symbolsupport;
		 case "team":
			 return symbolteam;
		 case "admin":
			 return symboladmin;
		 case "pm":
			 return symbolpm;
		 case "group":
			 return symbolgroup;
		 case "custom":
			 return symbolcustom;
		 }
	 }
}

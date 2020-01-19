package main.java.de.avankziar.simplechatchannels.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import main.java.de.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private Configuration cfg;
	private Configuration lgg;
	private String language;
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
		loadYaml();
		language = plugin.getYamlHandler().get().getString("language");
		symbolglobal = plugin.getYamlHandler().getL().getString(language+".channelsymbol.global");
		symboltrade = plugin.getYamlHandler().getL().getString(language+".channelsymbol.trade");
		symbolauction = plugin.getYamlHandler().getL().getString(language+".channelsymbol.auction");
		symbollocal = plugin.getYamlHandler().getL().getString(language+".channelsymbol.local");
		symbolworld = plugin.getYamlHandler().getL().getString(language+".channelsymbol.world");
		symbolsupport = plugin.getYamlHandler().getL().getString(language+".channelsymbol.support");
		symbolteam = plugin.getYamlHandler().getL().getString(language+".channelsymbol.team");
		symboladmin = plugin.getYamlHandler().getL().getString(language+".channelsymbol.admin");
		symbolpm = plugin.getYamlHandler().getL().getString(language+".channelsymbol.pm");
		symbolgroup = plugin.getYamlHandler().getL().getString(language+".channelsymbol.group");
		symbolcustom = plugin.getYamlHandler().getL().getString(language+".channelsymbol.custom");
	}
	
	public void loadYaml()
	 {
		 try 
		 {
			 cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "bungeeconfig.yml"));
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 try 
		 {
			 lgg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "language.yml"));
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
		
	 public void saveConfig()
	 {
		 try {
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, new File(plugin.getDataFolder(), "bungeeconfig.yml"));
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
	 
	 public void saveLanguage()
	 {
		 try {
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(lgg, new File(plugin.getDataFolder(), "language.yml"));
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
	 
	 public void mkdir()
	 {
		 if (!plugin.getDataFolder().exists())
		 {
			 plugin.getDataFolder().mkdir();
		 } 
	     File c = new File(plugin.getDataFolder(), "bungeeconfig.yml");  
	     if (!c.exists()) 
	     {
	    	 try (InputStream in = plugin.getResourceAsStream("bungeeconfig.yml")) 
	    	 {       
	    		 Files.copy(in, c.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	         }
	     }
	     File l = new File(plugin.getDataFolder(), "language.yml");  
	     if (!l.exists()) 
	     {
	    	 try (InputStream in = plugin.getResourceAsStream("language.yml")) 
	    	 {       
	    		 Files.copy(in, l.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	         }
	     }
	 }
	 
	 public Configuration get()
	 {
		 return cfg;
	 }
	 
	 public Configuration getL()
	 {
		 return lgg;
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

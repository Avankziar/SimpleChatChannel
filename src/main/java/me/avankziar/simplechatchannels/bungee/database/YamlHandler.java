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
	private String symbolpmre;
	private String symbolgroup;
	private String symbolcustom;
	
	public YamlHandler(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		mkdir();
		loadYaml();
		language = cfg.getString("language");
		symbolglobal = lgg.getString(language+".channelsymbol.global");
		symboltrade = lgg.getString(language+".channelsymbol.trade");
		symbolauction = lgg.getString(language+".channelsymbol.auction");
		symbollocal = lgg.getString(language+".channelsymbol.local");
		symbolworld = lgg.getString(language+".channelsymbol.world");
		symbolsupport = lgg.getString(language+".channelsymbol.support");
		symbolteam = lgg.getString(language+".channelsymbol.team");
		symboladmin = lgg.getString(language+".channelsymbol.admin");
		symbolpm = lgg.getString(language+".channelsymbol.message");
		symbolpmre = lgg.getString(language+".channelsymbol.messagere");
		symbolgroup = lgg.getString(language+".channelsymbol.group");
		symbolcustom = lgg.getString(language+".channelsymbol.custom");
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

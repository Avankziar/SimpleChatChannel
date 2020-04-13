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
	private Configuration cfg;
	private Configuration lgg;
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
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		if(!mkdir())
		{
			return false;
		}
		if(!loadYaml())
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
		symbolcustom = lgg.getString(languages+".ChannelSymbol.Custom", ";");
		return true;
	}
	
	public boolean loadYaml()
	 {
		 try 
		 {
			 cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "bungeeconfig.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 try 
		 {
			 lgg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "language.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	 }
		
	 public boolean saveConfig()
	 {
		 try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, new File(plugin.getDataFolder(), "bungeeconfig.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	 }
	 
	 public boolean saveLanguage()
	 {
		 try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(lgg, new File(plugin.getDataFolder(), "language.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	 }
	 
	 public boolean mkdir()
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
	        	 return false;
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
	        	 return false;
	         }
	     }
	     return true;
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
		 if(!cws.equalsIgnoreCase("Custom"))
		 {
			 if(msg.startsWith(symbolcustom))
			 {
				 return "Custom";
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
		} else if(channel.equals("Custom"))
		{
			 return symbolcustom;
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

package main.java.me.avankziar.simplechatchannels.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.database.Language.ISO639_2B;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private File config = null;
	private Configuration cfg = new Configuration();
	
	private File commands = null;
	private Configuration com = new Configuration();
	
	private String languages;
	private File language = null;
	private Configuration lang = new Configuration();
	
	private File chattitle = null;
	private Configuration cti = new Configuration();
	
	private File channels = null;
	private Configuration cha = new Configuration();
	
	private File emojis = null;
	private Configuration eji = new Configuration();
	
	private File wordFilter = null;
	private Configuration wfr = new Configuration();

	public YamlHandler(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public Configuration getConfig()
	{
		return cfg;
	}
	
	public Configuration getCommands()
	{
		return com;
	}
	
	public Configuration getLang()
	{
		return lang;
	}
	
	public Configuration getChatTitle()
	{
		return cti;
	}
	
	public Configuration getChannels()
	{
		return cha;
	}
	
	public Configuration getEmojis()
	{
		return eji;
	}
	
	public Configuration getWordFilter()
	{
		return wfr;
	}
	
	private Configuration loadYamlTask(File file, Configuration yaml)
	{
		Configuration y = null;
		try 
		{
			yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			SimpleChatChannels.log.info("File "+file.getName()+" loaded!");
		} catch (IOException e) 
		{
			SimpleChatChannels.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		y = yaml;
		return y;
	}
	
	private boolean writeFile(File file, Configuration yml, LinkedHashMap<String, Language> keyMap)
	{
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		try
		{
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, file);
			 SimpleChatChannels.log.info("File "+file.getName()+" saved!");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager());
		if(!mkdirStaticFiles())
		{
			return false;
		}
		if(!mkdirDynamicFiles())
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles()
	{
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			SimpleChatChannels.log.info("Create config.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, config.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		cfg = loadYamlTask(config, cfg);
		writeFile(config, cfg, plugin.getYamlManager().getConfigKey());
		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			SimpleChatChannels.log.info("Create commands.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, commands.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		com = loadYamlTask(commands, com);
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());
		
		chattitle = new File(plugin.getDataFolder(), "chattitle.yml");
		if(!chattitle.exists()) 
		{
			SimpleChatChannels.log.info("Create chattitle.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, chattitle.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		cti = loadYamlTask(chattitle, cti);
		writeFile(chattitle, cti, plugin.getYamlManager().getChatTitleKey());
		
		channels = new File(plugin.getDataFolder(), "channels.yml");
		if(!channels.exists()) 
		{
			SimpleChatChannels.log.info("Create channels.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, channels.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		cha = loadYamlTask(channels, cha);
		writeFile(channels, cha, plugin.getYamlManager().getChannelsKey());
		
		emojis = new File(plugin.getDataFolder(), "emojis.yml");
		if(!emojis.exists()) 
		{
			SimpleChatChannels.log.info("Create emojis.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, emojis.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		eji = loadYamlTask(emojis, eji);
		writeFile(emojis, eji, plugin.getYamlManager().getEmojiKey());
		
		wordFilter = new File(plugin.getDataFolder(), "wordfilter.yml");
		if(!wordFilter.exists()) 
		{
			SimpleChatChannels.log.info("Create wordfilter.yml...");
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, channels.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		wfr = loadYamlTask(wordFilter, wfr);
		writeFile(wordFilter, wfr, plugin.getYamlManager().getWordFilterKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles()
	{
		List<Language.ISO639_2B> types = new ArrayList<Language.ISO639_2B>(EnumSet.allOf(Language.ISO639_2B.class));
		ISO639_2B languageType = ISO639_2B.ENG;
		for(ISO639_2B type : types)
		{
			if(type.toString().equals(languages))
			{
				languageType = type;
				break;
			}
		}
		plugin.getYamlManager().setLanguageType(languageType);
		if(!mkdirLanguage())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		language = new File(directory.getPath(), languageString+".yml");
		if(!language.exists()) 
		{
			SimpleChatChannels.log.info("Create %lang%.yml...".replace("%lang%", languageString));
			 try (InputStream in = plugin.getResourceAsStream("default.yml")) 
	    	 {       
	    		 Files.copy(in, language.toPath());
	         } catch (IOException e) 
	    	 {
	        	 e.printStackTrace();
	        	 return false;
	         }
		}
		lang = loadYamlTask(language, lang);
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		return true;
	}
}

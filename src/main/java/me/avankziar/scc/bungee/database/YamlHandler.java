package main.java.me.avankziar.scc.bungee.database;

/*import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.database.FileHandler;
import main.java.me.avankziar.scc.database.FileHandler.ISO639_2B;
import main.java.me.avankziar.scc.database.YamlManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;*/

public class YamlHandler 
{
	/*private SimpleChatChannels plugin;
	private Configuration cfg = new Configuration();
	private String languages;
	private Configuration com = new Configuration();
	private Configuration lang = new Configuration();
	private Configuration cti = new Configuration();
	private Configuration cha = new Configuration();
	private Configuration eji = new Configuration();
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
			y = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) 
		{
			SimpleChatChannels.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		return y;
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager(false));
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
	
	public void save(Configuration yml, String filename)
	{
		try
		{
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, new File(plugin.getDataFolder(), filename+".yml"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void save(Configuration yml, String filename, String additionalDirectory)
	{
		try
		{
			 ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, 
					 new File(plugin.getDataFolder()+"/"+additionalDirectory+"/", filename+".yml"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private File fileHandling(String filename, LinkedHashMap<ISO639_2B, ArrayList<String>> map, boolean rewrite)
	{
		File file = FileHandler.initFile(plugin.getDataFolder(), filename+".yml");
		FileHandler.writeFile(file, FileHandler.readFile(file), 
				(map.get(plugin.getYamlManager().getLanguageType()) != null)
				? map.get(plugin.getYamlManager().getLanguageType())
				: map.get(plugin.getYamlManager().getDefaultLanguageType()), rewrite);
		return file;
	}
	
	private File fileHandling(String filename, LinkedHashMap<ISO639_2B, ArrayList<String>> map,
			String additionalDirectory, boolean rewrite)
	{
		File file = FileHandler.initFile(plugin.getDataFolder(), filename+".yml", additionalDirectory);
		FileHandler.writeFile(file, FileHandler.readFile(file), 
				(map.get(plugin.getYamlManager().getLanguageType()) != null)
				? map.get(plugin.getYamlManager().getLanguageType())
				: map.get(plugin.getYamlManager().getDefaultLanguageType()), rewrite);
		return file;
	}
	
	public boolean mkdirStaticFiles()
	{
		cfg = loadYamlTask(fileHandling("config", plugin.getYamlManager().getConfigMap(), true), cfg);		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		com = loadYamlTask(fileHandling("commands", plugin.getYamlManager().getCommandsMap(), true), com);
		cti = loadYamlTask(fileHandling("chattitle", plugin.getYamlManager().getChatTitleMap(), false), cti);
		cha = loadYamlTask(fileHandling("channels", plugin.getYamlManager().getChannelsMap(), false), cha);
		eji = loadYamlTask(fileHandling("emojis", plugin.getYamlManager().getEmojisMap(), false), eji);
		wfr = loadYamlTask(fileHandling("wordfilter", plugin.getYamlManager().getWordFilterMap(), false), wfr);
		return true;
	}
	
	private boolean mkdirDynamicFiles()
	{
		List<ISO639_2B> types = new ArrayList<ISO639_2B>(EnumSet.allOf(ISO639_2B.class));
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
		lang = loadYamlTask(fileHandling(languageString, plugin.getYamlManager().getLanguageMap(), "Languages", true), lang);
		return true;
	}*/
}

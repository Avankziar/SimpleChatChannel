package main.java.me.avankziar.scc.spigot.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.scc.database.FileHandler;
import main.java.me.avankziar.scc.database.FileHandler.ISO639_2B;
import main.java.me.avankziar.scc.database.YamlManager;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private YamlConfiguration com = new YamlConfiguration();
	
	private String languages;
	private YamlConfiguration lang = new YamlConfiguration();
	
	private YamlConfiguration cti = new YamlConfiguration();
	
	private YamlConfiguration cha = new YamlConfiguration();
	
	private YamlConfiguration eji = new YamlConfiguration();
	
	private YamlConfiguration wfr = new YamlConfiguration();
	
	private LinkedHashMap<String, File> guifiles = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> gui = new LinkedHashMap<>();
	
	public YamlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	public YamlConfiguration getCommands()
	{
		return com;
	}
	
	public YamlConfiguration getLang()
	{
		return lang;
	}
	
	public YamlConfiguration getChatTitle()
	{
		return cti;
	}
	
	public YamlConfiguration getChannels()
	{
		return cha;
	}
	
	public YamlConfiguration getEmojis()
	{
		return eji;
	}
	
	public YamlConfiguration getWordFilter()
	{
		return wfr;
	}
	
	public YamlConfiguration getGui(String guitype)
	{
		return gui.get(guitype);
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
	
	public void save(YamlConfiguration yml, String filename)
	{
		try
		{
			yml.save(new File(plugin.getDataFolder(), filename+".yml"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void save(YamlConfiguration yml, String filename, String additionalDirectory)
	{
		try
		{
			yml.save(new File(plugin.getDataFolder()+"/"+additionalDirectory+"/", filename+".yml"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void fileHandling(String filename, YamlConfiguration con, LinkedHashMap<ISO639_2B, ArrayList<String>> map)
	{
		File file = FileHandler.initFile(plugin.getDataFolder(), filename+".yml");
		FileHandler.writeFile(file, FileHandler.readFile(file), 
				(map.get(plugin.getYamlManager().getLanguageType()) != null)
				? map.get(plugin.getYamlManager().getLanguageType())
				: map.get(plugin.getYamlManager().getDefaultLanguageType()));
		loadYamlTask(file, con);
	}
	
	private void fileHandling(String filename, YamlConfiguration con, LinkedHashMap<ISO639_2B, ArrayList<String>> map,
			String additionalDirectory)
	{
		File file = FileHandler.initFile(plugin.getDataFolder(), filename, additionalDirectory);
		FileHandler.writeFile(file, FileHandler.readFile(file), 
				(map.get(plugin.getYamlManager().getLanguageType()) != null)
				? map.get(plugin.getYamlManager().getLanguageType())
				: map.get(plugin.getYamlManager().getDefaultLanguageType()));
		loadYamlTask(file, con);
	}
	
	public boolean mkdirStaticFiles()
	{
		fileHandling("config", cfg, plugin.getYamlManager().getConfigMap());
		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		fileHandling("commands", com, plugin.getYamlManager().getCommandsMap());
		fileHandling("chattitle", cti, plugin.getYamlManager().getChatTitleMap());
		fileHandling("channels", cha, plugin.getYamlManager().getChannelsMap());
		fileHandling("emojis", eji, plugin.getYamlManager().getEmojisMap());
		fileHandling("wordfilter", wfr, plugin.getYamlManager().getWordFilterMap());
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
		if(!mkdirGuis())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		fileHandling(languageString, cfg, plugin.getYamlManager().getLanguageMap(), "Languages");
		return true;
	}
	
	private boolean mkdirGuis()
	{
		File directory = new File(plugin.getDataFolder()+"/Guis/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		List<String> guilist = getConfig().getStringList("GuiList");
		if(guilist == null || guilist.isEmpty())
		{
			return false;
		}
		for(String g : guilist)
		{
			if(g.equalsIgnoreCase("DUMMY"))
			{
				continue;
			}
			if(guifiles.containsKey(g))
			{
				guifiles.remove(g);
			}
			YamlConfiguration gyaml = new YamlConfiguration();
			fileHandling(g, gyaml, plugin.getYamlManager().getGuiMap(g), "Guis");
			gui.put(g, gyaml);
		}
		return true;
	}
	
	private boolean loadYamlTask(File file, YamlConfiguration yaml)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			SimpleChatChannels.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
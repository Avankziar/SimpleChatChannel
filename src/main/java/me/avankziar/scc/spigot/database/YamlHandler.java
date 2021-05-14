package main.java.me.avankziar.scc.spigot.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import main.java.me.avankziar.scc.database.Language;
import main.java.me.avankziar.scc.database.Language.ISO639_2B;
import main.java.me.avankziar.scc.database.YamlManager;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;

public class YamlHandler 
{
	private SimpleChatChannels plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	
	private String languages;
	private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();
	
	private File chattitle = null;
	private YamlConfiguration cti = new YamlConfiguration();
	
	private File channels = null;
	private YamlConfiguration cha = new YamlConfiguration();
	
	private File emojis = null;
	private YamlConfiguration eji = new YamlConfiguration();
	
	private File wordFilter = null;
	private YamlConfiguration wfr = new YamlConfiguration();
	
	private LinkedHashMap<String, File> guifiles = new LinkedHashMap<>();
	private LinkedHashMap<String, YamlConfiguration> gui = new LinkedHashMap<>();
	
	public YamlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager(true));
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
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), config);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(config, cfg))
		{
			return false;
		}
		writeFile(config, cfg, plugin.getYamlManager().getConfigKey());
		
		languages = cfg.getString("Language", "ENG").toUpperCase();
		
		commands = new File(plugin.getDataFolder(), "commands.yml");
		if(!commands.exists()) 
		{
			SimpleChatChannels.log.info("Create commands.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), commands);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(commands, com))
		{
			return false;
		}
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());
		
		chattitle = new File(plugin.getDataFolder(), "chattitle.yml");
		if(!chattitle.exists()) 
		{
			SimpleChatChannels.log.info("Create chattitle.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), chattitle);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(chattitle, cti))
		{
			return false;
		}
		writeFile(chattitle, cti, plugin.getYamlManager().getChatTitleKey());
		
		channels = new File(plugin.getDataFolder(), "channels.yml");
		if(!channels.exists()) 
		{
			SimpleChatChannels.log.info("Create channels.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), channels);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(channels, cha))
		{
			return false;
		}
		writeFile(channels, cha, plugin.getYamlManager().getChannelsKey());
		
		emojis = new File(plugin.getDataFolder(), "emojis.yml");
		if(!emojis.exists()) 
		{
			SimpleChatChannels.log.info("Create emojis.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), emojis);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(emojis, eji))
		{
			return false;
		}
		writeFile(emojis, eji, plugin.getYamlManager().getEmojiKey());
		
		wordFilter = new File(plugin.getDataFolder(), "wordfilter.yml");
		if(!wordFilter.exists()) 
		{
			SimpleChatChannels.log.info("Create wordfilter.yml...");
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), wordFilter);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(wordFilter, wfr))
		{
			return false;
		}
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
		
		if(!mkdirGuis())
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
			try
			{
				FileUtils.copyToFile(plugin.getResource("default.yml"), language);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if(!loadYamlTask(language, lang))
		{
			return false;
		}
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
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
			File guifile = new File(directory.getPath(), g+".yml");
			if(!guifile.exists()) 
			{
				SimpleChatChannels.log.info("Create %file%.yml...".replace("%file%", g));
				try
				{
					FileUtils.copyToFile(plugin.getResource("default.yml"), guifile);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			YamlConfiguration gyaml = new YamlConfiguration();
			//Laden der Datei
			if(!loadYamlTask(guifile, gyaml))
			{
				return false;
			}
			//Niederschreiben aller Werte in die Datei
			writeFile(guifile, gyaml, plugin.getYamlManager().getGuiKeys(g));
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
	
	private boolean writeFile(File file, YamlConfiguration yml, LinkedHashMap<String, Language> keyMap)
	{
		yml.options().header("For more explanation see \n https://www.spigotmc.org/resources/simple-chat-channels.35220/");
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBukkit(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInputBukkit(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		try
		{
			yml.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
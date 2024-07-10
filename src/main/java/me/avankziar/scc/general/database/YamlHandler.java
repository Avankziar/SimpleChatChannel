package main.java.me.avankziar.scc.general.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import main.java.me.avankziar.scc.general.database.Language.ISO639_2B;

public class YamlHandler implements YamlHandling
{
	private String pluginname;
	private Path dataDirectory;
	private YamlManager yamlManager;
	private Logger logger;
	private String administrationLanguage = null;
	
	private GeneralSettings gsd = GeneralSettings.DEFAULT;
	private LoaderSettings lsd = LoaderSettings.builder().setAutoUpdate(true).build();
	private DumperSettings dsd = DumperSettings.DEFAULT;
	private UpdaterSettings usd = UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
			.setKeepAll(true)
			.setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build();
	
	private String languages;
	
	private YamlDocument config;
	public YamlDocument getConfig()
	{
		return config;
	}
	
	private YamlDocument commands;
	public YamlDocument getCommands()
	{
		return commands;
	}
	
	private YamlDocument lang;
	public YamlDocument getLang()
	{
		return lang;
	}
	
	private YamlDocument chattitle;
	public YamlDocument getChatTitle()
	{
		return chattitle;
	}
	
	private YamlDocument channels;
	public YamlDocument getChannels()
	{
		return channels;
	}
	
	private YamlDocument emojis;
	public YamlDocument getEmojis()
	{
		return emojis;
	}
	
	private YamlDocument wordfilter;
	public YamlDocument getWordFilter()
	{
		return wordfilter;
	}
	
	private LinkedHashMap<String, YamlDocument> gui = new LinkedHashMap<>();
	public YamlDocument getGui(String guitype)
	{
		return gui.get(guitype);
	}
	
	public YamlHandler(YamlManager.Type type, String pluginname, Logger logger, Path directory, String administrationLanguage)
	{
		this.pluginname = pluginname;
		this.logger = logger;
		this.dataDirectory = directory;
		this.administrationLanguage = administrationLanguage;
		loadYamlHandler(type);
	}
	
	public boolean loadYamlHandler(YamlManager.Type type)
	{
		yamlManager = new YamlManager(type);
		if(!mkdirStaticFiles(type))
		{
			return false;
		}
		if(!mkdirDynamicFiles(type))
		{
			return false;
		}
		return true;
	}
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}
	
	public boolean mkdirStaticFiles(YamlManager.Type type)
	{
		File directory = new File(dataDirectory.getParent().toFile(), "/"+pluginname+"/");
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		String f = "config";
		try
		{
			config = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, config, yamlManager.getConfigKey()))
			{
				return false;
			}
			f = "commands";
			commands = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, commands, yamlManager.getCommandsKey()))
			{
				return false;
			}
			f = "chattitle";
			chattitle = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, chattitle, yamlManager.getChatTitleKey()))
			{
				return false;
			}
			f = "channels";
			channels = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, channels, yamlManager.getChannelsKey()))
			{
				return false;
			}
			f = "emojis";
			emojis = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, emojis, yamlManager.getEmojiKey()))
			{
				return false;
			}
			f = "wordfilter";
			wordfilter = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, wordfilter, yamlManager.getWordFilterKey()))
			{
				return false;
			}
		} catch(Exception e)
		{
			logger.severe("Could not create/load %f%.yml file! Plugin will shut down!".replace("%f%", f));
			return false;
		}
		return true;
	}
	
	private boolean setupStaticFile(String f, YamlDocument yd, LinkedHashMap<String, Language> map) throws IOException
	{
		yd.update();
		if(f.equals("config") && config != null)
		{
			//If Config already exists
			languages = administrationLanguage == null 
					? config.getString("Language", "ENG").toUpperCase() 
					: administrationLanguage;
			setLanguage();
		}
		for(String key : map.keySet())
		{
			Language languageObject = map.get(key);
			if(languageObject.languageValues.containsKey(yamlManager.getLanguageType()) == true)
			{
				yamlManager.setFileInput(yd, map, key, yamlManager.getLanguageType());
			} else if(languageObject.languageValues.containsKey(yamlManager.getDefaultLanguageType()) == true)
			{
				yamlManager.setFileInput(yd, map, key, yamlManager.getDefaultLanguageType());
			}
		}
		yd.save();
		if(f.equals("config") && config != null)
    	{
			//if Config was created the first time
			languages = administrationLanguage == null 
					? config.getString("Language", "ENG").toUpperCase() 
					: administrationLanguage;
			setLanguage();
    	}
		return true;
	}
	
	private void setLanguage()
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
		yamlManager.setLanguageType(languageType);
	}
	
	private boolean mkdirDynamicFiles(YamlManager.Type type)
	{
		if(!mkdirLanguage(type))
		{
			return false;
		}
		if(!mkdirGuis(type))
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage(YamlManager.Type type)
	{
		String languageString = yamlManager.getLanguageType().toString().toLowerCase();
		File directory = new File(dataDirectory.getParent().toFile(), "/"+pluginname+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		String f = languageString;
		try
	    {
			lang = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", f)),
					getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
			if(!setupStaticFile(f, lang, yamlManager.getLanguageKey()))
			{
				return false;
			}
	    } catch (Exception e)
	    {
	    	logger.severe("Could not create/load %f%.yml file! Plugin will shut down!".replace("%f%", languageString));
	    	return false;
	    }
		return true;
	}
	
	private boolean mkdirGuis(YamlManager.Type type)
	{
		if(type != YamlManager.Type.SPIGOT)
		{
			return true;
		}
		File directory = new File(dataDirectory.getParent().toFile(), "/"+pluginname+"/Guis/");
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
			if(gui.containsKey(g))
			{
				gui.remove(g);
			}
			try
			{
				YamlDocument yd = YamlDocument.create(new File(directory,"%f%.yml".replace("%f%", g)),
						getClass().getResourceAsStream("/default.yml"),gsd,lsd,dsd,usd);
				if(!setupStaticFile(g, lang, yamlManager.getGuiKeys(g)))
				{
					return false;
				}
				gui.put(g, yd);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	@Override
	public String getCommandString(String s)
	{
		return getCommands().getString(s);
	}
	
	@Override
	public String getCommandString(String s, String defaults)
	{
		String r = getCommandString(s);
		return r != null ? r : defaults;
	}
}
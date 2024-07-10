package main.java.me.avankziar.scc.spigot.objects;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.database.YamlHandler;
import main.java.me.avankziar.scc.spigot.SCC;


public class PluginSettings
{
	private boolean bungee;
	private boolean mysql;
	private String server;
	private boolean debug;
	private LinkedHashMap<String, String> commands = new LinkedHashMap<>(); //To save commandstrings
	
	public static PluginSettings settings;
	
	public PluginSettings(){}
	
	public PluginSettings(boolean bungee, boolean mysql, String server, boolean debug)
	{
		setBungee(bungee);
		setMysql(mysql);
		setServer(server);
		setDebug(debug);
	}
	
	public static void initSettings(SCC plugin) throws SQLException
	{
		YamlHandler yh = plugin.getYamlHandler();
		boolean bungee = plugin.getYamlHandler().getConfig().getBoolean("IsBungeeActive", false);
		boolean mysql = false;
		if(plugin.getMysqlSetup().getConnection() != null)
		{
			mysql = true;
		}
		String server = yh.getConfig().getString("Server");
		boolean debug = yh.getConfig().getBoolean("Use.DebuggingMode", false);
		settings = new PluginSettings(bungee, mysql, server, debug);
		plugin.getLogger().info("Plugin Settings init...");
	}
	
	public static void debug(String s)
	{
		if(PluginSettings.settings != null && PluginSettings.settings.isDebug())
		{
			if(SCC.getPlugin() != null)
			{
				SCC.getPlugin().getLogger().info(s);
			}
		}
	}
	
	public static void debug(Player player, String s)
	{
		if(PluginSettings.settings != null && PluginSettings.settings.isDebug())
		{
			if(player != null)
			{
				player.sendMessage(s);
			}
		}
	}

	public boolean isMysql()
	{
		return mysql;
	}

	public void setMysql(boolean mysql)
	{
		this.mysql = mysql;
	}

	public boolean isBungee()
	{
		return bungee;
	}

	public void setBungee(boolean bungee)
	{
		this.bungee = bungee;
	}

	public boolean isDebug()
	{
		return debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public String getCommands(String key)
	{
		return commands.get(key);
	}

	public void setCommands(LinkedHashMap<String, String> commands)
	{
		this.commands = commands;
	}
	
	public void addCommands(String key, String commandString)
	{
		if(commands.containsKey(key))
		{
			commands.replace(key, commandString);
		} else
		{
			commands.put(key, commandString);
		}
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}
}

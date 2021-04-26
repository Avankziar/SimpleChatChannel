package main.java.me.avankziar.scc.spigot.objects;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.scc.spigot.SimpleChatChannels;

public class BypassPermission
{
	public static String USE_COLOR = "";
	public static String USE_COLOR_BYPASS = "";
	public static String USE_ITEM = "";
	public static String USE_ITEM_BYPASS = "";
	public static String USE_BOOK = "";
	public static String USE_BOOK_BYPASS = "";
	public static String USE_RUNCOMMAND = "";
	public static String USE_RUNCOMMAND_BYPASS = "";
	public static String USE_SUGGESTCOMMAND = "";
	public static String USE_SUGGESTCOMMAND_BYPASS = "";
	public static String USE_WEBSITE = "";
	public static String USE_WEBSITE_BYPASS = "";
	public static String USE_EMOJI = "";
	public static String USE_EMOJI_BYPASS = "";
	public static String USE_MENTION = "";
	public static String USE_MENTION_BYPASS = "";
	public static String USE_SOUND = "";
	public static String USE_POSITION = "";
	public static String USE_POSITION_BYPASS = "";
	
	public static String PERMBYPASSIGNORE = "";
	public static String PERMBYPASSOFFLINECHANNEL = "";
	
	public static String PERMBYPASSPC= "";
	
	public static String CUSTOM_ITEMREPLACERSTORAGE = "";
	
	public static void init(SimpleChatChannels plugin)
	{
		YamlConfiguration yml = plugin.getYamlHandler().getCommands();
		String bp = "Bypass.";
		String normal = "scc.channel.";
		String by = "scc.bypass.";
		USE_COLOR = yml.getString(bp+"Color.Channel", normal+"color");
		USE_COLOR_BYPASS = yml.getString(bp+"Color.Bypass", by+"color");
		USE_ITEM = yml.getString(bp+"Item.Channel", normal+"item");
		USE_ITEM_BYPASS = yml.getString(bp+"Item.Bypass", by+"item");
		USE_BOOK = yml.getString(bp+"Book.Channel", normal+"book");
		USE_BOOK_BYPASS = yml.getString(bp+"Book.Bypass", by+"book");
		USE_RUNCOMMAND = yml.getString(bp+"RunCommand.Channel", normal+"runcommand");
		USE_RUNCOMMAND_BYPASS = yml.getString(bp+"RunCommand.Bypass", by+"runcommand");
		USE_SUGGESTCOMMAND = yml.getString(bp+"SuggestCommand.Channel", normal+"suggestcommand");
		USE_SUGGESTCOMMAND_BYPASS = yml.getString(bp+"SuggestCommand.Bypass", by+"suggestcommand");
		USE_WEBSITE = yml.getString(bp+"Website.Channel", normal+"website");
		USE_WEBSITE_BYPASS = yml.getString(bp+"Website.Bypass", by+"website");
		USE_EMOJI = yml.getString(bp+"Emoji.Channel", normal+"emoji");
		USE_EMOJI_BYPASS = yml.getString(bp+"Emoji.Bypass", by+"emoji");
		USE_MENTION = yml.getString(bp+"Mention.Channel", normal+"mention");
		USE_MENTION_BYPASS = yml.getString(bp+"Mention.Bypass", by+"mention");
		USE_SOUND = yml.getString(bp+"Sound.Channel", normal+"sound");
		USE_POSITION = yml.getString(bp+"Position.Channel", normal+"position");
		USE_POSITION_BYPASS = yml.getString(bp+"Position.Bypass", by+"position");
		
		PERMBYPASSIGNORE = yml.getString(bp+"Ignore", by+"ignore");
		PERMBYPASSOFFLINECHANNEL = yml.getString(bp+"OfflineChannel", by+"offlinechannel");
		
		PERMBYPASSPC = yml.getString(bp+"PermanentChannel", by+"permanentchannel");
		
		String path = "Custom.";
		String cus = "scc.custom.";
		CUSTOM_ITEMREPLACERSTORAGE = yml.getString(path+"ItemReplacerStorage", cus+"itemreplacerstorage.");
	}
	
	
	
}

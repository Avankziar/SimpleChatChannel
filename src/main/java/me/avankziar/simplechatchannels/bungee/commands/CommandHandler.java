package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.HashMap;

import net.md_5.bungee.api.CommandSender;

public abstract class CommandHandler
{
	public String lable;
	public String permission;
    public int minArgs;
    public int maxArgs;
    public String[] aliases;

    public CommandHandler(String lable, String permission, HashMap<String, CommandHandler> map, 
    		int minArgs, int maxArgs, String... aliases)
    {
        this.lable = lable;
        this.permission = permission;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.aliases = aliases;

		map.put(lable, this);
		for(String alias : aliases)
		{
			map.put(alias, this);
		}
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args);
}

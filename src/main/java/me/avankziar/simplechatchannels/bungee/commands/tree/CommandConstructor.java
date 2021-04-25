package main.java.me.avankziar.simplechatchannels.bungee.commands.tree;

import java.util.ArrayList;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;

public class CommandConstructor extends BaseConstructor
{
	public ArrayList<ArgumentConstructor> subcommands;
    public ArrayList<String> tablist;

	public CommandConstructor(String path, boolean canConsoleAccess,
    		ArgumentConstructor...argumentConstructors)
    {
		super(SimpleChatChannels.getPlugin().getYamlHandler().getCommands().getString(path+".Name"),
				path,
				SimpleChatChannels.getPlugin().getYamlHandler().getCommands().getString(path+".Permission"),
				SimpleChatChannels.getPlugin().getYamlHandler().getCommands().getString(path+".Suggestion"),
				SimpleChatChannels.getPlugin().getYamlHandler().getCommands().getString(path+".CommandString"),
				SimpleChatChannels.getPlugin().getYamlHandler().getCommands().getString(path+".HelpInfo"),
				canConsoleAccess);
        this.subcommands = new ArrayList<>();
        this.tablist = new ArrayList<>();
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
        SimpleChatChannels.getPlugin().getCommandTree().add(this);
    }
}
package main.java.me.avankziar.simplechatchannels.bungee.commands.tree;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;

public class CommandConstructor extends BaseConstructor
{
    public ArrayList<ArgumentConstructor> subcommands;
    public ArrayList<String> tablist;

	public CommandConstructor(SimpleChatChannels plugin, String path,
    		ArgumentConstructor...argumentConstructors)
    {
		super(plugin.getYamlHandler().getCom().getString(path+".Name"),
				path,
				plugin.getYamlHandler().getCom().getString(path+".Permission"),
				plugin.getYamlHandler().getCom().getString(path+".Suggestion"));
        this.subcommands = new ArrayList<>();
        this.tablist = new ArrayList<>();
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
        plugin.getCommandTree().add(this);
    }
}
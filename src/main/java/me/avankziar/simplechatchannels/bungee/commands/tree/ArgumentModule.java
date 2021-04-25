package main.java.me.avankziar.simplechatchannels.bungee.commands.tree;

import java.io.IOException;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;

    public ArgumentModule(ArgumentConstructor argumentConstructor)
    {
       this.argumentConstructor = argumentConstructor;
       SimpleChatChannels.getPlugin().getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args) throws IOException;

}
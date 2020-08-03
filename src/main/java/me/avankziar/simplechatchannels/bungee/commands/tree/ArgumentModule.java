package main.java.me.avankziar.simplechatchannels.bungee.commands.tree;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;

    public ArgumentModule(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
    {
       this.argumentConstructor = argumentConstructor;
       plugin.getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args);

}

package main.java.me.avankziar.scc.bungee.commands.tree;

import java.io.IOException;

import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.BaseConstructor;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;

    public ArgumentModule(ArgumentConstructor argumentConstructor)
    {
       this.argumentConstructor = argumentConstructor;
       BaseConstructor.getArgumentMapBungee().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(net.md_5.bungee.api.CommandSender sender, String[] args) throws IOException;
}
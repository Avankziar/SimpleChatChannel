package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;

public class ARGCustomChannelLeave extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelLeave(SimpleChatChannels plugin)
	{
		super("ccleave","scc.cmd.cc.leave",SimpleChatChannels.sccarguments,1,1,"ccverlassen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"CustomChannelGeneral.NotInAChannel"));
			return;
		}
		final String name = cc.getName();
		cc.removeMembers(player);
		if(cc.getCreator().getName().equals(player.getName()))
		{
			Player newcreator = null;
			for(Player pp : cc.getMembers())
			{
				if(pp!=null)
				{
					newcreator = pp;
				}
			}
			if(newcreator!=null)
			{
				cc.setCreator(newcreator);
				///Du wurdest der neue Erstelle der CustomChannels &f%channel%
    			newcreator.spigot().sendMessage(utility.tctl(
    					plugin.getYamlHandler().getL().getString(language+"ChannelLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				CustomChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		///Du hast den CustomChannel &f%channel% &everlassen!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"ChannelLeave.YouLeft")
				.replace("%channel%", name)));
		return;
	}
}
package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"CustomChannelGeneral.NotInAChannel"));
			return;
		}
		final String name = cc.getName();
		cc.removeMembers(player);
		if(cc.getCreator().getName().equals(player.getName()))
		{
			ProxiedPlayer newcreator = null;
			for(ProxiedPlayer pp : cc.getMembers())
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
    			newcreator.sendMessage(utility.tctl(
    					plugin.getYamlHandler().getL().getString(language+"ChannelLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				CustomChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		///Du hast den CustomChannel &f%channel% &everlassen!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"ChannelLeave.YouLeft")
				.replace("%channel%", name)));
		return;
	}
}

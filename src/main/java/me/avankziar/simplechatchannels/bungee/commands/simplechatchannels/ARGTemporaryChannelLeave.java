package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelLeave extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelLeave(SimpleChatChannels plugin)
	{
		super("tcleave","scc.cmd.tc.leave",SimpleChatChannels.sccarguments,1,1,"tcverlassen",null);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotInAChannel"));
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
    					plugin.getYamlHandler().getL().getString(language+"TCLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				TemporaryChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		///Du hast den CustomChannel &f%channel% &everlassen!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCLeave.YouLeft")
				.replace("%channel%", name)));
		return;
	}
}

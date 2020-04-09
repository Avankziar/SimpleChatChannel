package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
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
		String language = plugin.getUtility().getLanguage();
		String scc = ".CMDSCC.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelLeave.msg01")));
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
    			newcreator.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"ChannelLeave.msg03")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				CustomChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelLeave.msg02")
				.replace("%channel%", name)));
		return;
	}
}

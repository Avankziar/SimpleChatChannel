package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelLeave extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelLeave(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotInAChannel")));
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
    			newcreator.sendMessage(ChatApi.tctl(
    					plugin.getYamlHandler().getL().getString(language+"TCLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				TemporaryChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		///Du hast den CustomChannel &f%channel% &everlassen!
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCLeave.YouLeft")
				.replace("%channel%", name)));
		return;
	}
}

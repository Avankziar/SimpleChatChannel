package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Kick extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Kick(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		String other = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
			return;
		}
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER,"`player_name` = ?", other);
		if(cuoffline == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		String targetuuid = cuoffline.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.ViceCannotKickCreator")));
			return;
		}
		if(!cc.getMembers().contains(targetuuid))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.NotAChannelMember")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.CannotSelfKick")));
			return;
		}
		cc.removeMembers(targetuuid);
		cc.removeVice(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		if(plugin.getServer().getPlayer(other).isPresent())
		{
			Player target = plugin.getServer().getPlayer(other).get();
			target.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.YouWereKicked")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.YouKicked")
				.replace("%player%", other).replace("%channel%", cc.getNameColor()+cc.getName())));
		
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.KickedSomeone")
				.replace("%player%", other).replace("%channel%", cc.getNameColor()+cc.getName());
		for(Player members : plugin.getServer().getAllPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tl(msg));
			}
		}
	}
}
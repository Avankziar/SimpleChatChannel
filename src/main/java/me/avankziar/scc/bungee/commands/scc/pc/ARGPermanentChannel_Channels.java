package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Channels extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Channels(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		List<BaseComponent> list = new ArrayList<>();
		for(int i = 0; i < PermanentChannel.getPermanentChannel().size(); i++)
		{
			PermanentChannel pc = PermanentChannel.getPermanentChannel().get(i);
			String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_PC_INFO)+pc.getName();
			if(i+1 == PermanentChannel.getPermanentChannel().size())
			{
				list.add(ChatApiOld.click(pc.getNameColor()+pc.getName(),
						"RUN_COMMAND", cmd));
			} else
			{
				list.add(ChatApiOld.click(pc.getNameColor()+pc.getName()+", &r",
						"RUN_COMMAND", cmd));
			}
		}
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Channels.Headline")));
		player.sendMessage(ChatApiOld.tctl(list));
	}
}
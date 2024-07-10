package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

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
		Player player = (Player) sender;
		List<String> list = new ArrayList<>();
		for(int i = 0; i < PermanentChannel.getPermanentChannel().size(); i++)
		{
			PermanentChannel pc = PermanentChannel.getPermanentChannel().get(i);
			String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_PC_INFO)+pc.getName();
			if(i+1 == PermanentChannel.getPermanentChannel().size())
			{
				list.add(ChatApi.click(pc.getNameColor()+pc.getName(),
						"RUN_COMMAND", cmd));
			} else
			{
				list.add(ChatApi.click(pc.getNameColor()+pc.getName()+", &r",
						"RUN_COMMAND", cmd));
			}
		}
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Channels.Headline")));
		player.spigot().sendMessage(ChatApi.tctl(String.join("", list)));
	}
}
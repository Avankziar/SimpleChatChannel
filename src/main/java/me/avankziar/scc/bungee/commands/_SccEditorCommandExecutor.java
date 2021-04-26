package main.java.me.avankziar.scc.bungee.commands;

public class _SccEditorCommandExecutor // extends Command
{
	/*private SimpleChatChannels plugin;
	private String scc = "CmdSccEditor.";
	private static CommandConstructor cc;
	
	public _SccEditorCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(),null);
		this.plugin = plugin;
		_SccEditorCommandExecutor.cc = cc;
	}
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(!(sender instanceof ProxiedPlayer))
    	{
    		return;
    	}
    	ProxiedPlayer player = (ProxiedPlayer) sender;
    	String µ = "µ";
    	if(!player.hasPermission(cc.getPermission()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.NoPermission")));
			return;
		}
    	if(args.length == 0)
    	{
    		if(plugin.editorplayers.contains(player.getName()))
    		{
    			plugin.editorplayers.remove(player.getName());
    			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString(scc+"Out")));
    			String message = "editor"+µ+player.getName()+µ+"remove";
    			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
    		} else
    		{
    			plugin.editorplayers.add(player.getName());
    			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString(scc+"In")));
    			String message = "editor"+µ+player.getName()+µ+"add";
    			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
    		}
    		return;
    	} else 
    	{
    		return;
    	}
    }*/
}

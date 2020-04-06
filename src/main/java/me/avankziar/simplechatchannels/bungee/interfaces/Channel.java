package main.java.me.avankziar.simplechatchannels.bungee.interfaces;

import java.util.ArrayList;
import java.util.Collection;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Channel
{
	//Eingangssymbol
	private String symbol;
	//Bspw.: '&9[Handel] '
	private String channelReplacer;
	//Bspw.: '&3: '
	private String chatSplit;
	private String channelColor;
	private String hoverMessage;
	private String joinReplacer;
	private Collection<ProxiedPlayer> target;
	
	private static ArrayList<Channel> allChannels;
	
	public Channel()
	{
		// TODO Auto-generated constructor stub
	}

}

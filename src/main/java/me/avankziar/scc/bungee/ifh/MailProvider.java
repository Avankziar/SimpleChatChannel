package main.java.me.avankziar.scc.bungee.ifh;

import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.ifh.general.interfaces.Mail;
import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import net.md_5.bungee.api.chat.TextComponent;

public class MailProvider implements Mail
{
	@Override
	public ArrayList<UUID> getCarbonCopys(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> getLastRecievedMails(UUID arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextComponent getParsedMessage(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProvider()
	{
		return SimpleChatChannels.pluginName;
	}

	@Override
	public String getRawMessage(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getReadedDate(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReciver(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getSendedDate(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSender(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubject(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> getUnreadedMails(UUID arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMail(UUID arg0, String arg1, String arg2)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendMail(UUID arg0, UUID arg1, String arg2, String arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

}

package main.java.me.avankziar.scc.objects.chat;

import java.util.ArrayList;

import net.md_5.bungee.api.chat.BaseComponent;

public class Components
{
	private ArrayList<BaseComponent> components = new ArrayList<>();
	private ArrayList<BaseComponent> componentsWithMentions = new ArrayList<>();
	private ArrayList<String> mentionPlayers = new ArrayList<>();
	
	public Components(){}

	public ArrayList<BaseComponent> getComponents()
	{
		return components;
	}

	public void setComponents(ArrayList<BaseComponent> components)
	{
		this.components = components;
	}

	public ArrayList<BaseComponent> getComponentsWithMentions()
	{
		return componentsWithMentions;
	}

	public void setComponentsWithMentions(ArrayList<BaseComponent> componentsWithMentions)
	{
		this.componentsWithMentions = componentsWithMentions;
	}
	
	public Components addAllComponents(Components components)
	{
		getComponents().addAll(components.getComponents());
		getComponentsWithMentions().addAll(components.getComponentsWithMentions());
		return this;
	}
	public Components addAllComponents(BaseComponent bc)
	{
		getComponents().add(bc);
		getComponentsWithMentions().add(bc);
		return this;
	}
	
	public Components addComponent(BaseComponent bc)
	{
		getComponents().add(bc);
		return this;
	}
	
	public Components addComponents(Components components)
	{
		getComponents().addAll(components.getComponents());
		return this;
	}
	
	public Components addComponentWithMentions(BaseComponent bc)
	{
		getComponentsWithMentions().add(bc);
		return this;
	}
	
	public Components addComponentsWithMentions(Components components)
	{
		getComponentsWithMentions().addAll(components.getComponentsWithMentions());
		return this;
	}
	
	public Components addMention(String playername)
	{
		this.mentionPlayers.add(playername);
		return this;
	}
	
	public boolean isMention(String playerName)
	{
		if(this.mentionPlayers.contains(playerName))
		{
			return true;
		}
		return false;
	}
}

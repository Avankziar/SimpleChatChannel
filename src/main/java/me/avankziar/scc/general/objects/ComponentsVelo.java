package main.java.me.avankziar.scc.general.objects;

import java.util.ArrayList;

public class ComponentsVelo
{
	private ArrayList<String> components = new ArrayList<>();
	private ArrayList<String> componentsWithMentions = new ArrayList<>();
	private ArrayList<String> mentionPlayers = new ArrayList<>();
	
	public ComponentsVelo(){}

	public ArrayList<String> getComponents()
	{
		return components;
	}

	public void setComponents(ArrayList<String> components)
	{
		this.components = components;
	}

	public ArrayList<String> getComponentsWithMentions()
	{
		return componentsWithMentions;
	}

	public void setComponentsWithMentions(ArrayList<String> componentsWithMentions)
	{
		this.componentsWithMentions = componentsWithMentions;
	}
	
	public ComponentsVelo addAllComponents(ComponentsVelo components)
	{
		getComponents().addAll(components.getComponents());
		getComponentsWithMentions().addAll(components.getComponentsWithMentions());
		this.mentionPlayers.addAll(components.mentionPlayers);
		return this;
	}
	public ComponentsVelo addAllComponents(String bc)
	{
		getComponents().add(bc);
		getComponentsWithMentions().add(bc);
		return this;
	}
	public ComponentsVelo addAllComponents(String... bc)
	{
		for(String b : bc)
		{
			getComponents().add(b);
			getComponentsWithMentions().add(b);
		}
		return this;
	}
	
	public ComponentsVelo addComponent(String bc)
	{
		getComponents().add(bc);
		return this;
	}
	
	public ComponentsVelo addComponents(ComponentsVelo components)
	{
		getComponents().addAll(components.getComponents());
		this.mentionPlayers.addAll(components.mentionPlayers);
		return this;
	}
	
	public ComponentsVelo addComponentWithMentions(String bc)
	{
		getComponentsWithMentions().add(bc);
		return this;
	}
	
	public ComponentsVelo addComponentsWithMentions(ComponentsVelo components)
	{
		getComponentsWithMentions().addAll(components.getComponentsWithMentions());
		this.mentionPlayers.addAll(components.mentionPlayers);
		return this;
	}
	
	public ComponentsVelo addMention(String playername)
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
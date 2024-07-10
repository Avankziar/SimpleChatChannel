package main.java.me.avankziar.scc.general.handlers;

public class ColorHandler
{	
	public static String getHex(String string)
	{
		String s = string;
		if(s.isEmpty() || s.isBlank())
		{
			s = "&#000000";
		}
		if(s.matches("[\\&][\\#][0-9a-fA-F]{6}"))
		{
			return s;
		} else if(s.matches("[\\&][0-9a-fA-F]"))
		{
			s = s.substring(1);
			switch(s)
			{
			case "0":
				s = "&#000000";
				break;
			case "1":
				s = "&#0000AA";
				break;
			case "2":
				s = "&#00AA00";
				break;
			case "3":
				s = "&#00AAAA";
				break;
			case "4":
				s = "&#AA0000";
				break;
			case "5":
				s = "&#AA00AA";
				break;
			case "6":
				s = "&#FFAA00";
				break;
			case "7":
				s = "&#AAAAAA";
				break;
			case "8":
				s = "&#555555";
				break;
			case "9":
				s = "&#5555FF";
				break;
			case "a":
			case "A":
				s = "&#55FF55";
				break;
			case "b":
			case "B":
				s = "&#55FFFF";
				break;
			case "c":
			case "C":
				s = "&#FF5555";
				break;
			case "d":
			case "D":
				s = "&#FF55FF";
				break;
			case "e":
			case "E":
				s = "&#FFFF55";
				break;
			case "f":
			case "F":
				s = "&#FFFFFF";
				break;
			}
			return s;
		}
		return "&#FFFFFF";
	}
}

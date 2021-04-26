package main.java.me.avankziar.scc.objects;

public class Advertising
{
	private String advertiser; //As UUID
	private long insertTime;
	private String rawText;
	private double costs;
	private int placedQuantity;
	private int remainingQuantity;
	private boolean directAdvertising; //Ob beim senden direkt die Werbung gepostet werden soll. Kostet extra
	
	public Advertising()
	{
		// ADDME
		/*
		 * Cooldown für direktWerbungen.
		 * Kosten pro Zeichen.
		 * Werbung per Befehl ausstellbar machen um sie nicht zu sehen.
		 * Aufbau so einer Werbung:
		 * .•>/^    [Werbung]    ^\<•.
		 * Text....
		 * Text...
		 * .•>/^    [Werbung]    ^\<•.
		 */
	}

}

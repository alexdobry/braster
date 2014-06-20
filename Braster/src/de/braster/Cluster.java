package de.braster;

import java.util.ArrayList;

public class Cluster {
	
	private String name;
	private ArrayList<Note> notes;
	
	public Cluster()
	{
		this.name = "";
		this.notes = new ArrayList<Note>();
	}
	
	public Cluster(String newName, ArrayList<Note> newNotes)
	{
		this.name = newName;
		this.notes = newNotes;
	}
	
	public ArrayList<Note> getNotes()
	{
		return this.notes;
	}

	public String getName()
	{
		return this.name;
	}
}

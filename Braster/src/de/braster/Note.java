package de.braster;

public class Note {
	
private String name;
private String clusterName;

public Note(String newName, String newClustername) {
	this.name = newName;
	this.clusterName = newClustername;
}

public Note(String newName)
{
	this.name = newName;
}

public Note()
{
	this.name ="";
	this.clusterName = "";
}


public String getName()
{
	return this.name;
}

public String getClusterName()
{
	return this.clusterName;
}


}

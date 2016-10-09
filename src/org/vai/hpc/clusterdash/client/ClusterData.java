package org.vai.hpc.clusterdash.client;

import java.io.Serializable;
import java.util.ArrayList;

public class ClusterData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nodeName;
	int coresAvail;
	double load;
	ArrayList<String> users;
	ArrayList<Integer> coresUsed;
	ArrayList<Integer> jobIds;
	public String getNodeName()
	{
		return nodeName;
	}
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	public int getCoresAvail()
	{
		return coresAvail;
	}
	public void setCoresAvail(int coresAvail)
	{
		this.coresAvail = coresAvail;
	}
	public double getLoad()
	{
		return load;
	}
	public void setLoad(double load)
	{
		this.load = load;
	}
	public ArrayList<String> getUsers()
	{
		return users;
	}
	public void setUsers(ArrayList<String> users)
	{
		this.users = users;
	}
	public ArrayList<Integer> getCoresUsed()
	{
		return coresUsed;
	}
	public void setCoresUsed(ArrayList<Integer> coresUsed)
	{
		this.coresUsed = coresUsed;
	}
	public ArrayList<Integer> getJobIds()
	{
		return jobIds;
	}
	public void setJobIds(ArrayList<Integer> jobIds)
	{
		this.jobIds = jobIds;
	} 
}
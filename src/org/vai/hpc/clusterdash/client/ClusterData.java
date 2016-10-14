package org.vai.hpc.clusterdash.client;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ClusterData implements IsSerializable
{
	/**
	 * 
	 */
	String nodeName;
	int coresAvail;
	double load;
	ArrayList<String> users;
	ArrayList<Integer> coresUsed;
	ArrayList<Integer> jobIds;
	ArrayList<Double> loadHistory;
	ArrayList<Integer> diskHistory;
	int KilobytesDiskActivtiy;
	
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
	public int getKilobytesDiskActivtiy()
	{
		return KilobytesDiskActivtiy;
	}
	public void setKilobytesDiskActivtiy(int kilobytesDiskActivtiy)
	{
		KilobytesDiskActivtiy = kilobytesDiskActivtiy;
	}
	public ArrayList<Double> getLoadHistory() {
		return loadHistory;
	}
	public void setLoadHistory(ArrayList<Double> loadHistory) {
		this.loadHistory = loadHistory;
	}
	public ArrayList<Integer> getDiskHistory() {
		return diskHistory;
	}
	public void setDiskHistory(ArrayList<Integer> diskHistory) {
		this.diskHistory = diskHistory;
	} 
}

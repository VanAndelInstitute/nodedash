package org.vai.hpc.clusterdash.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QuotaData implements IsSerializable
{
	 private static int lastId = 0;
	String id = "" + lastId++ ;
	String fileset;
	Long scratchUsed;
	Long scratchTotal;
	Long homeUsed;
	Long homeTotal;
	
	public String toString()
	{
		return fileset + "\t" + getHomeNormalized() + "\t" + getScratchNormalized();
	}
	
	public Float getHomeNormalized()
	{
		return 100.0f *(0.0f + getHomeUsed()) / (0.1f + getHomeTotal() + getScratchTotal());		
	}
	
	public Float getScratchNormalized()
	{
		return 100.0f * (0.0f + getScratchUsed()) / (0.1f + getHomeTotal() + getScratchTotal());		
	}
	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getFileset()
	{
		return fileset;
	}
	public void setFileset(String fileset)
	{
		this.fileset = fileset;
	}
	public Long getScratchUsed()
	{
		if(scratchUsed!=null)
			return scratchUsed;
		else
			return 0l;	
	}
	public void setScratchUsed(Long scratchUsed)
	{
		this.scratchUsed = scratchUsed;
	}
	public Long getScratchTotal()
	{
		if(scratchTotal!=null)
			return scratchTotal;
		else
			return 0l;	
	}
	public void setScratchTotal(Long scratchtotal)
	{
		this.scratchTotal = scratchtotal;
	}
	public Long getHomeUsed()
	{
		if(homeUsed!=null)
			return homeUsed;
		else
			return 0l;	
		
	}
	public void setHomeUsed(Long homeUsed)
	{
		this.homeUsed = homeUsed;
	}
	public Long getHomeTotal()
	{
		if(homeTotal!=null)
			return homeTotal;
		else
			return 0l;		
		
	}
	public void setHomeTotal(Long homeTotal)
	{
		this.homeTotal = homeTotal;
	}
	
	
}

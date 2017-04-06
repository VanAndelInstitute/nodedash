package org.vai.hpc.clusterdash.server;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.vai.hpc.clusterdash.client.ClusterData;
import org.vai.hpc.clusterdash.client.ClusterInfoService;
import org.vai.hpc.clusterdash.client.QuotaData;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ClusterInfoServiceImpl extends RemoteServiceServlet implements ClusterInfoService
{

	@Override
	public ArrayList<ClusterData> getCluster()
	{
		ArrayList<ClusterData> ret = new ArrayList<ClusterData>();
		try
		{
			
			URL url = new URL("http://login.hpc.vai.org/pbsnodezTxt.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = null;
			int j=0;
			ArrayList<Double> loadHist = new ArrayList<Double>();
			ArrayList<Integer> diskHist = new ArrayList<Integer>();
			HashMap<String,Integer> qstat = queueStat();
			
			while((line = in.readLine()) != null) 
			{
				if(j==0)
				{
					String[] loadHistStr = line.split("\\s+");
					for(String s : loadHistStr)
						loadHist.add(Double.parseDouble(s));
				}
				else if (j==1)
				{
					String[] diskHistStr = line.split("\\s+");
					for(String s : diskHistStr)
						diskHist.add(Integer.parseInt(s));
				}
				else
				{
					try{
						ClusterData d = new ClusterData();
						String[] parts = line.split("\\s+");
						d.setNodeName(parts[0]);
						d.setCoresAvail(Integer.parseInt(parts[1]));
						d.setKilobytesDiskActivtiy(Integer.parseInt(parts[2]));
						d.setLoad(Double.parseDouble(parts[3]));
						d.setQueueStat(qstat);
						
						if(parts.length > 4)
						{
							String[] users = parts[4].split(",");
							d.setUsers(new ArrayList<String>(Arrays.asList(users)));
						
							String[] coresUsedStr = parts[5].split(",");
							ArrayList<Integer> coresUsed = new ArrayList<Integer>();
							for(String str : coresUsedStr)
								coresUsed.add(Integer.parseInt(str));
							d.setCoresUsed(coresUsed);
							
							String[] jobsIdsStr = parts[6].split(",");
							ArrayList<Integer> jobsIds = new ArrayList<Integer>();
							for(String str : jobsIdsStr)
								jobsIds.add(Integer.parseInt(str));
							d.setJobIds(jobsIds);
						}
						ret.add(d);
					}
					catch(Exception e) {}
				}
				j++;
			}
			if(ret.size()>1)
			{
				ret.get(0).setDiskHistory(diskHist);
				ret.get(0).setLoadHistory(loadHist);
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	HashMap<String,Integer> queueStat()
	{
		URL url;
		HashMap<String,Integer> count = new HashMap<String,Integer>();
		count.put("R", 0);
		count.put("Q", 0);
		count.put("H", 0);
		count.put("C", 0);
		count.put("E", 0);
		try
		{
			url = new URL("http://login.hpc.vai.org/qstat.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = null;
			while((line = in.readLine()) != null) 
			{
				String[] fields = line.split("\\s+");
				count.put(fields[9], count.get(fields[9]) + 1);
				
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	
	}

	@Override
	public ArrayList<QuotaData> getQuota()
	{
		ArrayList<QuotaData> ret = new ArrayList<QuotaData>();
		HashMap<String,QuotaData> quotaMap = new HashMap<String,QuotaData>();
		try
		{
			URL url = new URL("http://login.hpc.vai.org/quotatable.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = null;
			while((line = in.readLine()) != null) 
			{
				QuotaData d;
				String[] parts = line.split("\\s+");
				if(quotaMap.containsKey(parts[0]))
					d = quotaMap.get(parts[0]);
				else
					d = new QuotaData();
				
				if(parts[1].contains("atch"))
				{
					d.setFileset(parts[0]);
					try {
						d.setScratchUsed(Long.parseLong(parts[3]));
						d.setScratchTotal(Long.parseLong(parts[5]));
					} catch (Exception e)
					{
						d.setScratchUsed(0l);
						d.setScratchTotal(0l);
					}
				}
				else if(parts[1].contains("ome"))
				{
					d.setFileset(parts[0]);
					try {
						d.setHomeUsed(Long.parseLong(parts[3]));
						d.setHomeTotal(Long.parseLong(parts[5]));
					} catch (Exception e)
					{
						d.setHomeUsed(0l);
						d.setHomeTotal(0l);
					}
				}
				quotaMap.put(parts[0], d);
				
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.addAll(quotaMap.values());
		Collections.sort(ret, new Comparator<QuotaData>(){

			@Override
			public int compare(QuotaData o1, QuotaData o2)
			{
				return o2.getFileset().compareTo(o1.getFileset());
			}});
		return ret;
	}


}

package org.vai.hpc.clusterdash.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.vai.hpc.clusterdash.client.ClusterData;
import org.vai.hpc.clusterdash.client.ClusterInfoService;
import org.vai.hpc.clusterdash.shared.FieldVerifier;
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

			while((line = in.readLine()) != null) 
			{
				ClusterData d = new ClusterData();
				String[] parts = line.split("\\s+");
				d.setNodeName(parts[0]);
				d.setCoresAvail(Integer.parseInt(parts[1]));
				d.setLoad(Double.parseDouble(parts[2]));
				
				if(parts.length > 3)
				{
					String[] users = parts[3].split(",");
					d.setUsers(new ArrayList<String>(Arrays.asList(users)));
				
					String[] coresUsedStr = parts[4].split(",");
					ArrayList<Integer> coresUsed = new ArrayList<Integer>();
					for(String str : coresUsedStr)
						coresUsed.add(Integer.parseInt(str));
					d.setCoresUsed(coresUsed);
					
					String[] jobsIdsStr = parts[5].split(",");
					ArrayList<Integer> jobsIds = new ArrayList<Integer>();
					for(String str : jobsIdsStr)
						jobsIds.add(Integer.parseInt(str));
					d.setJobIds(jobsIds);
				}
				ret.add(d);
				
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}


}

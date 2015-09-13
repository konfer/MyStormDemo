import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;
import clojure.main;

public class CountedTopology {

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		TopologyBuilder builder=new TopologyBuilder();
		
		builder.setSpout("randomCounted", new CountedSpout(), 10);
		builder.setBolt("randomCountedBolt", new CountedBolt(), 2).shuffleGrouping("randomCounted");
		
		Config conf=new Config();
		conf.setDebug(true);
		
		if (args!=null && args.length>0)
		{
			conf.setNumWorkers(3);
			
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		}
		else
		{
			LocalCluster cluster=new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology("test");
			cluster.shutdown();
		}
		
	}

}

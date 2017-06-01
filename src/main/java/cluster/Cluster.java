package cluster;

import util.PropertyHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author : Lknight
 * @Date : 2017/6/1
 * @Description : TODO
 * @Version : v1.0
 */
public class Cluster{
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        worker();
    }

    public static void worker(){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Integer.parseInt(PropertyHandler.getProperty("ThreadPoolCoreSize")));
        for(ResourceType type : ResourceType.values()){
            executorService.scheduleAtFixedRate(
                    new ClusterResource(type,Integer.parseInt(PropertyHandler.getProperty("resourceSpanDay"))),
                    0,
                    Integer.parseInt(PropertyHandler.getProperty("clusterScheduleDay")),
                    TimeUnit.DAYS);
        }
    }
}

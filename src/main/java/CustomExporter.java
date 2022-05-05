package main.java;

import io.prometheus.client.exporter.HTTPServer;
import main.java.chcollector.*;
import main.java.mecollector.SqlFailCountCollecter;
import main.java.mecollector.SqlTime1Collector;
import main.java.mecollector.SqlTimeCollector;
import main.java.rocollector.RpcByteCollector;
import main.java.rocollector.TableCollector;
import main.java.upcollector.*;

import java.io.IOException;

public class CustomExporter {
    public static void main(String[] args) throws IOException {
        new TpsCollecter().register();
        new LockWaitTimeCollecter().register();
        new TranCollector().register();
        new QpsCollecter().register();
        new LockFailCountCollecter().register();
        new TranQueueCollector().register();
        new LongWaitPacketCollector().register();

        new TableCollector().register();
        new RpcByteCollector().register();

        new SqlTimeCollector().register();
        new SqlTime1Collector().register();
        new SqlFailCountCollecter().register();

        new CachehitCollector().register();
        new GSTimeCollector().register();
        new SStableReadCollector().register();
        new SStableWriteCollector().register();
        new SStableCacheCollector().register();

        ReadPropertiesUtil rpu = new ReadPropertiesUtil("port");
        String port = rpu.getPropertys("port");
        HTTPServer server = new HTTPServer(Integer.valueOf(port));
    }
}
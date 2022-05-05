package main.java.upcollector;

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import main.java.connection.ConnectionGeter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//接收包时，等待时间过长平均每秒包的数量(这里只计算当前超时包数)
public class LongWaitPacketCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ups_pakcet_long_wait_count";
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'pakcet_long_wait_count'";
        Long pakcet_long_wait_count =  new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()) {
                pakcet_long_wait_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // With no labels.
        CounterMetricFamily packet_metric = new CounterMetricFamily(metricName, "The average number of packets waiting too long per second",pakcet_long_wait_count);
        mfs.add(packet_metric);
        return mfs;
    }
}

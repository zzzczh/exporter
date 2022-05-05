package main.java.chcollector;

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.GaugeMetricFamily;
import main.java.connection.ConnectionGeter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//平均get/scan每次执行时间,统计每个datasvr的总和
public class GSTimeCollector extends Collector{
    public List<Collector.MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<Collector.MetricFamilySamples>();
        String metricName = "ch_gs_execution_time";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'get_count'";
        Long get_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                get_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'scan_count'";
        Long scan_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                scan_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'get_time'";
        Long get_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                get_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'scan_time'";
        Long scan_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                scan_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // With labels
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily(metricName, "get and scan average execution time in chunkserver", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("get"), get_time/get_count);
        labeledGauge.addMetric(Arrays.asList("scan"), scan_time/scan_count);

        mfs.add(labeledGauge);
        return mfs;
    }
}

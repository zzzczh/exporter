package main.java.upcollector;

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import main.java.connection.ConnectionGeter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//事务的各种时间指标
public class TranCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ups_tran_times";
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_count'";
        Long trans_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()){
                trans_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_ctime'";
        Long trans_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()){
                trans_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_ftime'";
        Long trans_ftime = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()){
                trans_ftime += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_time'";
        Long trans_ctime = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()){
                trans_ctime += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // With labels
        CounterMetricFamily labeledGauge = new CounterMetricFamily(metricName, "some times about transaction in updataServer", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("Average waiting time for submission"), trans_ctime/trans_count);
        labeledGauge.addMetric(Arrays.asList("Average waiting time for flus log"), trans_ftime/trans_count);
        labeledGauge.addMetric(Arrays.asList("Average time for execution"), trans_time/trans_count);
        mfs.add(labeledGauge);
        return mfs;
    }
}

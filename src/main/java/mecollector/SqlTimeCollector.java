package main.java.mecollector;

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
//每秒sql执行次数
public class SqlTimeCollector  extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "up_sql_time";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_select_count'";
        Long sql_select_count = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            sql_select_count = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_insert_count'";
        Long sql_insert_count = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            sql_insert_count = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_update_count'";
        Long sql_update_count = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            sql_update_count = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_delete_count'";
        Long sql_delete_count = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            sql_delete_count = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CounterMetricFamily labeledGauge = new CounterMetricFamily(metricName, "the count of sql", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("select"), sql_select_count);
        labeledGauge.addMetric(Arrays.asList("insert"), sql_insert_count);
        labeledGauge.addMetric(Arrays.asList("delete"), sql_delete_count);
        labeledGauge.addMetric(Arrays.asList("update"), sql_update_count);
        mfs.add(labeledGauge);
        return mfs;

    }
}

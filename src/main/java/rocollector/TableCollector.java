package main.java.rocollector;

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
//表数行数数据量大小
public class TableCollector  extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();

        String metricName = "table_count";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaoadminsvr' and  name = 'all_table_count'";
        Long all_table_count = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            all_table_count = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaoadminsvr' and  name = 'all_row_count'";
        Long all_row_count = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            all_row_count = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaoadminsvr' and  name = 'all_data_size'";
        Long all_data_size = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            all_data_size = Long.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily(metricName, "the Statistical information of all table", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("table"), all_table_count);
        labeledGauge.addMetric(Arrays.asList("row"), all_row_count);
        labeledGauge.addMetric(Arrays.asList("data"),all_data_size);
        mfs.add(labeledGauge);
        return mfs;
    }
}

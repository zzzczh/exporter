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

public class QpsCollecter extends Collector {
    //1.Collector.MetricFamilySamples是一个指标,可能包含多个labelset不同的时间序列所以是list
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ups_qps_count";
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'get_count'";
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
        sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'scan_count'";
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
        CounterMetricFamily qps_metric = new CounterMetricFamily(metricName, "qps of updataServer", scan_count+get_count);
        mfs.add(qps_metric);
        return mfs;
    }
}
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
//锁失败次数
public class LockFailCountCollecter extends Collector {
    public List<Collector.MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<Collector.MetricFamilySamples>();
        String metricName = "ups_lock_fail_count";
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'lock_fail_count'";
        Long lock_fail_count= new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()) {
                lock_fail_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // With no labels.
        CounterMetricFamily lock_metric = new CounterMetricFamily(metricName, "updataServer lock fail count\n" +
                "\n", lock_fail_count);
        mfs.add(lock_metric);
        return mfs;
    }
}

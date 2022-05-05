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

public class TpsCollecter extends Collector{
    //1.Collector.MetricFamilySamples是一个指标,可能包含多个labelset不同的时间序列所以是list
    public List<Collector.MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<Collector.MetricFamilySamples>();

        String metricName = "ups_trans_count";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_count'";
        Long trans_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                trans_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//            With labels
//            CounterMetricFamily labeledGauge = new CounterMetricFamily(metricName, "the qps of updata server", Arrays.asList("labelname"));
//            labeledGauge.addMetric(Arrays.asList("foo"), 4);
//            labeledGauge.addMetric(Arrays.asList("bar"), 5);
        // With no labels.
        CounterMetricFamily tps_metric = new CounterMetricFamily(metricName, "Number of transactions executed by updataServer", trans_count);
        mfs.add(tps_metric);
        return mfs;
    }
}

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
//UPS提交队列事务数量
//UPS响应队列事务数量
public class TranQueueCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ups_tran_queue_count";
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_commit_queued_count'";
        Long trans_commit_queued_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                trans_commit_queued_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaotxnsvr' and  name = 'trans_response_queued_count'";
        Long trans_response_queued_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                trans_response_queued_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CounterMetricFamily queue_metric = new CounterMetricFamily(metricName, "Number of queued transactions\n" , Arrays.asList("type"));
        queue_metric.addMetric(Arrays.asList("commit queue"), trans_commit_queued_count);
        queue_metric.addMetric(Arrays.asList("response queue"), trans_response_queued_count);
        mfs.add(queue_metric);
        return mfs;
    }
}

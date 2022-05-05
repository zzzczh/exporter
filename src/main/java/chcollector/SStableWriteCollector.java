package main.java.chcollector;

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

public class SStableWriteCollector extends Collector{
    public List<Collector.MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<Collector.MetricFamilySamples>();
        String metricName = "ch_sstable_write_info";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'sstable_disk_io_write_num'";
        Long sstable_disk_io_write_num = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            int size = 0;
            while (rs.next()) {
                sstable_disk_io_write_num += Long.valueOf(rs.getString(col));
                size++;
            }
            sstable_disk_io_write_num = sstable_disk_io_write_num / size;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'sstable_disk_io_write_bytes'";
        Long sstable_disk_io_write_bytes = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            int size = 0;
            while (rs.next()) {
                sstable_disk_io_write_bytes += Long.valueOf(rs.getString(col));
                size++;
            }
            sstable_disk_io_write_bytes = sstable_disk_io_write_bytes / size;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // With labels
        CounterMetricFamily labeledGauge = new CounterMetricFamily(metricName, "some write info about sstable in chunkserver", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("io counts to disk"), sstable_disk_io_write_num);
        labeledGauge.addMetric(Arrays.asList("io bytes to disk"), sstable_disk_io_write_bytes);
        mfs.add(labeledGauge);
        return mfs;
    }
}

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

//平均每个chunksever从磁盘读sstable文件的次数
//平均每个chunksever从磁盘读sstable文件的总字节数
public class SStableReadCollector extends Collector {
    public List<Collector.MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<Collector.MetricFamilySamples>();
        String metricName = "ch_sstable_read_info";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'sstable_disk_io_num'";
        Long io_nums = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            int size = 0;
            while (rs.next()) {
                io_nums += Long.valueOf(rs.getString(col));
                size++;
            }
            io_nums = io_nums / size;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'sstable_disk_io_bytes'";
        Long io_bytes = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            int size = 0;
            while (rs.next()) {
                io_bytes += Long.valueOf(rs.getString(col));
                size++;
            }
            io_bytes = io_bytes / size;
        } catch (SQLException e) {
            e.printStackTrace();
        }



        // With labels
        CounterMetricFamily labeledGauge = new CounterMetricFamily(metricName, "some read info about sstable in chunkserver", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("io counts from disk"), io_nums);
        labeledGauge.addMetric(Arrays.asList("io bytes from disk"), io_bytes);

        mfs.add(labeledGauge);
        return mfs;
    }
}
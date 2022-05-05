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
//sstable的缓存命中率
public class SStableCacheCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ch_sstable_cache_hit";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'sstable_row_cache_hit'";
        Long sstable_row_cache_hit = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sstable_row_cache_hit += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'sstable_row_cache_miss'";
        Long sstable_row_cache_miss = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sstable_row_cache_miss += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GaugeMetricFamily labeledGauge = null;
        if(sstable_row_cache_hit+sstable_row_cache_miss==0){
             labeledGauge = new GaugeMetricFamily(metricName, "cache hit about sstable in chunkserver",0);
        }else {
            double cache_hit_percent=sstable_row_cache_hit/(sstable_row_cache_hit+sstable_row_cache_miss);
             labeledGauge = new GaugeMetricFamily(metricName, "cache hit about sstable in chunkserver",cache_hit_percent);
        }
        mfs.add(labeledGauge);
        return mfs;
    }
}

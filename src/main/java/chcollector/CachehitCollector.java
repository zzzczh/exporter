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
//缓存命中率
public class CachehitCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ch_cache_hit";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'block_index_cache_hit'";
        Double block_index_cache_hit = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            block_index_cache_hit = Double.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'block_cache_hit'";
        Double block_cache_hit = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            block_cache_hit = Double.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'block_cache_miss'";
        Double block_cache_miss = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            block_cache_miss = Double.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaodatasvr' and  name = 'block_index_cache_miss'";
        Double block_index_cache_miss = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            rs.next();
            block_index_cache_miss = Double.valueOf(rs.getString(col));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GaugeMetricFamily labeledGauge = new GaugeMetricFamily(metricName, "the cache hit of block", Arrays.asList("type"));
        //对于总访问次数为0的边界特处
        if(block_cache_hit+block_cache_miss != 0){
            double block_percent=block_cache_hit/(block_cache_hit+block_cache_miss);
            labeledGauge.addMetric(Arrays.asList("Block Cache"),block_percent);
        }else {
            labeledGauge.addMetric(Arrays.asList("Block Cache"), 0);
        }
        if((block_index_cache_hit+block_index_cache_miss) != 0){
            double block_index_percent=block_index_cache_hit/(block_index_cache_hit+block_index_cache_miss);
            labeledGauge.addMetric(Arrays.asList("Block Index Cache"), block_index_percent);
        }else {
            labeledGauge.addMetric(Arrays.asList("Block Index Cache"), 0);
        }
        mfs.add(labeledGauge);
        return mfs;
    }
}

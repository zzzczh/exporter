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

//每个sql平均执行用时
public class SqlTime1Collector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "up_sql_time_1";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_select_count'";
        Long sql_select_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_select_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_insert_count'";
        Long sql_insert_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_insert_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_update_count'";
        Long sql_update_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_update_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_delete_count'";
        Long sql_delete_count = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_delete_count += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_select_time'";
        Long sql_select_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_select_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_insert_time'";
        Long sql_insert_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_insert_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_update_time'";
        Long sql_update_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_update_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "select * from __all_server_stat where svr_type ='yaosqlsvr' and  name = 'sql_delete_time'";
        Long sql_delete_time = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                sql_delete_time += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        GaugeMetricFamily labeledGauge = new GaugeMetricFamily(metricName, "the average time of sql", Arrays.asList("type"));

        if(sql_select_count!=0){
           labeledGauge.addMetric(Arrays.asList("select"), sql_select_time / sql_select_count);
        }else {
            labeledGauge.addMetric(Arrays.asList("select"), 0);
        }
        if (sql_insert_count != 0){
            labeledGauge.addMetric(Arrays.asList("insert"), sql_insert_time / sql_insert_count);
        }else{
            labeledGauge.addMetric(Arrays.asList("insert"), 0);
        }
        if (sql_delete_count != 0){
            labeledGauge.addMetric(Arrays.asList("delete"), sql_delete_time/sql_delete_count);
        }else{
            labeledGauge.addMetric(Arrays.asList("delete"), 0);
        }
        if (sql_update_count != 0){
            labeledGauge.addMetric(Arrays.asList("update"), sql_update_time/sql_update_count);
        }else{
            labeledGauge.addMetric(Arrays.asList("update"), 0);
        }
        mfs.add(labeledGauge);
        return mfs;

    }
}

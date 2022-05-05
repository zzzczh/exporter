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
//平均接收数据包的总字节数
//平均发送数据包的总字节数
public class RpcByteCollector extends Collector {
    @Override
    public List<MetricFamilySamples> collect() {
        List<Collector.MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        String metricName = "ro_rpc_byte";
        //2.Your code to get metrics
        Connection conn = ConnectionGeter.getConnection();
        PreparedStatement pstmt;
        String sql = "select * from __all_server_stat where svr_type ='yaoadminsvr' and  name = 'rpc_bytes_in'";
        Long rpc_bytes_in = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                rpc_bytes_in += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select * from __all_server_stat where svr_type ='yaoadminsvr' and  name = 'rpc_bytes_out'";
        Long rpc_bytes_out = new Long(0);
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            while(rs.next()){
                rpc_bytes_out += Long.valueOf(rs.getString(col));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CounterMetricFamily labeledGauge = new CounterMetricFamily(metricName, "Total bytes of rpc packets", Arrays.asList("type"));
        labeledGauge.addMetric(Arrays.asList("in"), rpc_bytes_in);
        labeledGauge.addMetric(Arrays.asList("out"), rpc_bytes_out);

        mfs.add(labeledGauge);
        return mfs;
    }
}

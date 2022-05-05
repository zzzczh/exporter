package main.java;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 专门用于读取配置文件,提供访问配置文件的方法。
 * 文件内容为键值对形式，格式：key=value
 *
 */
public class ReadPropertiesUtil {

    private InputStream ins = null;

    private Properties pro = null;

    public ReadPropertiesUtil() {
    }

    /**
     * @param fileName 文件名
     */
    public ReadPropertiesUtil(String fileName) {
        // 配置文件所在路经
        String name = fileName + ".properties";
        ins = ReadPropertiesUtil.class.getClassLoader().getResourceAsStream(name);
        BufferedReader bf = new BufferedReader(new InputStreamReader(ins));
        pro = new Properties();
        try {
            pro.load(bf);
        } catch (IOException e) {
            System.out.println(name + "文件没有找到,请检查路径！");
            e.printStackTrace();
        }
    }

    /**
     * 通过key查询相应的value
     *
     * @param key
     * @return
     */
    public String getPropertys(String key) {
        return pro.getProperty(key);
    }

    /**
     * 返回键值对大小
     *
     * @return
     */
    public int size() {
        return pro.size();
    }

    /**
     * 返回所有键值对
     *
     * @return
     */
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<Object, Object> m : pro.entrySet()) {
            map.put(m.getKey().toString(), m.getValue().toString());
        }
        return map;
    }

}

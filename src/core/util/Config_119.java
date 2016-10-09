package core.util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.forestry.dao.sys.impl.OperateDaoImpl;
 
/**
 * 
 * Class Name: Config.java Function:配置文件类 Modifications:
 * 
 * @author TongXin DateTime 2013年8月23日 下午2:29:31
 * @version 1.0
 */
public class Config_119 {
	static Logger logger = Logger.getLogger(Config_119.class.getName());
	public String CONFIGURE_FILE = null;//Config.class.getClassLoader().getResource("/").getPath()+  "/common.properties";
	  
	public static int PORT;//socket服务器端口
	public static String HOST;//短信服务器ip
 
	public static int GPS_START_TIME;//gps请求任务启动时间
	public static int GPS_INTERVAL_TIME;//gps请求任务间隔时间
	public static int BUSSINESS_START_TIME;//策略请求任务启动时间
	public static int BUSSINESS_INTERVAL_TIME;//策略请求任务间隔时间
	public static int MAP_START_TIME;//地图下载请求任务启动时间
	public static int MAP_INTERVAL_TIME;//地图下载请求任务间隔时间
	public static int GPS_DATA_SIZE;//GPS上传数据大小
	public static int TACTICS_DATA_SIZE;//收费策略更新数据大小
	public static int SO_OUT_TIME;//超时连接
	public static int CONNECTION_TIMEOUT;//超时
	public static String EVENT_LOG_URL;//事件日志记录URL
	public static String EVENT_GPS_URL;//GPS请求URL
	public static String EVENT_BUSSINESS_URL;//策略请求URL
	public static String EVENT_MAP_URL;//地图下载请求URL
	public static String DEVICE;//设备标识
	public static String PING_IP;//ping IP
	
	public static String HTTP_DOWNLOAD_SIZE;//http下载文件大小
	public static String HTTP_DOWNLOAD_URL;//http存储路径
	public static String TCP_DOWNLOAD_URL;//tcp存储路劲
	
	public static String DRIVER;
	public static String URL;
	public static String USERNAME;
	public static String PASSWORD;
	
	public static String READ_FILE_ADDRESS;//读取文件地址
	public static String READ_DOUBLE_FILE1;//读取文件地址
	public static String READ_DOUBLE_FILE2;//读取文件地址
	public static String READ_XML_FILE;//读取XML文件地址
	 
	
	private static Config_119 CONFIG_OBJ = null;

	Config_119() throws Exception {
		Properties prop = new Properties();
		CONFIGURE_FILE = Config_119.class.getClassLoader().getResource("/").getPath()+  "/common_119.properties";
		InputStream inputStream = new FileInputStream(CONFIGURE_FILE);
		BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
		prop.load(bf);

		HOST = prop.getProperty("host");
	/*	if (null == HOST || "".equals(HOST)) {
			throw new Exception("未配置参数(host)");
		}
		
	
		
		 String temp = prop.getProperty("connection_timeout");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(connection_timeout)");
		}
		CONNECTION_TIMEOUT=Integer.parseInt(temp);
		
		temp = prop.getProperty("so_out_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(so_out_time)");
		}
		SO_OUT_TIME=Integer.parseInt(temp);
		
		EVENT_LOG_URL = prop.getProperty("event_log_url");
		if (null == EVENT_LOG_URL || "".equals(EVENT_LOG_URL)) {
			throw new Exception("未配置参数(event_log_url)");
		}
		
		EVENT_GPS_URL = prop.getProperty("event_gps_url");
		if (null == EVENT_GPS_URL || "".equals(EVENT_GPS_URL)) {
			throw new Exception("未配置参数(event_gps_url)");
		}
		
		PING_IP = prop.getProperty("ping_ip");
		if (null == PING_IP || "".equals(PING_IP)) {
			throw new Exception("未配置参数(ping_ip)");
		}
		
		EVENT_BUSSINESS_URL = prop.getProperty("event_bussiness_url");
		if (null == EVENT_BUSSINESS_URL || "".equals(EVENT_BUSSINESS_URL)) {
			throw new Exception("未配置参数(event_bussiness_url)");
		}
		
		EVENT_MAP_URL = prop.getProperty("event_map_url");
		if (null == EVENT_BUSSINESS_URL || "".equals(EVENT_MAP_URL)) {
			throw new Exception("未配置参数(event_map_url)");
		}
		
		DEVICE = prop.getProperty("device");
		if (null == DEVICE || "".equals(DEVICE)) {
			throw new Exception("未配置参数(device)");
		}
		
		HTTP_DOWNLOAD_SIZE = prop.getProperty("http_download_size");
		if (null == HTTP_DOWNLOAD_SIZE || "".equals(HTTP_DOWNLOAD_SIZE)) {
			throw new Exception("未配置参数(http_download_size)");
		}
		
		HTTP_DOWNLOAD_URL = prop.getProperty("http_download_url");
		if (null == HTTP_DOWNLOAD_URL || "".equals(HTTP_DOWNLOAD_URL)) {
			throw new Exception("未配置参数(http_download_url)");
		}
		
		TCP_DOWNLOAD_URL = prop.getProperty("tcp_download_url");
		if (null == TCP_DOWNLOAD_URL || "".equals(TCP_DOWNLOAD_URL)) {
			throw new Exception("未配置参数(tcp_download_url)");
		}
		
		READ_FILE_ADDRESS = prop.getProperty("read_file_address");
		if (null == READ_FILE_ADDRESS || "".equals(READ_FILE_ADDRESS)) {
			throw new Exception("未配置参数(read_file_address)");
		}
		
		READ_DOUBLE_FILE1 = prop.getProperty("read_double_file1");
		if (null == READ_DOUBLE_FILE1 || "".equals(READ_DOUBLE_FILE1)) {
			throw new Exception("未配置参数(read_double_file1)");
		}
		
		READ_DOUBLE_FILE2 = prop.getProperty("read_double_file2");
		if (null == READ_DOUBLE_FILE2 || "".equals(READ_DOUBLE_FILE2)) {
			throw new Exception("未配置参数(read_double_file2)");
		}
		
		
		temp = prop.getProperty("port");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(port)");
		}
		PORT=Integer.parseInt(temp);
		
		temp = prop.getProperty("gps_start_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(gps_start_time)");
		}
		GPS_START_TIME=Integer.parseInt(temp);
		
		temp = prop.getProperty("gps_interval_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(gps_interval_time)");
		}
		GPS_INTERVAL_TIME=Integer.parseInt(temp);
		
		temp = prop.getProperty("bussiness_start_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(bussiness_start_time)");
		}
		BUSSINESS_START_TIME=Integer.parseInt(temp);
		
		temp = prop.getProperty("bussiness_interval_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(bussiness_interval_time)");
		}
		BUSSINESS_INTERVAL_TIME=Integer.parseInt(temp);
		
		temp = prop.getProperty("map_start_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(map_start_time)");
		}
		MAP_START_TIME=Integer.parseInt(temp);
		
		temp = prop.getProperty("map_interval_time");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(map_interval_time)");
		}
		MAP_INTERVAL_TIME=Integer.parseInt(temp);
		
		temp = prop.getProperty("gps_data_size");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(gps_data_size)");
		}
		GPS_DATA_SIZE=Integer.parseInt(temp);
		
		temp = prop.getProperty("tactics_data_size");
		if (null == temp || "".equals(temp)) {
			throw new Exception("未配置参数(tactics_data_size)");
		}
		TACTICS_DATA_SIZE=Integer.parseInt(temp);
		
		
		*/
		
		/*READ_XML_FILE = prop.getProperty("read_xml_file");
		if (null == READ_XML_FILE || "".equals(READ_XML_FILE)) {
			throw new Exception("未配置参数(read_xml_file)");
		}*/
		
		URL = prop.getProperty("url");
		DRIVER = prop.getProperty("driverClassName");
		if (null == DRIVER || "".equals(DRIVER)) {
			throw new Exception("未配置参数(driverClassName)");
		}
		if (null == URL || "".equals(URL)) {
			throw new Exception("未配置参数(url)");
		}
		
		USERNAME = prop.getProperty("username");
		if (null == USERNAME || "".equals(USERNAME)) {
			throw new Exception("未配置参数(username)");
		}
		
		PASSWORD = prop.getProperty("password");
		if (null == PASSWORD || "".equals(PASSWORD)) {
			throw new Exception("未配置参数(password)");
		}
	}

 
	
	/**
	 * 
	 * Function:
	 * 
	 * @author TongXin DateTime 2013年8月23日 下午2:31:24
	 * @return
	 */
	public static Config_119 getInstance() {
		if (null == CONFIG_OBJ) {
			try {
				CONFIG_OBJ = new Config_119();
			} catch (Exception e) {
				e.printStackTrace();
				//LogUtil.log("error",logger, e.getMessage());
			}
		}

		return CONFIG_OBJ;
	}

}

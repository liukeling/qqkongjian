package comm;

import java.io.IOException;
import java.util.Properties;


public class dbvalue {
	public static String getValue(String key){
		String value = "";
		Properties prop = new Properties();
		try {
			prop.load(dbvalue.class
					.getResourceAsStream("/comm/db.properties"));
			value = (String) prop.get(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
}

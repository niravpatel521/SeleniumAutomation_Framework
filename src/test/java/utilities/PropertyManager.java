package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author shwetankvashishtha
 */

public class PropertyManager {

	public Properties getResourceBundle;

	public PropertyManager() {
		try {
			File pageSrc = new File("./resources/page.properties");
			FileInputStream pageFIS = new FileInputStream(pageSrc);
			getResourceBundle = new Properties();

			File baseSrc = new File("./resources/base.properties");
			FileInputStream baseFIS = new FileInputStream(baseSrc);
			getResourceBundle = new Properties();

			getResourceBundle.load(pageFIS);
			getResourceBundle.load(baseFIS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Print all key value pairs
	 */

	public void printKeyValue() {
		Enumeration KeyValues = getResourceBundle.keys();
		while (KeyValues.hasMoreElements()) {
			String key = (String) KeyValues.nextElement();
			String value = getResourceBundle.getProperty(key);
			System.out.println(key + ":- " + value);
		}
	}
}

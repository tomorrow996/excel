package com.cd.tech.report.util;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public class PropertiesUtil {
	
	private static Logger log = Logger.getLogger(PropertiesUtil.class);
	
	private static String configFile = null;
	
	static{
		configFile = System.getProperty("chem-config");
		if(StringUtils.isBlank(configFile)){
			configFile = "config.properties";
		}
		log.info(PropertiesUtil.getStringValue("prop.flag"));
	}
	
    /** 
     * 获取某个properties文件中的某个key对应的value值 
     * @param fileName 
     * @param key 
     * @return 返回key说对应的value值 
     * */  
    public static String getStringValue(String fileName, String key,String defaultValue){
    	String value="";
		CompositeConfiguration config = new CompositeConfiguration();
        PropertiesConfiguration pc = null;
        try {  
            pc = new PropertiesConfiguration(fileName);  
            config.addConfiguration(pc);  
            value = config.getString(key,defaultValue).trim();  
        } catch (ConfigurationException e) {  
        	log.error(e);
        } finally {
        	return value;  
        } 
    }  
    public static String getStringValue(String key,String defaultValue){  
        return PropertiesUtil.getStringValue(configFile, key,defaultValue);  
    }
    public static String getStringValue(String key){  
        return PropertiesUtil.getStringValue(key,"");  
    }
	public static int getIntValue(String fileName, String key,int defaultValue){
		int value=0;
		CompositeConfiguration config = new CompositeConfiguration();
    	PropertiesConfiguration pc = null;
    	value=0;
    	try {  
    		pc = new PropertiesConfiguration(fileName);  
    		config.addConfiguration(pc);  
    		value = config.getInt(key, defaultValue);
    	} catch (ConfigurationException e) {  
    		e.printStackTrace();  
    	} finally {
    		return value;  
    	} 
    }
    public static int getIntValue(String key,int defaultValue){  
    	return PropertiesUtil.getIntValue(configFile, key, defaultValue);  
    }
    public static int getIntValue(String key){  
    	return PropertiesUtil.getIntValue(key, 0);  
    }
    /** 
     * 获取某个properties文件中的某个key对应的value值(值是个数组) 
     * @param fileName 
     * @param key 
     * @param delimiter 
     * @return 返回key说对应的value数组值(使用时遍历数组值后要加.trim()) 
     * */  
    public static String[] getPropertiesValues(String fileName, String key, char delimiter){  
        CompositeConfiguration config = new CompositeConfiguration();  
        PropertiesConfiguration pc = null;  
            if(!Character.isWhitespace(delimiter)){  
               AbstractConfiguration.setDefaultListDelimiter(delimiter);  
            }  
            try {
				pc = new PropertiesConfiguration(fileName);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}  
            config.addConfiguration(pc);  
  
            String[] filevalues = config.getStringArray(key);  
            return filevalues;  
    } 
    
    public static void main(String[] args) {
    	String fileName = "D://config.properties";
    	String key = "prop.flag";
    	String value = "";
    	CompositeConfiguration config = new CompositeConfiguration();
        PropertiesConfiguration pc = null;
        try {  
            pc = new PropertiesConfiguration(fileName);  
            config.addConfiguration(pc);  
            value = config.getString(key,"").trim();  
        } catch (ConfigurationException e) {  
        	log.info(e);
        }
        System.out.println(value);
	}
}
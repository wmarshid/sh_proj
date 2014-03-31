package graspvis.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton Object to store data within the application. 
 * This object uses {@code Map} to store data as key and value pair.
 * @author nauval
 *
 */
public class Session {
	
	/**
	 * Sets of default keys that are used to store data in the session
	 */
	public enum Key {SOURCE_FILE, VIEW_FILE, VIEW_FILES, VIEW_CONFIGURATION}
	
	private Map<Object, Object> map;
	private Map<Object, Object[]> viewmap;
	
	private static final Session INSTANCE = new Session();
	
	private Session() {
		map = new HashMap<Object, Object>();
		viewmap = new HashMap<Object, Object[]>();
	}
	
	/**
	 * Gets the current session of the application
	 * @return the instance of the current {@code Session}
	 */
	public static Session getSession() {
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(Object key, Object value) {
		map.put(key, value);
	}
	
	
	public Object get(Object key) {
		return map.get(key);
	}
	
	public void remove(Object key) {
		map.remove(key);
	}
}

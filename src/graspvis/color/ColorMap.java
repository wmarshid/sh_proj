package graspvis.color;

import java.util.HashMap;
import java.util.Map;
/**
 * Maps object and colour
 * @author nauval
 *
 */
public class ColorMap {
	
	/**
	 * Unsynchronised map
	 */
	private Map<String, Integer> map;
	
	public ColorMap() {
		map = new HashMap<>();
	}
	
	public int getColor(Object key) {
		return map.get(key.toString()) == null? -1 : map.get(key.toString());
	}
	
	public int getColor(String key) {
		return map.get(key) == null? -1 : map.get(key);
	}
	
	public void setColor(Object key, int value) {
		map.put(key.toString(), value);
	}
	
	public void setColor(String key, int value) {
		map.put(key, value);
	}
	
	public boolean exist(int color) {
		return map.containsValue(color);
	}
	
	public boolean exist(Object key) {
		return map.containsKey(key);
	}
}

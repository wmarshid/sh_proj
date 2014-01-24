package graspvis.color;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BasicColorScheme implements ColorScheme {

	private Map<String, Integer> map;
	
	public BasicColorScheme() {
		map = new HashMap<>();
	}
	
	@Override
	public Collection<Integer> getColors() {
		// TODO Auto-generated method stub
		return map.values();
	}

	@Override
	public void add(String name, int color) {
		// TODO Auto-generated method stub
		map.put(name, color);
	}

	@Override
	public void remove(String name) {
		// TODO Auto-generated method stub
		map.remove(name);
	}

	@Override
	public int getRandomColor() {
		// TODO Auto-generated method stub
		int random = (int) Math.floor(Math.random() * map.size());
		Integer[] c = new Integer[map.size()];
		map.values().toArray(c);
		return c[random];
	}

	public boolean exist(int color) {
		return map.containsValue(color);
	}

	@Override
	public int get(String name) {
		// TODO Auto-generated method stub
		return map.get(name);
	}
}

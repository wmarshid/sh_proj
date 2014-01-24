package graspvis.color;

import java.util.Collection;

/**
 * Color scheme with capability to get random color
 * @author nauval
 *
 */
public interface ColorScheme {
	public Collection<Integer> getColors();
	public void add(String name, int color);
	public int get(String name);
	public void remove(String name);
	public int getRandomColor();
	public boolean exist(int color);
}

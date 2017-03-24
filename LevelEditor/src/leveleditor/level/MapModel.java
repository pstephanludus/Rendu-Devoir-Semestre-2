package leveleditor.level;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MapModel{

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	private char[][] map;

	private char defaultChar;

	private PropertyChangeSupport changeSupport;

	public MapModel(int columns, int rows, char defaultChar) {
		this.defaultChar = defaultChar;
		this.changeSupport = new PropertyChangeSupport(this);
		this.map = createEmptyMap(columns, rows);
	}

	public int getWidth() {
		return map[0].length;
	}

	public int getHeight() {
		return map.length;
	}

	public void setTile(int x, int y, char c) {
		map[y][x] = c;
		firePropertyChange();
	}

	public char getTile(int x, int y) {
		return map[y][x];
	}

	public char[][] getMap() {
		char[][] tmpMap = new char[map.length][map[0].length];
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				tmpMap[y][x] = map[y][x];
			}
		}
		return tmpMap;
	}

	public void expandMap(int n, int direction) {
		char[][] tmpMap;
		int northOffset = 0;
		int eastOffset = 0;
		int southOffset = 0;
		int westOffset = 0;
		
		if (direction == MapModel.NORTH) {
			northOffset = n;
		} else if (direction == MapModel.WEST) {
			westOffset = n;
		} else if (direction == MapModel.SOUTH) {
			southOffset = n;
		} else if (direction == MapModel.EAST) {
			eastOffset = n;
		} else {
			throw new IllegalArgumentException("Bad direction.");
		}

		int heightTmpMap = getWidth() + westOffset + eastOffset;
		int widthTmpMap = getHeight() + northOffset + southOffset;
		tmpMap = createEmptyMap(heightTmpMap, widthTmpMap);

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				tmpMap[y + northOffset][x + westOffset] = map[y][x];
			}
		}
		
		map = tmpMap;

		firePropertyChange();
	}

	public void fillMap(char[][] map, char character) {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				map[y][x] = character;
			}
		}
	}
	
	public String getMapAsString() {
		String s = "";
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				s += map[y][x];
			}
			s += "\n";
		}
		return s;
	}

	private char[][] createEmptyMap(int columns, int rows) {
		char[][] tmpMap = new char[rows][columns];
		fillMap(tmpMap, defaultChar);
		return tmpMap;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
	
	private void firePropertyChange() {
		changeSupport.firePropertyChange("model", false, true);
	}
}

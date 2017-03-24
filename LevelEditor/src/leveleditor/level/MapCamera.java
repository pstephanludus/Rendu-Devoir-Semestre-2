package leveleditor.level;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MapCamera{

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	private MapModel model;
	
	private Rectangle camera;

	private PropertyChangeSupport changeSupport;

	public MapCamera(int cols, int rows, int cameraWidth, int cameraHeight, char defaultChar) {
		this(cols, rows, cameraWidth, cameraHeight, 0, 0, defaultChar);
	}

	public MapCamera(int cols, int rows, int cameraWidth, int cameraHeight, int cameraX, int cameraY, char defaultChar) {
		checkValidCameraPosition(cols, rows, cameraWidth, cameraHeight, cameraX, cameraY);
		this.changeSupport = new PropertyChangeSupport(this);
		this.model = new MapModel(cols, rows, defaultChar);
		this.camera = new Rectangle(cameraX, cameraY, cameraWidth, cameraHeight);
	}

	public MapCamera(MapModel model, int cameraWidth, int cameraHeight) {
		this(model, cameraWidth, cameraHeight, 0, 0);
	}

	public MapCamera(MapModel model, int cameraWidth, int cameraHeight, int cameraX, int cameraY) {
		checkValidCameraPosition(model.getWidth(), model.getHeight(), cameraWidth, cameraHeight, cameraX, cameraY);
		this.changeSupport = new PropertyChangeSupport(this);
		this.model = model;
		this.camera = new Rectangle(cameraX, cameraY, cameraWidth, cameraHeight);
	}

	private void checkValidCameraPosition(int cols, int rows, int cameraWidth, int cameraHeight, int cameraX, int cameraY) {
		if (cameraWidth + cameraX > cols) {
			throw new IllegalArgumentException(
					"The camera width + camera x position > columns");
		} else if (cameraHeight + cameraY > rows) {
			throw new IllegalArgumentException(
					"The camera height + camera y position > rows");
		}
	}

	public int getWidth() {
		return camera.width;
	}

	public int getHeight() {
		return camera.height;
	}

	public int getX() {
		return camera.x;
	}

	public int getY() {
		return camera.y;
	}

	public int getModelWidth() {
		return model.getWidth();
	}

	public int getModelHeight() {
		return model.getHeight();
	}

	public void setTile(int x, int y, char c) {
		model.setTile(camera.x + x, camera.y + y, c);
		firePropertyChange("changedTile", new Point(x, y));
	}

	public char getTile(int x, int y) {
		return model.getTile(camera.x + x, camera.y + y);
	}

	public Rectangle getCamera() {
		return new Rectangle(camera);
	}

	public void moveCamera(int direction) {
		if (direction == MapCamera.NORTH) {
			if (camera.y > 0) {
				camera.setLocation(camera.x, --camera.y);
			}
		} else if (direction == MapCamera.EAST) {
			if (camera.x + camera.width < model.getWidth()) {
				camera.setLocation(++camera.x, camera.y);
			}
		} else if (direction == MapCamera.SOUTH) {
			if (camera.y + camera.height < model.getHeight()) {
				camera.setLocation(camera.x, ++camera.y);
			}
		} else if (direction == MapCamera.WEST) {
			if (camera.x > 0) {
				camera.setLocation(--camera.x, camera.y);
			}
		}

		firePropertyChange("movedCamera");
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
	
	private void firePropertyChange(String propertyName) {
		changeSupport.firePropertyChange(propertyName, false, true);
	}
	
	private void firePropertyChange(String propertyName, Object newValue) {
		changeSupport.firePropertyChange(propertyName, false, newValue);
	}
}

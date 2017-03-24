package leveleditor.editor;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import leveleditor.level.MapCamera;
import leveleditor.leveleditor.Constants;

public class CameraInformationLabel extends JLabel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private MapCamera camera;

	public CameraInformationLabel(MapCamera camera) {
		this.camera = camera;
		camera.addPropertyChangeListener(this);

		this.setText("Showing: 0 - " + Constants.GRID_WIDTH + "/"
				+ camera.getModelWidth() + ", 0 - " + Constants.GRID_HEIGHT
				+ "/" + camera.getModelHeight());
		this.setPreferredSize(new Dimension(220, 15));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("movedCamera")) {
			updateCameraInformation();
		}
	}
	
	private void updateCameraInformation() {
		this.setText("Showing: " + camera.getX() + " - " + (camera.getX() + camera.getWidth()) + "/" + camera.getModelWidth()
				+ ", " + camera.getY() + " - " + (camera.getY() + camera.getHeight()) + "/" + camera.getModelHeight());
	}
}

package leveleditor.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import leveleditor.leveleditor.Constants;

public class GridMouseInformationLabel extends JLabel implements MouseMotionListener {

	private static final long serialVersionUID = 1L;

	public GridMouseInformationLabel(Component mouseBroadcaster) {
		this.setText("Mouse: (0, 0), Hovering tile: (0, 0)");
		this.setPreferredSize(new Dimension(320, 15));
		mouseBroadcaster.addMouseMotionListener(this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		this.mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.updateMousePosition(e.getX(), e.getY());
	}

	private void updateMousePosition(int x, int y) {
		this.setText("Mouse: (" + x + ", " + y + "), Hovering tile: (" + (x/Constants.TILE_WIDTH+1) + ", " + (y/Constants.TILE_HEIGHT+1) + ")");
	}

	
}

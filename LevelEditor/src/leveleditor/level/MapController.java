package leveleditor.level;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import leveleditor.editor.GUIInformation;
import leveleditor.leveleditor.Constants;

public class MapController implements MouseListener, MouseMotionListener, ActionListener, KeyListener {

	private MapCamera camera;

	private int lastClickedTileX;
	private int lastClickedTileY;

	private GUIInformation guiInformation;

	public MapController(MapCamera camera, GUIInformation guiInformation) {
		this.camera = camera;
		this.guiInformation = guiInformation;
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		lastClickedTileX = e.getX() / Constants.TILE_WIDTH;
		lastClickedTileY = e.getY() / Constants.TILE_HEIGHT;
		if (ifLeftMouseButtonPressed(e)) {
			updateTile(lastClickedTileX, lastClickedTileY);
		}
	}

	private boolean ifLeftMouseButtonPressed(MouseEvent e) {
		return (e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK;
	}
	
	private boolean ifRightMouseButtonPressed(MouseEvent e) {
		return (e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK;
	}
	
	private void updateTile(int xCor, int yCor) {
		xCor = Math.max(0, Math.min(xCor, Constants.GRID_WIDTH-1));
		yCor = Math.max(0, Math.min(yCor, Constants.GRID_HEIGHT-1));
		if (guiInformation.getSelectedTile() != null) {
			camera.setTile(xCor, yCor, guiInformation.getSelectedTile().getCharacter());
		}
	}

	private void updateCamera(int newTileX, int newTileY) {
		if (newTileX != lastClickedTileX) {
			if (newTileX - lastClickedTileX > 0) {
				camera.moveCamera(MapCamera.WEST);
			} else {
				camera.moveCamera(MapCamera.EAST);
			}
		}
		if (newTileY != lastClickedTileY) {
			if (newTileY - lastClickedTileY > 0) {
				camera.moveCamera(MapCamera.NORTH);
			} else {
				camera.moveCamera(MapCamera.SOUTH);
			}
		}
		lastClickedTileX = newTileX;
		lastClickedTileY = newTileY;
	}

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (ifRightMouseButtonPressed(e)) {
			int newTileX = e.getX() / Constants.TILE_WIDTH;
			int newTileY = e.getY() / Constants.TILE_HEIGHT;
			updateCamera(newTileX, newTileY);
		}
		this.mousePressed(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			camera.moveCamera(MapCamera.NORTH);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			camera.moveCamera(MapCamera.EAST);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			camera.moveCamera(MapCamera.SOUTH);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			camera.moveCamera(MapCamera.WEST);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }
	
	@Override
	public void keyTyped(KeyEvent e) { }
}

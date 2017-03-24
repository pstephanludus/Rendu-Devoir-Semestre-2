package leveleditor.level;

import java.awt.GridLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

import leveleditor.editor.GUIInformation;
import leveleditor.leveleditor.Constants;

public class MapView extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	private MapCamera camera;

	private GridTileModel[][] layer1;

	private List<? extends TileModel> tiles;

	public MapView(GUIInformation guiInformation, MapCamera camera, List<? extends TileModel> tiles) {
		super(new GridLayout(camera.getHeight(), camera.getWidth()));
		
		this.tiles = tiles;
		
		this.camera = camera;
		this.camera.addPropertyChangeListener(this);
		MapController controller = new MapController(camera, guiInformation);
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
		
		layer1 = new GridTileModel[camera.getHeight()][camera.getWidth()];
		for (int y = 0; y < camera.getHeight(); y++) {
			for (int x = 0; x < camera.getWidth(); x++) {
				layer1[y][x] = new GridTileModel(tiles.get(0));
				layer1[y][x].addKeyListener(controller);
				layer1[y][x].setFocusable(true);
				this.add(layer1[y][x]);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("movedCamera")) {
			redrawGrid();
		} else if (evt.getPropertyName().equals("changedTile")) {
			redrawTile((Point) evt.getNewValue());
		}
	}
	
	public void redrawGrid() {
		for (int y = 0; y < Constants.GRID_HEIGHT; y++) {
			for (int x = 0; x < Constants.GRID_WIDTH; x++) {
				for (TileModel t : tiles) {
					if (camera.getTile(x, y) == t.getCharacter()) {
						layer1[y][x].setTile(t);
						layer1[y][x].grabFocus();
					}
				}
			}
		}
		this.repaint();
	}
	
	private void redrawTile(Point position) {
		for (TileModel t : tiles) {
			if (camera.getTile(position.x, position.y) == t.getCharacter()) {
				layer1[position.y][position.x].setTile(t);
			}
		}
	}
}

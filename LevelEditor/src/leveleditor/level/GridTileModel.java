package leveleditor.level;

import java.awt.Graphics;

import javax.swing.JLayeredPane;

public class GridTileModel extends JLayeredPane {

	private static final long serialVersionUID = 1L;
	private TileModel tile;

	public GridTileModel(TileModel tile) {
		this.tile = tile;
	}

	public void setTile(TileModel tile) {
		this.tile = tile;
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(tile.getImage(), 0, 0, null);
	}
}

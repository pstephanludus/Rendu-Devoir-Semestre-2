package leveleditor.level;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class TileModel {

	private char character;
	private List<String> properties;
	private String name;
	private BufferedImage image;

	public TileModel(final String filePath, final char character) {
		try {
			image = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println("Bad file path: " + filePath);
			System.exit(0);
		}
		this.name = new File(filePath).getName().split("\\.")[0];
		this.character = character;
		this.properties = new ArrayList<String>();
	}

	public Image getImage() {
		return deepCopy(image);
	}

	public Icon getIcon() {
		return new ImageIcon(image);
	}

	public char getCharacter() {
		return character;
	}
	
	public List<String> getProperties() {
		return properties;
	}
	
	public void setProperties(List<String> propertiesList) {
		properties = propertiesList;
	}
	
	public String getName() {
		return name;
	}
	
	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static List<TileModel> getTilesFromFolder(final String folderPath) {
		
		List<TileModel> tiles = new ArrayList<TileModel>();

		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		
		int character = 48;
		
		Map<String, File> map = new TreeMap<String, File>();

		for (File f : listOfFiles) {
			map.put(f.getName(), f);
		}
		
		for (File f : map.values()) {
			tiles.add(new TileModel(f.getPath(), (char)character++));
		}
		
		return tiles;
	}
}

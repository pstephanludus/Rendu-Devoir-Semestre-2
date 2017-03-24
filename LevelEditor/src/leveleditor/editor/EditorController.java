package leveleditor.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import leveleditor.level.MapCamera;
import leveleditor.level.MapModel;
import leveleditor.level.MapView;
import leveleditor.level.TileModel;
import leveleditor.leveleditor.Constants;

public class EditorController implements ActionListener, GUIInformation {

	private MapModel model;

	private TileModel selectedTile;
	private MapCamera camera;

	private List<TileModel> tiles;

	private MapView map;
	private EditorView view;

	private int gridWith = Constants.MAP_WIDTH;
	private int gridHeight = Constants.MAP_HEIGHT;

	public EditorController() {
		init(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);

	}

	public void init(int width, int height) {
		this.tiles = TileModel.getTilesFromFolder("data/");
		this.model = new MapModel(width, height, tiles.get(0).getCharacter());
		this.camera = new MapCamera(model, Constants.GRID_WIDTH,
				Constants.GRID_HEIGHT);

		map = new MapView(this, camera, tiles);

		this.view = new EditorView(this, camera, map, tiles);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (TileModel t : tiles) {
			if (e.getActionCommand().equals(
					Character.toString(t.getCharacter()))) {
				selectedTile = t;
				view.setProperties(selectedTile.getProperties());
				break;
			}
		}
		if (e.getActionCommand().equals("save")) {
			saveFile();
		} else if (e.getActionCommand().equals("load")) {
			loadFile();
		} else if (e.getActionCommand().equals("update")) {
			updateGrid(gridWith, gridHeight);
		} else if (e.getActionCommand().equals("properties")) {
			if (selectedTile == null)
				selectedTile = this.tiles.get(0);
			selectedTile.setProperties(view.getProperties());
		}
	}

	public void updateGrid(int width, int height) {
		view.close();
		init(width, height);
		view.setSize(width, height);
	}

	DocumentListener updateSizeFields = new DocumentListener() {

		public void changedUpdate(DocumentEvent e) {
		}

		public void removeUpdate(DocumentEvent e) {
			gridWith = view.getWidth();
			gridHeight = view.getHeight();
		}

		public void insertUpdate(DocumentEvent e) {
			gridWith = view.getWidth();
			gridHeight = view.getHeight();
		}
	};


	private void saveFile() {

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"xml files", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(null);
		try {
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				Element level = new Element("level");
				Document doc = new Document(level);
				doc.setRootElement(level);

				Element size = new Element("size");
				int height = model.getHeight();
				int width = model.getWidth();
				size.addContent(new Element("width").setText(width + ""));
				size.addContent(new Element("height").setText(height + ""));
				doc.getRootElement().addContent(size);

				for (int y = 0; y < height; y++) {
					Element row = new Element("row");
					for (int x = 0; x < width; x++) {
						char tileChar = model.getTile(x,y);
						String name = "AirTile";
						List<String> properties = new ArrayList<String>();
						for(TileModel tile: tiles) {
							if (tileChar==tile.getCharacter()) {
								name = tile.getName();
								properties = tile.getProperties();
								break;
							}
						}
						Element e = new Element("cell");
						if(!name.equals(""))
							e.setAttribute(new Attribute("name",String.join(",",name)));
						if(!properties.equals(""))
							e.setAttribute(new Attribute("properties",String.join(",",properties)));
						
						row.addContent(e.setText(String.valueOf(tileChar)));
					}
					doc.getRootElement().addContent(row);
				}
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput
						.output(doc, new FileWriter(chooser.getSelectedFile()));

			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Invalid file!", "error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
		}
	}

	public void loadFile() {
		SAXBuilder builder = new SAXBuilder();
		try {
			JFileChooser chooser = new JFileChooser();
			File selectedFile;

			int returnVal = chooser.showOpenDialog(null);
			Document document;
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFile = chooser.getSelectedFile();
				if (selectedFile.canRead() && selectedFile.exists()) {
					document = (Document) builder.build(selectedFile);

					Element rootNode = document.getRootElement();

					List<?> sizeList = rootNode.getChildren("size");
					Element sizeElem = (Element) sizeList.get(0);
					int height = Integer.parseInt(sizeElem
							.getChildText("height"));
					int width = Integer
							.parseInt(sizeElem.getChildText("width"));
					updateGrid(width, height);

					List<?> rows = rootNode.getChildren("row");
					for (int y = 0; y < rows.size(); y++) {
						Element cellsElem = (Element) rows.get(y);
						List<?> cells = cellsElem.getChildren("cell");

						for (int x = 0; x < cells.size(); x++) {
							Element cell = (Element) cells.get(x);

							char tileChar = cell.getText().charAt(0);
							String name = cell.getAttributeValue("name");
							List<String> properties = Arrays.asList(cell.getAttributeValue("properties").split(","));
							for(TileModel tile: tiles) {
								if (name.equals(tile.getName())) {
									tile.setProperties(properties);
									break;
								}
							model.setTile(x, y, tileChar);
							map.redrawGrid();
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public TileModel getSelectedTile() {
		return selectedTile;
	}
}

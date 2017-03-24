package leveleditor.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import leveleditor.level.MapCamera;
import leveleditor.level.MapView;
import leveleditor.level.TileModel;
import leveleditor.leveleditor.Constants;

public class EditorView {

	private JFrame frame;


	private JTextField txtWidth;
	private JTextField txtHeight;
	
	private JTextArea properties;

	public EditorView(EditorController controller, MapCamera camera, MapView grid,
			List<? extends TileModel> tiles) {

		grid.setPreferredSize(new Dimension(Constants.GRID_WIDTH
				* Constants.TILE_WIDTH, Constants.GRID_HEIGHT
				* Constants.TILE_HEIGHT));

		JPanel palette = new JPanel(new FlowLayout());
		
		for (TileModel t : tiles) {
			JButton button = new JButton();
			button.setPreferredSize(new Dimension(Constants.TILE_WIDTH,
					Constants.TILE_HEIGHT));
			button.setMinimumSize(new Dimension(0, 0));
			button.setIcon(t.getIcon());
			button.addActionListener(controller);
			button.setActionCommand(Character.toString(t.getCharacter()));
			palette.add(button);
		}
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(controller);
		saveButton.setActionCommand("save");

		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(controller);
		loadButton.setActionCommand("load");

		CameraInformationLabel cameraInformationLabel = new CameraInformationLabel(
				camera);
		GridMouseInformationLabel mouseInformationLabel = new GridMouseInformationLabel(
				grid);

		JLabel lblWidth = new JLabel("Width(min:32):");
		JLabel lblHeight = new JLabel("Height(min:20):");

		txtWidth = new JTextField(Constants.MAP_WIDTH + "", 3);
		txtWidth.getDocument().addDocumentListener(controller.updateSizeFields);

		txtHeight = new JTextField(Constants.MAP_HEIGHT + "", 3);
		txtHeight.getDocument()
				.addDocumentListener(controller.updateSizeFields);

		JButton updateSize = new JButton("Update/Reset");
		updateSize.addActionListener(controller);
		updateSize.setActionCommand("update");

		JPanel top = new JPanel();
		Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		top.setLayout(new FlowLayout(FlowLayout.LEFT));
		top.setBorder(border);
		top.add(cameraInformationLabel);
		top.add(mouseInformationLabel);
		top.add(lblWidth);
		top.add(txtWidth);
		top.add(lblHeight);
		top.add(txtHeight);
		top.add(updateSize);
		top.add(saveButton);
		top.add(loadButton);
		
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.setBorder(border);
		properties = new JTextArea();
		properties.setPreferredSize(new Dimension(150,500));
		properties.setLineWrap(true);
		properties.setWrapStyleWord(true);
		JButton savePropertiesButton = new JButton("Save properties");
		savePropertiesButton.addActionListener(controller);
		savePropertiesButton.setActionCommand("properties");
		right.add(savePropertiesButton, BorderLayout.NORTH);
		right.add(properties, BorderLayout.CENTER);
		

		JPanel layout = new JPanel(new BorderLayout());
		layout.add(grid, BorderLayout.WEST);
		layout.add(top, BorderLayout.NORTH);
		layout.add(palette, BorderLayout.SOUTH);
		layout.add(right, BorderLayout.EAST);

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Level Editor");
		frame.add(layout);
		frame.pack();
		frame.setVisible(true);
	}

	public int getWidth() {
		String value = txtWidth.getText();
		if (value.equals("")) {
			value = Constants.MAP_WIDTH + "";
		}
		return Integer.parseInt(value);
	}

	public int getHeight() {
		String value = txtHeight.getText();
		if (value.equals("")) {
			value = Constants.MAP_WIDTH + "";
		}
		return Integer.parseInt(value);
	}
	
	public void setSize(int width, int height){
		txtWidth.setText(width+"");
		txtHeight.setText(height+"");
	}

	public void close() {
		frame.setVisible(false);
	}
	
	public List<String> getProperties() {
		return Arrays.asList(this.properties.getText().split("\n"));
	}
	
	public void setProperties(List<String> propertiesList) {
		this.properties.setText(String.join("\n",  propertiesList));
	}
}

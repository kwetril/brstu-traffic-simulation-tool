package by.brstu.tst.mapeditor;

import by.brstu.tst.core.config.Configuration;
import by.brstu.tst.core.config.ConfigurationReader;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.MapBuilder;
import by.brstu.tst.core.map.primitives.MapPoint;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorFrame extends JFrame {

    private MapEditorPanel editorPanel;
    private JLabel statusLabel;

    public MapEditorFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        editorPanel = new MapEditorPanel(this);
        add(editorPanel, BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);
        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);

        pack();
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMapMenuItem = new JMenuItem("Open");
        openMapMenuItem.addActionListener(actionEvent -> {
            openMap();
        });
        fileMenu.add(openMapMenuItem);

        menuBar.add(fileMenu);
        return menuBar;
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    private void openMap() {
        ConfigurationReader configReader = new ConfigurationReader();
        Configuration config = configReader.read();
        MapBuilder mapBuilder = new MapBuilder(config);
        mapBuilder
                .addSourceElement("src-1", new MapPoint(2637872.9f, 6815772.77f))
                .addDestinationElement("dst-1", new MapPoint(2639162.77f, 6816176.45f))
                .addSourceElement("src-2", new MapPoint(2639081.56f, 6816202.73f))
                .addDestinationElement("dst-2", new MapPoint(2637791.68f, 6815801.43f))
                .addSourceElement("src-3", new MapPoint(2638369.74f, 6816678.07f))
                .addDestinationElement("dst-3", new MapPoint(2638728.04f, 6815366.7f))
                .addSourceElement("src-4", new MapPoint(2638785.37f, 6815385.81f))
                .addDestinationElement("dst-4", new MapPoint(2638417.51f, 6816666.13f))
                .addIntersection("crs-1", new MapPoint(2638577.55f, 6815992.52f));

        mapBuilder
                .addRoad("rd-1", "src-1", "crs-1", 3)
                .addRoad("rd-2", "src-2", "crs-1", 3)
                .addRoad("rd-3", "src-3", "crs-1", 3)
                .addRoad("rd-4", "src-4", "crs-1", 3)
                .addRoad("rd-5", "crs-1", "dst-1", 3)
                .addRoad("rd-6", "crs-1", "dst-2", 3)
                .addRoad("rd-7", "crs-1", "dst-3", 3)
                .addRoad("rd-8", "crs-1", "dst-4", 3);

        Map map = mapBuilder.build("map-1");

        editorPanel.showMap(map);
    }
}

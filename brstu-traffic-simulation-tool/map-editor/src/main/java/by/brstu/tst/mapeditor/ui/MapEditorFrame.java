package by.brstu.tst.mapeditor.ui;

import by.brstu.tst.core.config.Configuration;
import by.brstu.tst.core.config.ConfigurationReader;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.MapBuilder;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.io.xml.MapReader;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorFrame extends JFrame {

    private MapEditorPanel editorPanel;
    private JLabel statusLabel;
    private boolean simulationStarted;

    public MapEditorFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        editorPanel = new MapEditorPanel(this);
        add(editorPanel, BorderLayout.CENTER);
        add(createStatusPanel(), BorderLayout.SOUTH);
        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);

        simulationStarted = false;
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
        MapReader mapReader = new MapReader();
        Map map = mapReader.readMap("io-utils\\data\\map.xml");
        editorPanel.showMap(map);
    }
}

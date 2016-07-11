package by.brstu.tst.mapeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorFrame extends JFrame {

    private MapEditorPanel editorPanel;

    public MapEditorFrame() {
        setTitle("Traffic simulation tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        editorPanel = new MapEditorPanel();
        add(editorPanel, BorderLayout.CENTER);

        JMenuBar menuBar = createMenu();
        setJMenuBar(menuBar);

        pack();
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

    private void openMap() {
        ShapesContainer container = new ShapesContainer();
        editorPanel.showMap(container);
    }
}

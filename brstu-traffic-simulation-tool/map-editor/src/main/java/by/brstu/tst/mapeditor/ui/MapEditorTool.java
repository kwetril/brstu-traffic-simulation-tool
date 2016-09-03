package by.brstu.tst.mapeditor.ui;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorTool {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MapEditorFrame frame = new MapEditorFrame();
            frame.setVisible(true);
        });
    }
}

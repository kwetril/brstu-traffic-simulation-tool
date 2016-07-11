package by.brstu.tst.mapeditor;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditor {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MapEditorFrame frame = new MapEditorFrame();
            frame.setVisible(true);
        });
    }
}

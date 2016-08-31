package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.map.primitives.MapRectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorPanel extends TransformableCanvas {
    private Map map;
    private MapEditorFrame parentFrame;
    private MapRectangle mapBounds;

    public MapEditorPanel(MapEditorFrame parentFrame) {
        super(-3, 5, 0, 2.0);
        this.parentFrame = parentFrame;
        addMouseScalingTool();
        addMouseMovingTool();
    }

    public void showMap(Map map) {
        this.map = map;
        FindMapBoundsVisitor mapBoundsVisitor = new FindMapBoundsVisitor();
        map.visitElements(mapBoundsVisitor);
        mapBounds = mapBoundsVisitor.getMapBounds();
        showBounds(mapBounds.getMinX(), mapBounds.getMinY(),
                mapBounds.getMaxX(), mapBounds.getMaxY());
    }

    @Override
    public void update(Graphics graphics) {
        super.update(graphics);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (map != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;

            BufferedImage grassTileImage = null;
            try {
                grassTileImage = ImageIO.read(new File("map-editor/img/grass-tile-400.jpg"));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            TexturePaint grassTexture = new TexturePaint(grassTileImage,
                    new Rectangle2D.Float(mapBounds.getMinX() - 0.1f * mapBounds.getWidth(),
                    mapBounds.getMinY() - 0.1f * mapBounds.getHeight(), 100, 100));
            Paint oldPaint = graphics2D.getPaint();
            graphics2D.setPaint(grassTexture);
            graphics2D.fill(new Rectangle2D.Float(mapBounds.getMinX() - 0.1f * mapBounds.getWidth(),
                    mapBounds.getMinY() - 0.1f * mapBounds.getHeight(),
                    1.2f * mapBounds.getWidth(), 1.2f * mapBounds.getHeight()));
            graphics2D.setPaint(oldPaint);

            graphics.setColor(Color.BLACK);
            BaseRoadElementVisitor mapDrawingVisitor = new RoadElementDrawVisitor(graphics2D);
            map.visitElements(mapDrawingVisitor);
        }
        parentFrame.setStatus(String.format("Scale: %s; Scale power: %s; Translate X: %s; Translate Y: %s; (W, H)=%s,%s",
                transformState.getScaleX(), transformState.getScalePower(),
                transformState.getTranslateX(), transformState.getTranslateY(),
                getWidth(), getHeight()));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }
}

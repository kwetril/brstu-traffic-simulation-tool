package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.ModelState;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.map.primitives.MapRectangle;
import by.brstu.tst.core.vehicle.IVehicleVisitor;
import by.brstu.tst.ui.utils.FindMapBoundsVisitor;
import by.brstu.tst.ui.utils.RoadElementDrawVisitor;
import by.brstu.tst.ui.utils.TransformableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by kwetril on 7/5/16.
 */
public class SimulationPanel extends TransformableCanvas {
    private Map map;
    private ModelState modelState;
    private SimulationFrame parentFrame;
    private MapRectangle mapBounds;
    private MapImageCache mapImageCache;
    private volatile boolean simulationStarted;

    public SimulationPanel(SimulationFrame parentFrame) {
        super(-3, 5, 0, 2.0);
        this.parentFrame = parentFrame;
        this.simulationStarted = false;
        mapImageCache = new MapImageCache();
        addMouseScalingTool();
        addMouseMovingTool();
    }

    public boolean isSimulationStarted() {
        return simulationStarted;
    }

    public void showMap(Map map, ModelState modelState) {
        this.map = map;
        this.modelState = modelState;
        FindMapBoundsVisitor mapBoundsVisitor = new FindMapBoundsVisitor();
        map.visitElements(mapBoundsVisitor);
        mapBounds = mapBoundsVisitor.getMapBounds();
        showBounds(mapBounds.getMinX(), mapBounds.getMinY(),
                mapBounds.getMaxX(), mapBounds.getMaxY());
    }

    public void startSimulation() {
        simulationStarted = true;
        new Thread(() -> {
            IVehicleVisitor updateStateVisitor = new UpdateVehicleStateVisitor(0.1f);
            while (simulationStarted) {
                modelState.visitVehicles(updateStateVisitor);
                repaint();
                try {
                    Thread.sleep(1000 / 25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopSimulation() {
        simulationStarted = false;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        long startTime = System.nanoTime();
        super.paintComponent(graphics);
        if (map != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            drawMapToGraphics(graphics2D);
            if (mapImageCache.getImage(transformState.getScaleIndex()) == null) {
                Image img = createImageMap();
                mapImageCache.putImage(transformState.getScaleIndex(), img);
            }
        }
        if (modelState != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            IVehicleVisitor vehicleDrawVisitor = new VehicleDrawVisitor(graphics2D);
            modelState.visitVehicles(vehicleDrawVisitor);
        }
        parentFrame.setStatus(String.format("Scale: %s; Scale power: %s; Translate X: %s; Translate Y: %s; (W, H)=%s,%s",
                transformState.getScaleX(), transformState.getScalePower(),
                transformState.getTranslateX(), transformState.getTranslateY(),
                getWidth(), getHeight()));
        long endTime = System.nanoTime();
        System.out.printf("Time: %s ms\n", (endTime - startTime) / 1000000.0);
    }

    private Image createImageMap() {
        double scale = transformState.getScaleX();
        int height = (int) Math.round(mapBounds.getHeight() * scale * 1.2);
        int width = (int) Math.round(mapBounds.getWidth() * scale * 1.2);
        System.out.printf("Width: %s; Height: %s.\n", width, height);
        Image image = createImage(width, height);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setTransform(transformState.getTransform());

        //graphics.translate(-mapBounds.getMinX(), -mapBounds.getMinY());
        double dx = -(mapBounds.getMinX() - 0.1 * mapBounds.getWidth()) * transformState.getScaleX();
        double dy = -(mapBounds.getMaxY() + 0.1 * mapBounds.getHeight()) * transformState.getScaleY();
        dx -= transformState.getTranslateX();
        dy -= transformState.getTranslateY();
        dx /= transformState.getScaleX();
        dy /= transformState.getScaleY();
        graphics.translate(dx, dy);
        drawMapToGraphics(graphics);
        return image;
    }

    private void drawMapToGraphics(Graphics2D graphics2D) {
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

        graphics2D.setColor(Color.BLACK);
        BaseRoadElementVisitor mapDrawingVisitor = new RoadElementDrawVisitor(graphics2D);
        map.visitElements(mapDrawingVisitor);
    }
}

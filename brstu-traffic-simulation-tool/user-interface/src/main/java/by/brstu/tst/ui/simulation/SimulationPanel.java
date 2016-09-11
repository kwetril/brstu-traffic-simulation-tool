package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.SimulationModel;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.map.primitives.MapRectangle;
import by.brstu.tst.core.simulation.IVehicleVisitor;
import by.brstu.tst.ui.utils.FindMapBoundsVisitor;
import by.brstu.tst.ui.utils.RoadElementDrawVisitor;
import by.brstu.tst.ui.utils.TransformableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by kwetril on 7/5/16.
 */
public class SimulationPanel extends TransformableCanvas {
    private Map map;
    TexturePaint grassTexture;
    private SimulationModel simulationModel;
    private SimulationFrame parentFrame;
    private MapRectangle mapBounds;
    private MapImageCache mapImageCache;
    private volatile boolean simulationStarted;
    private BufferedImage grassTileImage;


    public SimulationPanel(SimulationFrame parentFrame) {
        super(-3, 5, 0, 2.0);
        this.parentFrame = parentFrame;
        this.simulationStarted = false;
        mapImageCache = new MapImageCache();
        try {
            grassTileImage = ImageIO.read(new File("map-editor/img/grass-tile-400.jpg"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        addMouseScalingTool();
        addMouseMovingTool();
    }

    public boolean isSimulationStarted() {
        return simulationStarted;
    }

    public void showMap(Map map, SimulationModel simulationModel) {
        this.map = map;
        FindMapBoundsVisitor mapBoundsVisitor = new FindMapBoundsVisitor();
        map.visitElements(mapBoundsVisitor);
        mapBounds = mapBoundsVisitor.getMapBounds();
        this.grassTexture = new TexturePaint(grassTileImage,
                new Rectangle2D.Float((float) (mapBounds.getMinX() - 0.1f * mapBounds.getWidth()),
                        (float) (mapBounds.getMinY() - 0.1f * mapBounds.getHeight()), 100, 100));
        this.simulationModel = simulationModel;
        showBounds(mapBounds.getMinX(), mapBounds.getMinY(),
                mapBounds.getMaxX(), mapBounds.getMaxY());
    }

    public void startSimulation() {
        final int FRAMES_PER_SECOND = 25;
        final long NANO_SEC_PER_FRAME = 1000000000 / FRAMES_PER_SECOND;
        final float simulationPlayVelocity = 2.0f;
        final float simulationTimeStepSec = 0.1f;

        simulationStarted = true;
        new Thread(() -> {
            long nextFrameNanos = System.nanoTime() + NANO_SEC_PER_FRAME;
            long updateModelTime = 0;
            float numSteps = 0;
            //calculate how many simulation steps are in one iteration
            float stepsPerIteration = NANO_SEC_PER_FRAME / (1000000000.0f * simulationTimeStepSec) * simulationPlayVelocity;
            while (simulationStarted) {
                //one iteration should run NANO_SEC_PER_FRAME nanoseconds
                long iterationStartTime = System.nanoTime();

                numSteps += stepsPerIteration;
                int intNumSteps = Math.round(numSteps);
                if (intNumSteps > 0) {
                    long updateModelStartTime = System.nanoTime();
                    simulationModel.performSimulationSteps(intNumSteps);
                    updateModelTime = System.nanoTime() - updateModelStartTime;
                    numSteps -= intNumSteps;
                    repaint();
                }

                //sleep the rest of iteration time
                long sleepNanos = nextFrameNanos - System.nanoTime();
                if (sleepNanos > 1000) {
                    try {
                        Thread.sleep(sleepNanos / 1000000, (int) (sleepNanos % 1000000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                nextFrameNanos += NANO_SEC_PER_FRAME;

                long iterationEndTime = System.nanoTime();
                System.out.printf("Simulation loop iteration: %s ms; Model update time: %s ms\n",
                        (iterationEndTime-iterationStartTime) / 1000000.0,
                        updateModelTime / 1000000.0);
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
            Image cachedImage = mapImageCache.getImage(transformState.getScaleIndex());
            if (cachedImage == null) {
                cachedImage = createImageMap();
                if (cachedImage == null) {
                    drawMapToGraphics(graphics2D);
                }
                else {
                    mapImageCache.putImage(transformState.getScaleIndex(), cachedImage);
                    paintImageMap(graphics2D, cachedImage);
                }
            }
            else {
                paintImageMap(graphics2D, cachedImage);
            }
        }
        long endTime = System.nanoTime();
        if (simulationModel != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            IVehicleVisitor vehicleDrawVisitor = new VehicleDrawVisitor(graphics2D);
            simulationModel.visitVehicles(vehicleDrawVisitor);
        }
        parentFrame.setStatus(String.format("Scale: %s; Scale power: %s; Translate X: %s; Translate Y: %s; (W, H)=%s,%s",
                transformState.getScaleX(), transformState.getScalePower(),
                transformState.getTranslateX(), transformState.getTranslateY(),
                getWidth(), getHeight()));
        System.out.printf("Rendering time: %s ms\n",
                (endTime - startTime) / 1000000.0);
    }

    private Image createImageMap() {
        double scale = transformState.getScaleX();
        long height = Math.round(mapBounds.getHeight() * scale * 1.2);
        long width = Math.round(mapBounds.getWidth() * scale * 1.2);
        //System.out.printf("Width: %s; Height: %s; W*H: %s.\n", width, height, width * height);
        if (height * width > 50000000) {
            return null;
        }
        Image image = createImage((int) width, (int) height);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setTransform(transformState.getTransform());

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

    private void paintImageMap(Graphics2D graphics, Image image) {
        BufferedImage bufferedImage = (BufferedImage) image;
        AffineTransform transform = graphics.getTransform();
        graphics.setTransform(new AffineTransform());
        double dx = (mapBounds.getMinX() - 0.1 * mapBounds.getWidth());
        double dy = (mapBounds.getMaxY() + 0.1 * mapBounds.getHeight());
        Point2D.Float startPoint = new Point2D.Float((float) dx, (float) dy);
        Point2D.Float endPoint = new Point2D.Float(0, 0);
        transform.transform(startPoint, endPoint);
        graphics.drawImage(bufferedImage, (int) endPoint.getX(), (int) endPoint.getY(),
                bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        graphics.setTransform(transform);
    }

    private void drawMapToGraphics(Graphics2D graphics2D) {
        Paint oldPaint = graphics2D.getPaint();
        graphics2D.setPaint(grassTexture);
        graphics2D.fill(new Rectangle2D.Float((float) (mapBounds.getMinX() - 0.1f * mapBounds.getWidth()),
                (float) (mapBounds.getMinY() - 0.1f * mapBounds.getHeight()),
                (float) (1.2f * mapBounds.getWidth()), (float) (1.2f * mapBounds.getHeight())));
        graphics2D.setPaint(oldPaint);

        graphics2D.setColor(Color.BLACK);
        BaseRoadElementVisitor mapDrawingVisitor = new RoadElementDrawVisitor(graphics2D);
        map.visitElements(mapDrawingVisitor);
        long endMap = System.nanoTime();
    }
}

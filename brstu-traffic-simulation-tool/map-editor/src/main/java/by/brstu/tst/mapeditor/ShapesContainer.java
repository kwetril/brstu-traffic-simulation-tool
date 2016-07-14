package by.brstu.tst.mapeditor;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by kwetril on 7/11/16.
 */
public class ShapesContainer {
    public Point2D point1 = new Point2D.Float(0, 0);

    public Line2D line1 = new Line2D.Float(0, 0, 200, 100);
    public Line2D line2 = new Line2D.Float(0, 0, 100, 200);


    public Rectangle2D.Float rectangle = new Rectangle2D.Float(50, 50, 40, 40);
}

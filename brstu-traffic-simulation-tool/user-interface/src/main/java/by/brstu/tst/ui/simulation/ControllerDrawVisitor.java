package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.utils.MapUtils;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.IControllerVisitor;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.ui.utils.ShapeUtils;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Created by a.klimovich on 23.10.2016.
 */
public class ControllerDrawVisitor implements IControllerVisitor {
    private Graphics2D graphics;
    private final BasicStroke stroke = new BasicStroke(0.5f);

    public ControllerDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void visit(IntersectionController controller) {
        Intersection intersection = controller.getIntersection();
        IntersectionState state = controller.getState();
        for (RoadConnectorDescription connector : state.getOpenedConnections()) {
            BezierCurve curve = intersection.getConnector(connector);
            Path2D.Float path = new Path2D.Float();
            ShapeUtils.updatePathWithBezierCurve(path, curve);
            graphics.setColor(Color.GREEN);
            graphics.setStroke(stroke);
            graphics.draw(path);
        }
    }
}

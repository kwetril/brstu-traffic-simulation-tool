package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.MapPoint;

/**
 * Created by kwetril on 8/17/16.
 */
public class Intersection extends NodeRoadElement {
    public Intersection(String name, MapPoint basePoint) {
        super(name, basePoint);
    }

    @Override
    public void accept(BaseRoadElementVisitor visitor) {
        visitor.visit(this);
    }
}

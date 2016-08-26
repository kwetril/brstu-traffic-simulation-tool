package by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 8/16/16.
 */
public abstract class EdgeRoadElement extends BaseRoadElement {
    NodeRoadElement sourceElement;
    NodeRoadElement destinationElement;

    @Override
    public void accept(BaseRoadElementVisitor visitor) {
        visitor.visit(this);
    }
}

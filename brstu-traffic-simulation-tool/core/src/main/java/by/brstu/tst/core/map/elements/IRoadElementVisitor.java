package by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 8/24/16.
 */
public interface IRoadElementVisitor {
    void visit(BaseRoadElement roadElement);
    void visit(NodeRoadElement roadElement);
    void visit(EdgeRoadElement roadElement);
    void visit(SourceElement roadElement);
    void visit(DestinationElement roadElement);
    void visit(Intersection roadElement);
    void visit(DirectedRoad roadElement);
}

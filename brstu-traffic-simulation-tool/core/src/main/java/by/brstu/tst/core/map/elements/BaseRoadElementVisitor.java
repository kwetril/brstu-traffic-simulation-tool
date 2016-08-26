package by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 8/24/16.
 */
public class BaseRoadElementVisitor {
    public void visit(BaseRoadElement roadElement) {
        //do nothig
    }
    public void visit(NodeRoadElement roadElement) {
        visit((BaseRoadElement) roadElement);
    }
    public void visit(EdgeRoadElement roadElement) {
        visit((BaseRoadElement) roadElement);
    }
    public void visit(SourceElement roadElement) {
        visit((NodeRoadElement) roadElement);
    }
    public void visit(DestinationElement roadElement) {
        visit((NodeRoadElement) roadElement);
    }
    public void visit(Intersection roadElement) {
        visit((NodeRoadElement) roadElement);
    }
    public void visit(DirectedRoad roadElement) {
        visit((EdgeRoadElement) roadElement);
    }
}

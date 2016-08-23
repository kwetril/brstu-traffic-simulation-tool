package by.brstu.tst.core.by.brstu.tst.core.map.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 8/16/16.
 */
public abstract class NodeRoadElement extends BaseRoadElement {
    private MapPoint basePoint;
    private List<EdgeRoadElement> inputElements;
    private List<EdgeRoadElement> outputElements;

    public NodeRoadElement(String name, MapPoint basePoint) {
        this.name = name;
        this.basePoint = basePoint;
        inputElements = new ArrayList<>();
        outputElements = new ArrayList<>();
    }

    public List<EdgeRoadElement> getInputElements() {
        return inputElements;
    }

    public List<EdgeRoadElement> getOutputElements() {
        return outputElements;
    }
}

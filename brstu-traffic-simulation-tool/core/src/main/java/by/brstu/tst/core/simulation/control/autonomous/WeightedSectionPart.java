package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import com.sun.org.apache.xml.internal.utils.Hashtree2Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class WeightedSectionPart {
    private HashSet<RoadConnectorDescription> connectorDescriptions;
    private HashSet<RoadConnectorDescription> nonConflicts;
    private double weight;

    public WeightedSectionPart(RoadConnectorDescription connectorDescription, double weight,
                               HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        connectorDescriptions = new HashSet<>();
        connectorDescriptions.add(connectorDescription);
        nonConflicts = nonConflictConnectors.get(connectorDescription);
        this.weight = weight;
    }

    public WeightedSectionPart(Iterable<RoadConnectorDescription> connectorDescriptions, double weight,
                               HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        this.connectorDescriptions = new HashSet<>();
        boolean first = true;
        for (RoadConnectorDescription item : connectorDescriptions) {
            this.connectorDescriptions.add(item);
            if (first) {
                this.nonConflicts = nonConflictConnectors.get(item);
                first = false;
            } else {
                HashSet<RoadConnectorDescription> tmp = new HashSet<>(nonConflictConnectors.get(item));
                tmp.retainAll(this.nonConflicts);
                this.nonConflicts = tmp;
            }
        }
        this.weight = weight;
    }

    public HashSet<RoadConnectorDescription> getConnectorDescriptions() {
        return connectorDescriptions;
    }

    public boolean hasConflicts(WeightedSectionPart sectionPart) {
        for (RoadConnectorDescription connector : sectionPart.getConnectorDescriptions()) {
            if (!nonConflicts.contains(connector)) {
                return true;
            }
        }
        return false;
    }

    public double getWeight() {
        return weight;
    }
}

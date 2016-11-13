package by.brstu.tst.io.xml;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.SimulationConfig;
import by.brstu.tst.core.simulation.control.IntersectionController;
import by.brstu.tst.core.simulation.control.IntersectionState;
import by.brstu.tst.core.simulation.control.adaptive.AdaptiveIntersectionController;
import by.brstu.tst.core.simulation.control.autonomous.AutonomousIntersectionController;
import by.brstu.tst.core.simulation.control.cyclic.CycleSection;
import by.brstu.tst.core.simulation.control.cyclic.CyclicIntersectionController;
import by.brstu.tst.core.simulation.distribution.ExponentialDistribution;
import by.brstu.tst.core.simulation.distribution.IRandomDistribution;
import by.brstu.tst.core.simulation.driving.IDriverFactory;
import by.brstu.tst.core.simulation.driving.adaptive.AdaptiveDriverFactory;
import by.brstu.tst.core.simulation.driving.autonomous.AutonomousDriverFactory;
import by.brstu.tst.core.simulation.driving.cyclic.CyclicDriverFactory;
import by.brstu.tst.core.simulation.flows.ActivationPeriod;
import by.brstu.tst.core.simulation.flows.IVehicleFlow;
import by.brstu.tst.core.simulation.flows.StaticVehicleFlow;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.vehicle.VehicleType;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static by.brstu.tst.io.xml.XmlUtils.*;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class SimulationConfigReader {
    private Map map;
    private IDriverFactory driverFactory;

    public SimulationConfigReader(Map map) {
        this.map = map;
    }

    public SimulationConfig readSimulationConfig(String path) throws IOException, ParseException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document simulationConfigDom = docBuilder.parse(new File(path));

            SimulationConfig simulationConfig = new SimulationConfig();
            NamedNodeMap simulationAttributes = simulationConfigDom
                    .getElementsByTagName("simulation").item(0).getAttributes();
            float timeStep = getFloatAttr(simulationAttributes, "timeStep");
            simulationConfig.setTimeStep(timeStep);
            String simulationType = getAttr(simulationAttributes, "type");
            switch (simulationType) {
                case "cyclic" :
                    driverFactory = new CyclicDriverFactory();
                    break;
                case "autonomous":
                    driverFactory = new AutonomousDriverFactory();
                    break;
                case "adaptive":
                    driverFactory = new AdaptiveDriverFactory();
                    break;
                default:
                    throw new ParseException(String.format("Simulation type %s not supported", simulationType));
            }

            NodeList vehicleFlowsXml = simulationConfigDom.getElementsByTagName("flow");
            for (int i = 0; i < vehicleFlowsXml.getLength(); i++) {
                IVehicleFlow vehicleFlow = parseVehicleFlow(vehicleFlowsXml.item(i));
                simulationConfig.addFlow(vehicleFlow);
            }

            NodeList intersectionControllersXml = simulationConfigDom.getElementsByTagName("controller");
            for (int i = 0; i < intersectionControllersXml.getLength(); i++) {
                IntersectionController controller = parseIntersectionController(intersectionControllersXml.item(i));
                simulationConfig.addIntersectionController(controller);
            }

            return simulationConfig;
        }
        catch (ParserConfigurationException parseConfigException) {
            throw new ParseException(parseConfigException.getMessage());
        }
        catch (SAXException saxException) {
            throw new ParseException(saxException.getMessage());
        }
    }

    private IVehicleFlow parseVehicleFlow(Node vehicleFlowXml) throws ParseException {
        NamedNodeMap vehicleFlowAttributes = vehicleFlowXml.getAttributes();
        String type = getAttr(vehicleFlowAttributes, "type");
        switch (type) {
            case "static":
                String vehicleTypeCode = getAttr(vehicleFlowAttributes, "vehicleType");
                VehicleType vehicleType;
                switch (vehicleTypeCode) {
                    case "car":
                        vehicleType = VehicleType.CAR;
                        break;
                    default:
                        throw new ParseException(String.format("Vehicle type %s not supported", vehicleTypeCode));
                }
                NodeList vehicleFlowChilds = vehicleFlowXml.getChildNodes();
                Node activationPeriodXml = filterNodesByTag(vehicleFlowChilds, "activation").get(0);
                ActivationPeriod activationPeriod = parseActivationPeriod(activationPeriodXml);

                Node randomDistributionXml = filterNodesByTag(vehicleFlowChilds, "distribution").get(0);
                IRandomDistribution randomDistribution = parseRandomDistribution(randomDistributionXml);

                Node routeXml = filterNodesByTag(vehicleFlowChilds, "route").get(0);
                List<Node> routeElementsXml = filterNodesByTag(routeXml.getChildNodes(), "routeElement");
                String[] routeElements = new String[routeElementsXml.size()];
                for (int i = 0; i < routeElements.length; i++) {
                    routeElements[i] = getAttr(routeElementsXml.get(i).getAttributes(), "name");
                }
                return new StaticVehicleFlow(vehicleType, driverFactory, new Route(map, routeElements),
                        randomDistribution, activationPeriod);
            default:
                throw new ParseException(String.format("Vehicle flow type %s not supported", type));
        }
    }

    private ActivationPeriod parseActivationPeriod(Node activationPeriodXml) {
        NamedNodeMap activationPeriodAttributes = activationPeriodXml.getAttributes();
        float from = getFloatAttr(activationPeriodAttributes, "from");
        float to = getFloatAttr(activationPeriodAttributes, "to");
        return new ActivationPeriod(from, to);
    }

    private IRandomDistribution parseRandomDistribution(Node randomDistributionXml) throws ParseException {
        NamedNodeMap randomDistributionAttributes = randomDistributionXml.getAttributes();
        String type = getAttr(randomDistributionAttributes, "type");
        switch (type) {
            case "exponential":
                float lambda = getFloatAttr(randomDistributionAttributes, "lambda");
                int seed = getIntAttr(randomDistributionAttributes, "seed");
                return new ExponentialDistribution(lambda, seed);
            default:
                throw new ParseException(String.format("Random distribution type %s not supported", type));
        }
    }

    private IntersectionController parseIntersectionController(Node controllerXml) throws ParseException {
        NamedNodeMap controllerAttributes = controllerXml.getAttributes();
        String type = getAttr(controllerAttributes, "type");
        String intersectionName = getAttr(controllerAttributes, "intersection");
        Intersection intersection =(Intersection) map.getNode(intersectionName);
        switch (type) {
            case "cycled":
                Node cycleXml = filterNodesByTag(controllerXml.getChildNodes(), "cycle").get(0);
                List<CycleSection> cycleSections = parseCycleSections(intersection, cycleXml);
                return new CyclicIntersectionController(intersection, cycleSections);
            case "autonomous":
                float recalculationPeriod = getFloatAttr(controllerAttributes, "recalculationPeriod");
                float operationRadius = getFloatAttr(controllerAttributes, "operationRadius");
                return new AutonomousIntersectionController(intersection, recalculationPeriod, operationRadius, false);
            case "adaptive":
                double period = getDoubleAttr(controllerAttributes, "period");
                int historyMinutes = getIntAttr(controllerAttributes, "historyMinutes");
                Node adaptiveCycleXml = filterNodesByTag(controllerXml.getChildNodes(), "cycle").get(0);
                HashMap<String, IntersectionState> roadToState = parseAdaptiveCycle(intersection, adaptiveCycleXml);
                return new AdaptiveIntersectionController(intersection, roadToState, period, historyMinutes);
            default:
                throw new ParseException(String.format("Intersection controller type %s not supported", type));
        }
    }

    private HashMap<String,IntersectionState> parseAdaptiveCycle(Intersection intersection, Node cycleXml) {
        List<Node> cycleSectionsXml = filterNodesByTag(cycleXml.getChildNodes(), "section");
        HashMap<String, IntersectionState> result = new HashMap<>();
        for (Node cycleSectionXml : cycleSectionsXml) {
            String road = getAttr(cycleSectionXml.getAttributes(), "road");
            Node connectorsXml = filterNodesByTag(cycleSectionXml.getChildNodes(), "connectors").get(0);
            HashSet<RoadConnectorDescription> roadConnections = new HashSet<>();
            for (Node connectorXml : filterNodesByTag(connectorsXml.getChildNodes(), "connector")) {
                NamedNodeMap connectorAttributes = connectorXml.getAttributes();
                String from = getAttr(connectorAttributes, "from");
                int fromLane = getIntAttr(connectorAttributes, "fromLane");
                String to = getAttr(connectorAttributes, "to");
                int toLane = getIntAttr(connectorAttributes, "toLane");
                roadConnections.add(new RoadConnectorDescription(map.getEdge(from).getDirectedRoadByEndNode(intersection),
                        fromLane, map.getEdge(to).getDirectedRoadByStartNode(intersection), toLane));
            }
            IntersectionState state = new IntersectionState(roadConnections);
            result.put(road, state);
        }
        return result;
    }

    private List<CycleSection> parseCycleSections(Intersection intersection, Node cycleXml) {
        List<Node> cycleSectionsXml = filterNodesByTag(cycleXml.getChildNodes(), "section");
        List<CycleSection> result = new ArrayList<>();
        for (Node cycleSectionXml : cycleSectionsXml) {
            float duration = getFloatAttr(cycleSectionXml.getAttributes(), "duration");
            Node connectorsXml = filterNodesByTag(cycleSectionXml.getChildNodes(), "connectors").get(0);
            HashSet<RoadConnectorDescription> roadConnections = new HashSet<>();
            for (Node connectorXml : filterNodesByTag(connectorsXml.getChildNodes(), "connector")) {
                NamedNodeMap connectorAttributes = connectorXml.getAttributes();
                String from = getAttr(connectorAttributes, "from");
                int fromLane = getIntAttr(connectorAttributes, "fromLane");
                String to = getAttr(connectorAttributes, "to");
                int toLane = getIntAttr(connectorAttributes, "toLane");
                roadConnections.add(new RoadConnectorDescription(map.getEdge(from).getDirectedRoadByEndNode(intersection),
                        fromLane, map.getEdge(to).getDirectedRoadByStartNode(intersection), toLane));
            }
            IntersectionState state = new IntersectionState(roadConnections);
            result.add(new CycleSection(state, duration));
        }
        return result;
    }
}

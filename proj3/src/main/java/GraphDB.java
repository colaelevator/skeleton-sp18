import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    public Map<Long,Node> nodes = new HashMap<>();
    public Map<Long,Way> ways = new HashMap<>();
    public Map<Long,Set<Long>> adjs = new HashMap<>();

    public void addAdjacent(Way way) {
        if (way.linkedNode.size() < 2) return;
        for (int i = 0; i < way.linkedNode.size() - 1; i++) {
            addEdge(way.linkedNode.get(i),way.linkedNode.get(i + 1));
        }
    }

    private void addEdge(long v,long w) {
        adjs.computeIfAbsent(v,k -> new HashSet<>()).add(w);
        adjs.computeIfAbsent(w,k -> new HashSet<>()).add(v);
    }

    static class Node {
        long id;
        double lat;
        double lon;
        Set<Long> linkedWays;
        Map<String,String> extraInfo;
        Node(String id,String lat,String lon) {
            this.id = Long.parseLong(id);
            this.lat = Double.parseDouble(lat);
            this.lon = Double.parseDouble(lon);
            extraInfo = new HashMap<>();
            linkedWays = new HashSet<>();
        }
    }

    public void addNode(Node node) {
        nodes.put(node.id, node);
    }

    static class Way {
        long id;
        boolean valid = false;
        List<Long> linkedNode;
        Map<String,String> extraInfo;
        Way(String id) {
            this.id = Long.parseLong(id);
            linkedNode = new ArrayList<>();
            extraInfo = new HashMap<>();
        }
    }

    public void addWay(Way way) {
        ways.put(way.id, way);
    }

    public String findWay(long c, long n) {
        for (long w : nodes.get(c).linkedWays) {
            if (ways.get(w).linkedNode.contains(n)) {
                String name = ways.get(w).extraInfo.get("name");
                return name == null ? "" : name;
            }
        }
        return "";
    }




    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> connected = new HashSet<>();
        for (Map.Entry<Long, Set<Long>> entry : adjs.entrySet()) {
            long v = entry.getKey();
            connected.add(v);
            connected.addAll(entry.getValue());
        }
        adjs.keySet().removeIf(id -> !connected.contains(id) || adjs.get(id).isEmpty());
        nodes.keySet().removeIf(id -> !connected.contains(id));
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return Collections.unmodifiableSet(nodes.keySet());
    }
    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return adjs.getOrDefault(v, Collections.emptySet());
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        long closestId = -1;

        for (Node n : nodes.values()) {
            double dist = distance(lon, lat, n.lon, n.lat);
            if (dist < minDist) {
                minDist = dist;
                closestId = n.id;
            }
        }
        return closestId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }
}

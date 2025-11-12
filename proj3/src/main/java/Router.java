import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    private static class NodeState implements Comparable<NodeState> {
        long ID;
        double dist;
        double priority;

        NodeState(long ID,double dist,double priority) {
            this.ID = ID;
            this.dist = dist;
            this.priority = priority;
        }

        @Override
        public int compareTo(NodeState o) {
            return Double.compare(this.priority,o.priority);
        }
    }



    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long stId = g.closest(stlon,stlat);
        long destId = g.closest(destlon,destlat);

        Map<Long,Double> distTo = new HashMap<>();
        Map<Long,Long> edgeTo = new HashMap<>();
        PriorityQueue<NodeState> pq = new PriorityQueue<>();

        for (long id : g.vertices()) {
            distTo.put(id,Double.POSITIVE_INFINITY);
        }
        distTo.put(stId,0.0);
        pq.add(new NodeState(stId,0.0,heuristic(g,stId,destId)));

        while (!pq.isEmpty()) {
            NodeState curr = pq.remove();
            long v = curr.ID;

            if (v == destId) {
                return buildPath(edgeTo,stId,destId);
            }

            for (long neigh : g.adjacent(v)) {
                double newDist = distTo.get(v) + g.distance(v,neigh);
                if (newDist < distTo.get(neigh)) {
                    distTo.put(neigh, newDist);
                    edgeTo.put(neigh, v);
                    double priority = newDist + heuristic(g, neigh, destId);
                    pq.add(new NodeState(neigh, newDist, priority));
                }
            }
        }
        return new ArrayList<>();
    }

    private static double heuristic(GraphDB g,long s,long v) {
        return g.distance(s,v);
    }

    private static List<Long> buildPath(Map<Long,Long> edgeTo,long start,long goal) {
        LinkedList<Long> path = new LinkedList<>();
        for (long at = goal;at != start;at = edgeTo.get(at)) {
            path.addFirst(at);
        }
        path.addFirst(start);
        return path;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of Navigatiion Direction objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> result = new ArrayList<>();
        if (route.size() < 2) return result;

        String currWay = g.findWay(route.get(0), route.get(1));
        NavigationDirection currDir = new NavigationDirection();
        currDir.way = currWay;
        currDir.direction = NavigationDirection.START;
        currDir.distance = 0.0;

        for (int i = 0; i < route.size() - 1; i++) {
            String nextWay = g.findWay(route.get(i), route.get(i + 1));
            double segmentDist = g.distance(route.get(i), route.get(i + 1));

            if (nextWay.equals(currWay)) {
                currDir.distance += segmentDist;
            } else {
                result.add(currDir);
                currDir = new NavigationDirection();
                currDir.way = nextWay;
                currDir.direction = findDirection(g, route.get(i - 1), route.get(i), route.get(i + 1));
                currDir.distance = segmentDist;
                currWay = nextWay;
            }
        }

        result.add(currDir);
        return result;
    }


    private static int findDirection(GraphDB g, long prev, long curr, long next) {
        double bearingPrev = g.bearing(prev, curr);
        double bearingNext = g.bearing(curr, next);
        double relBearing = bearingNext - bearingPrev;

        // 标准化角度到 [-180, 180)
        relBearing = (relBearing + 540) % 360 - 180;

        if (relBearing >= -15 && relBearing <= 15) {
            return NavigationDirection.STRAIGHT;
        } else if (relBearing > 15 && relBearing <= 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (relBearing > 30 && relBearing <= 100) {
            return NavigationDirection.RIGHT;
        } else if (relBearing > 100) {
            return NavigationDirection.SHARP_RIGHT;
        } else if (relBearing < -15 && relBearing >= -30) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (relBearing < -30 && relBearing >= -100) {
            return NavigationDirection.LEFT;
        } else {
            return NavigationDirection.SHARP_LEFT;
        }
    }




    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}

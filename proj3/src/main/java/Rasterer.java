import java.util.*;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public static final int TILE_SIZE = 256;
    private final List<Double> LonDPP_List = new LinkedList<>();

    private final double ROOT_ULLON = -122.2998046875;
    private final double ROOT_LRLON = -122.2119140625;
    private final double ROOT_ULLAT = 37.892195547244356;
    private final double ROOT_LRLAT = 37.82280243352756;


    public Rasterer() {
        for (int depth = 0; depth <= 7; depth++) {
            double lonDPP = (ROOT_LRLON - ROOT_ULLON) / (TILE_SIZE * Math.pow(2, depth));
            LonDPP_List.add(lonDPP);
        }
    }

    private double lonPerTile(int depth) {
        return (ROOT_LRLON - ROOT_ULLON) / (1 << depth);
    }

    private double latPerTile(int depth) {
        return (ROOT_ULLAT - ROOT_LRLAT) / (1 << depth);
    }

    private double tileUllon(int x,int depth) {
        return ROOT_ULLON + x * lonPerTile(depth);
    }

    private double tileLrlon(int x,int depth) {
        return ROOT_ULLON + (x + 1) * lonPerTile(depth);
    }

    private double tileUllat(int y,int depth) {
        return ROOT_ULLAT - y * latPerTile(depth);
    }

    private double tileLrlat(int y,int depth) {
        return ROOT_ULLAT - (y + 1) * latPerTile(depth);
    }

    private int getDepth(double queryLonDPP) {
        for (int i = 0; i < 8; i++) {
            if (LonDPP_List.get(i) <= queryLonDPP || i == 7) {
                return i;
            }
        }
        return 7;
    }


    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {

        Map<String, Object> results = new HashMap<>();
        double w = params.get("w");
        double h = params.get("h");
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");

        boolean query_success = !(lrlon <= ROOT_ULLON) && !(ullon >= ROOT_LRLON) && !(ullat <= ROOT_LRLAT) && !(lrlat >= ROOT_ULLAT) && !(ullon >= lrlon) && !(ullat <= lrlat);

        if (!query_success) {
            results.put("query_success", false);
            return results;
        }

        double LonDPP = (lrlon - ullon) / w;
        int depth = getDepth(LonDPP);

        int xStart = (int) Math.floor((ullon - ROOT_ULLON) / lonPerTile(depth));
        int xEnd = (int) Math.floor((lrlon - ROOT_ULLON) / lonPerTile(depth));
        int yStart = (int) Math.floor((ROOT_ULLAT - ullat) / latPerTile(depth));
        int yEnd = (int) Math.floor((ROOT_ULLAT - lrlat) / latPerTile(depth));
        xStart = Math.max(0, xStart);
        xEnd = Math.min((1 << depth) - 1, xEnd);
        yStart = Math.max(0, yStart);
        yEnd = Math.min((1 << depth) - 1, yEnd);

        double raster_ul_lon = tileUllon(xStart,depth);
        double raster_lr_lon = tileLrlon(xEnd,depth);
        double raster_ul_lat = tileUllat(yStart,depth);
        double raster_lr_lat = tileLrlat(yEnd,depth);

        String[][] render_grid = new String[yEnd - yStart + 1][xEnd - xStart + 1];
        for (int y = yStart; y <= yEnd; y++) {
            for (int x = xStart; x <= xEnd; x++) {
                render_grid[y - yStart][x - xStart] =
                        "d" + depth + "_x" + x + "_y" + y + ".png";
            }
        }

        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", depth);
        results.put("query_success", query_success);
        return results;
    }
}

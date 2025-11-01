package hw3.hash;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int N = oomages.size();
        LinkedHashMap<Integer,Integer> buckets = new LinkedHashMap<>();
        for (Oomage oomage : oomages) {
            int bucketNum = (oomage.hashCode() & 0x7FFFFFFF) % M;
            if (buckets.containsKey(bucketNum)) {
                buckets.put(bucketNum,buckets.get(bucketNum) + 1);
            } else {
                buckets.put(bucketNum,0);
            }
        }
        for (Map.Entry<Integer, Integer> entry : buckets.entrySet()) {
            if (entry.getValue() < N / 50 || entry.getValue() > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}

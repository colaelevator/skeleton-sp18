import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        if (asciis == null || asciis.length == 0) {
            return new String[0];
        }
        int maxLen = 0;
        for (String s : asciis) {
            if (s.length() > maxLen) {
                maxLen = s.length();
            }
        }
        String[] sorted = Arrays.copyOf(asciis, asciis.length);
        for (int i = maxLen - 1; i >= 0; i--) {
            sorted = sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] counts = new int[128];
        for (String s : asciis) {
            int c = index < s.length() ? s.charAt(s.length() - 1 - index) : 0;
            counts[c]++;
        }
        for (int i = 1; i < 128; i++) {
            counts[i] += counts[i - 1];
        }
        String[] sorted = new String[asciis.length];
        for (int i = asciis.length - 1; i >=0 ; i--) {
            int num = (index < asciis[i].length())
                    ? asciis[i].charAt(asciis[i].length() - 1 - index)
                    : 0;
            counts[num]--;
            sorted[counts[num]] = asciis[i];
        }
        return sorted;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        int R = 128;
        if (end - start <= 1) {
            return;
        }
        int[] counts = new int[R + 2];
        for (int i = start; i < end; i++) {
            int c = charAtOrZero(asciis[i], index);
            counts[c + 1]++;
        }
        for (int r = 0; r < R + 1; r++) {
            counts[r + 1] += counts[r];
        }
        String[] aux = new String[end - start];
        for (int i = start; i < end; i++) {
            int c = charAtOrZero(asciis[i], index);
            aux[counts[c]++] = asciis[i];
        }
        for (int i = 0; i < aux.length; i++) {
            asciis[start + i] = aux[i];
        }
        for (int r = 0; r < R; r++) {
            int lo = start + counts[r];
            int hi = start + counts[r + 1];
            if (hi - lo > 1) { // 至少两个元素才递归
                sortHelperMSD(asciis, lo, hi, index + 1);
            }
        }
    }

    private static int charAtOrZero(String s, int index) {
        if (index >= s.length()) {
            return 0;
        }
        return s.charAt(index);
    }

    public static String[] MSD(String[] asciis) {
        if (asciis == null || asciis.length == 0) {
            return new String[0];
        }
        String[] sorted = Arrays.copyOf(asciis, asciis.length);
        sortHelperMSD(sorted, 0, sorted.length, 0);
        return sorted;
    }
}

package processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class ImageProcessor {

    private   final int MINIMUM_DEVIATION_FROM_SD = 5;
    // private   final int DEFAULT_STAFF_THRESHOLD = 50;
    // private   final int DEFAULT_STAFF_LINE_THRESHOLD = 10;
    private   final int MAX_DIST_BETWEEN_LINES_WITHIN_STAFFLINE = 3;

    
    
    public int[] getBoundsForGame(Picture input, int patchX, int patchY, int tolerance){
    	return null;
    }
    
    /**
     * 
     * @param input
     *            : input picture object containing rgb values about pixels
     * @param patchX
     *            : height of the patch
     * @param patchY
     *            : length of the patch
     * @param tolerance
     *            : tolerance to intensity
     * @return returns a BitMap (black or white representation of array) Scans
     *         the input image patch by patch calculating the average intensity
     *         of the patch to loop over the pixels again comparing their
     *         intensity with that of the average and counting it as a dark spot
     *         if the intensity is above the average by a certain amount of
     *         tolerance
     */
    public BitMap genBitMap(Picture input, int patchX, int patchY,
            int tolerance) {

        int height = input.getHeight();
        int width = input.getWidth();
        Pixel[][] pixelMap = new Pixel[height][width];

        for (int i = 0; i < height / patchX + 1; i++) {
            for (int z = 0; z < width / patchY + 1; z++) {

                int avg = 0;

                for (int x = 0; x < patchX && i * patchX + x < height; x++) {
                    
                    for (int y = 0; y < patchY && z * patchY + y < width; y++) {
                    
                        Color pixelColor = input.getPixel(y + z * patchY, x + i
                                * patchX); // in the ith patch at the xth
                                           // offset.. zth patch yth offset

                        avg += (pixelColor.getBlue() + pixelColor.getGreen() + pixelColor
                                .getRed()) / 3; // adds the intensity of the
                                                // pixel
                    }
                }

                avg /= (patchX * patchY); // assumes the patch has area patchX *
                                          // patchY (need to handle edge cases)

                for (int x = 0; x < patchX && i * patchX + x < height; x++) {
                    for (int y = 0; y < patchY && z * patchY + y < width; y++) {

                        Color pixelColor = input.getPixel(y + z * patchY, x + i
                                * patchX);

                        int intensity = (pixelColor.getBlue()
                                + pixelColor.getGreen() + pixelColor.getRed()) / 3;

                        if (avg - intensity > tolerance)
                            pixelMap[x + i * patchX][y + z * patchY] = new Pixel(
                                    !Pixel.MARKED, Pixel.DARK);// creates a new
                                                               // pixel. First
                                                               // parameter sets
                                                               // the pixel to
                                                               // unmarked.
                                                               // The second
                                                               // sets it to
                                                               // Dark
                        else
                            pixelMap[x + i * patchX][y + z * patchY] = new Pixel(
                                    !Pixel.MARKED, !Pixel.DARK);

                    }
                }
            }
        }

        return new BitMap(pixelMap);

    }

    /**
     * To extract the staff lines, we extract all horizontal lines using the
     * hough transform We then sort them according to their y intercept. we then
     * form a new list which consists of the differences between the y
     * intercepts. Using this data we figure out a threshold for the distance
     * between staff lines. and the difference between staffs then we iterate
     * through the original set of lines. if the difference between the current
     * and the next is above the threshold of the staff then we create a new
     * staff and within the new staff we create a new staff line to which we add
     * this line. Also remember that if we come across a line that goes above
     * the threshold for a staff line before 5 staff lines have been added to
     * the current staff then junk the 4 or less lines that have been added. if
     * the difference is greater than the staff line threshold then we set the
     * bottom right of the current end point to the end point of the current
     * staff line. We also create a new staff line and add the staff line to the
     * current staff. We set the top left corner of the current staff to be the
     * start point of the current line
     * 
     * @param bmp
     * @param threshold
     * @return
     */
   
    private   Tuple<Integer, Integer> getThresholds(double[] lineSpacings) {
        Arrays.sort(lineSpacings);
        List<Tuple<Integer, Integer>> clumpsAndClumpSize = new ArrayList<Tuple<Integer, Integer>>();
        double avg = lineSpacings[0];
        double sd = 0;
        System.out.println(Arrays.toString(lineSpacings));
        int numElems = 1;
        for (int i = 1; i < lineSpacings.length; i++, numElems++) {
            System.out.println(avg + " " + lineSpacings[i] + " "
                    + (Math.abs(avg - lineSpacings[i]) - sd));
            if (Math.abs(avg - lineSpacings[i]) - sd > MINIMUM_DEVIATION_FROM_SD) {
                clumpsAndClumpSize.add(new Tuple<Integer, Integer>(
                        (int) lineSpacings[i - 1] + 1, numElems));
                avg = lineSpacings[i];
                numElems = 1;

            } else {
                avg = (avg * (numElems - 1) + lineSpacings[i]) / (numElems);
                sd = calcSD(lineSpacings, avg, i - numElems + 1, numElems);
            }
        }
        clumpsAndClumpSize.add(new Tuple<Integer, Integer>(
                (int) lineSpacings[lineSpacings.length - 1] + 1, numElems));
        Collections.sort(clumpsAndClumpSize,
                new Comparator<Tuple<Integer, Integer>>() {

                    @Override
                    public int compare(Tuple<Integer, Integer> arg0,
                            Tuple<Integer, Integer> arg1) {

                        return -arg0.getY() + arg1.getY();
                    }

                });
        System.out.println(clumpsAndClumpSize);
        // x holds the staff line threshold
        // y holds the staff threshold
        int min = Math.min(clumpsAndClumpSize.get(1).getX(), clumpsAndClumpSize
                .get(0).getX());
        int staffLineThreshold = min;
        int staffThreshold = Math.max(clumpsAndClumpSize.get(1).getX(),
                clumpsAndClumpSize.get(0).getX());
        if (min > MAX_DIST_BETWEEN_LINES_WITHIN_STAFFLINE) {
            staffLineThreshold = MAX_DIST_BETWEEN_LINES_WITHIN_STAFFLINE;
            staffThreshold = min + 1;
        }

        return new Tuple<Integer, Integer>(staffLineThreshold, staffThreshold);

    }

   

    private   double calcSD(double[] lineSpacings, double avg, int s,
            int len) {
        double sd = 0;
        for (int j = s; j < s + len; j++) {
            sd += Math.pow(lineSpacings[j] - avg, 2);
        }
        sd = Math.sqrt(sd);
        sd /= len;
        return sd;
    }

    private   double calcSD(
            List<Tuple<Integer, Integer>> orderedDarkSpotsPerRow, double avg,
            int i) {
        double sd = 0;
        for (int j = 0; j < i; j++) {
            sd += Math.pow(orderedDarkSpotsPerRow.get(j).getX() - avg, 2);
        }
        sd = Math.sqrt(sd);
        sd /= i;
        return sd;
    }

    // the lineSpacings are only of distances between lines that are a part of
    // staff lines.
    // therefore these distance clump into one of three groups.
    // the first group is for spacings between lines part of the same staff line
    // the next group is for line spacings corresponding to adjacent lines of
    // adjacent staff line.
    // The diff between the mean and the current element - sd >
    // minimum_devation_from_sd
    // if the current clump is for diff between lines within a staff line then
    // the standard
    // deviation is reset as well as the average. and we note the threshold to
    // be this minimum value
    // at a point if we have calculated the mean m, the new mean is (m*n +
    // ai)/(n+1)
    // the standard deviation is recalculated by summing the square of the
    // differences
    // from the mean, square rooting it and dividing by the number of elements
    //

    private   double[] getLineSpacings(List<Line> lines) {
        double[] lineSpacings = new double[lines.size() - 1];
        Iterator<Line> iter = lines.iterator();
        Line prev = iter.next();
        Line cur = prev;
        for (int i = 0; iter.hasNext(); i++) {
            prev = cur;
            cur = iter.next();
            lineSpacings[i] = cur.getYIntercept() - prev.getYIntercept();
        }

        return lineSpacings;
    }

}

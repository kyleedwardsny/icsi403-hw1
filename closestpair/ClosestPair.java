/** A main class for the Closest Pair algorithms
 *  Programming assignment for
 *  CSI403 Algorithms and Data Structures
 *  University at Albany - SUNY
 *  
 * Instructions: Implement methods: 
 * 1) getCPBruteForce()
 * 2) getCPDivideAndConquer()
 * As discussed in class and in the assignment part (a)
 */
package closestpair;
import java.util.HashSet;

public class ClosestPair {
	
	/** TODO: IMPLEMENT 
	 *  A brute force method for the closest pair
	 *  @returns an array of exactly the two closest points
	 *  IMPORTANT: DO NOT CHANGE THIS METHOD SIGNATURE,
	 *  ONLY THE BODY WILL BE CONSIDERED FOR GRADING
	 */
	public static Point[] getCPBruteForce (Point[] pts)  {
		// Get the distance between the first two points
		Point[] result = new Point[2];
		result[0] = pts[0];
		result[1] = pts[1];
		double closest = pts[0].dist(pts[1]);

		// Iterate through all points except the last
		for (int i = 0; i < pts.length - 1; ++i) {
			// Iterate through all points after the current
			for (int j = i + 1; j < pts.length; ++j) {
				double tmp = pts[i].dist(pts[j]);
				// If this pair is closer than the current min, save it
				if (tmp < closest) {
					closest = tmp;
					result[0] = pts[i];
					result[1] = pts[j];
				}
			}
		}
		return result;
	}
	
	/** A driver for the Divide-And-Conquer method for the closest pair
	 *  takes unsorted array of points, sorts them and invokes 
	 *  the recursive method you are required to implement
	 *  
	 *  @returns an array of exactly the two closest points
	 *  IMPORTANT: DO NOT CHANGE THIS METHOD
	 */
	public static Point[] getCPDivideAndConquer(Point[] pts) {
		Point[] ptsX = Point.sortByX(pts); 
		Point[] ptsY = Point.sortByY(pts);
		return getCPDivideAndConquer(ptsX, ptsY);
	}
	
	/** TODO: IMPLEMENT 
	 *  A Divide-And-Conquer method for the closest pair
	 *  takes as input the points sorted by increasing x
	 *  and y coordinates in ptsX and ptsY respectively
	 *  @returns an array of exactly the two closest points.
	 *  IMPORTANT: DO NOT CHANGE THIS METHOD SIGNATURE,
	 *  ONLY THE BODY WILL BE CONSIDERED FOR GRADING
	 */
	public static Point[] getCPDivideAndConquer(Point[] ptsX, Point[] ptsY) {
		// If n <= 3 then do brute force
		if (ptsX.length <= 3) {
			return getCPBruteForce(ptsX);
		}

		// Find the median x and use that as the vertical line
		int lIndex = ptsX.length / 2;
		double l = (ptsX[lIndex - 1].x + ptsX[lIndex].x) / 2;

		// Divide ptsX and ptsY into points left of line and points right of line
		Point[] xl = new Point[lIndex];
		Point[] xr = new Point[ptsX.length - lIndex];
		Point[] yl = new Point[lIndex];
		Point[] yr = new Point[ptsX.length - lIndex];
		int xltmp = 0;
		int xrtmp = 0;
		int yltmp = 0;
		int yrtmp = 0;

		for (int i = 0; i < ptsX.length; ++i) {
			if (ptsX[i].x < l) {
				xl[xltmp] = ptsX[i];
				++xltmp;
			} else {
				xr[xrtmp] = ptsX[i];
				++xrtmp;
			}
			if (ptsY[i].x < l) {
				yl[yltmp] = ptsY[i];
				++yltmp;
			} else {
				yr[yrtmp] = ptsY[i];
				++yrtmp;
			}
		}

		// Do the recursion
		Point[] lresult = getCPDivideAndConquer(xl, yl);
		Point[] rresult = getCPDivideAndConquer(xr, yr);
		double min = Math.min(lresult[0].dist(lresult[1]), rresult[0].dist(rresult[1]));

		// Check points in the vertical strip within min distance of the vertical line
		Point[] ptsYP = new Point[ptsX.length];
		int ptsYPSize = 0;
		for (int i = 0; i < ptsY.length; ++i) {
			if (Math.abs(ptsY[i].x - l) < min) {
				ptsYP[ptsYPSize] = ptsY[i];
				++ptsYPSize;
			}
		}
		double min2 = min;
		Point[] result = new Point[2];
		for (int i = 0; i < ptsYPSize - 1; ++i) {
			// Check only the 7 points after the current point
			for (int j = i + 1; j < Math.min(i + 7, ptsYPSize); ++j) {
				if (ptsYP[i].dist(ptsYP[j]) < min2) {
					min2 = ptsYP[i].dist(ptsYP[j]);
					result[0] = ptsYP[i];
					result[1] = ptsYP[j];
				}
			}
		}

		// If the min within this strip is less than the current min, use the new min
		if (min2 < min) {
			return result;
		}

		// Return the lesser of the left result and right result
		if (lresult[0].dist(lresult[1]) < rresult[0].dist(rresult[1])) {
			return lresult;
		}
		return rresult;
	}
}

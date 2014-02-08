package za.ac.sun.cs.semdiffplugin.lcs;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to compute the longest common subsequence in two strings. Algorithms
 * from Wikipedia:
 * http://en.wikipedia.org/wiki/Longest_common_subsequence_problem
 * 
 * @author jhess
 * 
 */
public abstract class LongestCommonSubsequence<VALUE> {

	private int[][] c;
	private ArrayList<DiffEntry<VALUE>> diff;
	private ArrayList<VALUE> backtrack;

	/**
	 * A constructor for classes inheriting this one, allowing them to do some
	 * initialization before setting the values of X and Y. Once the
	 * initialization is complete, the inheriting class must call
	 * initValues(VALUE[] x, VALUE[] y)
	 */
	protected LongestCommonSubsequence() {

	}

	protected abstract int lengthOfX();

	protected abstract int lengthOfY();

	protected abstract VALUE valueOfX(int index);

	protected abstract VALUE valueOfY(int index);

	protected abstract boolean isEquals(VALUE x, VALUE y);

	private boolean isXYEqual(int i, int j) {
		return isEquals(valueOfXInternal(i), valueOfYInternal(j));
	}

	private VALUE valueOfXInternal(int i) {
		return valueOfX(i - 1);
	}

	private VALUE valueOfYInternal(int j) {
		return valueOfY(j - 1);
	}

	public void calculateLcs() {
		if (c != null) {
			return;
		}
		c = new int[lengthOfX() + 1][];
		for (int i = 0; i < c.length; i++) {
			c[i] = new int[lengthOfY() + 1];
		}

		for (int i = 1; i < c.length; i++) {
			for (int j = 1; j < c[i].length; j++) {
				if (isXYEqual(i, j)) {
					c[i][j] = c[i - 1][j - 1] + 1;
				} else {
					c[i][j] = max(c[i][j - 1], c[i - 1][j]);
				}
			}
		}
	}

	public int getLcsLength() {
		calculateLcs();
		return c[lengthOfX()][lengthOfY()];
	}

	public int getMinEditDistance() {
		calculateLcs();
		return lengthOfX() + lengthOfY() - 2 * abs(getLcsLength());
	}

	public List<VALUE> backtrack() {
		calculateLcs();
		if (this.backtrack == null) {
			this.backtrack = new ArrayList<VALUE>();
			backtrack(lengthOfX(), lengthOfY());
		}
		return this.backtrack;
	}

	public void backtrack(int i, int j) {
		calculateLcs();

		if (i == 0 || j == 0) {
			return;
		} else if (isXYEqual(i, j)) {
			backtrack(i - 1, j - 1);
			backtrack.add(valueOfXInternal(i));
		} else {
			if (c[i][j - 1] > c[i - 1][j]) {
				backtrack(i, j - 1);
			} else {
				backtrack(i - 1, j);
			}
		}
	}

	public List<DiffEntry<VALUE>> diff() {
		calculateLcs();
		if (this.diff == null) {
			this.diff = new ArrayList<DiffEntry<VALUE>>();
			diff(lengthOfX(), lengthOfY());
		}
		return this.diff;
	}

	private void diff(int i, int j) {
		calculateLcs();

		while (!(i == 0 && j == 0)) {
			if (i > 0 && j > 0 && isXYEqual(i, j)) {
				this.diff.add(new DiffEntry<VALUE>(DiffType.NONE,
						valueOfXInternal(i)));
				i--;
				j--;

			} else {
				if (j > 0 && (i == 0 || c[i][j - 1] >= c[i - 1][j])) {
					this.diff.add(new DiffEntry<VALUE>(DiffType.ADD,
							valueOfYInternal(j)));
					j--;

				} else if (i > 0 && (j == 0 || c[i][j - 1] < c[i - 1][j])) {

					this.diff.add(new DiffEntry<VALUE>(DiffType.REMOVE,
							valueOfXInternal(i)));
					i--;
				}
			}
		}

		Collections.reverse(this.diff);
	}

	@Override
	public String toString() {
		calculateLcs();

		StringBuffer buf = new StringBuffer();
		buf.append("  ");
		for (int j = 1; j <= lengthOfY(); j++) {
			buf.append(valueOfYInternal(j));
		}
		buf.append("\n");
		buf.append(" ");
		for (int j = 0; j < c[0].length; j++) {
			buf.append(Integer.toString(c[0][j]));
		}
		buf.append("\n");
		for (int i = 1; i < c.length; i++) {
			buf.append(valueOfXInternal(i));
			for (int j = 0; j < c[i].length; j++) {
				buf.append(Integer.toString(c[i][j]));
			}
			buf.append("\n");
		}

		return buf.toString();
	}

	private static enum DiffType {
		ADD, REMOVE, NONE;
	}

	public static class DiffEntry<VALUE> {

		private DiffType type;
		private VALUE value;

		public DiffEntry(DiffType type, VALUE value) {
			super();
			this.type = type;
			this.value = value;
		}

		public void setType(DiffType type) {
			this.type = type;
		}

		public void setValue(VALUE value) {
			this.value = value;
		}

		public DiffType getType() {
			return this.type;
		}

		public VALUE getValue() {
			return this.value;
		}

		public boolean isAdded() {
			return this.type == DiffType.ADD;
		}

		public boolean isRemoved() {
			return this.type == DiffType.REMOVE;
		}

		public boolean isSame() {
			return this.type == DiffType.NONE;
		}

	}

}
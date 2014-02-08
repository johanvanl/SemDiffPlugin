package za.ac.sun.cs.semdiffplugin.lcs;

import java.util.Arrays;
import java.util.List;

public class LcsTextFile extends LongestCommonSubsequence<String> {

	List<String> X = null;
	List<String> Y = null;

	public LcsTextFile(List<String> original, List<String> revised) {
		this.X = original;
		this.Y = revised;
	}

	public LcsTextFile(String original, String revised) {
		this(stringToLines(original), stringToLines(revised));
	}

	private static List<String> stringToLines(String text) {
		String[] arr = text.split("\\r?\\n");
		List<String> out = Arrays.asList(arr);
		return out;
	}

	@Override
	protected int lengthOfX() {
		return this.X.size();
	}

	@Override
	protected int lengthOfY() {
		return this.Y.size();
	}

	@Override
	protected String valueOfX(int index) {
		return this.X.get(index);
	}

	@Override
	protected String valueOfY(int index) {
		return this.Y.get(index);
	}

	@Override
	protected boolean isEquals(String x, String y) {

		if (x == null) {
			return false;
		}

		return x.equals(y);
	}

}

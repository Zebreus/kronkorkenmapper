import java.util.Comparator;

public class SegmentComparator implements Comparator<Segment>{

	@Override
	public int compare(Segment arg0, Segment arg1) {
		return arg0.getBestRating()[0]-arg1.getBestRating()[0];
	}
	
}

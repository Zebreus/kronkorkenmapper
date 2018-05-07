import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	static int TYPE = 2;
	static final int SEGMENT_HEIGHT = 20;
	static final int SEGMENT_LENGTH = 20;
	static int horizontal;
	static int vertical;
	static Picture target;
	static Segment[] segments;
	static Segment[][] finished;
public static void main(String[] args) throws Exception{
	setup("demoInstructions.txt");
	for(int xx = 0;xx<horizontal;xx++){
		for(int yy = 0;yy<vertical;yy++){
			for(Segment s:segments){
						s.rate(xx, yy);
			}
		}
	}
	System.out.println(segments[0].getBestRating()[0]);
	int prev = 0;
	for(int i = 0;i<vertical*horizontal;i++){
		Arrays.sort(segments,new SegmentComparator());
		int id=0;
		while(segments[id].amount==0){
			id++;
		}
		int[] bestrate = segments[id].getBestRating();
		finished[bestrate[1]][bestrate[2]] = segments[id];
		segments[id].amount--;
		for(Segment s:segments){
			s.removeRating(bestrate[1], bestrate[2]);
		}
		if((int)(((double)i/(double)(vertical*horizontal))*100)>prev){
			prev = (int)(((double)i/(double)(vertical*horizontal))*100);
			System.out.println(prev+"% matched");
		}
//		System.out.println(i+" of "+vertical*horizontal+" pairings done");
	}
	target.finish();
}
public static void setup(String filename) throws Exception{
	Scanner s = new Scanner(new FileInputStream(new File("res/instructions/"+filename)));
	int width = -1;
	int height = -1;
	if(s.next().equals("kkmInstruction")){
		while(s.hasNext()){
			String c =  s.next();
			switch (c){
			case "kk":
				String in = "";
				in = s.next();
				LinkedList<Segment> seg = new LinkedList();
				while(!in.equals("kk")){
					seg.add(new Segment(in,Integer.parseInt(s.next())));
					in = s.next();
				}
				segments = new Segment[seg.size()];
				for(int i = 0;i<segments.length;i++){
					segments[i]=seg.poll();
				}
				break;
			case "pic":
				String pic = s.next();
				target=new Picture(pic,width,height);
				if(!s.next().equals("pic")){
					System.err.println("Malformed Fild at pic");
					throw new Exception();
				}
				break;
			case "width":
				int wid = Integer.parseInt(s.next());
				if(wid<=0){
					width=0;
				}else{
					width=wid;
				}
				if(!s.next().equals("width")){
					System.err.println("Malformed Fild at width");
					throw new Exception();
				}
				break;
			case "height":
				int hid = Integer.parseInt(s.next());
				if(hid<=0){
					height=0;
				}else{
					height=hid;
				}
				if(!s.next().equals("height")){
					System.err.println("Malformed Fild at height");
					throw new Exception();
				}
				break;
			}
		}
		finished = new Segment[horizontal][vertical];
	}
}
private static class SegmentComparator implements Comparator{

	@Override
	public int compare(Object arg0, Object arg1) {
		Segment seg1 = (Segment)arg0;
		Segment seg2 = (Segment)arg1;
		
		return seg1.getBestRating()[0]-seg2.getBestRating()[0];
	}
	
}
}

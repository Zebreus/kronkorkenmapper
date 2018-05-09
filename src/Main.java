import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.input.stax.DTDParser;

public class Main {
	static final String VERSION = "0.1.1";
	static int TYPE = 2;
	static final int SEGMENT_HEIGHT = 20;
	static final int SEGMENT_LENGTH = 20;
	static int horizontal;
	static int vertical;
	static Picture target;
	static Segment[] segments;
	static Segment[][] finished;
public static void main(String[] args) throws Exception{
	
	String instructions = "";
	
	for(int i = 0;i<args.length;i++){
		switch(args[i]) {
		case "-h":
			
			Output.NORMAL.logln("-h		Display this text");
			Output.NORMAL.logln("-i <instructionfile>	Use the given instructions");
			Output.NORMAL.logln("-d		Show debug output");
			Output.NORMAL.logln("-v		Show much output");
			Output.NORMAL.logln("-e		Show only errors");
			Output.NORMAL.logln("-s		Show no output");
			Output.NORMAL.logln("\nkronkorkenmapper version: "+VERSION);
			System.exit(0);
			break;
		case "-i":
			if(i+1<args.length){
				i++;
				instructions = args[i];
			}else {
				Output.ERROR.logln("Missing instruction file after -i");
			}
			break;
		case "-d":
			Output.level = Output.DEBUG;
			break;
		case "-v":
			Output.level = Output.HIGH;
			break;
		case "-e":
			Output.level = Output.ERROR;
			break;
		case "-s":
			Output.level = Output.NONE;
			break;
		default:
			Output.ERROR.logln("Invalid option -- "+args[i]);
			Output.ERROR.logln("Try kkmapper -h for help");
			break;
		}
	}
	Output.HIGH.logln("Outputlevel is "+Output.level.toString());
	if(!instructions.equals("")){
		setupXML(instructions);
	}else {
		Output.ERROR.logln("Missing instructions, quitting");
		System.exit(0);
	}
	
	for(int xx = 0;xx<horizontal;xx++){
		for(int yy = 0;yy<vertical;yy++){
			for(Segment s:segments){
						s.rate(xx, yy);
			}
		}
	}
	
	Output.DEBUG.logln("segments[0].getBestRating()[0]: "+segments[0].getBestRating()[0]);
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
			Output.NORMAL.logln(prev+"% matched");
		}
//		System.out.println(i+" of "+vertical*horizontal+" pairings done");
	}
	target.finish();
}

public static void setupXML(String filename){
	Output.HIGH.logln("Loading instruction file "+filename);
	//TODO finish dtd implementation
	Document doc = new Document();
	SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
	
	try {
		doc = builder.build(filename);
		Output.HIGH.logln("Successfully loaded and validatet instruction file");
		
		Element root = doc.getRootElement();
		
		
		String pictureDir = root.getAttributeValue("picture_directory");
		String subpictureDir = root.getAttributeValue("subpicture_directory");
		Output.DEBUG.logln("pictureDir: "+pictureDir);
		Output.DEBUG.logln("segmentDir: "+subpictureDir);
		
		List<Element> children = root.getChildren();
		
		for(Element picture:children){
			int width = 0;
			int height = 0;
			
			String pWidth = picture.getAttributeValue("width");
			String pHeight = picture.getAttributeValue("height");
			String file = picture.getAttributeValue("file");
			
			if(pWidth==null||pWidth.equals("")) {
				Output.HIGH.logln("No width specified, guessing value");
				width = 0;
			}else if(pWidth.matches("^[0-9]+$")){
				width = Integer.parseInt(pWidth);
				Output.DEBUG.logln("Width successfully parsed");
			}else {
				InstructionParsingException ipe = new InstructionParsingException("Could not parse width value "+pWidth+" for picture "+file);
				Output.logException(ipe, "Invalid width specified, guessing value");
			}
			
			if(pHeight==null||pHeight.equals("")) {
				Output.HIGH.logln("No height specified, guessing value");
				height = 0;
			}else if(pHeight.matches("^[0-9]+$")){
				height = Integer.parseInt(pHeight);
				Output.DEBUG.logln("Height successfully parsed");
			}else {
				InstructionParsingException ipe = new InstructionParsingException("Could not parse height value "+pHeight+" for picture "+file);
				Output.logException(ipe, "Invalid height specified, guessing value");
			}
			
			Output.DEBUG.logln("picture file: "+file);
			
			try {
				//TODO horizontal and vertical seemingly get set here
				target = new Picture(pictureDir+file,width,height);
			}catch(IOException e) {
				Output.logException(e,"Failed to load picture");
				System.exit(1);
			}
			
			Output.HIGH.logln("Picture "+file+" loaded");
			
			segments = new Segment[picture.getChildren().size()];
			int pos = 0;
			for(Element subpicture:picture.getChildren()){
				int amount = 0;
				String Sfile = subpicture.getAttributeValue("file");
				String Samount = subpicture.getAttributeValue("amount");
				
				if(Samount.equals("infinite")||Samount.equals("")){
					amount = Integer.MAX_VALUE;
				}else if(Samount.matches("^[0-9]+$")){
					amount = Integer.parseInt(Samount);
				}else {
					InstructionParsingException ipe = new InstructionParsingException("Could not parse segment amount "+Samount+" for segment "+Sfile);
					Output.logException(ipe, "Invalid amount");
					System.exit(1);
				}
				
				try {
				segments[pos] = new Segment(subpictureDir+Sfile,amount);
				}catch(IOException e) {
					Output.logException(e,"Failed to load segment");
					System.exit(1);
				}
				Output.HIGH.logln("Segment "+Sfile+" loaded");
				pos++;
			}
			
			//TODO multipicture support
			break;
		}
		
		finished = new Segment[horizontal][vertical];
		Output.HIGH.logln("Loaded instruction file");
	} catch (JDOMException e) {
		Output.logException(e, "Failed to parse instruction file");
		System.exit(1);
	} catch (IOException e) {
		Output.logException(e,"Failed to load instruction file");
		System.exit(1);
	}
}


}

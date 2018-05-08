import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Segment{
Pixel[][] pixels;
int[][] ratings;
int amount;
BufferedImage original;
BufferedImage scaled;
public Segment(String path,int amount) throws IOException{
	this.amount=amount;
	File f = new File(path);
	original = ImageIO.read(f);
	scaled = scaleSegment(original);
	ratings = new int[Main.horizontal][Main.vertical];
	for(int x = 0;x<Main.horizontal;x++){
		for(int y = 0;y<Main.vertical;y++){
			ratings[x][y] = -1;
		}
	}
	pixels = new Pixel[Main.SEGMENT_LENGTH][Main.SEGMENT_HEIGHT];
	for(int x = 0;x<Main.SEGMENT_LENGTH;x++){
		for(int y = 0;y<Main.SEGMENT_HEIGHT;y++){
			Color c = new Color(scaled.getRGB(x, y));
			Pixel p = new Pixel(c);
			pixels[x][y] = p;
		}
	}
}
public int[] getBestRating(){
//	int m = (int)(Math.random()*10000);
	int best = -1;
	int x = -1;
	int y = -1;
	for(int xx = 0;xx<Main.horizontal;xx++){
		for(int yy = 0;yy<Main.vertical;yy++){
			if(ratings[xx][yy]!=-1){
			if(best==-1){
				best=ratings[xx][yy];
				x=xx;
				y=yy;
			}else{
				
				if(ratings[xx][yy]<best){
//					System.out.println(m+"hh"+ratings[xx][yy]);
					best=ratings[xx][yy];
					x=xx;
					y=yy;
				}
			}
			}
		}
	}
	int[] ret = {best,x,y};
	return ret;
}
public void removeRating(int x, int y){
	ratings[x][y]=-1;
}
public void showScaled(){
	JFrame jf = new JFrame();
	ImageIcon imageIcon = new ImageIcon(scaled);
    JLabel jLabel = new JLabel();
    jLabel.setIcon(imageIcon);
    jf.getContentPane().add(jLabel, BorderLayout.CENTER);
    jf.pack();
    jf.setLocationRelativeTo(null);
    jf.setVisible(true);
}
public void showOriginal(){
	JFrame jf = new JFrame();
	ImageIcon imageIcon = new ImageIcon(scaled);
    JLabel jLabel = new JLabel();
    jLabel.setIcon(imageIcon);
    jf.getContentPane().add(jLabel, BorderLayout.CENTER);
    jf.pack();
    jf.setLocationRelativeTo(null);
    jf.setVisible(true);
}
public static BufferedImage scaleSegment(BufferedImage sbi) {
	int height = Main.SEGMENT_HEIGHT;
	int length = Main.SEGMENT_LENGTH;
    BufferedImage dbi = null;
    if(sbi != null) {
        dbi = new BufferedImage(length, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = dbi.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance((float)length/(float)sbi.getWidth(), (float)height/(float)sbi.getHeight());
        g.drawRenderedImage(sbi, at);
    }
    return dbi;
}
public int rate(int xPos,int yPos) throws Exception{
	if(ratings[xPos][yPos]==-1){
	if(xPos>Main.horizontal||yPos>Main.vertical){
		System.err.println("Tried to rate at invalid Position");
		throw new Exception();
	}
	int rating = 0;
	Picture pic=Main.target;
	for(int x = 0;x<Main.SEGMENT_LENGTH;x++){
		for(int y = 0;y<Main.SEGMENT_HEIGHT;y++){
			rating += pixels[x][y].difference(pic.pixels[xPos*Main.SEGMENT_LENGTH+x][yPos*Main.SEGMENT_HEIGHT+y]);
		}
	}
	ratings[xPos][yPos] = rating;
	return rating;
	}else
	return ratings[xPos][yPos];
}

}

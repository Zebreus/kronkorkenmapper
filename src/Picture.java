import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Picture {
Pixel[][] pixels;
BufferedImage original;
BufferedImage scaled;
BufferedImage finished;
public Picture(String path,int width,int height) throws IOException{
	File f = new File(path);
	original = ImageIO.read(f);
	scaled = scalePicture(original,height,width);
	finished = new BufferedImage(scaled.getWidth(),scaled.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
	pixels = new Pixel[scaled.getWidth()][scaled.getHeight()];
	for(int x = 0;x<scaled.getWidth();x++){
		for(int y = 0;y<scaled.getHeight();y++){
			Color c = new Color(scaled.getRGB(x, y));
			Pixel p = new Pixel(c);
			pixels[x][y] = p;
		}
	}
	showScaled();
}
public void finish(){
	for(int x = 0;x<Main.horizontal;x++){
		for(int y = 0;y<Main.vertical;y++){
			Pixel[][] pix = Main.finished[x][y].pixels;
			for(int xx = 0;xx<Main.SEGMENT_LENGTH;xx++){
				for(int yy = 0;yy<Main.SEGMENT_HEIGHT;yy++){
					finished.setRGB(x*Main.SEGMENT_LENGTH+xx, y*Main.SEGMENT_HEIGHT+yy, pix[xx][yy].getRGB());
				}
			}
		}
	}
	showFinished();
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
public void showFinished(){
	JFrame jf = new JFrame();
	ImageIcon imageIcon = new ImageIcon(finished);
    JLabel jLabel = new JLabel();
    jLabel.setIcon(imageIcon);
    jf.getContentPane().add(jLabel, BorderLayout.CENTER);
    jf.pack();
    jf.setLocationRelativeTo(null);
    jf.setVisible(true);
}
public static BufferedImage scalePicture(BufferedImage sbi, int vNum, int hNum) {
	if(vNum==0){
		if(hNum==0){
			vNum=((int)(sbi.getHeight()/Main.SEGMENT_HEIGHT));
			Output.HIGH.logln("Guessing width: "+vNum);
			hNum=((int)(sbi.getWidth()/Main.SEGMENT_LENGTH));
			Output.HIGH.logln("Guessing height: "+hNum);
		}else{
			vNum=(int)(sbi.getHeight()/((float)sbi.getWidth()/(float)hNum));
			Output.HIGH.logln("Guessing width: "+vNum);
		}
	}else if(hNum==0){
		hNum=(int)(sbi.getWidth()/((float)sbi.getHeight()/(float)vNum));
		Output.HIGH.logln("Guessing height: "+hNum);
	}
	Main.vertical=vNum;
	Main.horizontal=hNum;
	Output.DEBUG.logln("Height: "+hNum);
	Output.DEBUG.logln("Width: "+vNum);
	int height = vNum*Main.SEGMENT_HEIGHT;
	int length = hNum*Main.SEGMENT_LENGTH;
    BufferedImage dbi = null;
    if(sbi != null) {
        dbi = new BufferedImage(length, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = dbi.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance((float)length/(float)sbi.getWidth(), (float)height/(float)sbi.getHeight());
        g.drawRenderedImage(sbi, at);
    }
    return dbi;
}
}

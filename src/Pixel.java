import java.awt.Color;

public class Pixel {
	int r;
	int b;
	int g;
	
	float h;
	float s;
	float v;
	public Pixel(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Pixel(Color c) {
		this.r = c.getRed();
		this.g = c.getGreen();
		this.b = c.getBlue();
		float[] hsv = new float[3];
		Color.RGBtoHSB(r, g, b, hsv);
		h = hsv[0]*5;
		s=hsv[1]*5;
		v=hsv[2]*5;
	}

	public int getRGB() {
		Color c = new Color(r, g, b);
		return c.getRGB();
	}

	public int difference(Pixel pixel) {
		if (Main.TYPE == 0) {
			int dif = 0;
			dif += Math.abs(this.r - pixel.r);
			dif += Math.abs(this.g - pixel.g);
			dif += Math.abs(this.b - pixel.b);
			return dif;
		} else if (Main.TYPE == 1) {
			int dif = 0;
			dif = Math.abs(this.r - pixel.r);
			if (Math.abs(this.g - pixel.g) > dif) {
				dif = Math.abs(this.g - pixel.g);
			}
			if (Math.abs(this.b - pixel.b) > dif) {
				dif = Math.abs(this.b - pixel.b);
			}
			return dif;
		} else if (Main.TYPE == 2) {
			int dif = 0;
			dif = Math.abs(this.r - pixel.r);
			if (Math.abs(this.g - pixel.g) > dif) {
				dif = Math.abs(this.g - pixel.g);
			}
			if (Math.abs(this.b - pixel.b) > dif) {
				dif = Math.abs(this.b - pixel.b);
			}
			dif += Math.abs(this.r - pixel.r);
			dif += Math.abs(this.g - pixel.g);
			dif += Math.abs(this.b - pixel.b);
			return dif;
		}else if (Main.TYPE == 3) {
			int dif = 0;
			dif += Math.abs(this.h - pixel.h);
			dif += Math.abs(this.s - pixel.s);
			dif += Math.abs(this.v - pixel.v);
			return dif;
		}else if (Main.TYPE == 4) {
			int dif = 0;
			dif = Math.abs(this.r - pixel.r);
			if (Math.abs(this.g - pixel.g) > dif) {
				dif = Math.abs(this.g - pixel.g);
			}
			if (Math.abs(this.b - pixel.b) > dif) {
				dif = Math.abs(this.b - pixel.b);
			}
			dif += Math.abs(this.r - pixel.r);
			dif += Math.abs(this.g - pixel.g);
			dif += Math.abs(this.b - pixel.b);
			dif += Math.abs(this.h - pixel.h);
			dif += Math.abs(this.s - pixel.s);
			dif += Math.abs(this.v - pixel.v);
			return dif;
		}else if (Main.TYPE == 5) {
			//license tldr
			return 0;
		}
		else if (Main.TYPE == 6) {
			//license tldr
			return 0;
		}if (Main.TYPE == 7) {
			int dif = 0;
			dif = Math.abs(this.r - pixel.r);
			if (Math.abs(this.g - pixel.g) > dif) {
				dif = Math.abs(this.g - pixel.g);
			}
			if (Math.abs(this.b - pixel.b) > dif) {
				dif = Math.abs(this.b - pixel.b);
			}
			int dif2=0;
			dif2 += Math.abs(this.r - pixel.r);
			dif2 += Math.abs(this.g - pixel.g);
			dif2 += Math.abs(this.b - pixel.b);
			dif2 = dif2/3;
			dif+=dif2;
			return dif;
		}if (Main.TYPE == 8) {
			int dif = 0;
			dif = Math.abs(this.r - pixel.r);
			if (Math.abs(this.g - pixel.g) > dif) {
				dif = Math.abs(this.g - pixel.g);
			}
			if (Math.abs(this.b - pixel.b) > dif) {
				dif = Math.abs(this.b - pixel.b);
			}
			int dif2=0;
			dif2 += this.r - pixel.r;
			dif2 += this.g - pixel.g;
			dif2 += this.b - pixel.b;
			dif2 = dif2/3;
			dif+=Math.abs(dif2);
			return dif;
		}else
			return 0;
	}
}

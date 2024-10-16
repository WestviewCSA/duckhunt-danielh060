import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Character {
	private Image img;					
	private AffineTransform tx;
	private int vx, vy;							//for movement
	private int x, y;							//position
	private double xScale = 0.75, yScale = 0.75;	//scaling (sizing)
	private int width = 50, height = 50;
	private boolean showHitBox = true;
	
	public Character(String fileName) {
		img = getImage("/imgs/"+fileName); // load the image for Tree

		tx = AffineTransform.getTranslateInstance(0, 0);
		init(x, y); // initialize the location of the image
					// use your variables
	}

	public void toggleHitBox() {
		showHitBox = !showHitBox;
	}
	
	public Character() {
		img = getImage("/imgs/duck.gif"); // load the image for Tree

		tx = AffineTransform.getTranslateInstance(0, 0);
		init(x, y); // initialize the location of the image
					// use your variables
	}

	public void setWidthHeight(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setScale(double d, double e) {
		this.xScale = d;
		this.yScale = e;
		init(x, y);
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		init(x, y);
	}
	
	public int getVx() {
		return vx;
	}

	public void setVx(int vx) {
		this.vx = vx;
	}

	public int getVy() {
		return vy;
	}

	public void setVy(int vy) {
		this.vy = vy;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

	public void changePicture(String newFileName) {
		img = getImage(newFileName);
		init(x, y);
	}

	public void paint(Graphics g) {
		// these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		x += vx;
		y += vy;
		init(x,y);
		g2.drawImage(img, tx, null);
		
		
		if(showHitBox) 
		{
			g.setColor(Color.green);
			g2.setStroke(new BasicStroke(5));
			g2.drawRect(x, y, width, height);
		}
		
	}

	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(xScale, yScale);
	}

	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Character.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
package prototype;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

//prototype for the two games
public abstract class GamePrototype extends JPanel implements Runnable, Observer {

    public abstract void addFrameObserver(Observer obs);
    protected BufferedImage bufferimg;
    public static HashMap<String,Image> images;
    public static final Sound sound = new Sound();
    public static final FrameTracker frameTracker = new FrameTracker();
    private static Point speed=new Point(0,0);
    
    protected ArrayList<FixedObject> background;
    
    
    public static void setSpeed(Point speed){
    	GamePrototype.speed = speed;
    }
    
    public static Point getSpeed(){
    	return new Point(GamePrototype.speed);
    }

    public abstract Graphics2D createGraphics2D(int w, int h);
    
    public abstract Image seekImage(String path);
    
    abstract protected void putImages();

    public abstract void init();
    
    public abstract int getFrameNumber();
    
    public abstract void setDimensions(int w, int h);
    
    public abstract boolean isGameOver();
    
    public abstract void drawFrame(int w, int h, Graphics2D g2);
    
    public abstract void paint(Graphics g);
}

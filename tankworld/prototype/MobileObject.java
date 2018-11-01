package prototype;

import java.awt.Image;
import java.awt.Point;
import prototype.GameObject;
import prototype.MotionHandler;

public class MobileObject extends GameObject {

    protected int power;

    protected MotionHandler motion;

    public MobileObject(Point location, Point speed, Image img){
	super(location, speed, img);
	this.power=0;
        this.motion = new MotionHandler();
    }

    public int getPower(){
    	return power;
    }
    
    public void setPower(int power){
    	this.power = power;
    }
    
    @Override
    public void update(int w, int h){
    	motion.read(this);
    }
    
    public void start(){
    	motion.addObserver(this);
    }
}

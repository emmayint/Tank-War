package tank;

import prototype.MobileObject;
import prototype.MotionHandler;
import java.awt.Point;
import prototype.GameObject;


public class Bullet extends MobileObject {

    protected Tank owner;
    boolean friendly;

    public Bullet(Point location, Point speed, int power, MotionHandler motion, GameObject owner){
	super(location, speed, TankWorld.images.get("bullet"));
	this.power=power;
        if(owner instanceof Tank){
            this.owner = (Tank) owner;
            this.friendly=true;
            this.setImage(TankWorld.images.get("bullet"));
	}               
	this.motion = motion;
        motion.addObserver(this);
    }

    public Tank getOwner(){
        return owner;
    }

    public boolean isFriendly(){
        if(friendly){
            return true;
	}
        return false;
    }
}

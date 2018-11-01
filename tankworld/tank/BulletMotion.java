package tank;

import prototype.MobileObject;
import prototype.MotionHandler;

//for TankBullet
public class BulletMotion extends MotionHandler {
    
    int x,y;

    public BulletMotion(int direction) {
	super(TankWorld.getInstance());
    	y = (int)(10*(double)Math.cos(Math.toRadians(direction+90)));
    	x = (int)(10*(double)Math.sin(Math.toRadians(direction+90)));
    }
	
    public void read(Object theObject){
	MobileObject object = (MobileObject) theObject;
	object.move(x, y);
    }
}

package prototype;

import tank.Tank;
import tank.Bullet;
import java.util.Observer;
import tank.TankWorld;

//controlled by motion handler. observe motions
public abstract class Weapon extends Modifiable {

    protected Bullet[] bullets;
    int lastFired=0, reloadTime;
    protected int direction;
    public int reload = 5;
    boolean friendly;

    public Weapon(){
    }

    public Weapon(Observer world){
	super();
	this.addObserver(world);
    }
    
    public void remove(){
    }
	
    public void fireWeapon(Tank theShip){
	if(theShip instanceof Tank){
            direction = 1;
	}else{
            direction = -1;
	}
    }
	
    @Override
    public void read(Object game) {
        TankWorld world = (TankWorld) game;
	world.addBullet(bullets);	
    }
}

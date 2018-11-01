package tank;

import java.util.Observable;
import prototype.Modifiable;
import prototype.Weapon;
import java.awt.Point;

//powerup extents tank, with a field of weapon
public class PowerUp extends Tank {

    public PowerUp(Tank tank){
        super(tank.getLocationPoint(), tank.getSpeed(), 1, TankWorld.images.get("powerup"));
	this.weapon = tank.getWeapon();
    }
	
    public PowerUp(int location, int health, Weapon weapon){
        this(new Point(location, -100), health, weapon);
	this.weapon = weapon;
    }

    public PowerUp(Point location, int health, Weapon weapon){
        super(new Point(location),new Point(0,2), health, TankWorld.images.get("powerup"));
	this.weapon = weapon;
    }
	
    @Override
    public void update(Observable o, Object arg) {
        Modifiable modifier = (Modifiable) o;
	modifier.read(this);
    }
    //when tanks collide with powerup
    @Override
    public void die(){
    	this.show=false;
    	weapon.deleteObserver(this);
    	TankWorld.getInstance().removeClockObserver(motion);
    }

}

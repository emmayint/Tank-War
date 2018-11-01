package tank;

import java.awt.Point;
import prototype.Weapon;

//tanks fire powerup bullets after powerup
public class PowerUpBullet extends Weapon {

    public PowerUpBullet(){
        super(TankWorld.getInstance());
    }
    
    @Override
    public void fireWeapon(Tank tank) {
        super.fireWeapon(tank);
	Point location = tank.getLocationPoint();
        Point offset = tank.getGunLocation();
	location.x+=offset.x;
        location.y+=offset.y;
	Point speed = new Point(0,-15*direction);
        int strength = 7;
	reload = 15;		
	bullets = new Bullet[2];
	bullets[0] = new TankBullet(location, speed, strength, -5, (PlayerTank) tank);
        bullets[1] = new TankBullet(location, speed, strength, 5, (PlayerTank) tank);			
        this.setChanged();	
	this.notifyObservers();
    }
}

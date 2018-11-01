package tank;

import java.awt.Point;
import prototype.Weapon;

public class TankWeapon extends Weapon {

    public TankWeapon(){
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
        int strength = 10;
	reload = 15;
        TankBullet bullet = new TankBullet(location, speed, strength, (PlayerTank) tank);
	bullets = new Bullet[1];
        bullets[0] = bullet;			
        this.setChanged();
        this.notifyObservers();
    }
}

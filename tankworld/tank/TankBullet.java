package tank;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

public class TankBullet extends Bullet {

    public TankBullet(Point location, Point speed, int strength, PlayerTank owner){
	this(location, speed, strength, 0,owner);
    }
    
    public TankBullet(Point location, Point speed, int strength, int offset, PlayerTank owner){
        super(location, speed, strength, new BulletMotion(owner.direction+offset), owner);
	this.setImage(TankWorld.images.get("bullet"));
    }
	
    @Override
    public void draw(Graphics g, ImageObserver obs) {
    	if(show){
            g.drawImage(img, location.x, location.y, null);
    	}
    }
}

package tank;

import prototype.GameObject;
import prototype.FixedObject;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

public class BreakableWall extends FixedObject {

    public BreakableWall(int x, int y) {
        super(new Point(x * 32, y * 32), new Point(0, 0), TankWorld.images.get("wall2"));
    }
    //disappears on collision with bullets
    @Override
    public boolean collision(GameObject otherObject) {
        if (location.intersects(otherObject.getLocation())) {
            if (otherObject instanceof TankBullet) {
                this.show = false;
            }
            return true;
        } 
        return false;
    } 
    
    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (show) {
            super.draw(g, obs);
        }
    }
}

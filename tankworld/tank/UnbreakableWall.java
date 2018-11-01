package tank;

import prototype.FixedObject;
import java.awt.Point;

public class UnbreakableWall extends FixedObject {

    public UnbreakableWall(int x, int y){
	super(new Point(x*32, y*32), new Point(0,0), TankWorld.images.get("wall1"));
    }

    public void damage(int damage){
    }
}

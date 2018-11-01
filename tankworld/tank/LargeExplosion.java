package tank;

import prototype.GameObject;
import prototype.FixedObject;
import java.awt.Image;
import java.awt.Point;


// LargeExplosion when player dies. disappears by itself
public class LargeExplosion extends FixedObject {
    int timer;
    int frame;
    static Image bigExplosion = TankWorld.images.get("largeExplosion");

    public LargeExplosion(Point location) {
        super(location, bigExplosion);
	timer = 0;
        frame=0;
	TankWorld.sound.play("Resources/Explosion_large.wav");
    }
	
    public void update(int w, int h){
    	super.update(w, h);    
    	timer++;
    	if(timer%3==0){
            frame++;
            if(frame>3){
                this.show = false;
            }
    	}
    }

    @Override
    public boolean collision(GameObject otherObject){
        return false;
    }
}

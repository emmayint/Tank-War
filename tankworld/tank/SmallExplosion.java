package tank;

import prototype.FixedObject;
import java.awt.Image;
import java.awt.Point;
import prototype.GameObject;

//small explosion when bullets hit object. disappears by itself
public class SmallExplosion extends FixedObject {
    int timer;
    int frame;
    static Image smallExplosion = TankWorld.images.get("smallExplosion");
	
    public SmallExplosion(Point location) {
	super(location, smallExplosion);
	timer = 0;
        frame=0;
	TankWorld.sound.play("Resources/Explosion_small.wav");
    }

    @Override
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

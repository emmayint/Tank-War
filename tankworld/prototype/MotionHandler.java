package prototype;

import java.util.Observable;
import java.util.Observer;
import tank.Tank;
import tank.TankWorld;

public class MotionHandler extends Modifiable implements Observer {

    int fireInterval;
	
    public MotionHandler(){
        this(TankWorld.getInstance());
	fireInterval = -1;
    }
	
    public MotionHandler(GamePrototype game){
        game.addFrameObserver(this);
    }
	
    public void delete(Observer theObject){
	TankWorld.getInstance().removeClockObserver(this);
        this.deleteObserver(theObject);
    }
	
    //observe the frame tracker, control fire on frame change
    @Override
    public void update(Observable obs, Object obj){
        this.setChanged();
	this.notifyObservers();
    }

    @Override
    public void read(Object obj){
        Tank object = (Tank) obj;
	object.move();		
	if(TankWorld.getInstance().getFrameNumber()%fireInterval==0){
            object.fire();
	}
    }
}

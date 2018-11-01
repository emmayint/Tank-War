package tank;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Component;
import java.util.Observable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import prototype.MotionHandler;

public class InputHandler extends MotionHandler implements KeyListener{
    Field field;
    Method action;
    int moveState;
    int[] keys;
    boolean player;
	
    public InputHandler(Tank player){
        //set up keys
        this(player, new int[] {KeyEvent.VK_LEFT,KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE});
	moveState = 0;
        this.player = true;
    }
	
    public InputHandler(Tank player, int[] keys){
        this(player, keys, TankWorld.getInstance());

    }

    public InputHandler(Tank player, int[] keys, Component world){
	this.addObserver(player);
        this.action = null;
	this.field = null;
        this.keys = keys;
        world.addKeyListener(this);
    }

    public void signalKeyPress(KeyEvent e){
    }
	
    private void setMove(String direction) {
        try{
            field = Tank.class.getDeclaredField(direction);
            moveState=1;
            this.setChanged();
	}catch (Exception ex){
            ex.printStackTrace();
        }
	notifyObservers();
    }
	
    private void setFire(){
        field = null;
	try{
            action = Tank.class.getMethod("startFiring");
            this.setChanged();
	}catch(NoSuchMethodException ex){
            ex.printStackTrace();
        }
        notifyObservers();
    }	
	
    private void unsetMove(String direction) {
        try{
            field = Tank.class.getDeclaredField(direction);
            moveState = 0;
            this.setChanged();
	}catch (Exception ex){
            ex.printStackTrace();
        }
        notifyObservers();
    }
	
    private void unsetFire(){
        field = null;
        try{
            action = Tank.class.getMethod("stopFiring");
            this.setChanged();
	}catch(NoSuchMethodException ex){
            ex.printStackTrace();
        }
	notifyObservers();
    }
	
    public void read(Object theObject) {
	Tank player = (Tank) theObject;	
	try{
            field.setInt(player, moveState);
	}catch (Exception ex) {
            try {
                action.invoke(player);
            }catch (Exception e) {}
	}
    }
	
    public void clearChanged(){
        super.clearChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
    }
	
    public void keyPressed(KeyEvent event) {
        int code = event.getKeyCode();        
        if(code==keys[0]) {
            this.setMove("left");
	}else if(code==keys[1]) {
            this.setMove("up");
	}else if(code==keys[2]) {
            this.setMove("right");
	}else if(code==keys[3]) {
            this.setMove("down");
	}else if(code==keys[4]){
            this.setFire();
	}                
	setChanged();
        this.notifyObservers();
    }
    
    public void keyReleased(KeyEvent event) {
	int code = event.getKeyCode();
        if(code==keys[0]) {		
            this.unsetMove("left");
	}else if(code==keys[1]) {
            this.unsetMove("up");
	}else if(code==keys[2]) {
            this.unsetMove("right");
	}else if(code==keys[3]) {
            this.unsetMove("down");
	}else if(code==keys[4]){
            this.unsetFire();
	}
        setChanged();
	this.notifyObservers();
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}
package tank;

import prototype.Sound;
import prototype.MobileObject;
import java.util.Observable;
import java.util.Observer;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import prototype.Modifiable;
import prototype.Weapon;
import prototype.GameObject;
import prototype.MotionHandler;

public class Tank extends MobileObject implements Observer {
    protected String name;
    Weapon weapon;
    int health;
    Point gunLocation;
    int direction;
    int respawn;
    int score;
    Point resetPoint;
    int respawnCounter;
    int lastFired=0;
    boolean isFiring=false;
    //motion
    public int left=0, right=0, up=0, down=0;


    public Tank(Point location, Point speed, Image img, int[] controls, String name) {
        super(location,speed,img);
    	this.name = name;
        motion = new InputHandler(this, controls);
        this.power=100;
    	this.health=power;
    	this.gunLocation = new Point(15,20);
        resetPoint = new Point(location);
        this.gunLocation = new Point(18,0);        
        weapon = new TankWeapon();
        respawn = 3;
        health = 100;
        power = 100;
        score = 0;
        respawnCounter=0;
    }
    
    //ctors for powerup
    public Tank(Point location, Point speed, int power, Image img){
    	super(location,speed,img);
    	this.power=power;
    	this.health=power;
    	this.gunLocation = new Point(15,20);
    }
    
    public Tank(int x, Point speed, int strength, Image img){
    	this(new Point(x,-90), speed, strength, img);
    }
    
    //getters
    public int getLives(){
    	return this.respawn;
    }
    
    public int getScore(){
    	return this.score;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public Weapon getWeapon(){
    	return this.weapon;
    }
    
    public int getHealth(){
    	return health;
    }
    
    public MotionHandler getMotion(){
    	return this.motion;
    }
    
    public Point getGunLocation(){
    	return this.gunLocation;
    }
    
    public boolean isDead(){
    	if(respawn<0 && health<=0)
            return true;
    	else
            return false;
    }
    
    //setters
    
    public void incrementScore(int increment){
    	score += increment;
    }
    
    public void setHealth(int health){
    	this.health = health;
    }
        
    public void setWeapon(Weapon weapon){
    	this.weapon.remove();
    	this.weapon = weapon;
    }
    
    
    public void setMotion(MotionHandler motion){
    	this.motion = motion;
    }
    
    @Override
    public void draw(Graphics g, ImageObserver observer) {
    	if(respawnCounter<=0)
    		g.drawImage(img, location.x, location.y, observer);
    	else if(respawnCounter==80){
    		TankWorld.getInstance().addFrameObserver(this.motion);
    		respawnCounter -=1;
    	}
    	else if(respawnCounter<80){
    		if(respawnCounter%2==0) g.drawImage(img, location.x, location.y, observer);
    		respawnCounter -= 1;
    	}
    	else
    		respawnCounter -= 1;
    }

    public void damage(int damageDone){
    	if(respawnCounter<=0){
            this.health -= damageDone;
            if(health<=0){
    		this.die();
            }
            return;
        }	
    }

    public void update(int w, int h) {
    	if(isFiring){
    		int frame = TankWorld.getInstance().getFrameNumber();
    		if(frame>=lastFired+weapon.reload){
    			fire();
    			lastFired= frame;
    		}
    	}
    	
    	if((location.x>0 || right==1) && (location.x<w-width || left==1)){
    		location.x+=(right-left)*speed.x;
    	}
    	if((location.y>0 || down==1) && (location.y<h-height || up==1)){
    		location.y+=(down-up)*speed.x;
    	}
    }
    
    public void startFiring(){
    	isFiring=true;
    }
    
    public void stopFiring(){
    	isFiring=false;
    }
    
    public void fire()
    {
    	if(respawnCounter<=0){
    		weapon.fireWeapon(this);
    		Sound.play("Resources/Explosion_large.wav");
    	}
    }
    
    public void die(){
    	this.show=false;
    	LargeExplosion explosion = new LargeExplosion(new Point(location.x,location.y));
    	TankWorld.getInstance().addBackground(explosion);
    	respawn-=1;
    	if(respawn>=0){
        	TankWorld.getInstance().removeClockObserver(this.motion);
    		reset();
    	}
    	else{
    		this.motion.delete(this);
    	}
    }
   
    public void reset(){
    	this.setLocation(resetPoint);
    	health=power;
    	respawnCounter=160;
        this.weapon = new TankWeapon();
    }
    
    @Override
    public void update(Observable obs, Object obj) {
	Modifiable modifier = (Modifiable) obs;
	modifier.read(this);
    }
    
    public void collide(GameObject other){
    }
}

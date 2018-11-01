package prototype;

import java.util.Observable;
import java.util.Observer;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

//root for all game objects in the game
abstract public class GameObject implements Observer {
    
    protected Rectangle location;
    public Image img;
    protected Point speed;
    protected int height;
    protected int width;
    ImageObserver observer;
    public boolean show;

    public GameObject(Point location, Point speed, Image img){
	this.speed = speed;
	this.img=img;
        this.show=true;
        try{
            height = img.getWidth(observer);
	    width = img.getHeight(observer);
	}catch (NullPointerException e){
            height=0;
            width=0;
	}
        this.location = new Rectangle(location.x,location.y,width,height);
    }

    public GameObject(Point location, Image img){
        this(location, new Point(0,0), img);
    }
	
    public void draw(Graphics g, ImageObserver obs) {
    	if(show){
            g.drawImage(img, location.x, location.y, obs);
        }
    }
    
    public void update(int w, int h) {
    	location.x += speed.x;
        location.y += speed.y;
        
        if(location.y<-100 || location.y==h+100){
            this.show=false;
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        Modifiable modifier = (Modifiable) o;
	modifier.read(this);
    }
    
    //getters
    public Point getLocationPoint(){
    	return new Point(location.x, location.y);
    }
    
    public int getX(){
    	return location.x;
    }
   
    public int getY(){
    	return location.y;
    }
    
    public int getSizeX(){
    	return width;
    }
    
    public int getSizeY(){
    	return height;
    }
    
    public Point getSpeed(){
    	return speed;
    }
    
    public Rectangle getLocation(){
    	return new Rectangle(this.location);
    }
    
    
    //setters
    public void setLocation(Point newLocation){
    	location.setLocation(newLocation);
    }
    
    public void setImage(Image img){
    	this.img = img;
        try{
            height = img.getWidth(observer);
            width = img.getHeight(observer);
            this.location = new Rectangle(location.x,location.y,width,height);
	}catch (NullPointerException ex){
            height=0;
            width=0;
            this.location = new Rectangle(location.x,location.y,0,0);
	}
    }
    public void hide(){
    	this.show = false;
    }
    
    public void show(){
    	this.show = true;
    }
    
    //motion
    public void move(int x, int y){
    	location.translate(x, y);
    }
    
    public void move(){
    	location.translate(speed.x,speed.y);
    }

    //collide
    public boolean collision(GameObject other) {
        if(location.intersects(other.getLocation())){
        	return true;
        }
        return false;
    }
    
    
}
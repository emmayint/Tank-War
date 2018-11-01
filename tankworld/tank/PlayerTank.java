package tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class PlayerTank extends Tank {

    int speedup;
    int dir;
    public PlayerTank(Point location, Image img, int[] controls, String name) {
        super(location, new Point(0, 0), img, controls, name);
        resetPoint = new Point(location);
        this.gunLocation = new Point(32, 32);

        this.name = name;
        weapon = new TankWeapon();
        motion = new InputHandler(this, controls, TankWorld.getInstance());
        respawn = 3;
        health = 100;
        power = 100;
        score = 0;
        respawnCounter = 0;
        height = 64;
        width = 64;
        direction = 180;
        speedup = 4;
        dir = 6;
        this.location = new Rectangle(location.x, location.y, width, height);
    }

        public void turn(int angle) {
        this.direction = this.direction + angle;

        if (this.direction < 0) {
            this.direction = 359;
        } 
        //turning more than 360
        if (this.direction >= 360) {
            this.direction = 0;
        }
    }

    @Override
    public void update(int w, int h) {
        //forward backward (up and down/WS keys)
        if (up == 1 || down == 1) {
            int dx = (int) (speedup * Math.sin(Math.toRadians(this.direction + 90)));
            int dy = (int) (speedup * Math.cos(Math.toRadians(this.direction + 90)));
            this.location.x = this.location.x + dx * (up - down);
            this.location.y = this.location.y + dy * (up - down);
        }

        //rotation (left right/AD keys)
        if (left == 1 || right == 1) {
            this.turn(speedup * (left - right));
        } 

        if (location.x < 0) {
            this.location.x = this.width;
        }
        
        if (location.x > w - this.width) {
            this.location.x = w - this.width;
        }

        if (this.location.y < 0) {
            this.location.y = this.width;
        }
        
        if (this.location.y > h - this.height) {
            this.location.y = h - this.height;
        }

        if (isFiring) {
            int bulletFrame = TankWorld.getInstance().getFrameNumber();
            if (bulletFrame >= lastFired + weapon.reload) {
                fire();
                lastFired = bulletFrame;
            } 
        }
    }

    @Override
    public void draw(Graphics g, ImageObserver obs) {
        if (respawnCounter <= 0) {
            g.drawImage(img, location.x, location.y, location.x + this.getSizeX(), location.y + this.getSizeY(), 
                    (direction / dir) * this.getSizeX(), 0, ((direction / dir) * this.getSizeX()) + this.getSizeX(), this.getSizeY(), obs);
        }else if (respawnCounter == 25) {
            TankWorld.getInstance().addFrameObserver(this.motion);
            respawnCounter -= 1;
            System.out.println(Integer.toString(respawnCounter));
        }else if (respawnCounter < 25) {
            if (respawnCounter % 2 == 0) {
                g.drawImage(img, location.x, location.y, location.x + this.getSizeX(), location.y + this.getSizeY(), 
                        (direction / dir) * this.getSizeX(), 0, ((direction / dir) * this.getSizeX()) + this.getSizeX(), this.getSizeY(), obs);
            }
            respawnCounter -= 1;
        } else {
            respawnCounter -= 1;
        }
    }

    @Override
    public void reset() {
        this.setLocation(resetPoint);
        health = power;
        respawnCounter = 50;//respawn speed
        this.weapon = new TankWeapon();
    }

    @Override
    public void die() {
        this.show = false;
        TankWorld.setSpeed(new Point(0, 0));
        LargeExplosion explosion = new LargeExplosion(new Point(location.x, location.y));
        TankWorld.getInstance().addBackground(explosion);
        --respawn;
        if (respawn >= 0) {
            TankWorld.getInstance().removeClockObserver(this.motion);
            reset();
        }else {
            this.motion.delete(this);
        }
    }

}

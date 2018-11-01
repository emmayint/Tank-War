package tank;

import prototype.GameObject;
import prototype.FixedObject;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;

//background muse be drawn before other objects
public class Background extends FixedObject {
    int move, w, h;

    public Background(int w, int h, Point speed, Image img) {
		super(new Point(0,0), speed, img);
		this.img = img;
		this.w = w;
		this.h = h;
		move = 0;
    }
	
    @Override
    public void update(int w, int h) {
    }
	
    @Override
    public void draw(Graphics g, ImageObserver obs) {
        int TileWidth = img.getWidth(obs);
        int TileHeight = img.getHeight(obs);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g.drawImage(img, j * TileWidth, i * TileHeight + (move % TileHeight), TileWidth, TileHeight, obs);
            }
        }
        move += speed.y;
    }
	
    @Override
    public boolean collision(GameObject otherObject) {
        return false;
    }
}

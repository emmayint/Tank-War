package prototype;

import java.util.Observable;
import java.util.Observer;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.ImageObserver;

public abstract class UIElement implements Observer {
    Point location;
    ImageObserver observer;

    public abstract void draw(Graphics g, int x, int y);
    
    @Override
    public void update(Observable o, Object arg) {
    }
}

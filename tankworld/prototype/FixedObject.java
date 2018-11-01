package prototype;

import java.awt.Image;
import java.awt.Point;

public class FixedObject extends GameObject {

    public FixedObject(Point location, Image img){
	super(location, GamePrototype.getSpeed(), img);
    }

    public FixedObject(Point location, Point speed, Image img){
        super(location, speed, img);
    }
}

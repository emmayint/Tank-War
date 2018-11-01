package prototype;

import java.util.Observable;

//objects that change states
public abstract class Modifiable extends Observable{
	
    public Modifiable(){
    }
	
    public abstract void read(Object theObject);
}

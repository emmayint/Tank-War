package prototype;

import java.util.Observable;

public class FrameTracker extends Observable {
    private int startTime;
    private int frame;
	
    public FrameTracker(){
	startTime = (int) System.currentTimeMillis();
	frame = 0;
    }
		
    public void incrementFrame(){
        frame++;
	setChanged();
        this.notifyObservers();
    }

    public int getFrame(){
	return this.frame;
    }

    public int getTime(){
        return (int)System.currentTimeMillis()-startTime;
    }
}

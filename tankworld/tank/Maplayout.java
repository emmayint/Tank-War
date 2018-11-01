package tank;

import prototype.Modifiable;
import java.util.Observable;
import java.util.Observer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Maplayout extends Modifiable implements Observer {
    int start;
    Integer position;
    String filename;
    BufferedReader level;
    int w, h;
    int endgameDelay = 25;	
	
    public Maplayout(String filename){
        super();
	this.filename = filename;
        String line;
	try {
            level = new BufferedReader(new InputStreamReader(TankWorld.class.getResource(filename).openStream()));
            line = level.readLine();
            w = line.length();
            h=0;
            while(line!=null){
                h++;
		line = level.readLine();
            }
            level.close();
	}catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
	}
    }

	
    public void load(){
	TankWorld world = TankWorld.getInstance();	
	try {
            level = new BufferedReader(new InputStreamReader(TankWorld.class.getResource(filename).openStream()));
	}catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
	}		
	String line;
        try {
            line = level.readLine();
            w = line.length();
            h=0;
            while(line!=null){
                for(int i = 0, n = line.length() ; i < n ; i++) { 
		    char c = line.charAt(i); 				    
                    if(c=='1'){
               	    	UnbreakableWall wall = new UnbreakableWall(i,h);
                        world.addBackground(wall);
		    }		    
		    if(c=='2'){
                        BreakableWall wall = new BreakableWall(i,h);
		    	world.addBackground(wall);
                    }
                    if(c=='3'){
                        int[] controls = {KeyEvent.VK_A,KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_SPACE};
			world.addPlayer(new PlayerTank(new Point(i*32, h*32),world.images.get("player1"), controls, "Player1"));
                    }
                    if(c=='4'){
		    	int[] controls = new int[] {KeyEvent.VK_LEFT,KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER};
                        world.addPlayer(new PlayerTank(new Point(i*32, h*32),world.images.get("player2"), controls, "Player2"));
		    }
                    if(c=='5'){
		    	world.addPowerUp(new PowerUp(new Point(i*32, h*32), 0, new PowerUpBullet()));
                    }
		}
                h++;
		line = level.readLine();
            }
            level.close();
        }catch (IOException ex) {
            ex.printStackTrace();
	}
    }

    @Override
    public void update(Observable o, Object arg) {
        TankWorld world = TankWorld.getInstance();
	if(world.isGameOver()){
            if(endgameDelay<=0){
                world.removeClockObserver(this);
            } else {
                endgameDelay--;
            }
        }
    }
        	
    public void read(Object theObject){
    }
}

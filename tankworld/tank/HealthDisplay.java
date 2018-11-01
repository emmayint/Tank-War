package tank;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import prototype.UIElement;

public class HealthDisplay extends UIElement {
    PlayerTank player;
    String name;
	
    public HealthDisplay(PlayerTank player, String name){
        this.player = player;
	this.name = name;
    }
    
    @Override
    public void draw(Graphics g2, int x, int y){
        g2.setFont(new Font("Calibri", Font.PLAIN, 24));
        if(player.getHealth()>50){
            g2.setColor(Color.GREEN);
        }
        else if(player.getHealth()>25){
            g2.setColor(Color.YELLOW);
        }
        else{
            g2.setColor(Color.MAGENTA);
        }
        
        g2.fillRect(x+2, y-25, (int) Math.round(player.getHealth()*1.1), 20);
        
        for(int i=0;i<player.getLives();i++){
            g2.setColor(Color.RED);
            g2.fillOval(x+130 +i*30, y-25, 15, 15);        	
        }
        g2.setColor(Color.BLUE);
        g2.drawString(Integer.toString(player.getScore()), x+250, y-8);
    }
}

package tank;

import prototype.Sound;
import prototype.FrameTracker;
import prototype.FixedObject;
import prototype.Modifiable;
import prototype.Weapon;
import prototype.GamePrototype;
import prototype.GameObject;
import prototype.UIElement;
import prototype.MotionHandler;
import java.util.Observable;
import java.util.Observer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import javax.swing.*;
import java.util.logging.Logger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class TankWorld extends GamePrototype {

    private static final TankWorld tankWorld = new TankWorld();
    
    boolean gameOver;
    int mapx, mapy;
    Point mapDimension;
    public static int scale;
    private ArrayList<Bullet> bullets;
    private ArrayList<Tank> players;
    private ArrayList<UIElement> uiElement;
    private ArrayList<Tank> powerups;
    protected ArrayList<FixedObject> background;
    public static HashMap<String,Image> images = new HashMap<String,Image>();
    public static HashMap<String, MotionHandler> motions = new HashMap<String, MotionHandler>();
    
    public Maplayout maplayout;
    private Thread thread;
    public static final Sound sound = new Sound();
    public static final FrameTracker frameTracker = new FrameTracker();
    
    ImageObserver imgObserver;
    private BufferedImage bufferimg, player1view, player2view;
    private static Point speed;

    private TankWorld() {
        this.setFocusable(true);
        images = new HashMap<String,Image>();
        players = new ArrayList<Tank>();
        background = new ArrayList<FixedObject>();
        bullets = new ArrayList<Bullet>();
        powerups = new ArrayList<Tank>();
        uiElement = new ArrayList<UIElement>();
        motions = new HashMap<String, MotionHandler>();
        scale = 32;
        speed=new Point(0,0);
    }
   
    @Override
    public Image seekImage(String path) {
        URL url = TankWorld.class.getResource(path);
        Image image = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(image, 0);
            tracker.waitForID(0);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return image;
    }
    
    @Override
    protected void putImages() {
        images.put("background", seekImage("Resources/Background.bmp"));
        images.put("player1", seekImage("Resources/Tank1.png"));
        images.put("player2", seekImage("Resources/Tank2.png"));
        images.put("smallExplosion", seekImage("Resources/Explosion_small.png"));
        images.put("largeExplosion", seekImage("Resources/Explosion_large.png"));
        images.put("bullet", seekImage("Resources/Weapon.gif"));
        images.put("powerup", seekImage("Resources/Pickup.png"));
        images.put("wall1", seekImage("Resources/Wall1.gif"));
        images.put("wall2", seekImage("Resources/Wall2.gif"));
    }
    
    @Override
    public void init() {
        setBackground(Color.white);
        putImages();
        gameOver = false;
        imgObserver = this;
        maplayout = new Maplayout("Resources/maplayout.txt");
        maplayout.addObserver(this);
        mapDimension = new Point(maplayout.w * scale, maplayout.h * scale);
        addBackground(new Background(mapDimension.x, mapDimension.y, TankWorld.getSpeed(), images.get("background")));
        maplayout.load();
        frameTracker.addObserver(maplayout);
    }
    
    //getters
    
    public static TankWorld getInstance() {
        return tankWorld;
    }
    
    public static Point getSpeed(){
    	return new Point(TankWorld.speed);
    }
    
    @Override
    public int getFrameNumber() {
        return frameTracker.getFrame();
    }

    public ListIterator<FixedObject> getBackgroundObjects() {
        return background.listIterator();
    }

    public ListIterator<Tank> getPlayers() {
        return players.listIterator();
    }

    public ListIterator<Bullet> getBullets() {
        return bullets.listIterator();
    }
  
    @Override
    public boolean isGameOver() {
        return gameOver;
    }
    
    //setters
    @Override
    public void setDimensions(int w, int h) {
        this.mapx = w;
        this.mapy = h;
    }
    
    public static void setSpeed(Point speed){
    	TankWorld.speed = speed;
    }

    //adders
    public void addBullet(Bullet... newbullets) {
        for (Bullet bullet : newbullets) {
            bullets.add(bullet);
        }
    }

    public void addPlayer(Tank... tanks) {
        for (Tank player : tanks) {
            uiElement.add(new HealthDisplay((PlayerTank) player, Integer.toString(players.size())));
            players.add(player);
        }
    }

    public void addBackground(FixedObject... objs) {
        for (FixedObject object : objs) {
            background.add(object);
        }
    }

    public void addPowerUp(Tank powerup) {
        powerups.add(powerup);
    }

    @Override
    public void addFrameObserver(Observer obs) {
        frameTracker.addObserver(obs);
    }
    
    //remover
    public void removeClockObserver(Observer obs) {
        frameTracker.deleteObserver(obs);
    }
    
    //geme continues by displaying frame by frame
    @Override
    public void drawFrame(int w, int h, Graphics2D g2) {
        ListIterator<?> iterator = getBackgroundObjects();
        // iterate through all blocks
        while (iterator.hasNext()) {
            FixedObject obj = (FixedObject) iterator.next();
            obj.update(w, h);
            obj.draw(g2, this);
            if (obj instanceof LargeExplosion || obj instanceof SmallExplosion) {
                if (!obj.show) {
                    iterator.remove();
                }
                continue;
            }
            
            // check collisions
            ListIterator<Tank> players = getPlayers();
            while (players.hasNext() && obj.show) {
                PlayerTank player = (PlayerTank) players.next();
                if (obj.collision(player)) {
                    Rectangle playerLocation = player.getLocation();
                    Rectangle location = obj.getLocation();
                    //do not use "else if" here!!
                    if (playerLocation.x < location.x) {
                        player.move(-2, 0);
                    }
                    if (playerLocation.x > location.x) {
                        player.move(2, 0);
                    }
                    if (playerLocation.y < location.y) {
                        player.move(0, -2);
                    }
                    if (playerLocation.y > location.y) {
                        player.move(0, 2);
                    }
                }
            }
        }

        if (!gameOver) {
            //bullets
            ListIterator<Bullet> bullets = this.getBullets();
            while (bullets.hasNext()) {
                Bullet aBullet = bullets.next();
                // bullets go out of frame
                if (aBullet.getX() > w || aBullet.getY() > h) {
                    bullets.remove();
                }else {
                    iterator = this.getBackgroundObjects();
                    while (iterator.hasNext()) {
                        GameObject other = (GameObject) iterator.next();
                        // bullets hit background obj
                        if (other.show && other.collision(aBullet)) {
                            bullets.remove();
                            addBackground(new SmallExplosion(aBullet.getLocationPoint()));
                            break;
                        }
                    }
                }
                aBullet.draw(g2, this);
            }
            //check if any player died, hit by bullets or get powerups
            iterator = getPlayers();
            while (iterator.hasNext()) {
                Tank player = (Tank) iterator.next();
                if (player.isDead()) {
                    gameOver = true;
                    continue;
                }

                bullets = this.getBullets();
                while (bullets.hasNext()) {
                    Bullet bullet = bullets.next();
                    //bullets hit players, check friendly fire, then do damage, explosion...
                    if (bullet.getOwner() != player && bullet.collision(player) && player.respawnCounter <= 0) {
                        player.damage(bullet.getPower());
                        bullet.getOwner().incrementScore(bullet.getPower());
                        addBackground(new SmallExplosion(bullet.getLocationPoint()));
                        bullets.remove();
                    }
                } 
                
                // players get powerups
                ListIterator<Tank> powerups = this.powerups.listIterator();
                while (powerups.hasNext()) {
                    Tank powerup = powerups.next();
                    if(powerup.show){
                        powerup.draw(g2, this);
                        if (powerup.collision(player)) {
                        Weapon weapon = powerup.getWeapon();
                        player.setWeapon(weapon);
                        powerup.die();
                        }
                    }
                } 
                
            }
            
            //player1 collides player2
            Tank p1 = players.get(0);
            Tank p2 = players.get(1);
            if (p1.collision(p2)) {
                Rectangle p1Location = p1.getLocation();
                Rectangle p2Location = p2.getLocation();

                if (p1Location.x < p2Location.x) {
                    p1.move(-1, 0);
                }

                if (p1Location.x > p2Location.x) {
                    p1.move(1, 0);
                }
                
                if (p1Location.y < p2Location.y) {
                    p1.move(0, -1);
                }

                if (p1Location.y > p2Location.y) {
                    p1.move(0, 1);
                }
            }
            // player2 collides player1
            if (p2.collision(p1)) {
                Rectangle p1Location = p1.getLocation();
                Rectangle p2Location = p2.getLocation();

                if (p2Location.x < p1Location.x) {
                    p2.move(-1, 0);
                }

                if (p2Location.x > p1Location.x) {
                    p2.move(1, 0);
                }
                
                if (p2Location.y < p1Location.y) {
                    p2.move(0, -1);
                } 
                if (p2Location.y > p1Location.y) {
                    p2.move(0, 1);
                } 
            }

            p1.draw(g2, this);
            p2.draw(g2, this);
            p1.update(w, h);
            p2.update(w, h);

        }else { //game over display
            Tank p1 = players.get(0);
            Rectangle p1Location = p1.getLocation();
            g2.setColor(Color.cyan);
            g2.setFont(new Font("Helvetica", Font.CENTER_BASELINE, 32));
            g2.drawString("GAME OVER", p1Location.x, p1Location.y + 10);
            g2.drawString("Score:", p1Location.x, p1Location.y + 60);
            int i = 1;
            for (Tank player : players) {
                g2.drawString(player.getName() + " : " + Integer.toString(player.getScore()), p1Location.x, p1Location.y + 60 + (i * 25));
                i++;
            }
        }

    }

    @Override
    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bufferimg == null || bufferimg.getWidth() != w || bufferimg.getHeight() != h) {
            bufferimg = (BufferedImage) createImage(w, h);
        }
        g2 = bufferimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    // frame by frame
    @Override
    public void paint(Graphics g) {
        if (players.size() != 0) {
            frameTracker.incrementFrame();
        }
        Dimension windowSize = getSize();
        Graphics2D g2 = createGraphics2D(mapDimension.x, mapDimension.y);
        drawFrame(mapDimension.x, mapDimension.y, g2);
        g2.dispose();
        //paint player1
        int p1x = this.players.get(0).getX() - windowSize.width / 4 > 0 ? this.players.get(0).getX() - windowSize.width / 4 : 0;
        int p1y = this.players.get(0).getY() - windowSize.height / 2 > 0 ? this.players.get(0).getY() - windowSize.height / 2 : 0;

        if (p1x > mapDimension.x - windowSize.width / 2) {
            p1x = mapDimension.x - windowSize.width / 2;
        }
        if (p1y > mapDimension.y - windowSize.height) {
            p1y = mapDimension.y - windowSize.height;
        }
        //paint player2
        int p2x = this.players.get(1).getX() - windowSize.width / 4 > 0 ? this.players.get(1).getX() - windowSize.width / 4 : 0;
        int p2y = this.players.get(1).getY() - windowSize.height / 2 > 0 ? this.players.get(1).getY() - windowSize.height / 2 : 0;

        if (p2x > mapDimension.x - windowSize.width / 2) {
            p2x = mapDimension.x - windowSize.width / 2;
        }
        if (p2y > mapDimension.y - windowSize.height) {
            p2y = mapDimension.y - windowSize.height;
        }
        //paint split screens
        player1view = bufferimg.getSubimage(p1x, p1y, windowSize.width / 2, windowSize.height);
        player2view = bufferimg.getSubimage(p2x, p2y, windowSize.width / 2, windowSize.height);
        g.drawImage(player1view, 0, 0, this);
        g.drawImage(player2view, windowSize.width / 2, 0, this);
        g.drawImage(bufferimg, windowSize.width / 2 - 75, 400, 150, 150, imgObserver);
        g.drawRect(windowSize.width / 2 - 1, 0, 1, windowSize.height);
        g.drawRect(windowSize.width / 2 - 76, 399, 151, 151);
        //draw health bars
        ListIterator<UIElement> objects = uiElement.listIterator();
        int offset = 300;
        while (objects.hasNext()) {
            UIElement object = objects.next();
            object.draw(g, offset, windowSize.height);
            offset += 720;
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        while (thread == t) {
            this.requestFocusInWindow();
            repaint();
            try {
                thread.sleep(1000/144);
            } catch (InterruptedException ex) {
                Logger.getLogger(TankWorld.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }   
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Modifiable modifier = (Modifiable) o;
        modifier.read(this);
    }

    public static void main(String argv[]) {
        final TankWorld game = TankWorld.getInstance();
        JFrame f = new JFrame("Tank Game");
        f.addWindowListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                game.requestFocusInWindow();
            }
        });
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(1600, 900));
        game.init();
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TankWorld.sound.play("Resources/Music.wav");
        game.start();
    }
}

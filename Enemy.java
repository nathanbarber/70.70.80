import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;


public class Enemy {
    private double inity = (Core.yres / 3) - 31;
    private double initx = (int)(Math.random() * (Core.xres - 30));;
    private double x = (int)(Math.random() * (Core.xres - 30));;
    private double y = (Core.yres / 3) - 30;
    public double scale = 30;
    public double steps = 0.1;
    private BufferedImage b;
    private BufferedImage boom;
    private BufferedImage booom;
    public char boomtype = 'n';
    public boolean dead = false;

    public Enemy() {
        try {
            this.b = ImageIO.read(new File("./img/cactus.png"));
            this.boom = ImageIO.read(new File("./img/boom.png"));
            this.booom = ImageIO.read(new File("./img/booom.png"));
        } catch(IOException e) {
            System.out.println("Could not grab cactus");
        }
    }

    public void drawEnemy(Graphics g) {
        if(boomtype == 'n') {
            g.drawImage(this.b, (int)(this.x), (int)(this.y), (int)(this.scale), (int)(this.scale), null);
        } else if(boomtype == 'a') {
            g.drawImage(this.boom, (int)(this.x), (int)(this.y), (int)(this.scale), (int)(this.scale), null);
            Core.score += 5;
        } else if(boomtype == 's') {
            this.scale += 14;
            this.x -= 7;
            this.y -= 6;
            g.drawImage(this.booom, (int)(this.x), (int)(this.y), (int)(this.scale), (int)(this.scale), null);
            Core.shock();
            Core.score += 10;
        }
    }

    public void reset() {
        this.x = (int)(Math.random() * (Core.xres - 30));
        this.y = (Core.yres / 3) - 30;
        this.scale = 30;
        this.boomtype = 'n';
        if(this.dead == false) {
            Core.health -= (int)(Math.random() * 5) + 1;
            if(Core.health < 0) {
                Core.running = false;
                Core.renderpause = Core.forever;
            }
        }
        this.dead = false;
    }

    public int[] location() {
        int[] coor = {(int)(this.x), (int)(this.y)};
        return coor;
    }

    public char boom(char c) {
        this.boomtype = c;
        return boomtype;
    }

    public void creep(double steps) {
        if(this.x > Core.xres / 2) {
            this.x += steps / 2;
        } else {
            this.x -= steps;
        }

        this.y += steps;
        this.scale += steps;
    }
}
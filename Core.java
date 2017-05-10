import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;


public class Core extends JPanel implements MouseListener {
    static final int xres = 900;
    static final int yres = 450;
    static int r1 = 200;
    static int g1 = 200;
    static int b1 = 210;
    static Enemy[] em = new Enemy[2];

    public static int count = 0;
    public static int forever = 0;
    public static int renderpause = 0;
    public static int score = 0;
    public static int health = 100;
    public static boolean running = false;
    //public static String[] tones = {"./sound/s1.mp3", "./sound/s2.mp3"};
    //public static String[] waves = {"./sound/m1.mp3", "./sound/m2.mp3", "./sound/m3.mp3", "./sound/m4.mp3", "./sound/m5.mp3", "./sound/m6.mp3"};

    boolean enemybool = false;
    boolean wakeup = true;
    int i = 0;
    Font f = new Font("Dialog", Font.PLAIN, 16);

    public Core() {
        repaint();
        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Core.this.repaint();
            }
        };

        Timer t = new Timer(5, action);
        t.setRepeats(true);
        t.setInitialDelay(0);
        t.start();
    }

    public void hit(MouseEvent e, char b) {
        for(Enemy en : em) {
            int ex = en.location()[0];
            int ey = en.location()[1];
            int scale = (int)(en.scale);
            boolean withinx;
            boolean withiny;
            if(ex > (e.getX() - scale / 1.5) && ex < (e.getX() + scale / 1.5)) {
                withinx = true;
            } else {
                continue;
            }
            if(ey > (e.getY() - scale / 1.5) && ey < (e.getY() + scale / 1.5)) {
                withiny = true;
            } else {
                continue;
            }
            if(withinx && withiny) {
                en.dead = true;
                en.boom(b);
            }
        }
    }

    public static void shock() {
        r1 = 254;
        g1 = 180;
        b1 = 160;
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
        if(running) {
            hit(e, 's');
        } else {
            if(forever - renderpause > 100) {
                wakeup = false;
                score = 0;
                health = 100;
                em[0].reset();
                em[1].reset();
                running = true;
                //int rand = (int)(Math.random() * wavestream.length);
            }
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        if(running) {
            hit(e, 'a');
        }
    }

    @Override
    public void paintComponent(Graphics g2) {
        super.paintComponent(g2);

        Graphics2D g = (Graphics2D)(g2);

        if(running) {
            g.setColor(new Color(r1, g1, b1));
            g.fillRect(0,0,xres,yres);
            g.setColor(new Color(180, 190, 180));
            g.fillRect(0,(yres/3), xres,(yres/3) * 2);
            //System.out.println(i++);
            
            //enemy
            for(int k = 0; k < em.length; k++) {
                if(k == 0) {
                    em[k].drawEnemy(g);
                    em[k].creep(em[k].steps);
                    em[k].steps += 0.02;
                    if(em[k].location()[1] > ((5 * yres) / 7)) {
                        enemybool = true;
                    }
                    if(em[k].location()[1] > yres || em[k].location()[0] > xres || em[k].location()[0] < 0 - em[k].scale) {
                        em[k].reset();
                        //System.out.println("resetting");
                        em[k].steps = 0.1;
                    }
                }
                if((k == 1) && enemybool) {
                    em[k].drawEnemy(g);
                    em[k].creep(em[k].steps);
                    em[k].steps += 0.02;
                    if(em[k].location()[1] > yres || em[k].location()[0] > xres || em[k].location()[0] < 0 - em[k].scale) {
                        em[k].reset();
                        //System.out.println("resetting");
                        em[k].steps = 0.1;
                    }
                }
                count++;
            }
            if(count % 3 == 0) {
                if(b1 < 210) {
                    b1++;
                }
                if(g1 < 200) {
                    g1++;
                }
                if(r1 > 200) {
                    r1--;
                }
            }

            //call score

            g.setFont(f);
            g.setColor(new Color(50, 50, 50));
            g.drawString("Charred Cactus Chunks: " + score, 20, 20);
            g.drawString(health + ": Health", xres-100, 20);
        } else {
            g.setColor(new Color(200,200,210));
            g.fillRect(0,0,xres,yres);
            g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if(wakeup) {
                g.setFont(new Font("Courier", Font.BOLD, 30));
                g.setColor(new Color(70, 70, 70));
                g.drawString("Click anywhere to begin", 40, 40);
                g.setFont(new Font("Courier", Font.PLAIN, 24));
                g.setColor(new Color(40, 40, 40));
                g.drawString("Short press to ignite", 40, 80);
                g.drawString("Hold and release to nuke", 40, 120);
            } else {
                g.setFont(new Font("Courier", Font.BOLD, 30));
                g.setColor(new Color(70, 70, 70));
                g.drawString("Game Over", 40, 40);
                g.setFont(new Font("Courier", Font.PLAIN, 24));
                g.setColor(new Color(40, 40, 40));
                g.drawString("Cactus chunks collected: " + score, 40, 160);
                g.drawString("Short press to ignite", 40, 80);
                g.drawString("Hold and release to nuke", 40, 120);
                g.drawString("Click to restart", 40, 200);
            }

            try {
                BufferedImage b = ImageIO.read(new File("./img/cactus.png"));
                Image i = b.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                g.drawImage(i, (2 * xres) / 3, yres - 170, null);
            } catch(IOException e) {
                System.out.println("Whoops");
            }
        }



        forever++;
        //System.out.println(forever);
    }

    @SuppressWarnings("main")
    public static void main(String[] args) {
        for(int i = 0; i < em.length; i++) {
            em[i] = new Enemy();
        }
        //for(int i = 0; i < waves.length; i++) {}
        final JFrame frame = new JFrame();
        frame.setSize(xres, yres);
        Cursor hud = Toolkit.getDefaultToolkit().createCustomCursor(
            Tap.render(80, "./img/hit.png"), new Point(0, 0), "blank cursor"
            );
        frame.getContentPane().setCursor(hud);
        frame.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                frame.getContentPane().setCursor(hud);
            }
        });
        final Core panel = new Core();
        panel.setOpaque(true);
        frame.getContentPane().add(panel);
        frame.addMouseListener(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
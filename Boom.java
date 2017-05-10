import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class Boom {
    Image img;
    public Boom(String url, int size) {
        try {
            BufferedImage b = ImageIO.read(new File(url));
            this.img = b.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.PointerInfo;
import javax.imageio.*;
import java.awt.image.*;

public class Tap {
    public static Image render(int size, String link) {
        int scaleSize;
        BufferedImage b;
        Image scaledImage;
        try {
            scaleSize = size;
            b = ImageIO.read(new File(link));
            //System.out.println("input succeeded");
            scaledImage = b.getScaledInstance(scaleSize, scaleSize, Image.SCALE_SMOOTH);
            return scaledImage;
        } catch(IOException e) {
            System.out.println("IOException: " + e);
            return null;
        }
    }
}

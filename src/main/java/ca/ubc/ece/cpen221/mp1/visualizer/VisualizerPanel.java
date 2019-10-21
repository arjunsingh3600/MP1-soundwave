package ca.ubc.ece.cpen221.mp1.visualizer;

import ca.ubc.ece.cpen221.mp1.SoundWave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class VisualizerPanel extends JPanel {
    private JLabel display = new JLabel();
    private SoundWave sw;
    private double[] xDir;
    private Point2D.Double[] ptsArr;

    public VisualizerPanel(){

        sw = new SoundWave(1,0,10000,0.5);
        xDir = new double[sw.getLeftChannel().length];

        add(display);
        for(int i = 0; i < xDir.length; i++){
            xDir[i] = (double)i/50;
        }
        function();
        setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);

        for(Point2D.Double p : ptsArr){

            g2.draw(new Ellipse2D.Double(p.getX(), p.getY()/10.0 + 20, 1, 1));
            System.out.println(p);
        }

    }

    public void function(){

        ptsArr = new Point2D.Double[sw.getLeftChannel().length];

        for(int i = 0; i < ptsArr.length; i++){

            ptsArr[i] = new Point2D.Double(xDir[i], sw.getLeftChannel()[i]);

        }

    }

}

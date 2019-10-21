package ca.ubc.ece.cpen221.mp1.visualizer;

import javax.swing.JFrame;

public class Frame {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setTitle("Sound Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new VisualizerPanel());
        frame.setVisible(true);
    }

}
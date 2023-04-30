package Task2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 450;
    public static Task2.CatchedBallsCounter CatchedBallsCounter;
    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce programm");
        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = "
                + Thread.currentThread().getName());
        Container content = this.getContentPane();
        JPanel counterPanel = new JPanel();
        JLabel counterLabel = new JLabel("Caught balls: 0");
        CatchedBallsCounter = new CatchedBallsCounter(counterLabel);
        counterPanel.add(counterLabel);

        content.add(counterPanel, BorderLayout.NORTH);
        content.add(this.canvas, BorderLayout.CENTER);
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStop = new JButton("Stop");
        JButton buttonAddHole = new JButton("Add Hole");
        JButton buttonStart1 = new JButton("Add 1");
        JButton buttonStart10 = new JButton("Add 10");
        JButton buttonStart100 = new JButton("Add 100");


        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });
        buttonAddHole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Hole b = new Hole(canvas);
                canvas.addHole(b);
                canvas.repaint();
            }
        });
buttonStart1.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 1; i++){
            Ball b = new Ball(canvas);
            canvas.add(b);

            BallThread thread = new BallThread(b, CatchedBallsCounter);
            thread.start();
        }
    }
});
        buttonStart10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 10; i++){
                    Ball b = new Ball(canvas);
                    canvas.add(b);

                    BallThread thread = new BallThread(b, CatchedBallsCounter);
                    thread.start();
                }
            }
        });
        buttonStart100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 100; i++){
                    Ball b = new Ball(canvas);
                    canvas.add(b);

                    BallThread thread = new BallThread(b, CatchedBallsCounter);
                    thread.start();
                }
            }
        });


        buttonPanel.add(buttonAddHole);
        buttonPanel.add(buttonStart1);
        buttonPanel.add(buttonStart10);
        buttonPanel.add(buttonStart100);
        buttonPanel.add(buttonStop);
        buttonPanel.add(counterLabel);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
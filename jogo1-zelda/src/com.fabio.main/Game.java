package com.fabio.main;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable
{
    private Thread thread;
    private boolean isRunning;

    public static JFrame frame;
    private final int width = 160;
    private final int height = 120;
    private final int scale = 3;

    private BufferedImage img;

    private int x = 20;

    public Game()
    {
        this.setPreferredSize(new Dimension(width * scale,height * scale));
        initFrame();
        img = new BufferedImage(160, 120, BufferedImage.TYPE_INT_RGB);
    }

    public static void main(String[] args)
    {
        Game game = new Game();
        game.initFrame();
        game.start();
    }

    public void initFrame()
    {
        frame = new JFrame("Jogo #1");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        isRunning = true;
        thread.start();

    }

    public synchronized  void stop()
    {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick()
    {

    }

    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = img.getGraphics();
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,width,height);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, width * scale, height * scale, null);
        bs.show();
    }

    public void run()
    {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        int frames = 0;
        double timer = System.currentTimeMillis();

        while (isRunning)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1)
            {
                tick();
                render();
                frames++;
                delta--;
            }

            if(System.currentTimeMillis() - timer >= 1000)
            {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
    }
}


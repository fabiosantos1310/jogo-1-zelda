package com.fabio.main;


import com.fabio.entites.Enemy;
import com.fabio.entites.Entity;
import com.fabio.entites.Player;
import com.fabio.entites.Shoot;
import com.fabio.graficos.Spritesheet;
import com.fabio.graficos.UI;
import com.fabio.world.Camera;
import com.fabio.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener
{
    private Thread thread;
    private boolean isRunning;

    public static JFrame frame;
    public static final int width = 600;
    public static final int height = 240;
    public static final int scale = 2;

    private BufferedImage img;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<Shoot> shoots;
    public static Spritesheet sheet;
    public static Spritesheet sheetWeapon;
    public static Player player;
    public static World world;
    public static UI ui;

    public static Random rand;

    public Game()
    {
        //addKeyListener(this);
        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);
        this.setPreferredSize(new Dimension(width * scale,height * scale));
        initFrame();
        //Inicializando objs

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        shoots = new ArrayList<Shoot>();
        sheet = new Spritesheet("/spritesheet.png");
        sheetWeapon = new Spritesheet("/spriteplayer-w-gun.png");
        player = new Player(0,0,30,30, sheet.getSprite(60,0,30,30));
        entities.add(player);
        ui = new UI();

        world = new World("/maps.png");
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
        for(int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            e.tick();
        }
        for(int i = 0; i < shoots.size(); i++)
        {
            shoots.get(i).tick();
        }
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
        world.render(g);

        for(int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            e.render(g);
        }
        for(int i = 0; i < shoots.size(); i++)
        {
            shoots.get(i).render(g);
        }
        ui.render(g);

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
        requestFocus();
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

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            player.right = true;
        }else if(e.getKeyCode() == KeyEvent.VK_A)
        {
            player.left = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            player.up = true;
        }else if(e.getKeyCode() == KeyEvent.VK_S)
        {
            player.down = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_X && player.hasGun)
        {
            player.shoot = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            player.right = false;
            player.setMoved(false);
        }else if(e.getKeyCode() == KeyEvent.VK_A)
        {
            player.left = false;
            player.setMoved(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            player.up = false;
            player.setMoved(false);
        }else if(e.getKeyCode() == KeyEvent.VK_S)
        {
            player.down = false;
            player.setMoved(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_X && player.hasGun)
        {
            player.shoot = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(player.hasGun)
        {
            player.mouseShoot = true;
            player.mx = (e.getX() / 3);
            player.my = (e.getY() / 3);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        player.mouseShoot = false;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}


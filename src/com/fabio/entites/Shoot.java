package com.fabio.entites;

import com.fabio.main.Game;
import com.fabio.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shoot extends Entity
{

    private double dx, dy;
    private double speed = 4;

    private int life = 100, currentLife = 0;

    public Shoot(double x, double y, int width, int height, BufferedImage sprite, double dx, double dy)
    {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }

    public void tick()
    {
        x+=dx*speed;
        y+=dy*speed;
        currentLife++;
        if(currentLife == life)
        {
            Game.shoots.remove(this);
            return;
        }
    }

    public void render(Graphics g)
    {
        g.setColor(Color.blue);
        g.fillOval(this.getX() - Camera.x,this.getY() - Camera.y + 10, 4,4);
    }
}

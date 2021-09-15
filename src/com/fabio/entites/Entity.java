package com.fabio.entites;

import com.fabio.main.Game;
import com.fabio.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity
{
    public static BufferedImage LIFEPACK_EN = Game.sheet.getSprite(90,120,30,30);
    public static BufferedImage WEAPON_EN = Game.sheet.getSprite(120,210,30,30);
    public static BufferedImage POTION_EN = Game.sheet.getSprite(0,210,30,30);
    public static BufferedImage ENEMY_EN = Game.sheet.getSprite(0,180,30,30);

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    private BufferedImage sprite;

    public Entity(double x, double y, int width, int height, BufferedImage sprite)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX()
    {
        return (int)this.x;
    }

    public int getY()
    {
        return (int)this.y;
    }

    public int getWidth()
    {
        return (int)this.width;
    }

    public int getHeight()
    {
        return (int)this.height;
    }

    public void render(Graphics g)
    {
        g.drawImage( sprite, (int)this.getX() - Camera.x,(int)this.getY() - Camera.y,null);
    }

    public static boolean isColliding(Entity e1, Entity e2)
    {
        Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), 30,30);
        Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), 30,30);

        return e1Mask.intersects(e2Mask);
    }

    public void tick()
    {

    }
}

package com.fabio.world;

import com.fabio.main.Game;

import javax.rmi.CORBA.Tie;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile
{
    public static BufferedImage TILE_FLOOR = Game.sheet.getSprite(0,0,30,30);
    public static BufferedImage TILE_WALL = Game.sheet.getSprite(30,0,30,30);

    private BufferedImage sprite;
    private  int x, y;

    public Tile(int x, int y, BufferedImage sprite)
    {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void render(Graphics g)
    {
        g.drawImage(sprite,x - Camera.x,y - Camera.y,null);
    }
}

package com.fabio.entites;

import com.fabio.main.Game;
import com.fabio.world.Camera;
import com.fabio.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Potion extends Entity
{
    int frames = 0;
    int maxFrames = 15;
    int index = 0;
    int maxIndex = 3;
    private BufferedImage[] sprites;
    public Potion(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[4];
        for(int i = 0; i < sprites.length; i++)
            sprites[i] = Game.sheet.getSprite(0 + 30 * i, 210, 30,30);
    }

    public void tick()
    {
        frames++;
        if(frames == maxFrames)
        {
            frames = 0;
            index++;
            if(index > maxIndex)
                index = 0;
        }
    }
    public void render(Graphics g)
    {
        g.drawImage(sprites[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
        super.render(g);
    }
}


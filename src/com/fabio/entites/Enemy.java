package com.fabio.entites;

import com.fabio.main.Game;
import com.fabio.world.Camera;
import com.fabio.world.World;
import com.sun.prism.shader.DrawEllipse_Color_AlphaTest_Loader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity
{
    private double speed = 0.6;

    private byte life = 10;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 6;

    private BufferedImage[] sprites;

    public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[7];
        for(int i = 0; i < sprites.length; i++)
            sprites[i] = Game.sheet.getSprite(0 + i * 30, 180, 30,30);
    }

    public void tick()
    {
        if(this.isCollidingPlayer() == false)
        {
            if(x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
                && !isColliding((int)(x+speed), this.getY()))
            {
                x+=speed;
            } else if(x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
                && !isColliding((int)(x-speed), this.getY()))
            {
                x-=speed;
            }
            if(y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
                && !isColliding(this.getX(), (int)(y+speed)))
            {
                y+=speed;
            } else if(y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
                && !isColliding(this.getX(), (int)(y-speed)))
            {
                y-=speed;
            }
            frames++;
            if(frames == maxFrames)
            {
                frames = 0;
                index++;
                if(index > maxIndex)
                    index = 0;
            }
        } else{
            index = 0;
            if(Game.rand.nextInt(100) < 10)
            {
                Game.player.life -= Game.rand.nextInt(4);
                Game.player.isDameged = true;
                if(Game.player.life <= 0)
                {
                    //GameOver
                }
            }
        }
        collidingShoot();
        if(life <=0)
        {
            Game.entities.remove(this);
            return;
        }
    }

    private void collidingShoot()
    {
        for(int i = 0; i < Game.shoots.size(); i++)
        {
            Entity e = Game.shoots.get(i);
            if(e instanceof Shoot)
            {
                if(Entity.isColliding(this, e))
                {
                    life--;
                    Game.shoots.remove(i);
                    return;
                }
            }
        }
    }

    public void render(Graphics g)
    {
        g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        super.render(g);
    }

    public boolean isColliding(int xnext, int ynext)
    {
        Rectangle enemyCurrent = new Rectangle(xnext, ynext, World.TILE_SIZE, World.TILE_SIZE);

        for(int i = 0; i < Game.enemies.size(); i++)
        {
            Enemy e = Game.enemies.get(i);
            if(e == this)
                continue;
            Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);

            if(enemyCurrent.intersects(targetEnemy))
                return true;
        }

        return false;
    }

    public boolean isCollidingPlayer()
    {
        Rectangle enemyCurrent = new Rectangle(this.getX() + 10, this.getY(), 20, 20);

        Rectangle player = new Rectangle(Game.player.getX() + 10, Game.player.getY(), 20, 20);

        return enemyCurrent.intersects(player);
    }
}

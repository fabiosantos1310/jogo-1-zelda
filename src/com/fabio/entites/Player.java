package com.fabio.entites;

import com.fabio.graficos.Spritesheet;
import com.fabio.graficos.UI;
import com.fabio.main.Game;
import com.fabio.world.Camera;
import com.fabio.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity
{
    public boolean right, left, up, down;
    public int right_dir = 0, left_dir = 1, down_dir = 2, up_dir = 3;
    public int  dir_anterior = 0;
    public int dir = down_dir;
    private  double speed = 1.5;

    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 9, indexUpDown = 0, maxIndexUpDown = 10;
    private int damegeFrames = 0;
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] downPlayer;
    private BufferedImage[] upPlayer;

    private BufferedImage[] rightPlayerWP;
    private BufferedImage[] leftPlayerWP;
    private BufferedImage[] downPlayerWP;
    private BufferedImage[] upPlayerWP;

    private BufferedImage damegePlayerUp;
    private BufferedImage damegePlayerDown;
    private BufferedImage damegePlayerRight;
    private BufferedImage damegePlayerLeft;

    public boolean isDameged = false;
    public boolean hasGun = false;

    public boolean shoot = false, mouseShoot = false;

    public double life = 100, maxLife = 100;
    public double mx, my;
    public static double mana = 100, maxMana = 100;

    public Player(double x, double y, int width, int height, BufferedImage sprite)
    {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[10];
        leftPlayer = new BufferedImage[10];
        downPlayer = new BufferedImage[11];
        upPlayer = new BufferedImage[11];

        rightPlayerWP = new BufferedImage[10];
        leftPlayerWP = new BufferedImage[10];
        downPlayerWP = new BufferedImage[11];
        upPlayerWP = new BufferedImage[11];

        downPlayer[0] = Game.sheet.getSprite(60, 0,30,30);
        upPlayer[0] = Game.sheet.getSprite(90,0,30,30);

        //WITH WEAPON

        downPlayerWP[0] = Game.sheetWeapon.getSprite(0,0,30,30);
        upPlayerWP[0] = Game.sheetWeapon.getSprite(30,0,30,30);

        damegePlayerDown = Game.sheet.getSprite(120,0,30,30);
        damegePlayerLeft = Game.sheet.getSprite(150,0,30,30);
        damegePlayerUp = Game.sheet.getSprite(180,0,30,30);
        damegePlayerRight = Game.sheet.getSprite(210,0,30,30);

        for(int i = 0; i < rightPlayer.length; i++)
            rightPlayer[i] = Game.sheet.getSprite(0 + (i * 30),60,30,30);

        for(int i = 0; i < leftPlayer.length; i++)
            leftPlayer[i] = Game.sheet.getSprite(0 + (i * 30),30,30,30);

        for(int i = 1; i < downPlayer.length; i++)
            downPlayer[i] = Game.sheet.getSprite(0 + (i-1) * 30, 90, 30, 30);

        for(int i = 1; i < upPlayer.length; i++)
            upPlayer[i] = Game.sheet.getSprite(0 + (i-1) * 30, 150, 30, 30);

        //with weapon

        for(int i = 0; i < rightPlayerWP.length; i++)
            rightPlayerWP[i] = Game.sheetWeapon.getSprite(0 + (i * 30),60,30,30);

        for(int i = 0; i < leftPlayerWP.length; i++)
            leftPlayerWP[i] = Game.sheetWeapon.getSprite(0 + (i * 30),30,30,30);

        for(int i = 1; i < downPlayerWP.length; i++)
            downPlayerWP[i] = Game.sheetWeapon.getSprite(0 + (i-1) * 30, 90, 30, 30);

        for(int i = 1; i < upPlayerWP.length; i++)
            upPlayerWP[i] = Game.sheetWeapon.getSprite(0 + (i-1) * 30, 120, 30, 30);
    }

    public void setMoved(boolean b)
    {
        this.moved = b;
    }


    public void tick()
    {
        moved = false;
        if(right && World.isFree((int)(x + speed), this.getY()))
        {
            moved = true;
            dir = right_dir;
            dir_anterior = dir;
            x+=speed;
        }
        else if(left && World.isFree((int)(x - (int)speed), this.getY()))
        {
            moved = true;
            dir = left_dir;
            dir_anterior = dir;
            x -= speed;
        }
        if(up && World.isFree(this.getX(), (int)(y - speed)))
        {
            moved = true;
            dir = up_dir;
            y-=speed;
        }
        else if (down && World.isFree(this.getX(), (int)(y + speed)))
        {
            moved = true;
            dir = down_dir;
            y+=speed;
        }
        if(moved)
        {
            frames++;
            if(frames == maxFrames)
            {
                frames = 0;
                index++;
                if(index > maxIndex)
                    index = 0;
                indexUpDown++;
                if(indexUpDown > maxIndexUpDown)
                    indexUpDown = 0;
            }
        }

        this.checkLifePack();
        this.checkPotion();
        this.checkGun();

        if(isDameged)
        {
            this.damegeFrames++;
            if(damegeFrames == 5)
            {
                damegeFrames = 0;
                isDameged = false;
            }
        }


        if(shoot)
        {
            int dx = 0;
            if(dir_anterior == left_dir)
                dir_anterior = -1;
            else
                dir_anterior = 1;
            shoot = false;
            if(dir == right_dir)
            {
                dx = 1;
            }
            else if(dir == left_dir)
            {
                dx = -1;
            } else
                dx = dir_anterior;
            if(mana == 0)
            {

            }else
                {
            Shoot tiro = new Shoot(this.getX(), this.getY(), this.getWidth(), this.getHeight(), null, dx, 0);
            Game.shoots.add(tiro);
            }
            if(mana - 5 >= 0)
                mana -= 5;
        }

        if(mouseShoot)
        {
            shoot = false;
            double angle = Math.toDegrees(Math.atan2(my - (this.getY() + Camera.y), mx - (this.getY()) + Camera.x));
            double dx = Math.cos(angle);
            double dy = Math.sin(angle);
            int px = 0;
            int py = 0;
            if(dir == right_dir)
            {
                px = 18;
                dx = 1;
            }else{
                px = -8;
                dx = -1;
            }
            if(mana - 5 >= 0)
            {
                mana -= 5;
                Shoot tiro = new Shoot(this.getX(), this.getY(), this.getWidth(), this.getHeight(), null, dx, 0);
                Game.shoots.add(tiro);
            }


        }

        if(life <= 0)
        {
            Game.entities = new ArrayList<Entity>();
            Game.enemies = new ArrayList<Enemy>();
            Game.sheet = new Spritesheet("/spritesheet.png");
            Game.player = new Player(0,0,30,30, Game.sheet.getSprite(60,0,30,30));
            Game.entities.add(Game.player);
            Game.ui = new UI();

            Game.world = new World("/maps.png");
            return;
        }

        Camera.x = Camera.clamp(this.getX() - (Game.width / 2),0, World.WIDTH*30 -Game.width);
        Camera.y = Camera.clamp(this.getY() - (Game.height / 2),0, World.HEIGHT*30 -Game.height);
    }

    public void checkGun()
    {
        for(int i = 0; i < Game.entities.size(); i++)
        {
            Entity atual = Game.entities.get(i);
            if(atual instanceof Weapon)
                if(Entity.isColliding(this, atual))
                {
                    if(!hasGun)
                    {
                        Game.entities.remove(atual);
                        hasGun = true;
                    }
                }
        }
    }

    public void checkPotion()
    {
        for(int i = 0; i < Game.entities.size(); i++)
        {
            Entity atual = Game.entities.get(i);
            if(atual instanceof Potion)
                if(Entity.isColliding(this, atual))
                {
                    if(mana < 100)
                    {
                        mana += 25;
                        Game.entities.remove(atual);
                        if(mana > 100)
                            mana = 100;
                    }
                }
        }
    }

    public void checkLifePack()
    {
        for(int i = 0; i < Game.entities.size(); i++)
        {
            Entity atual = Game.entities.get(i);
            if(atual instanceof LifePack)
                if(Entity.isColliding(this, atual))
                {
                    if(life < 100)
                    {
                        life += 10;
                        Game.entities.remove(atual);
                        if(life > 100)
                            life = 100;
                    }
                }

        }
    }

    public void render(Graphics g)
    {
        if(!isDameged && !hasGun)
        {
            if(dir == right_dir)
            {
                if(!moved)
                    g.drawImage(rightPlayer[0], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
                else
                    g.drawImage(rightPlayer[index], (int)this.getX()-  Camera.x, (int)this.getY() - Camera.y, null);

            }else if(dir == left_dir)
            {
                if(!moved)
                    g.drawImage(leftPlayer[0], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
                else
                    g.drawImage(leftPlayer[index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
            } else if(dir == down_dir)
            {
                if(!moved)
                    g.drawImage(downPlayer[0], (int)this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
                else
                    g.drawImage(downPlayer[indexUpDown], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
            } else if(dir == up_dir)
            {
                if(!moved)
                    g.drawImage(upPlayer[0], (int)this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
                else
                    g.drawImage(upPlayer[indexUpDown], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
            }

        }
        if(!isDameged && hasGun)
        {
            if(dir == right_dir)
            {
                if(!moved)
                    g.drawImage(rightPlayerWP[0], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
                else
                    g.drawImage(rightPlayerWP[index], (int)this.getX()-  Camera.x, (int)this.getY() - Camera.y, null);

            }else if(dir == left_dir)
            {
                if(!moved)
                    g.drawImage(leftPlayerWP[0], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
                else
                    g.drawImage(leftPlayerWP[index], (int)this.getX() - Camera.x, (int)this.getY() - Camera.y, null);
            } else if(dir == down_dir)
            {
                if(!moved)
                    g.drawImage(downPlayerWP[0], (int)this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
                else
                    g.drawImage(downPlayerWP[indexUpDown], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
            } else if(dir == up_dir)
            {
                if(!moved)
                    g.drawImage(upPlayerWP[0], (int)this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
                else
                    g.drawImage(upPlayerWP[indexUpDown], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
            }
        } if(isDameged){
            if(dir == up_dir)
                g.drawImage(damegePlayerUp, this.getX() - Camera.x, this.getY() - Camera.y, null);
            else if(dir == down_dir)
                g.drawImage(damegePlayerDown, this.getX() - Camera.x, this.getY() - Camera.y, null);
            if(dir == left_dir)
                g.drawImage(damegePlayerLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
            else if(dir == right_dir)
                g.drawImage(damegePlayerRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }

}


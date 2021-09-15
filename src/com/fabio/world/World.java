package com.fabio.world;

import com.fabio.entites.*;
import com.fabio.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World
{
    public static int WIDTH;
    public static int HEIGHT;
    public static final int TILE_SIZE = 30;
    public static int potionX, potionY;

    public static Tile[] tiles;

    public World(String path)
    {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));

            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0,0,map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            for(int xx = 0; xx < map.getWidth(); xx++)
            {
                for(int yy = 0; yy < map.getHeight(); yy++)
                {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];
                    tiles[xx+(yy*WIDTH)] = new FloorTile(xx*30,yy*30, Tile.TILE_FLOOR);
                    switch (pixelAtual)
                    {
                        case 0xFFFF0000:
                            //ENEMY
                            Enemy en = new Enemy(xx*30,yy*30,30,30, Entity.ENEMY_EN);
                            Game.entities.add(en);
                            Game.enemies.add(en);
                            break;
                        case 0xFF21FF3B:
                            //weapon
                            Game.entities.add(new Weapon(xx*30,yy*30,30,30, Entity.WEAPON_EN));
                            break;
                        case 0xFFFFFFFF:
                            //wall
                            tiles[xx+(yy*WIDTH)] = new WallTile(xx*30,yy*30, Tile.TILE_WALL);
                            break;
                        case 0xFF000000:
                            //floor
                            break;
                        case 0xFF0026FF:
                            //player
                            Game.player.setX(xx*30);
                            Game.player.setY(yy*30);
                            break;
                        case 0xFF21FFFF:
                            //lifepack
                            Game.entities.add(new LifePack(xx*30,yy*30,30,30, Entity.LIFEPACK_EN));
                            break;
                        case 0xFFA916FF:
                            //potion
                            potionX = xx * 30;
                            potionY = yy * 30;
                            Game.entities.add(new Potion(xx*30,yy*30,30,30, Entity.POTION_EN));
                            break;
                    }
                }
                //0xFFFF0000 vermelho

                //0xFF21FFFF ciano

                //0xFFA916FF roxo

                //0xFF21FF3B verde claro

                //0xFF0026FF azul

                //0xFFFFFFFF branco

                //0xFF000000 preto
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g)
    {
        int xstart = Camera.x/30;
        int ystart = Camera.y/30;

        int xfinal = xstart + (Game.width/30);
        int yfinal = ystart + (Game.height/30);

        for(int xx = xstart; xx <= xfinal; xx++)
        {
            for(int yy = ystart; yy <= yfinal; yy++)
            {
                if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
                {
                    continue;
                }
                Tile tile = tiles[xx+(yy*WIDTH)];
                tile.render(g);
            }
        }
    }

    public static boolean isFree(int xNext, int yNext)
    {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
                tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
                tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
                tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);
    }
}

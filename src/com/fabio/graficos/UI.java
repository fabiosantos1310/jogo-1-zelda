package com.fabio.graficos;

import com.fabio.entites.Player;
import com.fabio.main.Game;

import java.awt.*;

public class UI
{
    public void render(Graphics g)
    {
        //vida
        g.setColor(Color.red);
        g.fillRect(10, 5, 50, 10);
        g.setColor(Color.green);
        g.fillRect(10, 5, (int)((Game.player.life / Game.player.maxLife) * 50), 10);
        //mana
        g.setColor(Color.red);
        g.fillRect(10, 17, 50, 5);
        g.setColor(Color.blue);
        g.fillRect(10, 17, (int)(( Game.player.mana / Game.player.maxMana) * 50), 5);

        g.setColor(Color.white);
        g.setFont( new Font("arial", Font.BOLD, 9));
        g.drawString( (int)Game.player.life + "/" + (int)Game.player.maxLife, 20,14);
        g.setFont(new Font("arial", Font.BOLD, 7));
        g.drawString(Player.mana + "/" + 100, 20, 21);
    }
}

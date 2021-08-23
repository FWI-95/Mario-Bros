package com.fwi95.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Coin extends InterActiveTileObject{
    public Coin(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);

    }
}

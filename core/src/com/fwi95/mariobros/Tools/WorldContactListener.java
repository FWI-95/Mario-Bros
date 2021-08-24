package com.fwi95.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.fwi95.mariobros.MarioBros;
import com.fwi95.mariobros.Sprites.Enemy;
import com.fwi95.mariobros.Sprites.InterActiveTileObject;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        // Gdx.app.log("Begin Contact", "");

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && object.getUserData() instanceof InterActiveTileObject){
                ((InterActiveTileObject) object.getUserData()).onHeadHit();
                
            }
        }

        switch(cDef){
            case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                        ((Enemy)fixA.getUserData()).hitOnHead();    
                else if(fixB.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT)
                        ((Enemy)fixB.getUserData()).hitOnHead();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub
        
    }
    
}

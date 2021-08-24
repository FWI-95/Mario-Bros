package com.fwi95.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fwi95.mariobros.MarioBros;
import com.fwi95.mariobros.Screens.PlayScreen;

public class Mario extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING };
    public State currentState, previousState;
    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTime;

    public boolean runningRight;

    private float lastX, lastY;

    public Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTime = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioRun = new Animation<TextureRegion>(0.1f, frames);
        
        frames.clear();

        for(int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioJump = new Animation<TextureRegion>(0.1f, frames);

        marioStand = new TextureRegion(getTexture(), 1, 11, 16, 16);

        defineMario();
        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float delta){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));

        if(lastX != getX() || lastY != getY()){
            Gdx.app.log("Mario X: " + getX() + " Y: " + getY(), "");
            lastX = getX();
            lastY = getY();
        }
    }

    public TextureRegion getFrame(float delta){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case JUMPING: 
                region = marioJump.getKeyFrame(stateTime);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTime, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;

        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM,32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        fdef.filter.maskBits = 
            MarioBros.GROUND_BIT 
            | MarioBros.COIN_BIT 
            | MarioBros.BRICK_BIT
            | MarioBros.OBJECT_BIT
            | MarioBros.ENEMY_BIT
            | MarioBros.ENEMY_HEAD_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // FixtureDef fdef2 = new FixtureDef();
        // EdgeShape feet = new EdgeShape();
        // feet.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
        // fdef2.shape = feet;
        // fdef2.filter.maskBits = MarioBros.NOTHING_BIT;
        // b2body.createFixture(fdef2);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }
}

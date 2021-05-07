package org.chis;

import org.ejml.simple.SimpleMatrix;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;





public class App 
{
    public static void main( String[] args )
    {

        
        World world = new World(new Vec2(0, -10));
        System.out.println( "Hello World!" );
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(0.0f, 4.0f);
        Body body = world.createBody(bodyDef);

        PolygonShape dynamicBox = new PolygonShape();;
        dynamicBox.setAsBox(1.0f, 1.0f);
     
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        body.createFixture(fixtureDef);

        while(true){
            world.step(1, 1, 1);
            System.out.println(body.getPosition());
        }




    }
}

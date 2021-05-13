package org.chis.mechs;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class RootMech extends Mech{


    public RootMech(float width, float height, float y, float density, World world){

        parameters = new float[] {width, height, y, density};
        this.world = world;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(0, y);
        body = world.createBody(bd);
        body.createFixture(fd);
    }

    public RootMech(float[] parameters, World world){
        new RootMech(parameters[0], parameters[1], parameters[2], parameters[3], world);
    }
}

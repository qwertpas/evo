package org.chis.mechs;

import org.chis.Mech;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class CircleMech extends Mech{
    public CircleMech(float radius, float density, Vec2 anchor, World world){

        parameters = new float[] {radius, density, anchor.x, anchor.y};
        this.world = world;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(anchor.x, anchor.y);
        
        body = world.createBody(bd);
        body.createFixture(fd);

    }

}

package org.chis.mechs;

import org.chis.Mech;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class RectMech extends Mech{
    
    public RectMech(float width, float height, float density, Vec2 center, float angle, World world){

        // parameters = new float[] {width, height, density, anchor.x, anchor.y};
        // this.parent = parent;
        // this.world = world;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(center);
        bd.angle = angle;

        body = world.createBody(bd);
        body.createFixture(fd);

    }

    
}

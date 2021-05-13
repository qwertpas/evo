package org.chis.mechs;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class RigidMech extends Mech{

    public RigidMech(float width, float height, float density, Vec2 anchor, Body parent, World world){

        

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(anchor.x, anchor.y + height);
        body = world.createBody(bd);
        body.createFixture(fd);

        WeldJointDef wjd = new WeldJointDef();

        wjd.initialize(parent, body, anchor);

        joint = world.createJoint(wjd);
    }
}

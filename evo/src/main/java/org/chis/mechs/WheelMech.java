package org.chis.mechs;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class WheelMech extends Mech{
    public WheelMech(float radius, float density, Vec2 anchor, Body parent, World world){

        parameters = new float[] {radius, density, anchor.x, anchor.y};
        this.parent = parent;
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

        RevoluteJointDef revJointDef = new RevoluteJointDef();

        revJointDef.initialize(parent, body, anchor);
        revJointDef.motorSpeed = 0.0f;
        revJointDef.maxMotorTorque = 200.0f;
        revJointDef.enableMotor = true;

        joint = world.createJoint(revJointDef);

    }

    public WheelMech(float[] parameters, Body parent, World world){
        new WheelMech(parameters[0], parameters[1], new Vec2(parameters[2], parameters[3]), parent, world);
    }
}

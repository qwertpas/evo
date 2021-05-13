package org.chis.mechs;

import org.chis.mechs.Vector2D.Type;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class ArmMech extends Mech{
    
    public ArmMech(float width, float height, float density, Vec2 anchor, Body parent, World world){

        parameters = new float[] {width, height, density, anchor.x, anchor.y};
        this.parent = parent;
        this.world = world;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = density;
        fd.friction = 0.9f;

        Vector2D out = new Vector2D(anchor.sub(parent.getPosition()));
        Vec2 outHeight = new Vector2D(anchor).add(new Vector2D(height, out.getAngle(), Type.POLAR)).toVec2();

        // System.out.println(out.getAngle());
        // System.out.println(outHeight);

        bd.position.set(outHeight.x, outHeight.y);
        bd.angle = (float) out.rotate90().getAngle();

        body = world.createBody(bd);
        body.createFixture(fd);

        RevoluteJointDef revJointDef = new RevoluteJointDef();

        revJointDef.initialize(parent, body, anchor);
        revJointDef.motorSpeed = 0.0f;
        revJointDef.maxMotorTorque = 200.0f;
        revJointDef.enableMotor = true;

        joint = world.createJoint(revJointDef);
    }

    public ArmMech(float[] parameters, Body parent, World world){
        new ArmMech(parameters[0], parameters[1], parameters[2], new Vec2(parameters[3], parameters[4]), parent, world);
    }
}

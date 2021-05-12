package org.chis;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Mech {
    public enum Motion{
        RIGID, ANGLE, WHEEL, SLIDE
    }

    public Neur inputNeur;

    Body body;
    Joint joint;

    Motion motionType;


    public Mech(float width, float height, Vec2 anchor, Body parent, World world){
        motionType = Motion.ANGLE;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = 1.0f;
        fd.friction = 0.9f;

        bd.position.set(anchor.x, anchor.y + height);
        body = world.createBody(bd);
        body.createFixture(fd);

        RevoluteJointDef revJointDef = new RevoluteJointDef();

        revJointDef.initialize(parent, body, anchor);
        revJointDef.motorSpeed = 0.0f;
        revJointDef.maxMotorTorque = 200.0f;
        revJointDef.enableMotor = true;

        joint = world.createJoint(revJointDef);
    }

    public void update(){
        if(motionType == Motion.ANGLE){
            RevoluteJoint revJoint = ((RevoluteJoint) joint);
            revJoint.setMotorSpeed(inputNeur.energy);
            

        }
        
    }

}

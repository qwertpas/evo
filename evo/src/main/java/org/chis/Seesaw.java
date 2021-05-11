package org.chis;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;


public class Seesaw {

    static RevoluteJoint m_joint1;
    static PrismaticJoint m_joint2;

    static World world = new World(new Vec2(0, -10));

    static Body bouncingBox;

    public static void initTest() {

        Body ground = null;
        {
            BodyDef bd = new BodyDef();
            ground = getWorld().createBody(bd);

            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
            ground.createFixture(shape, 0.0f);
        }

        {
            Body prevBody = ground;

            // Define crank.
            {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.5f, 2.0f);

                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.position.set(0.0f, 7.0f);
                Body body = getWorld().createBody(bd);
                body.createFixture(shape, 2.0f);

                RevoluteJointDef rjd = new RevoluteJointDef();
                rjd.initialize(prevBody, body, new Vec2(0.0f, 5.0f));
                rjd.motorSpeed = 1.0f * MathUtils.PI;
                rjd.maxMotorTorque = 10000.0f;
                rjd.enableMotor = true;
                m_joint1 = (RevoluteJoint) getWorld().createJoint(rjd);

                prevBody = body;
            }

            // Define follower.
            {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.5f, 4.0f);

                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.position.set(0.0f, 13.0f);
                Body body = getWorld().createBody(bd);
                body.createFixture(shape, 2.0f);

                RevoluteJointDef rjd = new RevoluteJointDef();
                rjd.initialize(prevBody, body, new Vec2(0.0f, 9.0f));
                rjd.enableMotor = false;
                getWorld().createJoint(rjd);

                prevBody = body;
            }

            // Define piston
            {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(1.5f, 1.5f);

                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.fixedRotation = true;
                bd.position.set(0.0f, 17.0f);
                Body body = getWorld().createBody(bd);
                body.createFixture(shape, 2.0f);

                RevoluteJointDef rjd = new RevoluteJointDef();
                rjd.initialize(prevBody, body, new Vec2(0.0f, 17.0f));
                getWorld().createJoint(rjd);

                PrismaticJointDef pjd = new PrismaticJointDef();
                pjd.initialize(ground, body, new Vec2(0.0f, 17.0f), new Vec2(0.0f, 1.0f));

                pjd.maxMotorForce = 1000.0f;
                pjd.enableMotor = false;

                m_joint2 = (PrismaticJoint) getWorld().createJoint(pjd);
            }

            // Create a payload
            {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(1.5f, 1.5f);

                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.position.set(0.0f, 23.0f);
                bouncingBox = getWorld().createBody(bd);
                bouncingBox.createFixture(shape, 2.0f);
            }
        }
    }

    public static World getWorld() {
        return world;
    }

    public static void main(String[] args) {
        initTest();
        for(int i = 0; i < 100; i++){
            world.step(1/60.0f, 1, 1);
            System.out.println(bouncingBox.getPosition());
        }
        
    }
}
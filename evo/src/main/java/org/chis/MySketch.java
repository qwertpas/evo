package org.chis;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import processing.core.PApplet;

public class MySketch extends PApplet {

    World world;

    Body groundBody;
    Body chassisBody;
    Body wheelLBody;
    Body wheelRBody;


    public void init() {

        { //WORLD
            world = new World(new Vec2(0, -10f));
        }

        { //GROUND
            BodyDef groundBodyDef = new BodyDef();
            groundBody = world.createBody(groundBodyDef);

            EdgeShape groundShape = new EdgeShape();
            groundShape.set(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
            groundBody.createFixture(groundShape, 0.0f);
        }

        { //ROBOT
            
            { //chassis
                BodyDef chassisBodyDef = new BodyDef();
                chassisBodyDef.type = BodyType.DYNAMIC;
                chassisBodyDef.position.set(0.0f, 2.0f);

                PolygonShape chassisShape = new PolygonShape();
                chassisShape.setAsBox(2.0f, 0.1f);
                
                chassisBody = world.createBody(chassisBodyDef);
                chassisBody.createFixture(chassisShape, 2.0f);
            }
            
            { //wheelL
                BodyDef wheelLBodyDef = new BodyDef();
                wheelLBodyDef.type = BodyType.DYNAMIC;
                wheelLBodyDef.position.set(-1.0f, 0.0f);

                CircleShape wheelLShape = new CircleShape();
                wheelLShape.setRadius(0.5f);

                wheelLBody = world.createBody(wheelLBodyDef);
                wheelLBody.createFixture(wheelLShape, 2.0f);

                RevoluteJointDef wheelLJoint = new RevoluteJointDef();
                wheelLJoint.initialize(wheelLBody, chassisBody, new Vec2(-1.0f, 0.0f));
                wheelLJoint.motorSpeed = 1.0f * MathUtils.PI;
                wheelLJoint.maxMotorTorque = 10000.0f;
                wheelLJoint.enableMotor = true;
            }

            { //wheelR
                BodyDef wheelRBodyDef = new BodyDef();
                wheelRBodyDef.type = BodyType.DYNAMIC;
                wheelRBodyDef.position.set(1.0f, 0.0f);

                CircleShape wheelRShape = new CircleShape();
                wheelRShape.setRadius(0.5f);

                wheelRBody = world.createBody(wheelRBodyDef);
                wheelRBody.createFixture(wheelRShape, 2.0f);

                RevoluteJointDef wheelRJoint = new RevoluteJointDef();
                wheelRJoint.initialize(wheelRBody, chassisBody, new Vec2(1.0f, 0.0f));
                wheelRJoint.motorSpeed = 1.0f * MathUtils.PI;
                wheelRJoint.maxMotorTorque = 10000.0f;
                wheelRJoint.enableMotor = true;
            }
        }
    }

    @Override
    public void settings() {
        init();
        size(500, 500);
    }

    @Override
    public void draw() {
        clear();

        PDraw.drawWorld(world, this);
        world.step(1/60.0f, 1, 1);
    }



    public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		MySketch mySketch = new MySketch();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
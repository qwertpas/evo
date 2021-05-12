package org.chis;

import java.awt.Toolkit;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import processing.core.PApplet;

public class MySketch extends PApplet {

    World world;

    Body groundBody;
    Body chassisBody;
    Body wheelLBody;
    Body wheelRBody;
    Body armBody;

    RevoluteJoint armJoint;

    public void init() {

        { // WORLD
            world = new World(new Vec2(0, -10f));
        }

        { // GROUND
            BodyDef groundBodyDef = new BodyDef();
            groundBody = world.createBody(groundBodyDef);

            EdgeShape groundShape = new EdgeShape();
            groundShape.set(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
            groundBody.createFixture(groundShape, 0.0f);
        }

        // Car
        {
            PolygonShape chassis = new PolygonShape();
            chassis.setAsBox(0.38f, 0.025f);

            CircleShape circle = new CircleShape();
            circle.m_radius = 0.076f;

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.fixedRotation = false;
            bd.position.set(0.0f, 1.0f);
            chassisBody = world.createBody(bd);
            chassisBody.createFixture(chassis, 10.0f);

            FixtureDef fd = new FixtureDef();
            fd.shape = circle;
            fd.density = 1.0f;
            fd.friction = 0.9f;

            bd.position.set(-0.35f, 1.0f);
            wheelLBody = world.createBody(bd);
            wheelLBody.createFixture(fd);

            bd.position.set(0.35f, 1.0f);
            wheelRBody = world.createBody(bd);
            wheelRBody.createFixture(fd);

            PolygonShape armShape = new PolygonShape();
            armShape.setAsBox(0.025f, 0.38f);

            fd.shape = armShape;
            bd.position.set(0, 1.5f);
            armBody = world.createBody(bd);
            armBody.createFixture(fd);

            bd.position.set(0, 2.4f);
            Body armBody2 = world.createBody(bd);
            armBody2.createFixture(fd);

            RevoluteJointDef jd = new RevoluteJointDef();

            jd.initialize(chassisBody, wheelLBody, wheelLBody.getPosition());
            jd.motorSpeed = 10.0f;
            jd.maxMotorTorque = 20.0f;
            jd.enableMotor = false;
            world.createJoint(jd);

            jd.initialize(chassisBody, wheelRBody, wheelRBody.getPosition());
            jd.motorSpeed = 0.0f;
            jd.maxMotorTorque = 10.0f;
            jd.enableMotor = false;
            world.createJoint(jd);

            jd.initialize(armBody, chassisBody, new Vec2(0, 1f));
            jd.motorSpeed = 0.0f;
            jd.maxMotorTorque = 10.0f;
            jd.enableMotor = true;
            armJoint = (RevoluteJoint) (world.createJoint(jd));

            jd.initialize(armBody, armBody2, new Vec2(0, 2f));
            jd.motorSpeed = 0.0f;
            jd.maxMotorTorque = 10.0f;
            jd.enableMotor = false;
            world.createJoint(jd);
        }
    }

    @Override
    public void settings() {
        init();
        size(Toolkit.getDefaultToolkit().getScreenSize().width, 500);
    }

    @Override
    public void draw() {
        clear();

        PDraw.drawWorld(world, this);
        world.step(1 / 60.0f, 1, 2);

    }

    @Override
    public void keyPressed(){
        if(key == 'a'){
            armJoint.setMotorSpeed(-5);
        }
        if(key == 'd'){
            armJoint.setMotorSpeed(5);
        }
    }

    @Override
    public void keyReleased(){
        armJoint.setMotorSpeed(0);
    }

    public static void main(String[] args) {
        String[] processingArgs = { "MySketch" };
        MySketch mySketch = new MySketch();

        PApplet.runSketch(processingArgs, mySketch);


        
        // mySketch.init();
        // long lasttime = System.nanoTime();
        // while(true){
        //     mySketch.world.step(1 / 60.0f, 1, 2);
        //     System.out.println((System.nanoTime() - lasttime) * 1e-9);
        //     lasttime = System.nanoTime();
        // }
    }
}
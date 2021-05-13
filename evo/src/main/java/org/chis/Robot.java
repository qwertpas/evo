package org.chis;

import java.util.ArrayList;
import java.util.Random;

import org.chis.mechs.ArmMech;
import org.chis.mechs.Mech;
import org.chis.mechs.RootMech;
import org.chis.mechs.WheelMech;
import org.jbox2d.collision.shapes.CircleShape;
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

public class Robot {
    public ArrayList<Body> bodies = new ArrayList<Body>();
    public ArrayList<Joint> joints = new ArrayList<Joint>();
    public ArrayList<Neur> neurs = new ArrayList<Neur>();

    Random rng = new Random();

    public Robot(World world){
        float y = 1;

        mechs.add(new RootMech(0.38f, 0.025f, y, 10, world));
        Body root = mechs.get(mechs.size()-1).body;
        

        mechs.add(new WheelMech(0.076f, 0.1f, new Vec2(-0.35f, y), root, world));
        mechs.add(new WheelMech(0.076f, 0.1f, new Vec2(0.35f, y), root, world));

        mechs.add(new ArmMech(0.025f, 0.38f, 0.1f, new Vec2(0, y+0.01f), root, world));
        // RevoluteJoint s = (RevoluteJoint) (mechs.get(mechs.size()-1).joint);
        // s.setMotorSpeed(1);

        addRevJoint(mechs.get(1).body, mechs.get(2).body, new Vec2(-0.35f, y), world);

        
    }

    public void addRectBody(float width, float height, float density, Vec2 center, float angle, World world){

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(center.x, center.y);
        bd.angle = angle;
        Body body = world.createBody(bd);
        body.createFixture(fd);

        bodies.add(body);
    }

    public void addCircleBody(float radius, float density, Vec2 center, World world){

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(center.x, center.y);
        Body body = world.createBody(bd);
        body.createFixture(fd);

        bodies.add(body);
    }

    public void addRevJoint(Body b1, Body b2, Vec2 anchor, World world){
        RevoluteJointDef revJointDef = new RevoluteJointDef();

        revJointDef.initialize(b1, b2, anchor);
        revJointDef.motorSpeed = 0.0f;
        revJointDef.maxMotorTorque = 200.0f;
        revJointDef.enableMotor = true;

        joints.add((RevoluteJoint) world.createJoint(revJointDef));
    }

    public void update(){
        
    }


    public void mutate(World newWorld){
        //randomize mechs and neurs, let Sim take care of detached nodes

        Robot newRobot = new Robot(newWorld);
        for(Mech mech : mechs){

            float[] newParameters = new float[mech.parameters.length];
            for(int i = 0; i < mech.parameters.length; i++){
                newParameters[i] = mech.parameters[i] + (float) rng.nextGaussian();
            }
            
            if(mech.getClass() == ArmMech.class){
                ArmMech oldMech = (ArmMech) mech;
                newRobot.mechs.add(new ArmMech(newParameters, oldMech.parent, newWorld));
            }

            if(mech.getClass() == WheelMech.class){
                WheelMech oldMech = (WheelMech) mech;
                newRobot.mechs.add(new WheelMech(newParameters, oldMech.parent, newWorld));
            }

            if(mech.getClass() == RootMech.class){
                newRobot.mechs.add(new RootMech(newParameters, newWorld));
            }



        }
    }

    public static void main(String[] args) {
        World world = new World(new Vec2(0, 0)); 
        new Robot(world);

        for (Body body = world.getBodyList(); body != null; body = body.getNext()){
            System.out.println(body.getPosition());
        }
        
    }


}

package org.chis;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class WorldSim {
    Robot robot;
    Body box;
    Body ground;
    World world;

    boolean attached = false;
    int lastAttachTick = 0;
    int tick = 0;
    WeldJoint weldJoint;

    public WorldSim(){

        { // WORLD
            world = new World(new Vec2(0, -10f));
        }

        { // GROUND
            BodyDef groundBodyDef = new BodyDef();
            ground = world.createBody(groundBodyDef);

            EdgeShape groundShape = new EdgeShape();
            groundShape.set(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
            ground.createFixture(groundShape, 0.0f);
        }
        
        { // BOX

            PolygonShape boxShape = new PolygonShape();
            boxShape.setAsBox(0.2f, 0.2f);

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.fixedRotation = false;
            bd.position.set(2, 2);
            box = world.createBody(bd);
            box.createFixture(boxShape, 0.001f); 
        }

        robot = new Robot(world);
    }


    public void loop() {

        
        robot.update(
            box.getPosition().x - robot.mechs.get(0).body.getPosition().x,
            attached
        );

        if(!robot.mechJoints.get(3).grab){
            world.destroyJoint(weldJoint);
            attached = false;
        }

        if(!attached && tick - lastAttachTick > 90){
            for (ContactEdge ce = box.getContactList(); ce != null; ce = ce.next){
                if (ce.other != ground && ce.contact.isTouching()){
                    System.out.println("grab");
    
                    WeldJointDef jd = new WeldJointDef();
    
                    jd.initialize(ce.other, box, box.getPosition());
                    weldJoint = (WeldJoint) world.createJoint(jd);
    
                    attached = true;
                    lastAttachTick = tick;
                    
                    break;
                }
            }
        }
        
        tick++;
        world.step(1 / 45.0f, 6, 8);

    }
    

    public static void main(String[] args) {
        WorldSim worldSim = new WorldSim();

        for(int t = 0; t < 670; t++){
            worldSim.loop();
        }
        System.out.println(worldSim.box.getPosition().x);
    }
}

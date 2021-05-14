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
                    // System.out.println("grab");
    
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

    public float workout(){
        for(int t = 0; t < 670; t++){
            loop();
        }
        float endPos = box.getPosition().x;
        return -endPos + 2;
    }
    

    public static void main(String[] args) {

        int pop = 50;
        int generations = 5;

        WorldSim[] worldSims = new WorldSim[pop];
        Robot[] robots = new Robot[pop];

        Robot bestRobot = new Robot(new WorldSim().world);

        // System.out.println("\n original genome: " + bestRobot.getGenome());

        for(int g = 0; g < generations; g++){

            float bestScore = 0;

            for(int i = 0; i < pop; i++){
                worldSims[i] = new WorldSim();
                robots[i] = bestRobot.mutate(worldSims[i].world);
                worldSims[i].robot = robots[i];
            }

            for(int i = 0; i < pop; i++){
                float score = worldSims[i].workout();
                if(score > bestScore){
                    bestScore = score;
                    bestRobot = robots[i];
                }
            }

            System.out.println("gen: " + g + ", bestScore: " + bestScore);
            // System.out.println("genome: " + bestRobot.getGenome());

        }

        WorldSim demoSim = new WorldSim();
        demoSim.robot = bestRobot.copy(demoSim.world);
        new Visualizer(demoSim).run();

    }
}

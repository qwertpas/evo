package org.chis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
            groundShape.set(new Vec2(-2000.0f, 0.0f), new Vec2(40.0f, 0.0f));

            FixtureDef fd = new FixtureDef();
            fd.shape = groundShape;
            fd.density = 0;
            fd.friction = 0.9f;

            ground.createFixture(fd);
        }
        
        { // BOX

            PolygonShape boxShape = new PolygonShape();
            boxShape.setAsBox(0.2f, 0.2f);

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.fixedRotation = false;
            bd.position.set(2, 2);
            box = world.createBody(bd);
            box.createFixture(boxShape, 0.1f); 
        }
    }


    public void loop() {

        
        robot.update(
            box.getPosition().x - robot.mechs.get(0).body.getPosition().x,
            attached
        );

        if(!robot.mechJoints.get(3).grab){
            if(weldJoint != null){
                world.destroyJoint(weldJoint);
            }
            attached = false;
        }

        if(!attached && tick - lastAttachTick > 30){
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
        Integer[] generations = new Integer[]{5, 10, 20};
        int lastGen = Collections.max(Arrays.asList(generations));

        WorldSim[] worldSims = new WorldSim[pop];
        Robot[] robots = new Robot[pop];

        Robot bestRobot = new Robot(new WorldSim().world);

        if(generations[0] == 0){
            visualize(bestRobot, 0);
        }


        for(int g = 1; g < lastGen + 1; g++){

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

            final int gen = g;
            if(Arrays.stream(generations).anyMatch(i -> i == gen)){
                visualize(bestRobot, g);
                if(gen == lastGen){
                    break;
                }
            }

        }
        System.exit(0);


    }

    private static void visualize(Robot robot, int g){
        WorldSim demoSim = new WorldSim();
        demoSim.robot = robot.copy(demoSim.world);
        Visualizer v = new Visualizer(demoSim, g);
        v.run();

        BufferedReader input = new BufferedReader (new InputStreamReader (System.in));

        try {
            input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        v.noLoop();
    }
}

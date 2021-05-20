package org.chis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
            fd.friction = 1f;

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

            FixtureDef fd = new FixtureDef();
            fd.shape = boxShape;
            fd.density = 0.1f;
            fd.friction = 1f;

            box.createFixture(fd); 
        }

        { // obstacle

            PolygonShape boxShape = new PolygonShape();
            boxShape.setAsBox(0.5f, 0.5f);

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.fixedRotation = false;
            bd.position.set(3.5f, 2);
            Body obstacle = world.createBody(bd);

            FixtureDef fd = new FixtureDef();
            fd.shape = boxShape;
            fd.density = 100f;
            fd.friction = 0.1f;

            obstacle.createFixture(fd); 
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
        world.step(1 / 60.0f, 6, 8);

    }

    public float workout(){
        for(int t = 0; t < 650; t++){
            loop();
        }
        float endPos = box.getPosition().x;
        return -(-endPos + 2);
    }
    

    public static void main(String[] args) {

        int pop = 50;
        Integer[] generations = new Integer[]{40};
        int lastGen = Collections.max(Arrays.asList(generations));

        WorldSim[] worldSims = new WorldSim[pop];
        Robot[] robots = new Robot[pop];

        Robot bestRobot = new Robot(new WorldSim().world);

        if(generations[0] == 0){
            Visualizer.visualize(bestRobot, 0, true);
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

            saveRobot(bestRobot, g);
            System.out.println("g: " + g + ", n: " + bestRobot.neurs.size() +  ", nj: " + bestRobot.neurJoints.size() + ", bestScore: " + bestScore);

            final int gen = g;
            if(Arrays.stream(generations).anyMatch(i -> i == gen)){
                Visualizer.visualize(bestRobot, g, true);
                if(gen == lastGen){
                    break;
                }
            }

        }
        System.exit(0);


    }

    

    private static void saveRobot(Robot robot, int g){
        try {
            File file = new File("evo/saves", g+".robot");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(robot);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}

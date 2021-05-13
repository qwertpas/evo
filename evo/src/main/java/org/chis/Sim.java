package org.chis;

import java.awt.Toolkit;
import java.util.ArrayList;

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

import processing.core.PApplet;

public class Sim extends PApplet{
    ArrayList<Robot> robots = new ArrayList<Robot>();
    Body box;
    Body ground;
    World world;

    @Override
    public void settings() {
        size(Toolkit.getDefaultToolkit().getScreenSize().width, 500);

        init();
    }

    public void init(){

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

        if(robots.size() > 0){
            Robot lastRobot = robots.get(robots.size() - 1);
            robots.add(lastRobot.mutate(world));
        }else{
            robots.add(new Robot(world));
        }
    }

    boolean attached = false;
    long lastAttachTime = 0;
    WeldJoint weldJoint;

    @Override
    public void draw() {
        clear();



        if(!attached && System.currentTimeMillis() - lastAttachTime > 1000){
            for (ContactEdge ce = box.getContactList(); ce != null; ce = ce.next){
                if (ce.other != ground && ce.contact.isTouching()){
                    System.out.println("grab");
    
                    WeldJointDef jd = new WeldJointDef();
    
                    jd.initialize(ce.other, box, box.getPosition());
                    weldJoint = (WeldJoint) world.createJoint(jd);
    
                    attached = true;
                    lastAttachTime = System.currentTimeMillis();
                    
                    break;
                }
            }
        }
        

        PDraw.drawWorld(world, this);
        world.step(1 / 60.0f, 1, 2);

    }

    @Override
    public void keyPressed() {
        if(key == 'm'){
            System.out.println("mutate");

            init();
        }
    }

    

    public static void main(String[] args) {
        String[] processingArgs = { "Sim" };
        Sim sim = new Sim();

        PApplet.runSketch(processingArgs, sim);


        
        // mySketch.init();
        // long lasttime = System.nanoTime();
        // while(true){
        //     mySketch.world.step(1 / 60.0f, 1, 2);
        //     System.out.println((System.nanoTime() - lasttime) * 1e-9);
        //     lasttime = System.nanoTime();
        // }
    }
}

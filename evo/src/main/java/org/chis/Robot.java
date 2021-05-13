package org.chis;

import java.util.ArrayList;
import java.util.Random;

import org.chis.Mech.Shape;
import org.chis.MechJoint.Type;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class Robot {
    public ArrayList<Mech> mechs = new ArrayList<Mech>();
    public ArrayList<MechJoint> mechJoints = new ArrayList<MechJoint>();
    public ArrayList<Neur> neurs = new ArrayList<Neur>();

    public ArrayList<Integer[]> jointMap = new ArrayList<Integer[]>();

    public int maxMechID = -1;

    Random random = new Random();

    public Robot(World world){
        float y = 1;

        addRectMech(0.38f, 0.025f, 10, new Vec2(0, y), 0, world); //base
        addCircleMech(0.076f, 0.1f, new Vec2(-0.35f, y), world); //wheel left
        addCircleMech(0.076f, 0.1f, new Vec2(+0.35f, y), world); //wheel right
        addRectMech(0.025f, 0.38f, 0.1f, new Vec2(0, y+0.38f), 0, world); //arm
        
        addMechJointRev(0, 1, new Vec2(-0.35f, y), world);
        addMechJointRev(0, 2, new Vec2(+0.35f, y), world);
        addMechJointRev(0, 3, new Vec2(0, y+0.01f), world);
    }

    public Robot(){}

    public void addRectMech(float width, float height, float density, Vec2 center, float angle, World world){
        maxMechID += 1;

        Mech mech = new Mech(width, height, density, center, angle, world);

        mech.id = maxMechID;
        
        mechs.add(mech);
    }

    public void addCircleMech(float radius, float density, Vec2 center, World world){
        maxMechID += 1;

        Mech mech = new Mech(radius, density, center, world);

        mech.id = maxMechID;
        
        mechs.add(mech);
    }
    

    public void addMechJointRev(int idA, int idB, Vec2 anchor, World world){

        mechJoints.add(new MechJoint(mechs.get(idA), mechs.get(idB), anchor, world));

    }

    public void addMechJointWeld(int idA, int idB, World world){

        mechJoints.add(new MechJoint(mechs.get(idA), mechs.get(idB), world));

    }

    public void update(){
        
    }


    public Robot mutate(World newWorld){

        Robot newRobot = new Robot();

        for(Mech oldMech : this.mechs){
            if(oldMech.shape == Shape.CIRCLE){
                newRobot.addCircleMech(
                    oldMech.radius + rng(0.01), 
                    oldMech.density + rng(1), 
                    oldMech.center.add(new Vec2(rng(0.1), rng(0.1))), 
                    newWorld
                );
            }
            if(oldMech.shape == Shape.RECT){
                newRobot.addRectMech(oldMech.width, oldMech.height, oldMech.density, oldMech.center, oldMech.angle, newWorld);
            }
        }
        
        for(MechJoint oldJoint : this.mechJoints){
            if(oldJoint.type == Type.REV){
                newRobot.addMechJointRev(oldJoint.mechA.id, oldJoint.mechB.id, oldJoint.anchor, newWorld);
            }
            if(oldJoint.type == Type.WELD){
                newRobot.addMechJointWeld(oldJoint.mechA.id, oldJoint.mechB.id, newWorld);
            }
        }

        return newRobot;
    }

    public float rng(double scale){
        return (float) (random.nextGaussian() * scale);
    }

    public void growArm(){
        // Vector2D out = new Vector2D(anchor.sub(parent.getPosition()));
        // Vec2 outHeight = new Vector2D(anchor).add(new Vector2D(height, out.getAngle(), Type.POLAR)).toVec2();



        // bd.position.set(outHeight.x, outHeight.y);
        // bd.angle = (float) out.rotate90().getAngle();
    }

    public static void main(String[] args) {
        World world = new World(new Vec2(0, 0)); 
        new Robot(world);

        for (Body body = world.getBodyList(); body != null; body = body.getNext()){
            System.out.println(body.getPosition());
        }
        
    }


}

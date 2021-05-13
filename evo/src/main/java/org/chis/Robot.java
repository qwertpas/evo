package org.chis;

import java.util.ArrayList;
import java.util.Random;

import org.chis.Mech.Shape;
import org.chis.MechJoint.Type;
import org.chis.NeurJoint.NeurType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class Robot {
    public ArrayList<Mech> mechs = new ArrayList<Mech>();
    public ArrayList<MechJoint> mechJoints = new ArrayList<MechJoint>();

    public ArrayList<Neur> neurs = new ArrayList<Neur>();
    public ArrayList<NeurJoint> neurJoints = new ArrayList<NeurJoint>();

    public ArrayList<Float> data = new ArrayList<Float>();

    public int maxMechID = -1;
    public int maxMechJointID = -1;

    Random random = new Random();

    public Robot(World world){
        float y = 1;

        addRectMech(0.38f, 0.025f, 10, new Vec2(0, y), 0, world); //base
        addCircleMech(0.076f, 0.1f, new Vec2(-0.35f, y), world); //wheel left
        addCircleMech(0.076f, 0.1f, new Vec2(+0.35f, y), world); //wheel right
        addRectMech(0.025f, 0.5f, 0.1f, new Vec2(0, y+0.5f), 0, world); //arm
        
        addMechJointRev(0, 1, new Vec2(-0.35f, y), world); //0
        addMechJointRev(0, 2, new Vec2(+0.35f, y), world); //1
        addMechJointRev(0, 3, new Vec2(0, y+0.01f), world); //2
        addMechJointRelease(); //3

        for(int i = 0; i < 10; i++){
            Neur neur = new Neur();
            neur.id = i;
            neurs.add(neur);
        }

        //get arm angle and put into neur 2
        setNeurSensor(2, 2, -6f, -5f);

        //NOT attached
        setNeurInner(1, 3, -10, 1);

        //boxX > 0
        setNeurInner(0, 4, 20, -10);

        //(NOT attached) AND (boxX > 0)
        setNeurInner(3, 5, 10, -7.5f);
        setNeurInner(4, 5, 10, -7.5f);

        //set wheel powers
        setNeurOutput(5, 0, -20, 10);
        setNeurOutput(5, 1, -20, 10);

        //if arm too high
        setNeurInner(2, 6, -10, +10f);

        //(NOT attached) AND arm too high
        setNeurInner(3, 7, 10, -7.5f);
        setNeurInner(6, 7, 10, -7.5f);


        //set arm power
        setNeurOutput(7, 2, -5, 2.5f);

        //arm high enough to launch
        setNeurInner(2, 8, -5, 2.5f);
        
        //attached AND arm too high
        setNeurInner(1, 9, 10, -7.5f);
        setNeurInner(8, 9, 10, -7.5f);

        //release
        setNeurOutput(9, 3, 10, -5);


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
        maxMechJointID += 1;
        MechJoint mechJoint = new MechJoint(mechs.get(idA), mechs.get(idB), anchor, world);
        mechJoint.id = maxMechJointID;
        mechJoints.add(mechJoint);

    }

    public void addMechJointWeld(int idA, int idB, World world){
        maxMechJointID += 1;
        MechJoint mechJoint = new MechJoint(mechs.get(idA), mechs.get(idB), world);
        mechJoint.id = maxMechJointID;
        mechJoints.add(mechJoint);
    }

    public void addMechJointRelease(){
        maxMechJointID += 1;
        MechJoint mechJoint = new MechJoint();
        mechJoint.id = maxMechJointID;
        mechJoints.add(mechJoint);
    }


    public void setNeurSensor(int mechJID, int neurID, float weight, float bias){
        neurJoints.add(new NeurJoint(mechJoints.get(mechJID), neurs.get(neurID), weight, bias));
    }

    public void setNeurInner(int neurInputID, int neurOutputID, float weight, float bias){
        neurJoints.add(new NeurJoint(neurs.get(neurInputID), neurs.get(neurOutputID), weight, bias));
    }

    public void setNeurOutput(int neurID, int mechJID, float weight, float bias){
        neurJoints.add(new NeurJoint(neurs.get(neurID), mechJoints.get(mechJID), weight, bias));
        // System.out.println("newNeurOutput: " + neurID + " to " + mechJID);
    }

    public void update(float boxX, boolean attached){
        neurs.get(0).energy = boxX;
        neurs.get(1).energy = attached ? 999 : -999;

        for(NeurJoint neurJoint : neurJoints){
            neurJoint.update();
        }

        for(int i = 0; i<neurs.size(); i++){
            System.out.println("neur " + i + " : " + neurs.get(i).get());
        }

        for(Neur neur : neurs){
            neur.energy = 0;
        }

        
    }


    public Robot mutate(World newWorld){

        Robot newRobot = new Robot();

        for(Mech oldMech : this.mechs){
            if(oldMech.shape == Shape.CIRCLE){
                newRobot.addCircleMech(
                    oldMech.radius + rng(0.0), 
                    oldMech.density + rng(0), 
                    oldMech.center.add(new Vec2(rng(0.0), rng(0.0))), 
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
            if(oldJoint.type == Type.RELEASE){
                newRobot.addMechJointRelease();
            }
        }

        for(int i = 0; i<this.neurs.size(); i++){
            newRobot.neurs.add(new Neur());
        }

        for(NeurJoint oldJoint : this.neurJoints){
            if(oldJoint.type == NeurType.SENSOR){
                newRobot.setNeurSensor(oldJoint.inputMechJ.id, oldJoint.outputNeur.id, oldJoint.weight, oldJoint.bias);
            }
            if(oldJoint.type == NeurType.INNER){
                newRobot.setNeurInner(oldJoint.inputNeur.id, oldJoint.outputNeur.id, oldJoint.weight, oldJoint.bias);
            }
            if(oldJoint.type == NeurType.OUTPUT){
                newRobot.setNeurOutput(oldJoint.inputNeur.id, oldJoint.outputMechJ.id, oldJoint.weight, oldJoint.bias);
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

package org.chis;

import java.io.Serializable;
import java.util.function.DoubleSupplier;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class MechJoint implements Serializable{
    public enum Type{
        REV, WELD, RELEASE
    }

    private static final long serialVersionUID = 1503664786564030777L;
    
    public transient RevoluteJoint revJoint;
    public transient DoubleSupplier angle;

    public WeldJoint weldJoint;

    public Mech mechA;
    public Mech mechB;

    public Vec2 anchor;

    public Type type;

    public boolean grab;

    public int id;

    public MechJoint(Mech mechA, Mech mechB, Vec2 anchor, World world){

        type = Type.REV;

        this.mechA = mechA;
        this.mechB = mechB;
        this.anchor = anchor;
        

        RevoluteJointDef revJointDef = new RevoluteJointDef();

        revJointDef.initialize(mechA.body, mechB.body, anchor);
        revJointDef.motorSpeed = 0.0f;
        revJointDef.maxMotorTorque = 1.0f;
        revJointDef.enableMotor = true;

        revJoint = ((RevoluteJoint) world.createJoint(revJointDef));
        angle = () -> revJoint.getJointAngle();
    }

    public MechJoint(Mech mechA, Mech mechB, World world){

        type = Type.WELD;

        this.mechA = mechA;
        this.mechB = mechB;

        WeldJointDef weldJointDef = new WeldJointDef();

        weldJointDef.initialize(mechA.body, mechB.body, mechB.body.getPosition());

        weldJoint = ((WeldJoint) world.createJoint(weldJointDef));
    }

    public MechJoint(){
        type = Type.RELEASE;
        grab = true;
    }

    public void setPower(float speed){
        if(type == Type.RELEASE){
            if(speed > 0){
                grab = false;
                // System.out.println("released");
            }else{
                grab = true;
                // System.out.println("try grab");
            }
            return;
        }

        speed = limit(speed, 10);
        // System.out.println(mechA.id + " and " + mechB.id + ": " + power);
        revJoint.setMotorSpeed(speed);
        // revJoint.setMaxMotorTorque(limit(5 / speed, 5));
    }

    public float limit(float val, float limit){
        if(val > limit){
            return limit;
        }else if(val < -limit){
            return -limit;
        }else{
            return val;
        }
    }

}

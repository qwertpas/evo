package org.chis;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJoint;
import org.jbox2d.dynamics.joints.WeldJointDef;

public class MechJoint {
    public enum Type{
        REV, WELD
    }
    
    public RevoluteJoint revJoint;
    public WeldJoint weldJoint;

    public Mech mechA;
    public Mech mechB;

    public Vec2 anchor;

    public Type type;

    public MechJoint(Mech mechA, Mech mechB, Vec2 anchor, World world){

        type = Type.REV;

        this.mechA = mechA;
        this.mechB = mechB;
        this.anchor = anchor;
        

        RevoluteJointDef revJointDef = new RevoluteJointDef();

        revJointDef.initialize(mechA.body, mechB.body, anchor);
        revJointDef.motorSpeed = 5.0f;
        revJointDef.maxMotorTorque = 200.0f;
        revJointDef.enableMotor = true;

        revJoint = ((RevoluteJoint) world.createJoint(revJointDef));
    }

    public MechJoint(Mech mechA, Mech mechB, World world){

        type = Type.WELD;

        this.mechA = mechA;
        this.mechB = mechB;

        WeldJointDef weldJointDef = new WeldJointDef();

        weldJointDef.initialize(mechA.body, mechB.body, mechB.body.getPosition());

        weldJoint = ((WeldJoint) world.createJoint(weldJointDef));
    }



    // public void setPower(float power){
        
    // }
}

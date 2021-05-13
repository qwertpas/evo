package org.chis.mechs;

import org.chis.Neur;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;

public class Mech {
   

    public Neur inputNeur;

    public Body body;
    public Joint joint;

    public float[] parameters;
    public Body parent;
    public World world;

   
    public void setPower(float power){
        
    }

   
}

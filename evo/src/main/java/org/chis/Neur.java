package org.chis;

import java.util.ArrayList;

public class Neur {
    public float energy = 0.0f;

    public boolean active = false;

    public ArrayList<Neur> outputs = new ArrayList<Neur>();

    

    public void giveEnergy(float input){
        energy += input;
    }

    public void update(){
        
    }




}

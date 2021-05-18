package org.chis;

import java.io.Serializable;

public class Neur implements Serializable{

    public float energy = 0.0f;

    public int id;



    public Neur(){

    }

    

    public void giveEnergy(float input){
        energy += input;
    }

    public float get(){
        return (float) (1 / (1 + Math.exp(-energy)));
    }




}

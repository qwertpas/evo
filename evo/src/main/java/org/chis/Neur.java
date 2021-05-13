package org.chis;

public class Neur {

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

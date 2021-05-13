package org.chis;

public class Neur {

    public enum Type{
        INPUT, INNER, OUTPUT
    }

    public float energy = 0.0f;

    public boolean active = false;


    public Neur(){

    }

    

    public void giveEnergy(float input){
        energy += input;
    }

    public float get(){
        return (float) (1 / (1 + Math.exp(-energy)));
    }




}

package org.chis;

public class NeurJoint {

    public enum Type{
        SENSOR, INNER, OUTPUT
    }

    public float weight;
    public float bias;

    MechJoint inputMechJ;
    Neur inputNeur;
    Neur outputNeur;
    MechJoint outputMechJ;

    Type type;


    // public NeurJoint(float inputNum, Neur outputNeur, float weight){
    //     type = Type.INPUT;

    //     this.inputNum = inputNum;
    //     this.outputNeur = outputNeur;
    //     this.weight = weight;
    // }

    public NeurJoint(MechJoint inputMechJ, Neur outputNeur, float weight, float bias){
        type = Type.SENSOR;

        this.inputMechJ = inputMechJ;
        this.outputNeur = outputNeur;
        this.weight = weight;
        this.bias = bias;
    }

    public NeurJoint(Neur inputNeur, Neur outputNeur, float weight, float bias){
        type = Type.INNER;
        
        this.inputNeur = inputNeur;
        this.outputNeur = outputNeur;
        this.weight = weight;
        this.bias = bias;
    }

    public NeurJoint(Neur inputNeur, MechJoint outputMechJ, float weight, float bias){
        type = Type.OUTPUT;
        
        this.inputNeur = inputNeur;
        this.outputMechJ = outputMechJ;
        this.weight = weight;
        this.bias = bias;
    }

    public void update(){
        switch(type){
            // case INPUT:
            //     outputNeur.energy = (float) inputMechJ.angle.getAsDouble() * weight;
            //     break;
            case INNER:
                outputNeur.energy += (inputNeur.get() * weight + bias);
                break;
            case SENSOR:
                outputNeur.energy += (float) inputMechJ.angle.getAsDouble() * weight + bias;
                break;
            case OUTPUT:
                outputMechJ.setPower(inputNeur.get() * weight + bias);
                break;
            default:
                break;
        }
    }
}

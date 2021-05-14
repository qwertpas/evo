package org.chis;

import java.awt.Toolkit;

import processing.core.PApplet;


public class Visualizer extends PApplet{

    WorldSim worldSim;

    int tick;

    public Visualizer(WorldSim worldSim){
        this.worldSim = worldSim;
    }

    public void run(){
        PApplet.runSketch(new String[]{"Visualizer"}, this);
    }

    @Override
    public void settings() {
        size(Toolkit.getDefaultToolkit().getScreenSize().width, 500);
    }

    @Override
    public void draw() {
        if(tick < 650){
            worldSim.loop();
            tick++;
        }else{
            return;
        }

        clear();

        if(worldSim.box.getPosition().x < -4){
            PDraw.scale = 50;
            PDraw.offset.x = 1200;
            
        }else{
            PDraw.scale = 150;
            PDraw.offset.x = width/2;
            PDraw.offset.y = -height + 50;
        }

        PDraw.drawWorld(worldSim.world, this);
        PDraw.drawText("Box moved: " + (-worldSim.box.getPosition().x + 2), 50, this);
        PDraw.drawText("Time left: " + Math.round((650 - tick)/0.6) / 100.0, 80, this);

        
    }
}

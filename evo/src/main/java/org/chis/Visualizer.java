package org.chis;

import java.awt.Toolkit;

import processing.core.PApplet;


public class Visualizer extends PApplet{

    WorldSim worldSim;

    PDraw pDraw = new PDraw();

    int tick;

    int generation;

    boolean running = false;

    public Visualizer(WorldSim worldSim, int g){
        this.worldSim = worldSim;
        this.generation = g;
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

        if(running){
            if(tick < 650){
                worldSim.loop();
                tick++;
            }else{
                return;
            }

            clear();

    
            if(true){
                pDraw.scale = 150;
                pDraw.offset.x = 1200 - worldSim.box.getPosition().x * pDraw.scale;
                pDraw.offset.y = -height + 50;
                
            }else{
                pDraw.scale = 150;
                pDraw.offset.x = width/2;
                pDraw.offset.y = -height + 50;
            }
    
            pDraw.drawWorld(worldSim.world, this);
            pDraw.drawText("Generation: " + generation, 50, this);
            pDraw.drawText("Box moved: " + (-worldSim.box.getPosition().x + 2), 80, this);
            pDraw.drawText("Time left: " + Math.round((650 - tick)/0.6) / 100.0, 110, this);
        }

        

        
    }

    @Override
    public void exitActual() {
    }

    @Override
    public void keyPressed() {
        if(key == 'p'){
            running = running ? false : true;
        }
    }
}

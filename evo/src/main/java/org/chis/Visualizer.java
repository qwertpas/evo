package org.chis;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

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

    
            pDraw.scale = 150;
            pDraw.offset.x = width/2 - (worldSim.box.getPosition().x - 2) * pDraw.scale;
            pDraw.offset.y = -height + 50;
                
            // pDraw.scale = 150;
            // pDraw.offset.x = width/2;
            // pDraw.offset.y = -height + 50;
            
    
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

    public static void visualize(Robot robot, int g, boolean block){
        WorldSim demoSim = new WorldSim();
        demoSim.robot = robot.copy(demoSim.world);


        Visualizer v = new Visualizer(demoSim, g);
        v.run();

        BufferedReader input = new BufferedReader (new InputStreamReader (System.in));

        if(block){
            try {
                input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

        v.noLoop();
    }

    public static void main(String[] args) {
        while(true){
            BufferedReader input = new BufferedReader (new InputStreamReader (System.in));

            String arg = "-1";
            
            System.out.println("Which robot to show?");
            try {
                arg = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file;
            int gen;
            if(Character.isDigit(arg.charAt(0))){
                gen = Integer.parseInt(arg);
                file = new File("evo/saves", gen + ".robot");
                System.out.println("Showing gen " + gen + " from temp save");
            }
            else{
                String galleryName = arg.substring(0, arg.indexOf(" "));
                gen = Integer.parseInt(arg.substring(arg.indexOf(" ") + 1));
                file = new File("evo/gallery/" + galleryName, gen + ".robot");
                System.out.println("Showing gen " + gen + " from gallery " + galleryName);

            }
            


            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Robot result = (Robot) ois.readObject();
                ois.close();

                visualize(result, gen, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

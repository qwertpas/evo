package org.chis;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import processing.core.PApplet;

public class PDraw {

    public float scale = 150;
    public Vec2 offset = new Vec2(5, 5);


    public void drawText(String text, float y, PApplet pApplet){
        pApplet.fill(74, 195, 255);
        pApplet.textSize(32);
        pApplet.text(text, 50, y); 
    }

    public void drawWorld(World world, PApplet pApplet){

        int top = pApplet.height - 50;
        for(int i = 0; i < pApplet.width/scale; i=i+2){
            float left = i*scale + offset.x % (2*scale);
            pApplet.rect(left, top, scale, 50);
        }

        Body body = world.getBodyList();
        // System.out.println("Draw World");

        while(true){

            if(body == null){
                return;
            }

            pApplet.stroke(255);

            pApplet.strokeWeight(0.001f);
            pApplet.fill(255);

            
            
            drawShape(body, pApplet);
            
            body = body.getNext();
        }
    }

    public void drawShape(Body body, PApplet pApplet) {


        Fixture fixture = body.getFixtureList();

        int red = colorSig(fixture.getDensity());

        pApplet.fill(red, 255 - red, 255/2);

        


        pApplet.pushMatrix();
        pApplet.translate(scale * body.getPosition().x, scale * -body.getPosition().y);

        pApplet.translate(offset.x, -offset.y);

        pApplet.rotate(-body.getAngle());

        pApplet.scale(scale);
        
        switch (fixture.getType()) {

            case CIRCLE:
                // System.out.println("Drawing Circle: " + body.getPosition().toString());

                CircleShape circle = (CircleShape) fixture.getShape();

                // Vec2 center = body.getPosition();
                // Transform.mulToOutUnsafe(xf, circle.m_p, center);
                float radius = circle.m_radius;
                pApplet.ellipse(0, 0, radius*2 , radius*2);

                // float sin = (float) Math.sin(body.getAngle());
                // float cos = (float) Math.cos(body.getAngle());
                pApplet.line(0, 0, radius, 0);


                break;

            case POLYGON: 
                // System.out.println("Drawing Polygon: " + body.getPosition().toString());

                PolygonShape poly = (PolygonShape) fixture.getShape();
                int vertexCount = poly.m_count;

                pApplet.beginShape();

                for (int i = 0; i < vertexCount; i++) {
                    Vec2 vToDraw = poly.m_vertices[i];
                    pApplet.vertex(vToDraw.x, vToDraw.y);
                }

                pApplet.endShape(PApplet.CLOSE);
                break;

            case EDGE: 
                // System.out.println("Drawing Edge: " + body.getPosition().toString());

                EdgeShape edge = (EdgeShape) fixture.getShape();
                Vec2 startpoint = edge.m_vertex1;
                Vec2 endpoint = edge.m_vertex2;
                
                pApplet.line(startpoint.x, startpoint.y, endpoint.x, endpoint.y);
                break;

            default:
                break;

        }
        
        pApplet.popMatrix();

    }

     int colorSig(double density){
        return (int) (255 / (1+Math.exp(-(density-5)/2.0)));
    }
}

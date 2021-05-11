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

    public static float scale = 50;
    public static Vec2 offset = new Vec2(100f, -500f);
    // public static color color;

    public static void drawWorld(World world, PApplet pApplet){

        offset.x = pApplet.width/2;
        offset.y = -pApplet.height + 50;

        Body body = world.getBodyList();
        System.out.println("Draw World");

        while(true){

            if(body == null){
                return;
            }

            pApplet.stroke(255, 0, 0);

            pApplet.fill(0, 255, 0);

            
            
            PDraw.drawShape(body, pApplet);
            
            body = body.getNext();
        }
    }

    public static void drawShape(Body body, PApplet pApplet) {
        Fixture fixture = body.getFixtureList();
        
        switch (fixture.getType()) {
            case CIRCLE:
                System.out.println("Drawing Circle: " + body.getPosition().toString());

                CircleShape circle = (CircleShape) fixture.getShape();

                Vec2 center = body.getPosition().mul(scale).add(offset);
                // Transform.mulToOutUnsafe(xf, circle.m_p, center);
                float radius = circle.m_radius * scale;
                
                pApplet.ellipse(center.x, -center.y, radius, radius);


                break;

            case POLYGON: 
                System.out.println("Drawing Polygon: " + body.getPosition().toString());

                PolygonShape poly = (PolygonShape) fixture.getShape();
                int vertexCount = poly.m_count;

                pApplet.beginShape();

                for (int i = 0; i < vertexCount; ++i) {
                    Vec2 vToDraw = poly.m_vertices[i].add(body.getPosition()).mul(scale).add(offset);
                    pApplet.vertex(vToDraw.x, -vToDraw.y);
                }

                pApplet.endShape();
                break;

            case EDGE: 
                System.out.println("Drawing Edge: " + body.getPosition().toString());

                EdgeShape edge = (EdgeShape) fixture.getShape();
                Vec2 startpoint = edge.m_vertex1.add(body.getPosition()).mul(scale).add(offset);
                Vec2 endpoint = edge.m_vertex2.add(body.getPosition()).mul(scale).add(offset);
                
                pApplet.line(startpoint.x, -startpoint.y, endpoint.x, -endpoint.y);
                break;

            default:
                break;
        }
    }
}

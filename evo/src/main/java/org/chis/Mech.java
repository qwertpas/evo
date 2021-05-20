package org.chis;

import java.io.Serializable;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;

public class Mech implements Serializable{

    private static final long serialVersionUID = 892393725108602894L;

    public enum Shape{
        RECT, CIRCLE
    }
   

    public Neur inputNeur;

    public transient Body body;
    public Joint joint;

    public int id;

    public Shape shape;

    public float radius;
    public float density;
    public Vec2 center;
    public float width;
    public float height;
    public float angle;

   
    
    public Mech(float radius, float density, Vec2 center, World world){

        shape = Shape.CIRCLE;

        this.radius = radius;
        this.density = density;
        this.center = center;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(center.x, center.y);
        
        body = world.createBody(bd);
        body.createFixture(fd);

    }

    public Mech(float width, float height, float density, Vec2 center, float angle, World world){

        shape = Shape.RECT;

        this.width = width;
        this.height = height;
        this.density = density;
        this.center = center;
        this.angle = angle;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = rect;
        fd.density = density;
        fd.friction = 0.9f;

        bd.position.set(center);
        bd.angle = angle;

        body = world.createBody(bd);
        body.createFixture(fd);

    }

   
}

package org.chis;

import javax.swing.JFrame;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.testbed.framework.TestList;
import org.jbox2d.testbed.framework.TestbedController;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedSetting;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.TestbedSetting.SettingType;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

public class Testtest extends TestbedTest {

    @Override
    public void initTest(boolean argDeserialized) {
      setTitle("Couple of Things Test");
  
      getWorld().setGravity(new Vec2());
  
      for (int i = 0; i < 2; i++) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1, 1);
        
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(5 * i, 0);
        bodyDef.angle = (float) (Math.PI / 4 * i);
        bodyDef.allowSleep = false;
        Body body = getWorld().createBody(bodyDef);
        body.createFixture(polygonShape, 5.0f);
  
        body.applyForce(new Vec2(-10000 * (i - 1), 0), new Vec2());
      }
    }
  
    @Override
    public String getTestName() {
      return "Couple of Things";
    }

    public static void main(String[] args) {
        TestbedModel model = new TestbedModel();         // create our model

        // add tests
        TestList.populateModel(model);                   // populate the provided testbed tests
        model.addCategory("My Super Tests");             // add a category
        model.addTest(new Testtest());                // add our test

        // add our custom setting "My Range Setting", with a default value of 10, between 0 and 20
        model.getSettings().addSetting(new TestbedSetting("My Range Setting", SettingType.ENGINE, 10, 0, 20));

        TestbedPanel panel = new TestPanelJ2D(model);    // create our testbed panel

        JFrame testbed = new TestbedFrame(model, panel, TestbedController.UpdateBehavior.UPDATE_CALLED); // put both into our testbed frame
        // etc
        testbed.setVisible(true);
        testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
  }
package org.chis;

import javax.swing.JFrame;

import org.jbox2d.testbed.framework.TestList;
import org.jbox2d.testbed.framework.TestbedController.UpdateBehavior;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

public class RunTestbed {
    public static void main(String[] args) {
        TestbedModel model = new TestbedModel();         // create our model

        TestList.populateModel(model);                   // populate the provided testbed tests
        
        TestbedPanel panel = new TestPanelJ2D(model);    // create our testbed panel
        
        JFrame testbed = new TestbedFrame(model, panel, UpdateBehavior.UPDATE_CALLED); // put both into our testbed frame

        testbed.setVisible(true);
        testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    }
}

package com.myntai.slightech.rostest;

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.rosjava_geometry.Transform;

import geometry_msgs.PoseWithCovarianceStamped;

public class OnePubNode implements NodeMain {

    private String TAG = "OnePubNode";

    private OnePubNode(){}
    private static class InstanceHolder{
        private static OnePubNode instance = new OnePubNode();
    }
    public static OnePubNode getInstance(){
        return InstanceHolder.instance;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NodeName.OnePubNode);
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Publisher<PoseWithCovarianceStamped> pub =  connectedNode.newPublisher(TopicName.OneTopic, PoseWithCovarianceStamped._TYPE);
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            @Override
            protected void loop() throws InterruptedException {
                PoseWithCovarianceStamped msg = pub.newMessage();
                msg.getHeader().setFrameId("/map");
//                msg.getPose().setPose(new Transform().toPoseMessage(msg.getPose().getPose()));
                /*------------------------start--------------------------*/
                double[] covariance = msg.getPose().getCovariance();
                covariance[6 * 0 + 0] = 0.5 * 0.5;
                covariance[6 * 1 + 1] = 0.5 * 0.5;
                covariance[6 * 5 + 5] = (float) (Math.PI / 12.0 * Math.PI / 12.0);
                /*-----------------------end----------------------------*/
                pub.publish(msg);
                Log.i(TAG, "loop: one pub msg success!!!");
                Thread.sleep(1000);
            }
        });
    }

    @Override
    public void onShutdown(Node node) {

    }

    @Override
    public void onShutdownComplete(Node node) {

    }

    @Override
    public void onError(Node node, Throwable throwable) {

    }
}

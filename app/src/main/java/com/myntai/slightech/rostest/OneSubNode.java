package com.myntai.slightech.rostest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import geometry_msgs.PoseWithCovarianceStamped;

public class OneSubNode implements NodeMain {

    static final String TAG = "OneSubNode";
    static int i = 0;

    private OneSubNode() {
    }

    private static class InstanceHolder {
        private static OneSubNode instance = new OneSubNode();
    }

    public static OneSubNode getInstance() {
        return OneSubNode.InstanceHolder.instance;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of(NodeName.OneSubNode);//todo 要给这个节点命名，　不然运行时，会报错的
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<PoseWithCovarianceStamped> subscriber;
        subscriber = connectedNode.newSubscriber(TopicName.OneTopic, PoseWithCovarianceStamped._TYPE);
        subscriber.addMessageListener(new MessageListener<PoseWithCovarianceStamped>() {
            @Override
            public void onNewMessage(PoseWithCovarianceStamped poseWithCovarianceStamped) {
                ++i;
                Log.i(TAG, "onNewMessage: sub -> " + i);
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

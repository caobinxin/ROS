package com.myntai.slightech.rostest;
import android.os.Bundle;
import org.ros.android.RosActivity;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.topic.Publisher;

import java.net.URI;

import std_msgs.String;

public class MainActivity extends RosActivity {

    protected MainActivity() {
        super("ros_test", "ros_test", URI.create("http://192.168.1.195:11311")); //ROS_MASTER_URL地址
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());//设置master的地址

        //启动一个测试节点
        nodeMainExecutor.execute(new NodeMain() {
            @Override
            public GraphName getDefaultNodeName() {
                return GraphName.of("ros_test");
            }

            @Override
            public void onStart(ConnectedNode connectedNode) {
                final Publisher<std_msgs.String> pub =  connectedNode.newPublisher("/test", String._TYPE);
                connectedNode.executeCancellableLoop(new CancellableLoop() {
                    @Override
                    protected void loop() throws InterruptedException {
                        std_msgs.String msg = pub.newMessage();
                        msg.setData("hello world");
                        pub.publish(msg);
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
        }, nodeConfiguration);

        //启动另外一个节点
        nodeMainExecutor.execute(OnePubNode.getInstance(), nodeConfiguration);

        if(true){
            //两个　configuration 都对
            nodeMainExecutor.execute(OneSubNode.getInstance(), nodeConfiguration);
        }else {
            URI master = URI.create("http://192.168.1.195:11311");
            final java.lang.String host = NetUtil.getWifiIpAddress();
            NodeConfiguration configuration = NodeConfiguration.newPublic(host, master);
            nodeMainExecutor.execute(OneSubNode.getInstance(), configuration);
        }
    }
}

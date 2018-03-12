/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proprobo;
import robocode.*;

/**
 *
 * @author e7844438
 */
public class robot_v1 extends Robot{
    public void run(){
        turnRight(getHeading());
        while(true){
            ahead(500);
            turnLeft(90);
        }
    }
    public void onScannedRobot(ScannedRobotEvent e){
        fire(5);
    }
    public void onHitByBullet(HitByBulletEvent e){
        //turnLeft(180);
    }
}

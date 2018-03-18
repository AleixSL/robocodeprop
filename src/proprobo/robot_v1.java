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
    private byte moveDirection = 1;
    public void run() {
        
        while(true){
            turnGunRight(360);
        }
    }
 
    public void onScannedRobot(ScannedRobotEvent e) {
        stop();
        setAdjustGunForRobotTurn(true); // Desacoplamos el cuerpo y el resto del robot
        // calculate firepower based on distance
       double firePower = Math.min(500 / e.getDistance(), 3);
       // calculate speed of bullet
       double bulletSpeed = 20 - firePower * 3;
       // distance = rate * time, solved for time
       long time = (long)(e.getDistance() / bulletSpeed);
       // calculate gun turn to predicted x,y location
        // turn the gun to the predi
        if(e.getDistance() < 150 || e.getVelocity() == 0) // Si el enemigo está cerca
            fire(3); // o no se mueve, disparamos con la potencia máxima.
        else
            fire(1); // En caso contrario disparamos con potencia 1.
        doMove(e);
    }
 
    public void onHitByBullet(HitByBulletEvent e) {
        // Si alguien nos acierta un disparo retrocedemos 10 píxeles.
        back(50);
    }
 
    public void onHitWall(HitWallEvent e) {
        // Si chocamos con un muro retrocedemos 20 píxeles.
        moveDirection *= -1;
        ahead(200);
    }
    public void doMove(ScannedRobotEvent e) {

	// switch directions if we've stopped
	if (getVelocity() == 0)
		moveDirection *= -1;

	// circle our enemy
	turnRight(a360(e.getBearing() + 90 - (30 * moveDirection)));
	ahead(100 * moveDirection);
}
    double a360(double angulo) {
	if (angulo >  180) angulo -= 360;
	if (angulo < -180) angulo += 360;
	return angulo;
}
   
}

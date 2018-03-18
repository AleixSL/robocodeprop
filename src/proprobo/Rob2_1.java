package proprobo;
import robocode.*;
import java.awt.*;
import robocode.util.*;
import java.awt.geom.*;
import robocode.util.Utils;
import java.awt.Color;
import java.lang.*;         
import java.util.*; 
import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;

//Autor: Victor Gomez Gamero
//DNI: 47962446V
//Assignatura: PROP

public class Rob2_1 extends AdvancedRobot
{
    public boolean scaner; 
    public long tiempo;
    public static double energiaguardada = 1;
    int direccionmovimiento= 1;
    int margen = 60;
    static double lastDistance;   
    int contDisp = 0;
	public void run() {
                centro();
                setAdjustRadarForRobotTurn(true);
              	setColors(Color.black,Color.black,Color.black);
                moverarma();
                while(true) {
                    radar();              
                    }
	}
        public double energia(ScannedRobotEvent e){
                double energy = e.getEnergy();
                return energy;
                }
        public void radar(){
            //radar de giro infinito
            tiempo = getTime();
            turnRadarRightRadians(Double.POSITIVE_INFINITY);
            scaner = true;
        }
        public void movimientocuerpo(ScannedRobotEvent e){
            //colocacion del cuerpo siempre apuntando a 90 grados del enemigo
            setTurnRight(e.getBearing() + 90);
        }
        
        
        public void centro() {
            //Movimiento para intentar alejarse de las esquinas e ir al centro
            double central = Math.atan2(getBattleFieldWidth()/2-getX(), getBattleFieldHeight()/2-getY());
            setTurnRightRadians(Utils.normalRelativeAngle(central- getHeadingRadians()));
            setAhead(300);
        }
        public void moverarma(){
            //ponemos el arma estatica apuntando al otro robot ya que nosotros ya colocamos el cuerpo para siempre estar a 90 grados del otro robot
            //por lo que no es preciso moverla mucho
            setTurnGunRight(270);
        }
	public void onScannedRobot(ScannedRobotEvent e) {
                    double distance = e.getDistance();
                    movimientocuerpo(e);
                    ataque(e);
                    if(energiaguardada != e.getEnergy()){
                         //si el enemigo pierde energia se mueve para esquivar ya que detecta que pierde energia                   
                        double energia2;
                        energia2 = energiaguardada - e.getEnergy();
                        if(energia2 <= 3.1){
                            setAhead(75*direccionmovimiento);
                            System.out.printf("ahead");
                            System.out.println();
                            energiaguardada = e.getEnergy();
                        }
                        else{
                            energiaguardada = e.getEnergy();
                        }
                    }
                    else{
                        ataque(e);
                    }
                    if(distance<200){
                        //Esto esta mas pensado para los robots que van mas a cuerpo a cuerpo y puede que no disparen al principio, si se ponen
                        //a bocajarro podrian destruir a mi robot por lo que intenta tirar atras cuando se le acercan demasiado para evitarlo
                        setBack(75*direccionmovimiento);
                    }
                    //direccionmovimiento = (direccionmovimiento*(-1));
                    /*double tamcampo = getBattleFieldHeight();
                    double tcamp = getBattleFieldWidth();
                    double miposicionX = getX();
                    double miposicionY = getY();                  
                    if(miposicionY+150>= tamcampo){
                        //moveralcentro();
                        setBack(200*direccionmovimiento);
                        }
                    if(miposicionY-150<= 0){
                        //moveralcentro();
                        setBack(200*direccionmovimiento);
  
                        }
                    if(miposicionX+150>= tcamp){
                       // moveralcentro();
                        setBack(200*direccionmovimiento);
                        }
                    if(miposicionX-150<= 0){;
                        //moveralcentro();1;
                        setBack(200*direccionmovimiento);
                        }  */
	}
	public void onHitByBullet(HitByBulletEvent e) {
            //si le toca una bala cambia de direccion para intentar esquivar las siguientes
           setAhead(150*(direccionmovimiento*-1));
	}
        public void ataque(ScannedRobotEvent e){
            //ajustamos arma, disparamos y ajustamos radar
            setTurnGunRightRadians(Utils.normalRelativeAngle(apuntararma2(e.getBearingRadians()))) ;
            disparo3(e.getDistance(), e.getVelocity(), e.getEnergy());
            //disparo2(e);
            setTurnRadarRightRadians(Utils.normalRelativeAngle(Posicionenemigo(e.getBearingRadians())) * 2); 
	}
	
        public double Posicionenemigo(double bearingRadians){
            //apuntar el radar hacia el enemigo, devolvemos los grados que tendria que girar nuestro radar
		double enemigoHeading = bearingRadians + getHeadingRadians();	 
		return enemigoHeading - getRadarHeadingRadians();	
	}
        public double apuntararma2(double bearingRadians){
            //movemos unos grados el arma para intentar posicionarla bien
		double enemigoHeading = bearingRadians + getHeadingRadians();	 
		return enemigoHeading - getGunHeadingRadians();	
	}
	public void disparo3(double distancia, double velEnemigo, double energiaEnemigo){
	//funcion disparo donde intentamos premiar si el enemigo esta quieto o si tiene poca energia para disparar con mas potencia o menos	
		if(velEnemigo == 0){	
			setFire(3);
		}else if(energiaEnemigo < 12){	
			setFire((energiaEnemigo / 4) + .1);
		}else if (distancia > 450 || getEnergy() < 15) {	
			setFire(1);	
		} else if (distancia > 150) {	
			setFire(2);
		} else {		
			setFire(3);
	    }
	}        
	public void onHitWall(HitWallEvent e) {
		if(e.getBearing() < 0) direccionmovimiento *= -1;
		setBack(200*direccionmovimiento);
	}	
        public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {
			turnRight(30);
			turnLeft(30);
		}
	}
}

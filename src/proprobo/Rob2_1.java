package proprobo;
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import robocode.ScannedRobotEvent;

public class Rob2_1 extends AdvancedRobot
{
    public static double energiaguardada = 1;
    int direccionmovimiento= 1;
    int tiro=1;
    int fallados=0;
    @Override
    public void run() {
        
        centro();
        setAdjustRadarForRobotTurn(true);
        setColors(Color.black,Color.black,Color.black);

        while(true) {
            turnRadarRightRadians(360);

            }
    }

    public void centro() {
        //Movimiento para intentar alejarse de las esquinas e ir al centro
        double central = Math.atan2(getBattleFieldWidth()/2-getX(), getBattleFieldHeight()/2-getY());
        setTurnRightRadians(Utils.normalRelativeAngle(central- getHeadingRadians()));
        setAhead(300);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double distance = e.getDistance();
        setTurnRight(e.getBearing() + 90);
        ataque(e);
        if(energiaguardada != e.getEnergy()){
             //si el enemigo pierde energia se mueve para esquivar ya que detecta que pierde energia                  
            direccionmovimiento *= -1;
            setAhead(75*direccionmovimiento);
            energiaguardada = e.getEnergy();
           }
        else{
            ataque(e);
        }
        if(distance<100){
            //Esto esta mas pensado para los robots que van mas a cuerpo a cuerpo y puede que no disparen al principio, si se ponen
            //a bocajarro podrian destruir a mi robot por lo que intenta tirar atras cuando se le acercan demasiado para evitarlo
            centro();
        }

    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        //si le tocamueve el radar para detectar el robot que le disparo y cambia de direccion para intentar esquivar las siguientes
       stop();
       setTurnRadarRightRadians(Utils.normalRelativeAngle(Posicionenemigo(e.getBearingRadians())) * 2);
            
        
        centro();

    }
    public void onMissBullet(BulletMissedEvent e){
        // cada vez que falla un disparo cambia la direccion donde va a disparar, y si ha fallado 10 seguidos se reposiciona
        tiro = tiro * -1;
        fallados +=1;
        if(fallados<10){
            stop();
            centro();
            fallados=0;
        }
    }

    public void ataque(ScannedRobotEvent e){
    //hacemos una diferencia para los casos en los que el robot no se mueve, disparamos a la posicion que nos da el radar
    //en caso contrario ajustamos arma un poco mas adelante o mas atras, disparamos y ajustamos radar
        
        if(e.getVelocity() ==0)setTurnGunRightRadians(Utils.normalRelativeAngle(apuntararma(e.getBearingRadians()))) ;
        else setTurnGunRightRadians(Utils.normalRelativeAngle(apuntararma(e.getBearingRadians()+0.2*tiro))) ;
        disparo(e.getDistance(), e.getVelocity(), e.getEnergy());
        setTurnRadarRightRadians(Utils.normalRelativeAngle(Posicionenemigo(e.getBearingRadians())) * 2); 
    }

    public double Posicionenemigo(double bearingRadians){
        //apuntar el radar hacia el enemigo, devolvemos los grados que tendria que girar nuestro radar
        double enemigoHeading = bearingRadians + getHeadingRadians();	 
        return enemigoHeading - getRadarHeadingRadians();	
    }
    
    public double apuntararma(double bearingRadians){
        //movemos unos grados el arma para intentar posicionarla bien
        double enemigoHeading = bearingRadians + getHeadingRadians();	 
        return enemigoHeading - getGunHeadingRadians();	
    }
    
    public void disparo(double distancia, double velEnemigo, double energiaEnemigo){
    //intentamos malgastar el minimo de energia posible, dispararemos en los casos que tengamos cierta probabilidad de acertar
    //si no nos queda poca energia nos recolocaremos y en caso de estar cerca del objetivo dispararemos.
        if(velEnemigo == 0 & getEnergy()>40){		
            setFire(2);
        }
        else if (distancia >150) {	
            if(getEnergy() < 20){centro();}
            else setFire(1);	
        } else if (distancia < 150) {	
            if(getEnergy() < 20)setFire(1);
            else setFire(2);
        }
    }
    
    @Override
    public void onHitWall(HitWallEvent e) {
        //en caso de que golpeemos un muro nos recolocaremos en el centro
        centro();
    }	
}

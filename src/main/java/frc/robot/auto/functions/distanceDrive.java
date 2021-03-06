package frc.robot.auto.functions;
 
import frc.robot.auto.setup.RobotFunction;
import frc.robot.subsystems.Drivetrain;

// Drives straight for specified distance
public class distanceDrive extends RobotFunction {
 
    private double startTicks, speed, targetTicks;
    private double ticksPerInch = 400.0;
 
    //Takes speed from -1 to 1 and a distance in inches
    public distanceDrive(double speed, double distance) {
        this.speed = speed;
        targetTicks = distance * ticksPerInch;
    }
    @Override
    public void init() {
        startTicks = Drivetrain.getTicks();
    }
    @Override
    public void run() {
        Drivetrain.drive(speed);
    }
    @Override
    public boolean isFinished() {
        return Drivetrain.getLeftTicks() - startTicks > targetTicks;
    }
    @Override
    public void stop(){
        Drivetrain.drive(0.0);
    }
}

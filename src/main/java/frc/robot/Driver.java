package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Driver {
    Controller joy;
    Drivetrain drive;
    Climber climb;

    private SendableChooser<Byte> driveType;
    private final Byte arcade = 0;
    private final Byte tank = 1;
    
    SendableChooser<Byte> neutralModeType;//Sets whether motors brake or coast
    private final Byte brake = 0;
    private final Byte coast = 1;

    public Driver(int port) {
        joy = new Controller(port);
        drive = new Drivetrain();
        climb = new Climber();
        
        //Init driveType
        driveType = new SendableChooser<>();
        driveType.setDefaultOption("Arcade", arcade);
        driveType.addOption("Tank", tank);
        SmartDashboard.putData("Drive Type", driveType);
        
        //Init neutralModelType
        neutralModeType = new SendableChooser<>();
        neutralModeType.setDefaultOption("Brake", brake);
        neutralModeType.addOption("Coast", coast);
        SmartDashboard.putData("Drivetrain Coast Type", neutralModeType);
    }
 
    public void runDriveControls() {
        driveType();
        neutralModeType();
        faultCheck();
        
        //Climber stuff
        if (OP.getDPadUp()){
            climb.actuation();
        }
        else if(OP.getDPadDown()) {
            climb.actuationStop();
        }
        else if(getActuated() && getUpLimit()) {
            climb.hold();
        }

        if(OP.getDPadLeft()){
            climb.extention();
        }
        else {
            climb.extentionStop();
        }

        if(OP.getDPadRight()){
            climb.winchination();
        }
        else {
            climb.winchinationStop();
        }
        
        SmartDashboard.putNumber("Left Ticks", drive.getLeftTicks());
        SmartDashboard.putNumber("Right Ticks", drive.getRightTicks());
    }
    
    //Selects driveType and drives
    private void driveType() {
        if(driveType.getSelected().equals(arcade)) {
            drive.arcadeDrive(joy.getRightYAxis(), joy.getLeftTrigger(), joy.getRightTrigger());
            System.out.println("Y Axis: " + joy.getRightYAxis() + "     Left: " + joy.getLeftTrigger() + "     Right: " + joy.getRightYAxis());
        } 
        else if(driveType.getSelected().equals(tank)) {
            drive.tankDrive(joy.getLeftYAxis(), joy.getRightYAxis());
        } 
        else {
            System.out.println("Error: No drive type chosen");
        }
    }
    
    //Selects coast/brake
    private void neutralModeType() {
        if(neutralModeType.getSelected().equals(brake)) {
            drive.setNeutralMode(NeutralMode.Brake);
        }
        else if(neutralModeType.getSelected().equals(coast)) {
            drive.setNeutralMode(NeutralMode.Coast);
        }
        else {
            System.out.println("Error: No coast type chosen");
        }
    }
    
    //Checks for driveTrain motor faults
    private void faultCheck() {
        if(drive.isFault()) {
            DriverStation.reportError(drive.getFaults(),false);
            System.out.print(drive.getFaults());
        }
    }

}

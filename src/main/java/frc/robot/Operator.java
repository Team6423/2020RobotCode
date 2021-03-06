package frc.robot;

import frc.robot.subsystems.*;

public class Operator {

    private Controller OP;
    private Wheels wheels;
    private ControlPanel controlPanel;

    private boolean startedPositionControl;
    private boolean finishedPositionControl;
    
    private long lastButtonPress;//Last time rotation control button was pressed
    private final int BUTTON_DELAY = 500;//Delay time for rotation control button in ms
    
    private long lastPositionPress;//Last time position control button was pressed
    private final int POSITION_TIMEOUT = 1000;//Position control timeout in ms
    
    public Operator(int port) {
        OP = new Controller(port);
        wheels = new Wheels();
        controlPanel = new ControlPanel();
        
        resetControlPanel();
        lastButtonPress = (Timer.getFPGATimestamp()/1000);
    }

    public void opControls() {
        controlPanelControl();        
        
        if (OP.getXButton()) {
            wheels.spinBigWheelFor();
            System.out.println("attempting to spin big wheel forward");
        } else if (OP.getTriangleButton()) {
            wheels.spinBigWheelBac();
            System.out.println("attempting to spin big wheel backwards");
        } else {
            wheels.stopBigWheel();
            System.out.println("attempting to stop wheel");
        }

        if (OP.getOButton()) {
            wheels.spinIntake();
        } else {
            wheels.stopIntake();
        }

        if (OP.getSquareButton()) {
            wheels.spinShooter();
        } else {
            wheels.stopShooter();
        }
        
    }
    
    private void controlPanelControl(){
        //Position control
        if(OP.getRightBumper()) {
            startedPositionControl = true;
            lastPositionPress = (Timer.getFPGATimestamp()/1000);
            flipUpMotor();
        }
        
        if(startedPositionControl && !finishedPositionControl){
            finishedPositionControl = positionControl();
        }
        
        if(finishedPositionControl) {
            flipDownMotor();
            resetControlPanel();
        }
        
        //Pos control timeout
        if((lastPositionPress + POSITION_TIMEOUT) < (Timer.getFPGATimestamp()/1000)) {
            finishedPositionControl = true;
        }
        
        //Rotation control
        if(OP.getLeftBumper() && ((lastButtonPress + BUTTON_DELAY) < (Timer.getFPGATimestamp()/1000))) {
            addControlPanelRotation(1);//Each button press queues another rotation for the motor to spin through
            lastButtonPress = (Timer.getFPGATimestamp()/1000);
        }
        rotationControl();
    }
            
    private void resetControlPanel() {
        startedPositionControl = false;
        finishedPositionControl = false;
    }
}

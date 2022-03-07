package com.fireteam322.frc.robot.subsystems;

import com.fireteam322.frc.robot.Constants;
import com.fireteam322.frc.robot.interfaces.MotorInterface;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HookClimbingSubSystem extends SubsystemBase implements MotorInterface {
    private MotorController leftRearHook = new HookArm(Constants.HOOK_LEFT_REAR_MOTOR);
    private MotorController rightRearHook = new HookArm(Constants.HOOK_RIGHT_REAR_MOTOR);
    private MotorController frontHook = new HookArm(Constants.HOOK_FRONT_MOTOR);
    private final MotorController rearHookMotorGroup = new MotorControllerGroup(leftRearHook, rightRearHook);

    public HookClimbingSubSystem() {
        super();
        this.setName("HookClimb_SubSystem");
        // TODO We have to figure out which motors need to be inverted cause they are on
        // oppsite sides.

    }

    @Override
    public void periodic() {

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }

    public void runBackHooks(double speed) {
        rearHookMotorGroup.set(speed);
    }

    public void runFrontHooks(double speed) {
        frontHook.set(speed);
    }

    @Override
    public void run(double speed) {

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }
}

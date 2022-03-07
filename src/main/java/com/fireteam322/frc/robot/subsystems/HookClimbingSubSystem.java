package com.fireteam322.frc.robot.subsystems;

import com.fireteam322.frc.robot.Constants;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HookClimbingSubSystem extends SubsystemBase {
    private MotorController leftRearHook = new HookArm(Constants.HOOK_LEFT_REAR_MOTOR);
    private MotorController rightRearHook = new HookArm(Constants.HOOK_RIGHT_REAR_MOTOR);
    private MotorController frontHook = new HookArm(Constants.HOOK_FRONT_MOTOR);
    private final MotorController rearHookMotorGroup = new MotorControllerGroup(leftRearHook, rightRearHook);

    public HookClimbingSubSystem() {
        super();
        this.setName("HookClimb_SubSystem");
    }

    @Override
    public void periodic() {

    }
}

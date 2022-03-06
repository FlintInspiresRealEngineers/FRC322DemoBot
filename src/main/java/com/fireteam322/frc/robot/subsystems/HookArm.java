package com.fireteam322.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class HookArm extends WPI_TalonFX {

    public HookArm(int deviceNumber) {
        super(deviceNumber);
    }

    public HookArm(int deviceNumber, String canbus) {
        super(deviceNumber, canbus);
    }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.fireteam322.frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
	// AddressableLEDs.java
	public static final int ADDRESSABLE_LED_PORT = 0,
			ADDRESSABLE_LED_LENGTH = 60;

	// AutomaticLED.java
	public static final double AUTONOMOUS_BLINK_RATE = 0.25,
			TELOP_BLINK_RATE = 0.5,
			DISABLED_BLINK_RATE = 0.0,
			DEFAULT_BLINK_RATE = 2.5;

	// Chassis.java
	public static final int DRIVE_LEFTFRONT = 1,
			DRIVE_LEFTREAR = 2,
			DRIVE_RIGHTFRONT = 3,
			DRIVE_RIGHTREAR = 4,
			TICKS_PER_INCH = 512;

	// Feeder.java
	public static final int FEEDER_MOTOR = 5;

	// Intake.jave
	public static final int INTAKE_MOTOR = 8;

	// Shooter.java
	public static final int LEFT_SHOOTER_MOTOR = 6;
	public static final int RIGHT_SHOOTER_MOTOR = 7;

	// RobotContainer.java
	public static final int DRIVE_STICK = 0,
			MANIPULATOR_STICK = 1,
			DEBUGGER_STICK = 2,
			CAMERASERVER_FEEDS = 2;
	public static final double FEEDER_SPEED = 0.75,
			FEEDER_REVERSE_SPEED = -(0.5),
			INTAKE_SPEED = 1.0,
			INTAKE_REVERSE_SPEED = -(0.75),
			SHOOTER_SPEED = 1.0,
			SHOOTER_REVERSE_SPEED = -(0.5),
			FRONT_HOOKS_REVERSE_SPEED = 0.25,
			FRONT_HOOKS_SPEED = 0.25,
			REAR_HOOKS_REVERSE_SPEED = 0.25,
			REAR_HOOKS_SPEED = 0.25;

	// Robot Power
	public static final int PDP_CHANNEL = 0;

	// Autonomous
	public static final double DEFAULT_AUTONOMOUS_DISTANCE = 24.0,
			DEFAULT_AUTONOMOUS_SPEED = 0.50,
			DEFAULT_AUTONOMOUS_HEADING = 0.0,
			AUTONOMOUS_DISTANCE_ERROR_FACTOR = 6.0,
			DEFAULT_AUTONOMOUS_TIME = 5.0,
			SHOOTER_AUTONOMOUS_DRIVETIME = 3.0;

	public static final int HOOK_LEFT_REAR_MOTOR = 15,
			HOOK_RIGHT_REAR_MOTOR = 16,
			HOOK_FRONT_MOTOR = 17;

}

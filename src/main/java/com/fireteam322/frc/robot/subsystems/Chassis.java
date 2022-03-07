/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.fireteam322.frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.fireteam322.frc.robot.Constants;

public class Chassis extends SubsystemBase {
	/**
	 * The Chassis subsystem incorporates the sensors and actuators attached to the
	 * robots chassis.
	 * It includes the four drive motors (two on each side), quadrature encoders
	 * connected directly to each
	 * front motor Talon SRX, and a navX-MXP IMU.
	 */

	private final WPI_TalonSRX m_leftFrontMotor = new WPI_TalonSRX(Constants.DRIVE_LEFTFRONT);
	private final WPI_TalonSRX m_leftRearMotor = new WPI_TalonSRX(Constants.DRIVE_LEFTREAR);
	private final WPI_TalonSRX m_rightFrontMotor = new WPI_TalonSRX(Constants.DRIVE_RIGHTFRONT);
	private final WPI_TalonSRX m_rightRearMotor = new WPI_TalonSRX(Constants.DRIVE_RIGHTREAR);

	private final MotorController m_leftMotors = new MotorControllerGroup(m_leftFrontMotor, m_leftRearMotor);
	private final MotorController m_rightMotors = new MotorControllerGroup(m_rightFrontMotor, m_rightRearMotor);

	private final DifferentialDrive m_drive;

	private final AHRS m_imu = new AHRS();

	/**
	 * Creates a new Chassis.
	 */
	public Chassis() {
		super();
		// Invert all four motors due to the way they're mounted.
		m_leftMotors.setInverted(false);
		m_rightMotors.setInverted(false);
		// I noticed the api says invert the motors or setting the inversion befor
		// passing.
		m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

		// Change the motors to "coast" mode
		coast(true);

		// Setup the encoders
		m_leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		m_rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		System.out.println("Encoders Setup");

		// Choose whether to invert the encoders on the Talon SRX's
		m_leftFrontMotor.setSensorPhase(false);
		m_rightFrontMotor.setSensorPhase(false);
		System.out.println("Encoder Phasing Complete");
	}

	/**
	 * Modern racing game style driving for the Chassis.
	 *
	 * @param speed    Speed in range [-1.0,1.0]
	 * @param rotation Rotation in range [-1.0,1.0]
	 */
	public void drive(double speed, double rotation) {
		m_drive.arcadeDrive(speed, rotation);
	}

	/**
	 * MotionMagic Autonomous driving for the Chassis.
	 *
	 * @param heading  Heading in degrees
	 * @param distance Distance in inches
	 */
	public void autoDriveStraight(double heading, double distance) {
		double ticks = distance * Constants.TICKS_PER_INCH;
		m_leftFrontMotor.set(ControlMode.MotionMagic, ticks, DemandType.AuxPID, heading);
		m_leftRearMotor.follow(m_leftFrontMotor, FollowerType.AuxOutput1);
		m_rightFrontMotor.follow(m_leftFrontMotor, FollowerType.AuxOutput1);
		m_rightRearMotor.follow(m_leftFrontMotor, FollowerType.AuxOutput1);
	}

	// This method sets the robot to brake when the throttle is idle.
	public void brake(boolean brake) {
		if (brake) {
			m_leftFrontMotor.setNeutralMode(NeutralMode.Brake);
			m_leftRearMotor.setNeutralMode(NeutralMode.Brake);
			m_rightFrontMotor.setNeutralMode(NeutralMode.Brake);
			m_rightRearMotor.setNeutralMode(NeutralMode.Brake);
		} else {
			m_leftFrontMotor.setNeutralMode(NeutralMode.Coast);
			m_leftRearMotor.setNeutralMode(NeutralMode.Coast);
			m_rightFrontMotor.setNeutralMode(NeutralMode.Coast);
			m_rightRearMotor.setNeutralMode(NeutralMode.Coast);
		}
	}

	// This method sets the robot to coast when the throttle is idle.
	public void coast(boolean coast) {
		if (coast) {
			m_leftFrontMotor.setNeutralMode(NeutralMode.Coast);
			m_leftRearMotor.setNeutralMode(NeutralMode.Coast);
			m_rightFrontMotor.setNeutralMode(NeutralMode.Coast);
			m_rightRearMotor.setNeutralMode(NeutralMode.Coast);

		} else {
			m_leftFrontMotor.setNeutralMode(NeutralMode.Brake);
			m_leftRearMotor.setNeutralMode(NeutralMode.Brake);
			m_rightFrontMotor.setNeutralMode(NeutralMode.Brake);
			m_rightRearMotor.setNeutralMode(NeutralMode.Brake);
		}
	}

	// This stops the robot
	public void stop() {
		m_drive.arcadeDrive(0.0, 0.0);
		brake(true);
	}

	// The following are feed-through functions providing public access to Encoder
	// ticks from the Talon SRX's

	// Raw encoder output from the left encoder
	public double leftDistance() {
		return m_leftFrontMotor.getSelectedSensorPosition();
	}

	// Raw encoder output from the right encoder
	public double rightDistance() {
		return m_rightFrontMotor.getSelectedSensorPosition();
	}

	// Encoder output from the left encoder in inches
	public double leftDistanceIn() {
		return leftDistance() / Constants.TICKS_PER_INCH;
	}

	// Encoder output from the right encoder in inches
	public double rightDistanceIn() {
		return rightDistance() / Constants.TICKS_PER_INCH;
	}

	// This method checks for magnetic heading reliability.
	public boolean isHeadingReliable() {
		if (m_imu.isMagnetometerCalibrated() && !(m_imu.isMagneticDisturbance()))
			return true;
		else
			return false;
	}

	/**
	 * Returns true if the sensor is currently performing automatic
	 * gyro/accelerometer calibration. Automatic calibration occurs
	 * when the sensor is initially powered on, during which time the
	 * sensor should be held still, with the Z-axis pointing up
	 * (perpendicular to the earth).
	 * <p>
	 * NOTE: During this automatic calibration, the yaw, pitch and roll
	 * values returned may not be accurate.
	 * <p>
	 * Once calibration is complete, the sensor will automatically remove
	 * an internal yaw offset value from all reported values.
	 * <p>
	 * 
	 * @return Returns true if the sensor is currently automatically
	 *         calibrating the gyro and accelerometer sensors.
	 */
	public boolean isCalibrating() {
		return m_imu.isCalibrating();
	}

	// The following are feed-through functions providing public access to the IMU
	// data.

	/**
	 * Returns the current tilt-compensated compass heading
	 * value (in degrees, from 0 to 360) reported by the sensor.
	 * <p>
	 * Note that this value is sensed by a magnetometer,
	 * which can be affected by nearby magnetic fields (e.g., the
	 * magnetic fields generated by nearby motors).
	 * <p>
	 * Before using this value, ensure that (a) the magnetometer
	 * has been calibrated and (b) that a magnetic disturbance is
	 * not taking place at the instant when the compass heading
	 * was generated.
	 * 
	 * @return The current tilt-compensated compass heading, in degrees (0-360).
	 */
	public float getCompassHeading() {
		if (isHeadingReliable())
			return m_imu.getCompassHeading();
		else
			return 0.0f;
	}

	/**
	 * Returns the "fused" (9-axis) heading.
	 * <p>
	 * The 9-axis heading is the fusion of the yaw angle, the tilt-corrected
	 * compass heading, and magnetic disturbance detection. Note that the
	 * magnetometer calibration procedure is required in order to
	 * achieve valid 9-axis headings.
	 * <p>
	 * The 9-axis Heading represents the sensor's best estimate of current heading,
	 * based upon the last known valid Compass Angle, and updated by the change in
	 * the
	 * Yaw Angle since the last known valid Compass Angle. The last known valid
	 * Compass
	 * Angle is updated whenever a Calibrated Compass Angle is read and the sensor
	 * has recently rotated less than the Compass Noise Bandwidth (~2 degrees).
	 * 
	 * @return Fused Heading in Degrees (range 0-360)
	 */
	public float getHeading() {
		if (isHeadingReliable() && !isCalibrating())
			return m_imu.getFusedHeading();
		else
			return 0.0f;
	}

	/**
	 * Returns the total accumulated yaw angle (Z Axis, in degrees)
	 * reported by the sensor.
	 * <p>
	 * NOTE: The angle is continuous, meaning it's range is beyond 360 degrees.
	 * This ensures that algorithms that wouldn't want to see a discontinuity
	 * in the gyro output as it sweeps past 0 on the second time around.
	 * <p>
	 * Note that the returned yaw value will be offset by a user-specified
	 * offset value; this user-specified offset value is set by
	 * invoking the zeroYaw() method.
	 * <p>
	 * 
	 * @return The current total accumulated yaw angle (Z axis) of the robot
	 *         in degrees. This heading is based on integration of the returned rate
	 *         from the Z-axis (yaw) gyro.
	 */
	public double getAngle() {
		return m_imu.getAngle();
	}

	/**
	 * Return the rate of rotation of the yaw (Z-axis) gyro, in degrees per second.
	 * <p>
	 * The rate is based on the most recent reading of the yaw gyro angle.
	 * <p>
	 * 
	 * @return The current rate of change in yaw angle (in degrees per second)
	 */
	public double getRate() {
		return m_imu.getRate();
	}

	/**
	 * Returns the current pitch value (in degrees, from -180 to 180)
	 * reported by the sensor. Pitch is a measure of rotation around
	 * the X Axis.
	 * 
	 * @return The current pitch value in degrees (-180 to 180).
	 */
	public float getPitch() {
		return m_imu.getPitch();
	}

	/**
	 * Returns the current roll value (in degrees, from -180 to 180)
	 * reported by the sensor. Roll is a measure of rotation around
	 * the X Axis.
	 * 
	 * @return The current roll value in degrees (-180 to 180).
	 */
	public float getRoll() {
		return m_imu.getRoll();
	}

	/**
	 * Returns the current yaw value (in degrees, from -180 to 180)
	 * reported by the sensor. Yaw is a measure of rotation around
	 * the Z Axis (which is perpendicular to the earth).
	 * <p>
	 * Note that the returned yaw value will be offset by a user-specified
	 * offset value; this user-specified offset value is set by
	 * invoking the zeroYaw() method.
	 * 
	 * @return The current yaw value in degrees (-180 to 180).
	 */
	public float getYaw() {
		return m_imu.getYaw();
	}

	/**
	 * Returns the current linear acceleration in the X-axis (in G).
	 * <p>
	 * World linear acceleration refers to raw acceleration data, which
	 * has had the gravity component removed, and which has been rotated to
	 * the same reference frame as the current yaw value. The resulting
	 * value represents the current acceleration in the x-axis of the
	 * body (e.g., the robot) on which the sensor is mounted.
	 * <p>
	 * 
	 * @return Current world linear acceleration in the X-axis (in G).
	 */
	public float getWorldLinearAccelX() {
		return m_imu.getWorldLinearAccelX();
	}

	/**
	 * Returns the current linear acceleration in the Y-axis (in G).
	 * <p>
	 * World linear acceleration refers to raw acceleration data, which
	 * has had the gravity component removed, and which has been rotated to
	 * the same reference frame as the current yaw value. The resulting
	 * value represents the current acceleration in the Y-axis of the
	 * body (e.g., the robot) on which the sensor is mounted.
	 * <p>
	 * 
	 * @return Current world linear acceleration in the Y-axis (in G).
	 */
	public float getWorldLinearAccelY() {
		return m_imu.getWorldLinearAccelY();
	}

	/**
	 * Returns the current linear acceleration in the Z-axis (in G).
	 * <p>
	 * World linear acceleration refers to raw acceleration data, which
	 * has had the gravity component removed, and which has been rotated to
	 * the same reference frame as the current yaw value. The resulting
	 * value represents the current acceleration in the Z-axis of the
	 * body (e.g., the robot) on which the sensor is mounted.
	 * <p>
	 * 
	 * @return Current world linear acceleration in the Z-axis (in G).
	 */
	public float getWorldLinearAccelZ() {
		return m_imu.getWorldLinearAccelZ();
	}

	/**
	 * Indicates if the sensor is currently detecting motion,
	 * based upon the X and Y-axis world linear acceleration values.
	 * If the sum of the absolute values of the X and Y axis exceed
	 * a "motion threshold", the motion state is indicated.
	 * <p>
	 * 
	 * @return Returns true if the sensor is currently detecting motion.
	 */
	public boolean isMoving() {
		return m_imu.isMoving();
	}

	/**
	 * Indicates if the sensor is currently detecting yaw rotation,
	 * based upon whether the change in yaw over the last second
	 * exceeds the "Rotation Threshold."
	 * <p>
	 * Yaw Rotation can occur either when the sensor is rotating, or
	 * when the sensor is not rotating AND the current gyro calibration
	 * is insufficiently calibrated to yield the standard yaw drift rate.
	 * <p>
	 * 
	 * @return Returns true if the sensor is currently detecting rotation.
	 */
	public boolean isRotating() {
		return m_imu.isRotating();
	}

	/**
	 * Returns the velocity (in meters/sec) of the X axis [Experimental].
	 *
	 * NOTE: This feature is experimental. Velocity measures rely on integration
	 * of acceleration values from MEMS accelerometers which yield "noisy" values.
	 * The
	 * resulting velocities are not known to be very accurate.
	 * 
	 * @return Current Velocity (in meters/squared).
	 */
	public float getVelocityX() {
		return m_imu.getVelocityX();
	}

	/**
	 * Returns the velocity (in meters/sec) of the Y axis [Experimental].
	 *
	 * NOTE: This feature is experimental. Velocity measures rely on integration
	 * of acceleration values from MEMS accelerometers which yield "noisy" values.
	 * The
	 * resulting velocities are not known to be very accurate.
	 * 
	 * @return Current Velocity (in meters/squared).
	 */
	public float getVelocityY() {
		return m_imu.getVelocityY();
	}

	/**
	 * Returns the velocity (in meters/sec) of the Z axis [Experimental].
	 *
	 * NOTE: This feature is experimental. Velocity measures rely on integration
	 * of acceleration values from MEMS accelerometers which yield "noisy" values.
	 * The
	 * resulting velocities are not known to be very accurate.
	 * 
	 * @return Current Velocity (in meters/squared).
	 */
	public float getVelocityZ() {
		return m_imu.getVelocityZ();
	}

	/**
	 * Returns the displacement (in meters) of the X axis since resetDisplacement()
	 * was last invoked [Experimental].
	 *
	 * NOTE: This feature is experimental. Displacement measures rely on
	 * double-integration
	 * of acceleration values from MEMS accelerometers which yield "noisy" values.
	 * The
	 * resulting displacement are not known to be very accurate, and the amount of
	 * error
	 * increases quickly as time progresses.
	 * 
	 * @return Displacement since last reset (in meters).
	 */
	public float getDisplacementX() {
		return m_imu.getDisplacementX();
	}

	/**
	 * Returns the displacement (in meters) of the Y axis since resetDisplacement()
	 * was last invoked [Experimental].
	 *
	 * NOTE: This feature is experimental. Displacement measures rely on
	 * double-integration
	 * of acceleration values from MEMS accelerometers which yield "noisy" values.
	 * The
	 * resulting displacement are not known to be very accurate, and the amount of
	 * error
	 * increases quickly as time progresses.
	 * 
	 * @return Displacement since last reset (in meters).
	 */
	public float getDisplacementY() {
		return m_imu.getDisplacementY();
	}

	/**
	 * Returns the displacement (in meters) of the Z axis since resetDisplacement()
	 * was last invoked [Experimental].
	 *
	 * NOTE: This feature is experimental. Displacement measures rely on
	 * double-integration
	 * of acceleration values from MEMS accelerometers which yield "noisy" values.
	 * The
	 * resulting displacement are not known to be very accurate, and the amount of
	 * error
	 * increases quickly as time progresses.
	 * 
	 * @return Displacement since last reset (in meters).
	 */
	public float getDisplacementZ() {
		return m_imu.getDisplacementZ();
	}

	/**
	 * Reset the Yaw gyro.
	 * <p>
	 * Resets the Gyro Z (Yaw) axis to a heading of zero. This can be used if
	 * there is significant drift in the gyro and it needs to be recalibrated
	 * after it has been running.
	 */
	public void reset() {
		m_imu.reset();
	}

	/**
	 * Sets the user-specified yaw offset to the current
	 * yaw value reported by the sensor.
	 * <p>
	 * This user-specified yaw offset is automatically
	 * subtracted from subsequent yaw values reported by
	 * the getYaw() method.
	 *
	 * NOTE: This method has no effect if the sensor is
	 * currently calibrating, since resetting the yaw will
	 * interfere with the calibration process.
	 */
	public void zeroYaw() {
		m_imu.zeroYaw();
	}

	@Override
	// This method will be called once per scheduler run
	public void periodic() {
	}
}

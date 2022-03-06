/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.fireteam322.frc.robot.subsystems;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FrontCamera extends SubsystemBase {
	private static UsbCamera frontCameraServer;
	/**
	 * Creates a new FrontCamera.
	 */
	public FrontCamera() {
		//Setup Camera
		frontCameraServer = new UsbCamera("Front Camera", 0);
	}

	public void setResolution(int width, int height) {
		frontCameraServer.setResolution(width, height);
	}


	/**
	 * This method returns the Limelight HttpCamera feed.
	 * @return Returns a UsbCamera feed.
	 */
	public UsbCamera getCameraFeed() {
		return frontCameraServer;
	}

	public void setFPS(int fps) {
		frontCameraServer.setFPS(fps);
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.fireteam322.frc.robot.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.fireteam322.frc.robot.Constants;
import com.fireteam322.frc.robot.subsystems.LED;

public class AutomaticLED extends CommandBase {
	private final LED m_led;
	/**
	 * Creates a new AutomaticLED.
	 */
	public AutomaticLED(LED led) {
		m_led = led;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(m_led);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		m_led.setRGB(Color.kWhite, Constants.DEFAULT_BLINK_RATE);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		m_led.automaticLEDSetter();
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		m_led.setRGB(Color.kWhite, Constants.DEFAULT_BLINK_RATE);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}
}

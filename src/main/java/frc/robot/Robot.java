// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;



/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private TalonSRX FL = new TalonSRX(1);
  private TalonSRX BL = new TalonSRX(3);
  private TalonSRX FR = new TalonSRX(0);
  private TalonSRX BR = new TalonSRX(2);
  private Joystick joystick1 = new Joystick(0);
  private Joystick joystick2 = new Joystick(1);
  private JoystickButton steerSwitch = new JoystickButton(joystick1, 2);
  private Boolean arcade = true;
  private Boolean sSPressed = false;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Motors
    FL.set(ControlMode.PercentOutput, 0);
    BL.set(ControlMode.PercentOutput, 0);
    FR.set(ControlMode.PercentOutput, 0);
    BR.set(ControlMode.PercentOutput, 0);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double stick1 = joystick1.getRawAxis(1);
    double stick2 = joystick2.getRawAxis(1);
    double speed = Math.max(0, 1 - joystick1.getRawAxis(3));
    double twistDegrees = joystick1.getTwist();

    if (steerSwitch.get()) {
      if (!sSPressed) {
        switchSteer();
        sSPressed = true;
      }
    } else {
      sSPressed = false;
    }

    if (arcade)
    {
      double[] converted = arcadeConversion(stick1, twistDegrees);
      stick1 = converted[0] * -1;
      stick2 = converted[1] * -1;
    }

    stick1 *= speed;
    stick2 *= speed;

    FL.set(ControlMode.PercentOutput, stick1);
    BL.set(ControlMode.PercentOutput, stick1);
    FR.set(ControlMode.PercentOutput, -stick2);
    BR.set(ControlMode.PercentOutput, -stick2);
  }

  public double[] arcadeConversion(double throttleValue, double turnValue) {
    double leftMtr;
    double rightMtr;
    leftMtr = throttleValue + turnValue;
    rightMtr = throttleValue - turnValue;
    return new double[] {leftMtr, rightMtr};
  }

  public void switchSteer() {
    arcade = !arcade;
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
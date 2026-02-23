package frc.robot.subsystems.shooter;

import edu.wpi.first.math.geometry.Translation2d;

public class ShooterConstants {
  /** Initial projectile velocity in meters per second. */
  public static double shooterSpeedMps = 8.0;

  /** Launch angle in degrees from horizontal. */
  public static double shooterAngleDegrees = 60.0;

  /** Height of the shooter from the ground in meters. */
  public static double shooterHeightMeters = 0.4;

  /** Number of shots per second in simulation. */
  public static double shotsPerSecond = 3.0;

  /** Offset of the shooter from the robot center (in robot frame). */
  public static final Translation2d shooterOffset = new Translation2d(0.2, 0);
}

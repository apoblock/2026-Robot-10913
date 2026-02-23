package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {
  @AutoLog
  public static class ShooterIOInputs {
    public boolean isShooting = false;
    public int shotsFired = 0;
    public int shotsScored = 0;
  }

  /** Updates the set of loggable inputs. */
  public default void updateInputs(ShooterIOInputs inputs) {}

  /** Enable or disable shooting. */
  public default void setShooting(boolean shooting) {}

  /** Set the shooter speed in meters per second. */
  public default void setShooterSpeed(double speedMps) {}
}

package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Shooter extends SubsystemBase {
  private final ShooterIO io;
  private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();

  public Shooter(ShooterIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Shooter", inputs);
  }

  /** Start shooting projectiles. */
  public void startShooting() {
    io.setShooting(true);
  }

  /** Stop shooting projectiles. */
  public void stopShooting() {
    io.setShooting(false);
  }

  /** Set the shooter speed in meters per second. */
  public void setShooterSpeed(double speedMps) {
    io.setShooterSpeed(speedMps);
  }

  /** Command that shoots while held and stops when released. */
  public Command shootCommand() {
    return this.startEnd(this::startShooting, this::stopShooting);
  }

  /** Returns the number of shots fired. */
  public int getShotsFired() {
    return inputs.shotsFired;
  }

  /** Returns the number of shots scored. */
  public int getShotsScored() {
    return inputs.shotsScored;
  }

  /** Returns whether the shooter is currently firing. */
  public boolean isShooting() {
    return inputs.isShooting;
  }
}

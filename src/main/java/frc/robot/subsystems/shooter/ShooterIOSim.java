package frc.robot.subsystems.shooter;

import static frc.robot.subsystems.shooter.ShooterConstants.*;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.Timer;
import org.ironmaple.simulation.IntakeSimulation;
import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.seasonspecific.rebuilt2026.RebuiltFuelOnFly;
import org.littletonrobotics.junction.Logger;

public class ShooterIOSim implements ShooterIO {
  private final SwerveDriveSimulation driveSim;
  private final IntakeSimulation intakeSim;

  private boolean isShooting = false;
  private int shotsFired = 0;
  private int shotsScored = 0;
  private double lastShotTime = 0.0;
  private double speedMps = shooterSpeedMps;

  public ShooterIOSim(SwerveDriveSimulation driveSim, IntakeSimulation intakeSim) {
    this.driveSim = driveSim;
    this.intakeSim = intakeSim;
  }

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    // Attempt to fire if shooting is enabled
    if (isShooting) {
      double now = Timer.getFPGATimestamp();
      double shotInterval = 1.0 / shotsPerSecond;
      if (now - lastShotTime >= shotInterval) {
        tryLaunchProjectile();
        lastShotTime = now;
      }
    }

    inputs.isShooting = isShooting;
    inputs.shotsFired = shotsFired;
    inputs.shotsScored = shotsScored;
  }

  @Override
  public void setShooting(boolean shooting) {
    this.isShooting = shooting;
    if (shooting) {
      // Reset shot timer so first shot fires immediately
      lastShotTime = 0.0;
    }
  }

  @Override
  public void setShooterSpeed(double speedMps) {
    this.speedMps = speedMps;
  }

  private void tryLaunchProjectile() {
    // Try to consume a game piece from the intake
    if (!intakeSim.obtainGamePieceFromIntake()) {
      return; // No game pieces available
    }

    shotsFired++;

    // Create the projectile
    RebuiltFuelOnFly fuel =
        new RebuiltFuelOnFly(
            driveSim.getSimulatedDriveTrainPose().getTranslation(),
            shooterOffset,
            driveSim.getDriveTrainSimulatedChassisSpeedsFieldRelative(),
            driveSim.getSimulatedDriveTrainPose().getRotation(),
            Units.Meters.of(shooterHeightMeters),
            Units.MetersPerSecond.of(speedMps),
            Units.Degrees.of(shooterAngleDegrees));

    // Configure trajectory visualization for AdvantageScope
    fuel.withProjectileTrajectoryDisplayCallBack(
        (poses) ->
            Logger.recordOutput("Shooter/SuccessfulShotTrajectory", poses.toArray(Pose3d[]::new)),
        (poses) ->
            Logger.recordOutput("Shooter/MissedShotTrajectory", poses.toArray(Pose3d[]::new)));

    // Count scored shots when projectile hits the hub
    fuel.withHitTargetCallBack(this::onTargetHit);

    // Missed shots land on the field as regular fuel
    fuel.enableBecomesGamePieceOnFieldAfterTouchGround();

    // Register the projectile with the arena
    SimulatedArena.getInstance().addGamePieceProjectile(fuel);
  }

  /** Increment the scored counter (called from hit target callback). */
  public void onTargetHit() {
    shotsScored++;
  }
}

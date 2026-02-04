package frc.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/** Utility class for controller helpers (rumble, etc.). */
public class ControllerUtils {

  /**
   * Creates a command that rumbles the controller a specified number of times.
   *
   * @param hid The controller HID to rumble.
   * @param times Number of rumble pulses.
   * @param rumbleTime Duration of each rumble pulse in seconds.
   * @param gapTime Duration of the gap between pulses in seconds.
   * @return A command that executes the rumble pattern.
   */
  public static Command rumble(GenericHID hid, int times, double rumbleTime, double gapTime) {
    Command[] steps = new Command[times * 2 + (times - 1)];
    int idx = 0;
    for (int i = 0; i < times; i++) {
      if (i > 0) {
        steps[idx++] = new WaitCommand(gapTime);
      }
      steps[idx++] = Commands.runOnce(() -> hid.setRumble(GenericHID.RumbleType.kBothRumble, 1.0));
      steps[idx++] = new WaitCommand(rumbleTime);
    }
    // Final step: turn off rumble
    return Commands.sequence(steps)
        .finallyDo(() -> hid.setRumble(GenericHID.RumbleType.kBothRumble, 0.0));
  }
}

package rip.hippo.slog.configuration.format

/**
 * @author Hippo
 * @version 1.0.0, 6/23/21
 * @since 1.0.0
 */
trait FormatStage {
  def format(builder: StringBuilder, level: String, message: String): Unit
}

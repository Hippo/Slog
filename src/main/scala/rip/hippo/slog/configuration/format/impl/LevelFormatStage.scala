package rip.hippo.slog.configuration.format.impl

import rip.hippo.slog.configuration.format.FormatStage

/**
 * @author Hippo
 * @version 1.0.0, 6/24/21
 * @since 1.0.0
 */
final case class LevelFormatStage() extends FormatStage {
  override def format(builder: StringBuilder, level: String, message: String): Unit = builder.append(level)
}

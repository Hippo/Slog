package rip.hippo.slog.configuration.format.impl

import rip.hippo.slog.configuration.format.FormatStage

import java.time.LocalDate

/**
 * @author Hippo
 * @version 1.0.0, 6/23/21
 * @since 1.0.0
 */
final case class DateFormatStage() extends FormatStage {
  override def format(builder: StringBuilder, level: String, message: String): Unit = builder.append(LocalDate.now())
}
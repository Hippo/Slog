package rip.hippo.slog.configuration

import rip.hippo.slog.configuration.format.FormatStage

import java.io.OutputStream
import scala.collection.mutable.ListBuffer

/**
 * @author Hippo
 * @version 1.0.0, 6/23/21
 * @since 1.0.0
 */
trait LoggerConfiguration {
  def getFormatStages: ListBuffer[FormatStage]
  def getOutputStream: OutputStream
  def logAsynchronously: Boolean
}

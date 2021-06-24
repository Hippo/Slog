package rip.hippo.slog.configuration.builder

import rip.hippo.slog.configuration.format.FormatStage
import rip.hippo.slog.configuration.format.impl._

import scala.collection.mutable.ListBuffer

/**
 * @author Hippo
 * @version 1.0.0, 6/24/21
 * @since 1.0.0
 */
final class MessageFormatBuilder(loggerConfigurationBuilder: LoggerConfigurationBuilder) {

  val formatStages: ListBuffer[FormatStage] = ListBuffer[FormatStage]()

  def date(): MessageFormatBuilder = {
    formatStages += DateFormatStage()
    this
  }

  def inject(inject: String): MessageFormatBuilder = {
    formatStages += InjectFormatStage(inject)
    this
  }

  def level(): MessageFormatBuilder = {
    formatStages += LevelFormatStage()
    this
  }

  def message(): MessageFormatBuilder = {
    formatStages += MessageFormatStage()
    this
  }

  def space(): MessageFormatBuilder = {
    formatStages += SpaceFormatStage()
    this
  }

  def time(): MessageFormatBuilder = {
    formatStages += TimeFormatStage()
    this
  }

  def endFormatting(): LoggerConfigurationBuilder =
    loggerConfigurationBuilder
}

/*
 *    Copyright 2021 Hippo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package rip.hippo.slog.configuration.builder

import rip.hippo.slog.configuration.LoggerConfiguration
import rip.hippo.slog.configuration.format.FormatStage

import java.io.OutputStream
import scala.collection.mutable.ListBuffer


/**
 * @author Hippo
 * @version 1.0.0, 6/24/21
 * @since 1.0.0
 */
final class LoggerConfigurationBuilder {
  private var outputStream: Option[OutputStream] = Option.empty
  private var asynchronous: Boolean = false
  private val messageFormatBuilder = new MessageFormatBuilder(this)

  def out(outputStream: OutputStream): LoggerConfigurationBuilder = {
    this.outputStream = Option(outputStream)
    this
  }

  def async(): LoggerConfigurationBuilder = {
    asynchronous = true
    this
  }

  def startFormatting(): MessageFormatBuilder =
    messageFormatBuilder

  def build(): LoggerConfiguration =
    new LoggerConfiguration {
      override def getFormatStages: ListBuffer[FormatStage] = messageFormatBuilder.formatStages

      override def getOutputStream: OutputStream = outputStream match {
        case Some(value) => value
        case None => throw new NullPointerException("No output stream configured")
      }

      override def logAsynchronously: Boolean = asynchronous
    }

}

object LoggerConfigurationBuilder {
  def newBuilder(): LoggerConfigurationBuilder = new LoggerConfigurationBuilder()

  def defaultConfiguration(): LoggerConfiguration =
    newBuilder()
      .out(System.out)
      .async()
      .startFormatting()
        .inject("[")
        .date()
        .inject("]")
        .space()
        .inject("[")
        .time()
        .inject("]")
        .space()
        .level()
        .space()
        .message()
      .endFormatting()
      .build()
}

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

package rip.hippo.slog

import rip.hippo.slog.Logger.instances
import rip.hippo.slog.configuration.LoggerConfiguration
import rip.hippo.slog.configuration.builder.LoggerConfigurationBuilder

import java.nio.charset.StandardCharsets
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}
import scala.collection.immutable.ArraySeq

/**
 * @author Hippo
 * @version 1.0.0, 6/23/21
 * @since 1.0.0
 */
final case class Logger(loggerConfiguration: LoggerConfiguration) {
  instances += 1

  val messageQueue: BlockingQueue[String] = new LinkedBlockingQueue[String]()
  val loggerThread: Thread = new Thread(() => {
    while (true)
      if (!messageQueue.isEmpty)
        loggerConfiguration.getOutputStream.write(messageQueue.take().getBytes(StandardCharsets.UTF_8))
  }, s"Slog Thread ($instances)")
  loggerThread.setDaemon(true)
  if (loggerConfiguration.logAsynchronously) {
    loggerThread.start()
  }

  def info(message: String, args: Any*): Unit = log("INFO", message, args.toArray)
  def warning(message: String, args: Any*): Unit = log("WARNING", message, args.toArray)
  def debug(message: String, args: Any*): Unit = log("DEBUG", message, args.toArray)
  def error(message: String, args: Any*): Unit = log("ERROR", message, args.toArray)
  def error(message: String, throwable: Throwable, args: Any*): Unit = {
    val stringBuilder = new StringBuilder
    stringBuilder.append(message).append('\n')
    throwable.getStackTrace.foreach(element =>
      stringBuilder.append(element.toString).append('\n')
    )
    log("ERROR", replaceString(stringBuilder.toString(), args.toArray), Array.empty)
  }

  def log(level: String, message: String, args: Array[Any]): Unit = {
    val replacedMessage = replaceString(message, args)

    val formatBuilder = new StringBuilder

    loggerConfiguration.getFormatStages.foreach(_.format(formatBuilder, level, replacedMessage))

    val formattedMessage = formatBuilder.toString()

    if (loggerConfiguration.logAsynchronously) {
      messageQueue.offer(formattedMessage)
    } else {
      loggerConfiguration.getOutputStream.write(formattedMessage.getBytes(StandardCharsets.UTF_8))
    }
  }


  private def replaceString(message: String, args: Array[Any]): String = {
    val stringBuilder = new StringBuilder
    var index = 0
    val iterator = message.toCharArray.iterator

    while (iterator.hasNext) {
      val char = iterator.next()
      if (char == '{') {
        val secondChar = iterator.next()
        if (secondChar == '}') {
          if (index < args.length) {
            stringBuilder.append(args(index))
            index += 1
          } else {
            stringBuilder.append(char).append(secondChar)
          }
        } else {
          stringBuilder.append(char).append(secondChar)
        }
      } else {
        stringBuilder.append(char)
      }
    }
    stringBuilder.append('\n')
    stringBuilder.toString()
  }

}

object Logger {
  private var instances = 0

  def getDefaultLogger(): Logger =
    Logger(LoggerConfigurationBuilder.defaultConfiguration())

}

package rip.hippo.slog.test

import org.scalatest.FunSuite
import rip.hippo.slog.Logger

/**
 * @author Hippo
 * @version 1.0.0, 6/24/21
 * @since 1.0.0
 */
final class BasicLogTest extends FunSuite {

  test("Logger.basic") {
    val logger = Logger.getDefaultLogger()

    logger.info("Test {}")
    logger.info("Format {}", 69)
    logger.warning("Cool warning")
    logger.error("Error: {}", "Cool warning here")
    logger.debug("Debug message")

    logger.error("NPE: ", new NullPointerException)
  }

}

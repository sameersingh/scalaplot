package sameersingh.scalaplot

import org.junit._

/**
 * @author sameer
 * @date 10/6/12
 */
@Test
class PlotTest {

  @Test
  def testWrite(): Unit = {
    val testFile = java.io.File.createTempFile("test", "exps")
    println(testFile.getCanonicalPath)
  }

}

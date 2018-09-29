package battleship

import org.scalatest._
import org.scalatest.FunSuite

class HelloSpec extends FunSuite with Matchers {
    test ("The Hello print should contains MY GRID.") {
        assert(Hello.greeting contains "MY GRID")
    }
}

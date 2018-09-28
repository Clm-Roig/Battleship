package battleship

import org.scalatest._
import org.scalatest.FunSuite

class HelloSpec extends FunSuite with Matchers {
    test ("The Hello object should contain hello") {
        assert(Hello.greeting contains "MY GRID")
    }
}

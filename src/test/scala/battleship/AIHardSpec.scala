package battleship
import org.scalatest.fixture

class AIHardSpec extends fixture.FunSuite {

    case class FixtureParam(ai: AIHard)

    def withFixture(test: OneArgTest): org.scalatest.Outcome = {
        val ai = new AIHard()
        try test(FixtureParam(ai))
        finally {
            // perform some cleanup...
        }
    }

    // ===== askForShootCoordinates() tests
    test("askForShootCoordinates(): 1 cell already hit, nothing hit next to it.") { f => 
        val ai = f.ai.copyWithNewShotsFired(Set(
            (5,5,Grid.HIT)
        ))
        val possibleShots = List((4,5),(5,4),(6,5),(5,6))
        val shot = ai.askForShootCoordinates()
        assert(possibleShots.contains(shot))
    }

    test("askForShootCoordinates(): 1 cell already hit + 3 water it next to it => should return the remaining cell not hit.") { f => 
        val ai = f.ai.copyWithNewShotsFired(Set(
            (5,5,Grid.HIT),(5,4,Grid.WATER),(4,5,Grid.WATER),(5,6,Grid.WATER)
        ))
        val shot = ai.askForShootCoordinates()
        assert(shot == (6,5))
    }
}
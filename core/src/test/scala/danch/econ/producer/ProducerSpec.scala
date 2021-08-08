package danch.econ.producer

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, BehaviorTestKit, TestInbox}
import akka.actor.typed.receptionist.Receptionist
import danch.econ.protocols.{MarketProtocol, SimProtocol}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class ProducerSpec extends WordSpecLike with BeforeAndAfterAll {
   val testKit = ActorTestKit()

   override def afterAll(): Unit = testKit.shutdownTestKit()

   val dummyProducerPath = "dummy-producer-path"
   val dummyLocation = "test-location"

   "A Producer" must {

   }
}

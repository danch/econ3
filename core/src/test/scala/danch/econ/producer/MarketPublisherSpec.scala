package danch.econ.producer

import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.receptionist.Receptionist
import danch.econ.market.MarketProtocol

class MarketPublisherSpec extends WordSpecLike with BeforeAndAfterAll {
   val testKit = ActorTestKit()

   override def afterAll(): Unit = testKit.shutdownTestKit()

   val dummyProducerPath = "dummy-producer-path"

   "MarketPublisher" must {
      "translate and propagate the event" in {
          val key = MarketProtocol.key("test-location")
          val probe = testKit.createTestProbe[MarketProtocol.MarketEvent]("listener")
          testKit.system.receptionist ! Receptionist.Register(key, probe.ref)
          val publisher = testKit.spawn(MarketPublisher(key, Producer.InventoryLevelNotification(dummyProducerPath, "itemId1", 42)))
          probe.expectMessage(MarketProtocol.CommoditySupply(dummyProducerPath, "itemId1", 42.0d))
      }
   }

}

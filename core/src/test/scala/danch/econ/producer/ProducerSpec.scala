package danch.econ.producer

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, BehaviorTestKit, TestInbox}
import akka.actor.typed.receptionist.Receptionist
import danch.econ.market.MarketProtocol
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class ProducerSpec extends WordSpecLike with BeforeAndAfterAll {
   val testKit = ActorTestKit()

   override def afterAll(): Unit = testKit.shutdownTestKit()

   val dummyProducerPath = "dummy-producer-path"
   val dummyLocation = "test-location"

   "A Producer" must {
       "Respond to an AddInventory command by publishing the appropriate MarketEvent" in {
          val key = MarketProtocol.key(dummyLocation)
          val probe = testKit.createTestProbe[MarketProtocol.MarketEvent]("listener")
          testKit.system.receptionist ! Receptionist.Register(key, probe.ref)

          val producer = testKit.spawn(Producer("producer1", dummyLocation))
          producer ! Producer.AddInventory("itemId1", 42.0)

          probe.expectMessage(MarketProtocol.CommoditySupply(producer.path.toStringWithoutAddress, "itemId1", 42.0d))
       }
      "Respond to a RemoveInventory command by publishing the appropriate MarketEvent" in {
         val key = MarketProtocol.key(dummyLocation)
         val probe = testKit.createTestProbe[MarketProtocol.MarketEvent]("listener")
         testKit.system.receptionist ! Receptionist.Register(key, probe.ref)

         val producer = testKit.spawn(Producer("producer1", dummyLocation))
         producer ! Producer.RemoveInventory("itemId1", 42.0)

         probe.expectMessage(MarketProtocol.CommoditySupply(producer.path.toStringWithoutAddress, "itemId1", -42.0d))
      }
   }
}

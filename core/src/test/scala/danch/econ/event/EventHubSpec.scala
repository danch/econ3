package danch.econ.event

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import danch.econ.PubSubTest.guardian
import danch.econ.protocols.SimProtocol.TickEvent
import danch.econ.protocols.{MarketProtocol, SimProtocol}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class EventHubSpec extends WordSpecLike with BeforeAndAfterAll {
  val testKit = ActorTestKit()

  override def afterAll(): Unit = testKit.shutdownTestKit()

  val dummyLocation = "test-location"
  val sellerPath = "seller"


  "The Event Hub" must {
    "Echo received messages to the subscribers" in {
      //val probe = testKit.createTestProbe[SimProtocol.SimEvent]("listener")
      val probe = testKit.spawn(Behaviors.receive[SimProtocol.SimEvent]( (context, event ) => {
        Behaviors.same
      }))
      //sut ! SimProtocol.Subscribe(probe)

      val message: SimProtocol.SimEvent = SimProtocol.Sold(sellerPath, "buyer", "item", 5.0)

      //sut ! message
    }
  }
}

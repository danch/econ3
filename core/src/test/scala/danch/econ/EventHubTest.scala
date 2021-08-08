package danch.econ

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.util.Timeout
import danch.econ.event.EventHub
import danch.econ.protocols.SimProtocol
import danch.econ.protocols.SimProtocol.{ForSale, SimEvent, Subscribe, TickEvent, Wanted}

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{DurationInt, FiniteDuration}

//TODO: make this actually a test
object EventHubTest {

  def main(args: Array[String]) : Unit = {
    val actorSystem = ActorSystem[SimProtocol.SimEvent](guardian(), "PubSubTest")
  }

  def guardian() : Behavior[SimEvent] = Behaviors.setup( context => {
    val hub = context.spawn(EventHub("location"), "local_events")
    hub ! Subscribe(context.self)

    val send = context.spawn(sender(hub), "sender")

    Behaviors.receive((context, message) => {
      message match {
        case Wanted(_, item, quant) =>
          println(s"Wanted: $quant $item")
        case ForSale(_, item, quant) =>
          println(s"For Sale: $quant $item")
      }
      Behaviors.same
    })
  })

  def sender(topic: ActorRef[SimEvent]) : Behavior[SimEvent] = Behaviors.setup( context => {
    implicit val timeout: Timeout = 3.seconds

    context.scheduleOnce(2.seconds, context.self, TickEvent(1))
    Behaviors.receive((context, message) => {

      message match {
        case TickEvent(tickNumber) => {
          topic ! Wanted(context.self.path.toString, "bread", 100.0)

          context.scheduleOnce(2.seconds, context.self, TickEvent(tickNumber + 1))
        }
      }
      Behaviors.same
    })
  })
}

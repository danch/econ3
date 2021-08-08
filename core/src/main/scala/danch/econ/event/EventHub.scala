package danch.econ.event

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.pubsub.Topic
import danch.econ.protocols.SimProtocol

object EventHub {

  def apply(location: String): Behavior[SimProtocol.SimEvent] = Behaviors.setup( (context) => {
    val topic = context.spawn(Topic[SimProtocol.SimEvent](location), "local_events")
    Behaviors.receive((context, simEvent) =>
       simEvent match {
         case SimProtocol.Subscribe(subscriber) =>
           topic ! Topic.subscribe(subscriber)
           Behaviors.same
         case _ =>
           topic ! Topic.Publish(simEvent)
           Behaviors.same
       }
    )
  })

}

package danch.econ.producer

import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import danch.econ.market.MarketProtocol

/** Simple delegate transient actor that converts Producer land events to Market publications and sends them to
  * the servicekey indicated
  */
object MarketPublisher {

   def apply(key: ServiceKey[MarketProtocol.MarketEvent], notification: Producer.Notification) : Behavior[Receptionist.Listing] = {
      Behaviors.setup { context => {
            //Note: we may need to make this actor multi-use and subscribe to notifications on the service list (performance)
            context.system.receptionist ! Receptionist.Find(key, context.self)
            publishInventory(key, notification match {
               case Producer.InventoryLevelNotification(producerPath, itemId, quantity) =>
                  MarketProtocol.CommoditySupply(producerPath, itemId, quantity)
            })
         }
      }
   }

   def publishInventory(key: ServiceKey[MarketProtocol.MarketEvent], event: MarketProtocol.MarketEvent) : Behavior[Receptionist.Listing] = Behaviors.receive { (context, listing) =>
      listing.serviceInstances(key).foreach( a => a ! event)
      Behaviors.stopped
   }
}

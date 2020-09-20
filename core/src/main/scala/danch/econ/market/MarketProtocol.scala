package danch.econ.market

import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.ServiceKey
import danch.econ.model.{ItemType, PreferenceRelation}

object MarketProtocol {
   sealed trait MarketCommand

   final case class CommoditySupply(producer: ActorRef[MarketEvent], itemId: String, quantityAvailable: Double) extends MarketCommand
   final case class CommodityDemand(consumer: ActorRef[MarketEvent], utilityModel: PreferenceRelation) extends MarketCommand

   sealed trait MarketEvent
   case class ItemTraded(item: ItemType, quantity: Double)
   final case class CommodityExchange(partyA: ActorRef[MarketEvent], partyB: ActorRef[MarketEvent], exchange: Seq[ItemTraded])

   def key(location: String) : ServiceKey[MarketCommand] = ServiceKey[MarketCommand](location)
}

package danch.econ.market

import java.math.BigDecimal

import akka.actor.typed.receptionist.ServiceKey

object MarketProtocol {
   sealed trait MarketEvent

   final case class CommoditySupply(producerPath: String, itemId: String, quantityAvailable: Double) extends MarketEvent
   final case class CommodityDemand(itemId: String) extends MarketEvent

   def key(location: String) = ServiceKey[MarketEvent](location)
}

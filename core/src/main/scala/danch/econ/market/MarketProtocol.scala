package danch.econ.market

import java.math.BigDecimal

class MarketProtocol {
   sealed trait MarketEvent

   final case class CommoditySupply(itemId: String, quantityAvailable: Double) extends MarketEvent
   final case class CommodityDemand(itemId: String)
}

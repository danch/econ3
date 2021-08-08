package danch.econ.protocols

import akka.actor.typed.ActorRef

object SimProtocol {
  sealed trait SimEvent;
  final case class TickEvent(tickNumber: Long) extends SimEvent

  sealed trait HubCommand extends SimEvent
  final case class Subscribe(subscriber: ActorRef[SimEvent]) extends HubCommand


  sealed trait MarketEvent extends SimProtocol.SimEvent
  /* Incoming market events */
  final case class ForSale(producerPath: String, itemId: String, quantityAvailable: Double) extends MarketEvent
  final case class Wanted(buyerPath: String, itemId: String, quantity: Double) extends MarketEvent
  /* outgoing market events */
  final case class Sold(sellerPath: String, buyerPath: String, itemId: String, quantity: Double) extends MarketEvent
}

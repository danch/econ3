package danch.econ.producer

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import danch.econ.market.MarketProtocol
import danch.econ.market.MarketProtocol.CommodityExchange

object MarketParticipant {
   sealed trait Command
   sealed trait Notification

   final case class AddInventory(itemId: String, quantity: Double) extends Command
   final case class RemoveInventory(itemId: String, quantity: Double) extends Command
   final case class WrappedMarketEvent(marketEvent: MarketProtocol.MarketEvent) extends Command

   final case class InventoryLevelNotification(producer: ActorRef[Command], itemId: String, quantity: Double) extends Notification

   private case class ProducerState(id: String, location: String, inventory: Map[String, Double],
                                    marketKey: ServiceKey[MarketProtocol.MarketCommand], marketEventAdapter: ActorRef[MarketProtocol.MarketEvent]) {
      def withInventoryChange(itemId: String, deltaQuantity: Double) : ProducerState = {
         val currentQuant = inventory.getOrElse(itemId, 0.0d)
         val newInventory = inventory + (itemId -> (currentQuant + deltaQuantity))
         ProducerState(id, location, newInventory, marketKey, marketEventAdapter)
      }
   }

   def apply(id: String, location: String) : Behavior[Command] = {
      Behaviors.setup { context =>
         val marketResponseMapper: ActorRef[MarketProtocol.MarketEvent] = context.messageAdapter(rsp => WrappedMarketEvent(rsp))
         producer(ProducerState(id, location, collection.immutable.Map[String, Double](), MarketProtocol.key(location), marketResponseMapper))
      }
   }

   private def producer(state: ProducerState) : Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
         case AddInventory(itemId, quantity) =>
            val newState = state.withInventoryChange(itemId, quantity)
            val newQuantity = newState.inventory.getOrElse(itemId, 0.0d)
            context.spawnAnonymous[Receptionist.Listing](MarketPublisher(state.marketKey, InventoryLevelNotification(context.self, itemId, newQuantity)))
            producer(newState)
         case RemoveInventory(itemId, quantity) =>
            val newState = state.withInventoryChange(itemId, -quantity)
            val newQuantity = newState.inventory.getOrElse(itemId, 0.0d)
            context.spawnAnonymous[Receptionist.Listing](MarketPublisher(state.marketKey, InventoryLevelNotification(context.self, itemId, newQuantity)))
            producer(newState)
      }
   }

   private def marketEventAdapter(participant: ActorRef[Command])  : Behavior[MarketProtocol.MarketEvent] = Behaviors.receive { (context, message) =>
      message match {
         case CommodityExchange(a, b, fromA, fromB) => {

         }
      }
   }


}

package danch.econ.producer

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.scaladsl.Behaviors
import danch.econ.market.MarketProtocol

object Producer {
   sealed trait Command
   sealed trait Notification

   final case class AddInventory(itemId: String, quantity: Double, replyTo: Option[ActorRef[Notification]]) extends Command
   final case class RemoveInventory(itemId: String, quantity: Double, replyTo: Option[ActorRef[Notification]]) extends Command

   final case class InventoryLevelNotification(producerPath: String, itemId: String, quantity: Double) extends Notification

   private case class ProducerState(id: String, location: String, inventory: Map[String, Double],
                                    marketKey: ServiceKey[MarketProtocol.MarketEvent]) {
      def withInventoryChange(itemId: String, deltaQuantity: Double) : ProducerState = {
         val currentQuant = inventory.getOrElse(itemId, 0.0d)
         val newInventory = inventory + (itemId -> (currentQuant + deltaQuantity))
         ProducerState(id, location, newInventory, marketKey)
      }
   }

   def apply(id: String, location: String) : Behavior[Command] = {
      Behaviors.setup { context =>

         producer(ProducerState(id, location, collection.immutable.Map[String, Double](), MarketProtocol.key(location)))
      }
   }

   private def producer(state: ProducerState) : Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
         case AddInventory(itemId, quantity, replyTo) =>
            val newState = state.withInventoryChange(itemId, quantity)
            val newQuantity = newState.inventory.getOrElse(itemId, 0.0d)
            replyTo.foreach(replyTo => {
               replyTo ! InventoryLevelNotification(context.self.path.toStringWithoutAddress, itemId, newQuantity)
            })
            producer(newState)
         case RemoveInventory(itemId, quantity, replyTo) =>
            val newState = state.withInventoryChange(itemId, -quantity)
            val newQuantity = newState.inventory.getOrElse(itemId, 0.0d)
            replyTo.foreach(replyTo => {
               replyTo ! InventoryLevelNotification(context.self.path.toStringWithoutAddress, itemId, newQuantity)
            })
            producer(newState)
      }
   }


}

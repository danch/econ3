package danch.econ.producer

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.pubsub.Topic
import danch.econ.protocols.SimProtocol

object Producer {
   sealed trait Command
   sealed trait Notification

   final case class AddInventory(itemId: String, quantity: Double) extends Command
   final case class RemoveInventory(itemId: String, quantity: Double) extends Command

   final case class InventoryLevelNotification(producerPath: String, itemId: String, quantity: Double) extends Notification

   private case class ProducerState(id: String, location: String, inventory: Map[String, Double]) {
      def withInventoryChange(itemId: String, deltaQuantity: Double) : ProducerState = {
         val currentQuant = inventory.getOrElse(itemId, 0.0d)
         val newInventory = inventory + (itemId -> (currentQuant + deltaQuantity))
         ProducerState(id, location, newInventory)
      }
   }

   def apply(id: String, location: String, topicMap: Map[String, ActorRef[SimProtocol.MarketEvent]]) : Behavior[Command] = {
      Behaviors.setup { context =>
         producer(ProducerState(id, location, collection.immutable.Map[String, Double]()))
      }
   }

   private def marketListener(producer: ActorRef[Command]) : Behavior[SimProtocol.MarketEvent] = Behaviors.receive { (context, message) =>
     message match {
        case SimProtocol.Sold(sellerPath, buyerPath, itemId, quantity) =>
           if (sellerPath == producer.path) {
               producer ! RemoveInventory(itemId, quantity)
           }
           if (buyerPath == producer.path) {
               producer ! AddInventory(itemId, quantity)
           }
         Behaviors.same
     }
   }

   private def producer(state: ProducerState) : Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
         case AddInventory(itemId, quantity) =>
            val newState = state.withInventoryChange(itemId, quantity)
            val newQuantity = newState.inventory.getOrElse(itemId, 0.0d)
            producer(newState)
         case RemoveInventory(itemId, quantity) =>
            val newState = state.withInventoryChange(itemId, -quantity)
            val newQuantity = newState.inventory.getOrElse(itemId, 0.0d)
            producer(newState)
      }
   }

}

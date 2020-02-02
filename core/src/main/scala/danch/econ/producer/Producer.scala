package danch.econ.producer

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.parallel.immutable

object Producer {
   sealed trait Command
   sealed trait Notification

   final case class AddInventory(itemId: String, quantity: Double, replyTo: Option[ActorRef[Notification]]) extends Command
   final case class RemoveInventory(itemId: String, quantity: Double, replyTo: Option[ActorRef[Notification]]) extends Command

   final case class InventoryLevelNotification(producerPath: String, itemId: String, quantity: Double) extends Notification

   private case class ProducerState(id: String, location: String, inventory: Map[String, Double]) {
      def withInventoryChange(itemId: String, deltaQuantity: Double) : ProducerState = {
         val currentQuant = inventory.getOrElse(itemId, 0.0d)
         val newInventory = inventory + (itemId -> (currentQuant + deltaQuantity))
         ProducerState(id, location, newInventory)
      }
   }

   def apply(id: String, location: String) : Behavior[Command] = {
      producer(ProducerState(id, location, collection.immutable.Map[String, Double]() ))
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

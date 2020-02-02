package danch.econ.producer

import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, TestInbox}
import org.scalatest.WordSpecLike

class ProducerSpec extends WordSpecLike {
   "A Producer" must {
       "Reply to an AddInventory command with the updated inventory level" in {
          val testKit = BehaviorTestKit(Producer("test-producer", "a-location"))
          val inbox = TestInbox[Producer.Notification]()
          testKit.run(Producer.AddInventory("itemId1", 42.0, Option(inbox.ref)))
          inbox.expectMessage(Producer.InventoryLevelNotification(testKit.ref.path.toStringWithoutAddress, "itemId1", 42.0d))
       }
      "Reply to an RemoveInventory command with the updated inventory level" in {
         val testKit = BehaviorTestKit(Producer("test-producer", "a-location"))
         val inbox = TestInbox[Producer.Notification]()
         testKit.run(Producer.RemoveInventory("itemId1", 42.0, Option(inbox.ref)))
         inbox.expectMessage(Producer.InventoryLevelNotification(testKit.ref.path.toStringWithoutAddress, "itemId1", -42.0d))
      }
   }
}

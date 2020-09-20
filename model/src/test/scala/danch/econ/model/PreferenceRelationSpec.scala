package danch.econ.model

import danch.econ.model.PreferenceToken.{EquivalentTo, ItemToken, SlightlyLessThan, SomewhatLessThan}
import org.scalatest.WordSpecLike

class PreferenceRelationSpec extends WordSpecLike {

   val itemTypeA = ItemType("1", "A")
   val itemTypeB = ItemType("2", "B")
   "PreferenceRelation" when {
      "Initialized" must {
         val relation = PreferenceRelation(itemTypeA)
         "Contain 1 element" in {
            assert(relation.tokenList.size == 1)
         }
      }
      "appended to once with SlightlyLessThan" must {
         val relation = PreferenceRelation(itemTypeA) < itemTypeB
         "have 3 elements" in {
            assert(relation.tokenList.size == 3)
         }
         "contain SlightlyLessThan" in {
            assert(relation.tokenList.contains(SlightlyLessThan))
         }
         "contain both itemTypes" in {
            assert(relation.tokenList.contains(ItemToken(itemTypeA)))
            assert(relation.tokenList.contains(ItemToken(itemTypeB)))
         }
      }
      "appended to once with SomewhatLessThan" must {
         val relation = PreferenceRelation(itemTypeA) << itemTypeB
         "contain SomewhatLessThan" in {
            assert(relation.tokenList.contains(SomewhatLessThan))
         }
         "contain both itemTypes" in {
            assert(relation.tokenList.contains(ItemToken(itemTypeA)))
            assert(relation.tokenList.contains(ItemToken(itemTypeB)))
         }
      }
      "appended to once with EquivalentTo" must {
         val relation = PreferenceRelation(itemTypeA) ~ itemTypeB
         "contain EquivalentTo" in {
            assert(relation.tokenList.contains(EquivalentTo))
         }
         "contain both itemTypes" in {
            assert(relation.tokenList.contains(ItemToken(itemTypeA)))
            assert(relation.tokenList.contains(ItemToken(itemTypeB)))
         }
      }
   }

}

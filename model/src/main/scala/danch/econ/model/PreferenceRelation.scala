package danch.econ.model

import danch.econ.model.PreferenceToken.{EquivalentTo, ItemToken, SlightlyLessThan, SomewhatLessThan}


object PreferenceRelation {

   def apply(item: ItemType) : PreferenceRelation = {
      new PreferenceRelation(List(ItemToken(item)))
   }
   def apply(item: ItemType, quantityDesired: Double ) : PreferenceRelation = {
      new PreferenceRelation(List(ItemToken(item, quantityDesired)))
   }
}

case class PreferenceRelation(tokenList: List[PreferenceToken]) {

   def <(other: ItemType, quantityDesired: Double = Double.MaxValue) : PreferenceRelation = {
      PreferenceRelation(tokenList ++ List(SlightlyLessThan, ItemToken(other, quantityDesired)))
   }
   def <<(other: ItemType, quantityDesired: Double = Double.MaxValue) : PreferenceRelation = {
      PreferenceRelation(tokenList ++ List(SomewhatLessThan, ItemToken(other, quantityDesired)))
   }
   def ~(other: ItemType, quantityDesired: Double = Double.MaxValue) : PreferenceRelation = {
      PreferenceRelation(tokenList ++ List(EquivalentTo, ItemToken(other, quantityDesired)))
   }
}
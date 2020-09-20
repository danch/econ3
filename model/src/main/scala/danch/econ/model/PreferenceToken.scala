package danch.econ.model

sealed abstract class PreferenceToken extends Product

object PreferenceToken {

   final case class ItemToken(itemType: ItemType, quantityDesired: Double = Double.MaxValue) extends PreferenceToken

   final case object EquivalentTo extends PreferenceToken

   final case object SlightlyLessThan extends PreferenceToken

   final case object SomewhatLessThan extends PreferenceToken

}

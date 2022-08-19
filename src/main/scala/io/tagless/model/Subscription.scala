package io.tagless.model

import Subscription.SubscriptionId

case class Subscription(
    subscriptionId: SubscriptionId,
    userId: User.UserId,
    channel: YouTubeChannel)

object Subscription:
  opaque type SubscriptionId = Long

  extension (value: SubscriptionId) def inc: SubscriptionId = value + 1

  object SubscriptionId:
    def fromLong(value: Long): SubscriptionId = value

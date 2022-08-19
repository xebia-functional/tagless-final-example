package io.tagless.algebras

import io.tagless.model.{Subscription, YouTubeChannel}
import io.tagless.model.User.UserId

trait SubscriptionPersistence[F[_]] {
  def addSubscription(userId: UserId, channel: YouTubeChannel): F[Subscription]
  def getByUser(userId: UserId): F[List[Subscription]]
  def removeSubscription(subscription: Subscription): F[Unit]
}

package io.tagless.interpreters

import cats.data.StateT
import cats.effect.IO
import io.tagless.algebras.SubscriptionPersistence
import io.tagless.model.{Subscription, User, YouTubeChannel}
import io.tagless.service.{ServiceState, SubscriptionServiceState}

class SubscriptionPersistenceImpl extends SubscriptionPersistence[SubscriptionServiceState] {
  def addSubscription(
      userId: User.UserId,
      channel: YouTubeChannel): StateT[IO, ServiceState, Subscription] =
    StateT { state => IO.pure(state.addSubscription(userId, channel)) }

  def getByUser(userId: User.UserId): SubscriptionServiceState[List[Subscription]] =
    StateT.inspect(state => state.subscriptionTable.filter(_.userId == userId))

  def removeSubscription(subscription: Subscription): SubscriptionServiceState[Unit] =
    StateT.modify(state => state.removeSubscription(subscription))
}

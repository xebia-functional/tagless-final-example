package io.tagless.service

import io.tagless.model.{Notification, Subscription, User, YouTubeChannel}

case class ServiceState(
    subscriptionTable: List[Subscription],
    notificationQueue: List[Notification],
    youTubeChannels: List[YouTubeChannel]) {
  def addSubscription(
      userId: User.UserId,
      channel: YouTubeChannel): (ServiceState, Subscription) = {
    val subscriptionId = this
      .subscriptionTable
      .lastOption
      .map(_.subscriptionId.inc)
      .getOrElse(Subscription.SubscriptionId.fromLong(1L))

    val subscription = Subscription(subscriptionId, userId, channel)

    (
      this.copy(
        subscriptionTable = this.subscriptionTable :+ subscription
      ),
      subscription
    )
  }

  def removeSubscription(subscription: Subscription): ServiceState =
    this.copy(
      subscriptionTable =
        this.subscriptionTable.filterNot(_.subscriptionId == subscription.subscriptionId)
    )

  def sendNotification(notification: Notification): ServiceState =
    this.copy(
      notificationQueue = this.notificationQueue :+ notification
    )

  def withSubscriptions(subscriptions: Subscription*): ServiceState =
    this.copy(subscriptionTable = subscriptions.toList)
}

object ServiceState:
  val empty: ServiceState = ServiceState(Nil, Nil, Nil)
  def withChannels(channels: YouTubeChannel*): ServiceState =
    ServiceState(Nil, Nil, channels.toList)

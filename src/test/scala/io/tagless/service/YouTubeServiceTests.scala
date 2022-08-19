package io.tagless.service

import cats.data.StateT
import io.tagless.algebras.SubscriptionPersistence
import io.tagless.interpreters.*
import io.tagless.model.Subscription.SubscriptionId
import io.tagless.model.{ChannelNotFound, Subscription, YouTubeChannel}
import io.tagless.model.User.UserId

class YouTubeServiceTests extends munit.CatsEffectSuite {
  val notificationClient = new NotificationClientImpl()
  val subscriptionPersistence = new SubscriptionPersistenceImpl()
  val youTubeAPIClient = new YouTubeAPIClientImpl()
  val service = new YouTubeService[SubscriptionServiceState](
    notificationClient,
    subscriptionPersistence,
    youTubeAPIClient)

  val fortySevenDegChannel: YouTubeChannel = YouTubeChannel("https://youtube.com/c/47deg")
  val userId: UserId = UserId.fromLong(1L)
  val subscriptionId: SubscriptionId = SubscriptionId.fromLong(1L)
  val existingSubscription: Subscription =
    Subscription(subscriptionId, userId, fortySevenDegChannel)

  test("A new subscription should generate a notification") {
    val state = service
      .subscribe(userId, fortySevenDegChannel)
      .runS(ServiceState.withChannels(fortySevenDegChannel))

    assertIOBoolean(
      state.map(s =>
        s.subscriptionTable.length == 1 &&
          s.subscriptionTable
            .exists(subs => subs.userId == userId && subs.channel == fortySevenDegChannel) &&
          s.notificationQueue.length == 1 &&
          s.notificationQueue.exists(_.channel == fortySevenDegChannel))
    )
  }

  test("A duplicated subscription shouldn't generate a notification") {
    val state = service
      .subscribe(userId, fortySevenDegChannel)
      .runS(
        ServiceState.withChannels(fortySevenDegChannel).withSubscriptions(existingSubscription))

    assertIOBoolean(
      state.map(s =>
        s.subscriptionTable.length == 1 &&
          s.subscriptionTable
            .exists(subs => subs.userId == userId && subs.channel == fortySevenDegChannel) &&
          s.notificationQueue.isEmpty)
    )
  }

  test("A new subscription should raise a ChannelNotFound error if the channel doesn't exist") {
    val result = service.subscribe(userId, fortySevenDegChannel).run(ServiceState.empty)
    interceptIO[ChannelNotFound](result)
  }
}

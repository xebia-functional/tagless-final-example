package io.tagless.service

import cats.{Monad, MonadError}
import cats.syntax.applicative.*
import cats.syntax.apply.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import io.tagless.algebras.NotificationClient
import io.tagless.algebras.SubscriptionPersistence
import io.tagless.algebras.YouTubeAPIClient
import io.tagless.model.{Notification, Subscription, User, YouTubeChannel}

class YouTubeService[F[_]: Monad](
    notificationClient: NotificationClient[F],
    subscriptionPersistence: SubscriptionPersistence[F],
    youTubeAPIClient: YouTubeAPIClient[F]) {

  private[this] def saveSubscription(
      userId: User.UserId,
      channel: YouTubeChannel,
      currentSubscriptions: List[Subscription]): F[Subscription] =
    currentSubscriptions
      .find(_.channel == channel)
      .fold(
        subscriptionPersistence
          .addSubscription(userId, channel)
          .flatMap(subscription =>
            notificationClient.send(Notification.Subscription(channel)).as(subscription))
      )(s => s.pure[F])

  def subscribe(userId: User.UserId, channel: YouTubeChannel): F[Subscription] = for {
    _ <- youTubeAPIClient.validateChannel(channel)
    currentSubscriptions <- subscriptionPersistence.getByUser(userId)
    subscription <- saveSubscription(userId, channel, currentSubscriptions)
  } yield subscription

}

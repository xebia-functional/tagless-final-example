package io.tagless.interpreters

import cats.data.StateT
import cats.effect.IO
import io.tagless.algebras.NotificationClient
import io.tagless.model.Notification
import io.tagless.service.{ServiceState, SubscriptionServiceState}

class NotificationClientImpl extends NotificationClient[SubscriptionServiceState] {
  override def send(notification: Notification): StateT[IO, ServiceState, Unit] =
    StateT.modify(state => state.sendNotification(notification))
}

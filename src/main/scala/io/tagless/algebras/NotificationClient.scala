package io.tagless.algebras

import io.tagless.model.Notification

trait NotificationClient[F[_]] {
  def send(notification: Notification): F[Unit]
}

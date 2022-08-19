package io.tagless.algebras

import io.tagless.model.YouTubeChannel

trait YouTubeAPIClient[F[_]] {
  def validateChannel(channel: YouTubeChannel): F[Unit]
}

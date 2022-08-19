package io.tagless.interpreters

import cats.data.StateT
import cats.effect.IO
import io.tagless.algebras.YouTubeAPIClient
import io.tagless.model.{ChannelNotFound, YouTubeChannel}
import io.tagless.service.{ServiceState, SubscriptionServiceState}

class YouTubeAPIClientImpl extends YouTubeAPIClient[SubscriptionServiceState] {
  def validateChannel(channel: YouTubeChannel): StateT[IO, ServiceState, Unit] =
    StateT.inspectF(state =>
      IO.raiseUnless(state.youTubeChannels.exists(_ == channel))(ChannelNotFound(channel)))
}

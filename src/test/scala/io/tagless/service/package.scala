package io.tagless

import cats.data.StateT
import cats.effect.IO

package object service {
  type SubscriptionServiceState[A] = StateT[IO, ServiceState, A]
}

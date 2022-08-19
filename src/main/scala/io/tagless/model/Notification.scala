package io.tagless.model

enum Notification(val channel: YouTubeChannel):
  case Subscription(c: YouTubeChannel) extends Notification(c)
  case Cancellation(c: YouTubeChannel) extends Notification(c)

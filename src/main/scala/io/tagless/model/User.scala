package io.tagless.model

import User.{UserId, UserName}

case class User(id: UserId, username: UserName)

object User:
  opaque type UserId = Long
  
  object UserId:
    def fromLong(value: Long): UserId = value

  opaque type UserName = String
    def fromString(value: String): UserName = value

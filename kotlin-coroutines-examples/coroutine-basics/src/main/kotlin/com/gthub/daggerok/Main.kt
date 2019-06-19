package com.gthub.daggerok

fun main(args: Array<String>) {
  val arguments = args.toMutableList().plus(3)
  val first = arguments.first()
  println("first: $first")
}

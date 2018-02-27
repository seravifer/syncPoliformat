package utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object IOExecutor: Executor by Executors.newFixedThreadPool(1)
object NetworkExecutor: Executor by Executors.newCachedThreadPool()
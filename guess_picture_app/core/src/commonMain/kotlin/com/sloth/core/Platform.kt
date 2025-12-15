package com.sloth.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
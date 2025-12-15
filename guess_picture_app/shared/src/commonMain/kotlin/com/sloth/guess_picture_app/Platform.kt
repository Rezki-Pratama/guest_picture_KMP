package com.sloth.guess_picture_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
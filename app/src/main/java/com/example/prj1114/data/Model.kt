package com.example.prj1114.data

interface Dto {
    fun asMap(): HashMap<String, Any?>
    fun docId(): String?
    fun chkNull(): Boolean
}

data class User(
    var userId: String? = null,
    var gender: String? = null,
    var email: String? = null,
    var nickname: String? = null
): Dto {
    override fun asMap(): HashMap<String, Any?> {
        return hashMapOf(
            "userId" to this.userId,
            "gender" to this.gender,
            "email" to this.email,
            "nickname" to this.nickname
        )
    }
    override fun docId(): String? {
        return this.userId
    }
    override fun chkNull(): Boolean {
        return (this.userId !== null) &&
                (this.gender !== null) &&
                (this.email !== null) &&
                (this.nickname !== null)
    }
}

data class UserToken(
    var userId: String? = null,
    var device: String? = null,
    var token: String? = null)
    : Dto {
    override fun asMap(): HashMap<String, Any?> {
        return hashMapOf(
            "userId" to this.userId,
            "device" to this.device,
            "token" to this.token
        )
    }
    override fun docId(): String {
        return userId + device
    }
    override fun chkNull(): Boolean {
        return (this.userId !== null) &&
                (this.device !== null) &&
                (this.token !== null)
    }
}

data class UserTeam(
    var userId: String? = null,
    var teamId: String? = null,
    var joinTime: Long? = null
): Dto {
    override fun asMap(): HashMap<String, Any?> {
        return hashMapOf(
            "userId" to this.userId,
            "teamId" to this.teamId,
            "joinTime" to this.joinTime
        )
    }
    override fun docId(): String {
        return userId + teamId
    }
    override fun chkNull(): Boolean {
        return (this.userId !== null) &&
                (this.teamId !== null) &&
                (this.joinTime !== null)
    }
}

data class Team(
    var teamId: String? = null,
    var time: Long? = null,
    var start: Juso? = null,
    var end: Juso? = null,
    var status: Int? = null,
    var max: Int? = null,
    var curr: Int? = null)
    : Dto {
    override fun asMap(): HashMap<String, Any?> {
        return hashMapOf(
            "teamId" to this.teamId,
            "time" to this.time,
            "start" to this.start,
            "end" to this.end,
            "status" to this.status,
            "max" to this.max,
            "curr" to this.curr
        )
    }
    override fun docId(): String? {
        return null
    }
    override fun chkNull(): Boolean {
        return (this.time !== null) &&
                (this.start !== null) &&
                (this.end !== null) &&
                (this.status !== null) &&
                (this.max !== null) &&
                (this.curr !== null)
    }
}

data class Chat(
    var chatId: String? = null,
    var userId: String? = null,
    var teamId: String? = null,
    var message: String? = null,
    val time: Long? = null
): Dto {
    override fun asMap(): HashMap<String, Any?> {
        return hashMapOf(
            "chatId" to this.chatId,
            "userId" to this.userId,
            "teamId" to this.teamId,
            "message" to this.message,
            "time" to this.time,
        )
    }
    override fun docId(): String? {
        return null
    }
    override fun chkNull(): Boolean {
        return (this.userId !== null) &&
                (this.teamId !== null) &&
                (this.message !== null) &&
                (this.time !== null)
    }
}
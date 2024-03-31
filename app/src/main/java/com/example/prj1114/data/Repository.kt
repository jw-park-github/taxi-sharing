package com.example.prj1114.data

import android.util.Log
import com.example.prj1114.common.CurrentInfo
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import org.checkerframework.checker.units.qual.Current
import java.util.Calendar

interface RepositoryInterface {
    val myFirestore: MyFirestore
    /** 각 데이터 생성 */
    suspend fun createUser(user: User): String?
    suspend fun createTeam(team: Team): String?
    suspend fun createChat(chat: Chat): String?
    suspend fun createUserTeam(userId: String, teamId: String): String?
    suspend fun createUserToken(userId: String, device: String, token: String): String?

    /** 각 데이터 get */
    suspend fun getUser(userId: String): User?
    suspend fun getTeam(teamId: String): Team?
    suspend fun getUserTeam(userId: String, teamId: String): UserTeam?
    suspend fun getUserToken(userId: String, device: String): UserToken?

    /** team 검색 */
    suspend fun searchTeam(
        time: Long
    ): ArrayList<Team>?

    /** 팀의 모든 채팅 get */
    suspend fun getAllTeamChat(
        teamId: String,
        time: Long
    ): ArrayList<Chat>?

    /** 팀의 모든 채팅 listen */
    suspend fun listenAllTeamChat(
        teamId: String
    ): Flow<QuerySnapshot>?

    /** 유저의 모든 team get */
    suspend fun getAllUserTeam(
        userId: String
    ): ArrayList<Team>?

    /** 팀의 모든 유저 get */
    suspend fun getAllTeamUser(
        teamId: String
    ): ArrayList<User>?

    /** 유저의 모든 디바이스 토큰 */
    suspend fun getAllUserToken(
        userId: String
    ): ArrayList<UserToken>?

    /** update */
    suspend fun updateUser(
        userId: String,
        data: Pair<String, Any>
    ): String?
    suspend fun updateTeam(
        teamId: String,
        data: Pair<String, Any>
    ): String?
    suspend fun updateUserToken(
        userId: String,
        device: String,
        token: String
    ): String?
    /** do not update chat and userTeam */

    /** delete **/
    suspend fun deleteUser(
        userId: String
    ): Boolean?
    suspend fun deleteTeam(
        teamId: String
    ): Boolean?
    suspend fun deleteUserToken(
        userId: String,
        device: String
    ): Boolean?
    suspend fun deleteUserTeam(
        userId: String,
        teamId: String
    ): Boolean?
    /** do not delete single chat */

    /** delete all */
    suspend fun deleteAllTeamUser(
        teamId: String
    ): Boolean?
    suspend fun deleteAllUserTeam(
        userId: String
    ): Boolean?
    suspend fun deleteAllUserToken(
        userId: String
    ): Boolean?
    suspend fun deleteAllTeamChat(
        teamId: String
    ): Boolean?

    /** chk */
    suspend fun chkUserByField(
        data: Pair<String, Any>
    ): Boolean

    suspend fun chkUserById(
        userId: String
    ): Boolean
}

class Repository: RepositoryInterface {
    override val myFirestore = MyFirestore()
    private val tag = "Prj1114"

    override suspend fun createUser(user: User): String? {
        return myFirestore.create("User", user)
    }

    override suspend fun createUserToken(userId: String, device: String, token: String): String? {
        if (myFirestore.chkUnique("User", userId)) {
            Log.w(tag, "존재하지 않는 유저")
            return null
        }
        return myFirestore.create("UserToken", UserToken(userId, device, token))
    }

    override suspend fun createUserTeam(userId: String, teamId: String): String? {
        if(myFirestore.chkUnique("Team", teamId)){
            Log.w(CurrentInfo.TAG, "존재하지 않는 팀")
            return null
        }
        if(myFirestore.chkUnique("User", userId)){
            Log.w(CurrentInfo.TAG, "존재하지 않는 유저")
            return null
        }
        Log.d(CurrentInfo.TAG,"${myFirestore.get("Team", teamId)}")
        val isAvailable = myFirestore.get("Team", teamId)?.toObject<Team>()?.let {
            (it.max!! - it.curr!!) > 0
        }
        Log.d(CurrentInfo.TAG,"Repo:4 $isAvailable")

        return when(isAvailable) {
            true -> myFirestore.create(
                "UserTeam",
                UserTeam(userId, teamId, Calendar.getInstance().timeInMillis))
            false -> {
                Log.w(tag, "정원 초과")
                "MAX ERROR"
            }
            null -> null
        }
    }

    override suspend fun createTeam(team: Team): String? {
        return myFirestore.add("Team", team)?.let {
            myFirestore.update("Team", it, Pair("teamId", it) )
        }
    }

    override suspend fun createChat(chat: Chat): String? {
        return myFirestore.add("Chat", chat)
    }

    override suspend fun getUser(userId: String): User? {
        return myFirestore.get("User", userId)?.toObject<User>()
    }

    override suspend fun getUserToken(userId: String, device: String): UserToken? {
        return myFirestore.get("UserToken", userId+device)?.toObject<UserToken>()
    }

    override suspend fun getUserTeam(userId: String, teamId: String): UserTeam? {
        return myFirestore.get("UserTeam", userId+teamId)?.toObject<UserTeam>()
    }

    override suspend fun getTeam(teamId: String): Team? {
        return myFirestore.get("Team", teamId)?.toObject<Team>()
    }

    override suspend fun searchTeam(time: Long): ArrayList<Team>? {
        val queries = arrayOf(Where(WhereType.GREATER_THAN_EQUAL, "time", time))
        return myFirestore.get("Team", queries, null)?.toListObject()
    }

    override suspend fun getAllTeamChat(teamId: String, time: Long): ArrayList<Chat>? {
        val queries = arrayOf(
            Where(WhereType.EQUAL, "teamId", teamId),
            Where(WhereType.GREATER_THAN_EQUAL,"time",time)
        )
        val sorts = arrayOf(OrderBy("time", true))
        return myFirestore.get("Chat", queries, sorts)?.toListObject()
    }

    override suspend fun getAllUserTeam(userId: String): ArrayList<Team>? {
        val result: ArrayList<Team> = arrayListOf()
        val queries = arrayOf(Where(WhereType.EQUAL, "userId", userId))

        myFirestore.get("UserTeam", queries, null)
            ?.toListObject<UserTeam>()
            ?.forEach { userTeam ->
                userTeam.teamId?.let { team ->
                    getTeam(team)?.let {
                        result.add(it)
                    }
                }
            }

        return if(result.isEmpty()) null
        else result
    }

    override suspend fun getAllTeamUser(teamId: String): ArrayList<User>? {
        val result: ArrayList<User> = arrayListOf()
        val queries = arrayOf(Where(WhereType.EQUAL, "teamId", teamId))

        myFirestore.get("UserTeam", queries, null)
            ?.toListObject<UserTeam>()
            ?.forEach { userTeam ->
                userTeam.userId?.let { user ->
                    getUser(user)?.let {
                        result.add(it)
                    }
                }
            }

        return if(result.isEmpty()) null
        else result
    }

    override suspend fun getAllUserToken(userId: String): ArrayList<UserToken>? {
        val queries = arrayOf(Where(WhereType.EQUAL, "userId", userId))
        return myFirestore.get("UserToken", queries, null)?.toListObject()
    }


    override suspend fun listenAllTeamChat(teamId: String): Flow<QuerySnapshot> {
        val queries = arrayOf(Where(WhereType.EQUAL, "teamId", teamId))
        val sorts = arrayOf(OrderBy("time", true))
        return myFirestore.listen("Chat", queries, sorts)
    }

    override suspend fun updateUser(userId: String, data: Pair<String, Any>): String? {
        return myFirestore.update("User", userId, data)
    }

    override suspend fun updateTeam(teamId: String, data: Pair<String, Any>): String? {
        return myFirestore.update("Team", teamId, data)
    }

    override suspend fun updateUserToken(userId: String, device: String, token: String): String? {
        return myFirestore.update("UserToken", userId+device, Pair("token", token))
    }

    override suspend fun deleteUser(userId: String): Boolean? {
        return deleteAllUserToken(userId)?.let {
            deleteAllUserTeam(userId)?.let {
                myFirestore.delete("User", userId)
            }
        }
    }

    override suspend fun deleteTeam(teamId: String): Boolean? {
        return deleteAllTeamUser(teamId)?.let {
            deleteAllTeamChat(teamId)?.let {
                myFirestore.delete("Team", teamId)
            }
        }
    }

    override suspend fun deleteUserToken(userId: String, device: String): Boolean? {
        return myFirestore.delete("UserToken", userId+device)
    }

    override suspend fun deleteUserTeam(userId: String, teamId: String): Boolean? {
        return myFirestore.delete("UserTeam", userId+teamId)
    }

    override suspend fun deleteAllTeamUser(teamId: String): Boolean? {
        val query = arrayOf(Where(WhereType.EQUAL, "teamId", teamId))
        return myFirestore.delete("UserTeam", query, null)
    }

    override suspend fun deleteAllUserTeam(userId: String): Boolean? {
        val query = arrayOf(Where(WhereType.EQUAL, "userId", userId))
        return myFirestore.delete("UserTeam", query, null)
    }

    override suspend fun deleteAllUserToken(userId: String): Boolean? {
        val query = arrayOf(Where(WhereType.EQUAL, "userId", userId))
        return myFirestore.delete("UserToken", query, null)
    }

    override suspend fun deleteAllTeamChat(teamId: String): Boolean? {
        val query = arrayOf(Where(WhereType.EQUAL, "teamId", teamId))
        return myFirestore.delete("Chat", query, null)
    }

    /** unique 한 경우 true 반환 */
    override suspend fun chkUserByField(data: Pair<String, Any>): Boolean {
        val queries = arrayOf(Where(WhereType.EQUAL, data.first, data.second))
        return myFirestore.chkUnique("User", queries, null)
    }

    /** unique 한 경우 true 반환 */
    override suspend fun chkUserById(userId: String):Boolean {
        return myFirestore.chkUnique("User", userId)
    }

    private inline fun <reified T> QuerySnapshot.toListObject(): ArrayList<T> {
        val rtn = ArrayList<T>()
        if(!this.isEmpty){
            this.forEach { doc ->
                doc.toObject<T>()?.let {
                    rtn.add(it)
                }
            }
        } else {
            Log.w(tag, "querySnapshot is empty")
        }
        return rtn
    }

    private inline fun <reified T> MutableList<DocumentChange>.toListObject(): ArrayList<T> {
        val rtn = ArrayList<T>()
        if(this.isNotEmpty()){
            this.forEach { doc ->
                doc.document.toObject<T>()?.let {
                    rtn.add(it)
                }
            }
        } else {
            Log.w(tag, "querySnapshot is empty")
        }
        return rtn
    }
}
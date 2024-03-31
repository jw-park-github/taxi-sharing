package com.example.prj1114.data

import android.util.Log
import com.example.prj1114.common.CurrentInfo
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface MyFirestoreInterface {
    val db: FirebaseFirestore

    /** 전달된 id로 추가 */
    suspend fun create(collection: String, dto: Dto): String?

    /** firestore 자동 생성된 id로 추가 */
    suspend fun add(collection: String, dto: Dto): String?

    /** get single document */
    suspend fun get(
        collection: String,
        documentId: String
    ): DocumentSnapshot?

    /** get queried documents */
    suspend fun get(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): QuerySnapshot?

    /** listen queried documents */
    suspend fun listen(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): Flow<QuerySnapshot>?

    /** update single document, single field */
    suspend fun update(
        collection: String,
        documentId: String,
        data: Pair<String, Any>
    ): String?

    /** delete single document */
    suspend fun delete(
        collection: String,
        documentId: String
    ): Boolean?

    /** delete all queried documents */
    suspend fun delete(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): Boolean?

    /** chk if specific document exist */
    suspend fun chkUnique(
        collection: String,
        documentId: String
    ): Boolean?

    /** chk if queried data exist */
    suspend fun chkUnique(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): Boolean?
}

class MyFirestore: MyFirestoreInterface {
    override var db = Firebase.firestore
    private val tag = "Prj1114"

    override suspend fun create (collection: String, dto: Dto): String? {
        if(!dto.chkNull()) return null
        if(dto.docId() == null) {
            Log.w(tag, "doc id is null")
            return null
        }
        return suspendCoroutine { continuation ->
            db.collection(collection)
                .document(dto.docId().toString())
                .set(dto.asMap())
                .addOnSuccessListener {
                    continuation.resume(dto.docId()!!)
                }
                .addOnFailureListener {
                    Log.w(tag, "MyFirestoreImpl create", it)
                    continuation.resume(null)
                }
        }
    }

    override suspend fun add(collection: String, dto: Dto): String? {
        if(!dto.chkNull()) return null
        if(dto.docId() != null) return null

        return suspendCoroutine { continuation ->
            db.collection(collection)
                .add(dto.asMap())
                .addOnSuccessListener { document ->
                    continuation.resume(document.id)
                }
                .addOnFailureListener {
                    Log.w(tag, "MyFirestoreImpl add", it)
                    continuation.resume(null)
                }
        }
    }

    override suspend fun get (
        collection: String,
        documentId: String
    ): DocumentSnapshot? {

        return suspendCoroutine { continuation ->
            db.collection(collection)
                .document(documentId)
                .get()
                .addOnSuccessListener { document ->
                    continuation.resume(document)
                }
                .addOnFailureListener {
                    Log.w(tag, "MyFirestoreImpl get", it)
                    continuation.resume(null)
                }
        }
    }

    override suspend fun get(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): QuerySnapshot? {
        return suspendCoroutine { continuation ->
            var colRef: Query = db.collection(collection)

            queries?.forEach {
                colRef = setQuery(colRef, it)
            }

            sorts?.forEach {
                colRef = setQuery(colRef, it)
            }

            colRef.get()
                .addOnSuccessListener { documents ->
                    continuation.resume(documents)
                }
                .addOnFailureListener {
                    Log.w(tag, "MyFirestoreImpl get", it)
                    continuation.resume(null)
                }
        }
    }

    override suspend fun listen(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): Flow<QuerySnapshot> = callbackFlow {
        var colRef: Query = db.collection(collection)

        queries?.forEach {
            colRef = setQuery(colRef, it)
        }

        sorts?.forEach {
            colRef = setQuery(colRef, it)
        }

        val chat = colRef.addSnapshotListener{ documents, e ->
            if(e != null) {
                Log.w(tag, "error fetching collection")
                return@addSnapshotListener
            }
            if(documents!!.metadata.isFromCache) return@addSnapshotListener
            trySend(documents)
        }

        awaitClose { chat.remove() }
    }

    override suspend fun update(
        collection: String,
        documentId: String,
        data: Pair<String, Any>
    ): String? {
        return suspendCoroutine { continuation ->
            db.collection(collection)
                .document(documentId)
                .update(data.first, data.second)
                .addOnSuccessListener {
                    continuation.resume(documentId)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    override suspend fun delete(
        collection: String,
        documentId: String
    ): Boolean? {

        return suspendCoroutine { continuation ->
            db.collection(collection)
                .document(documentId)
                .delete()
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    /***
     * 삭제할 쿼리 데이터가 없으면 false,
     * 삭제 도중 오류 발생 시 null
     * 삭제 완료되면 true 반환
     */
    override suspend fun delete(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): Boolean? {
        val documents: QuerySnapshot = get(collection, queries, sorts) ?: return false

        for (doc in documents) {
            if(delete(collection, doc.id) == null) return null
        }

        return true
    }

    override suspend fun chkUnique(
        collection: String,
        documentId: String
    ): Boolean {
        Log.d("APPLE", get(collection, documentId)!!.data.toString())
        Log.d("APPLE", get(collection, documentId)!!.metadata.toString())
        return (get(collection, documentId)!!.data == null)
//        return get(collection, documentId)!!.metadata.isFromCache.not()
    }

    override suspend fun chkUnique(
        collection: String,
        queries: Array<Where>?,
        sorts: Array<OrderBy>?
    ): Boolean {
        Log.d("APPLE", get(collection, queries, sorts)!!.documents.toString())
        Log.d("APPLE", get(collection, queries, sorts)!!.isEmpty.not().toString())
        return get(collection, queries, sorts)!!.isEmpty

//        return get(collection, queries, sorts)!!.metadata.isFromCache
    }

    private fun setQuery(
        colRef: Query,
        query: Where
    ): Query {
        return when(query.type) {
            WhereType.EQUAL -> colRef.whereEqualTo(query.field, query.value)
            WhereType.NOT_EQUAL -> colRef.whereNotEqualTo(query.field, query.value)
            WhereType.LESS_THAN -> colRef.whereLessThan(query.field, query.value)
            WhereType.LESS_THAN_EQUAL -> colRef.whereLessThanOrEqualTo(query.field, query.value)
            WhereType.GREATER_THAN -> colRef.whereGreaterThan(query.field, query.value)
            WhereType.GREATER_THAN_EQUAL -> colRef.whereGreaterThanOrEqualTo(query.field, query.value)
            WhereType.ARRAY_CONTAINS -> colRef.whereArrayContains(query.field, query.value)
        }
    }

    private fun setQuery(
        colRef: Query,
        query: OrderBy
    )
    : Query {
        return when(query.isAsc) {
            true -> colRef.orderBy(query.field)
            false -> colRef.orderBy(query.field, Query.Direction.DESCENDING)
        }
    }
}

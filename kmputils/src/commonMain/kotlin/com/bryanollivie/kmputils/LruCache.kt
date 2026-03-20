package com.bryanollivie.kmputils

class LruCache<K, V>(private val maxSize: Int) {
    private val keys = mutableListOf<K>()
    private val map = mutableMapOf<K, V>()

    val size: Int get() = map.size

    fun get(key: K): V? {
        val value = map[key] ?: return null
        keys.remove(key)
        keys.add(key)
        return value
    }

    fun put(key: K, value: V): V? {
        val previous = map[key]
        if (previous != null) {
            keys.remove(key)
        }
        keys.add(key)
        map[key] = value
        trimToSize()
        return previous
    }

    fun remove(key: K): V? {
        keys.remove(key)
        return map.remove(key)
    }

    fun clear() {
        keys.clear()
        map.clear()
    }

    fun containsKey(key: K): Boolean = map.containsKey(key)

    fun keys(): Set<K> = keys.toSet()

    fun values(): List<V> = keys.mapNotNull { map[it] }

    fun snapshot(): Map<K, V> = keys.filter { map.containsKey(it) }.associateWith { map[it]!! }

    fun getOrPut(key: K, defaultValue: () -> V): V {
        val existing = get(key)
        if (existing != null) return existing
        val value = defaultValue()
        put(key, value)
        return value
    }

    private fun trimToSize() {
        while (keys.size > maxSize) {
            val eldest = keys.removeFirst()
            map.remove(eldest)
        }
    }
}


package com.manager.filemanager.manager.adapter

import java.util.*

class ManagerUtil {

    private var pathStack = Stack<String>()

    private val basePath = "/storage/emulated/0"




    fun getPreviousPath(): String {
        if (!pathStack.isEmpty()) {
            if (pathStack.size == 1) {
                // Caso especial: voltando para o caminho base
                pathStack.pop()
                return basePath
            } else {
                // Obtém o caminho anterior
                pathStack.pop() // Remove o caminho atual da pilha
                val previousPath = pathStack.peek()
                return previousPath
            }
        } else {
            return basePath
        }
    }
    fun addToPathStack(path: String) {
        pathStack.push(path)
    }

    companion object {
        @Volatile
        private var instance: ManagerUtil? = null

        fun getInstance(): ManagerUtil {
            return instance ?: synchronized(this) {
                instance ?: ManagerUtil().also { instance = it }
            }
        }
    }
}
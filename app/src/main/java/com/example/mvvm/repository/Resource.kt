package com.example.mvvm.repository

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
        fun <T> error(msg: String, data: T): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        /**
         * Devuelve un objeto Resource con el estado LOADING.
         * @param data Datos pertenecientes de la base de datos local ya que al realizar cualquier
         * petición primero intentamos cargar los datos más recientes de nuestra base de datos. Este
         * parámetro puedo ser nulo si ningún datos perteneciente a la petición es encontrado en nuestra
         * base de datos local.
         */
        fun <T>loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

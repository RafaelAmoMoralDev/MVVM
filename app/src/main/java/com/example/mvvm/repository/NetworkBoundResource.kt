package com.example.mvvm.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mvvm.AppExecutor
import com.example.mvvm.api.ApiSuccessResponse

abstract class NetworkBoundResource<ResultType, RequesType>
@MainThread
constructor(private val appExecutors: AppExecutor) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchDataFromNetwork(dbSource)
            } else {
                result.addSource(dbSource){newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchDataFromNetwork(dbSource: LiveData<ResultType>): Boolean {
        val apiResponse = createCall()
        /*En primer lugar añadimos los datos de la base de datos local al objeto mediator ya que
        estos datos se cargarán antes y en caso de que la llamada al web service falle, al menos, mostraremos
        los datos cargados más recientemente en nuestra base de datos local*/
        result.addSource(dbSource){newData->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse){response->
           result.removeSource(apiResponse)
           result.removeSource(dbSource)
            when(response){
//                is ApiSuccessResponse -> {
//                    appExecutors.diskIO().execute{
//                        saveCallResult(processResponse(response))
//                    }
//                }
            }
        }
    }

    protected open fun onFetchFailed(){}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequesType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequesType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    //TODO: ¿Es necesario anotar como main thread cuando la clase principal es main thread?
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract  fun createCall(): LiveData<RequesType>

}


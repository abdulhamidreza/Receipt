package com.valiance.receipt.room

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReceiptViewModel(private val receiptRepository: ReceiptRepository) : ViewModel() {
    val formatterDate: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a")


    val errorMessage = MutableLiveData<String>()
    val loadingStatus = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private var job: Job? = null


    fun updateReceipt(
        title: String, description: String
    ) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            withContext(Dispatchers.Main) {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    loadingStatus.postValue(true)
                    viewModelScope.launch(Dispatchers.IO) {
                        receiptRepository.updateReceipt(
                            Receipt(
                                formatterDate.format(LocalDateTime.now()),
                                1.1,
                                1,
                                3.1,
                                2,
                                2.1,
                                1,
                                ""
                            )
                        )

                        onError("Receipt saved")
                    }
                } else {
                    if (title.isEmpty()) {
                        onError("Title can not blank")
                    } else
                        if (description.isEmpty()) {
                            onError("Description can not blank")
                        }
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loadingStatus.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


    val allReceiptRealTime: LiveData<MutableList<Receipt>> = receiptRepository.allReceipts.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insertReceipt(user: Receipt) = viewModelScope.launch {
        receiptRepository.insertReceipt(user)
    }


    class ReceiptViewModelFactory(private val receiptRepository: ReceiptRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReceiptViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReceiptViewModel(receiptRepository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }

    }
}

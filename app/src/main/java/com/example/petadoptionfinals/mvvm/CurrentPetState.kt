package com.example.petadoptionfinals.mvvm

import com.example.petadoptionfinals.model.petModel

sealed class CurrentPetState {

    data class Success(val studentsList: ArrayList <petModel>?) : CurrentPetState()

    object Error : CurrentPetState()

    object Loading : CurrentPetState()

}

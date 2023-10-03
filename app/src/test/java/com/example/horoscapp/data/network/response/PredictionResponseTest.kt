package com.example.horoscapp.data.network.response

import com.example.horoscapp.domain.model.PredictionModel
import io.kotlintest.shouldBe
import org.junit.Assert.*
import org.junit.Test

class PredictionResponseTest{
    @Test
    fun `toDomain should return correct PredictionModel`(){
      // Given
        val horoscopeResponse = PredictionResponse("date", "prediction","taurus" )
      // When
       val predictionModel:PredictionModel = horoscopeResponse.toDomain()
      // Then
        predictionModel.sign shouldBe  horoscopeResponse.sign
        predictionModel.horoscope shouldBe  horoscopeResponse.horoscope
    }
}
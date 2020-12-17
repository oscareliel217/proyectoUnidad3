/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentJuegoBinding

class JuegoFragment : Fragment() {
    data class Pregunta(
            val texto: String,
            val respuestas: List<String>)

    // Lista con todas las preguntas
    private val preguntas: MutableList<Pregunta> = mutableListOf(
            Pregunta(texto = "¿Que es Android Jetpack?",
                    respuestas = listOf("Todas las opciones", "tools", "documentation", "libraries")),
            Pregunta(texto = "¿Clase base para una Layout?",
                    respuestas = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")),
            Pregunta(texto = "¿Layout para diseños complejos",
                    respuestas = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")),
            Pregunta(texto = "¿Pasando datos estructurados en una Layout?",
                    respuestas = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick")),
            Pregunta(texto = "¿Inflar layout en fragments?",
                    respuestas = listOf("onCreateView", "onViewCreated", "onCreateLayout", "onInflateLayout")),
            Pregunta(texto = "¿Build system para Android?",
                    respuestas = listOf("Gradle", "Graddle", "Grodle", "Groyle")),
            Pregunta(texto = "¿Formato de vectores en Android?",
                    respuestas = listOf("VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector")),
            Pregunta(texto = "¿Componente de navegacion en Android?",
                    respuestas = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")),
            Pregunta(texto = "¿Registra la aplicacion con el launcher?",
                    respuestas = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")),
            Pregunta(texto = "Marcar una layour para Data Binding?",
                    respuestas = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>"))
    )

    lateinit var preguntaActual: Pregunta
    lateinit var respuestas: MutableList<String>
    private var preguntasPosicion = 0
    private val numeroPreguntas = Math.min((preguntas.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Infla la layout de este fragment
        val binding = DataBindingUtil.inflate<FragmentJuegoBinding>(inflater, R.layout.fragment_juego, container, false)

        // Crea aleatoriamente las preguntas
        crearPreguntasAleatorias()

        // Bind this fragment class to the layout
        binding.juego = this


        binding.responderButton.setOnClickListener { vista: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (respuestas[answerIndex] == preguntaActual.respuestas[0]) {
                    preguntasPosicion++
                    // Advance to the next question
                    if (preguntasPosicion < numeroPreguntas) {
                        preguntaActual = preguntas[preguntasPosicion]
                        establecerPregunta()
                        binding.invalidateAll()
                    } else {
                        // Ganamos!  Navigate al JuegoGanadoFragment.
                        vista.findNavController().navigate(JuegoFragmentDirections.actionJuegoFragmentToJuegoGanadoFragment(
                                preguntasPosicion,numeroPreguntas))
                    }
                } else {
                    // Perdiste! Navigate al JuegoPerdidoFragment.
                    vista.findNavController().navigate(JuegoFragmentDirections.actionJuegoFragmentToJuegoPerdidoFragment())

                }
            }
        }
        return binding.root
    }

    // Establece las preguntas aleatorias y establece la primer pregunta.
    private fun crearPreguntasAleatorias() {
        preguntas.shuffle()
        preguntasPosicion = 0
        establecerPregunta()
    }

    // Establece las preguntas y respuestas aleatoriamente..
    // Al llamar invalidateAll en el  FragmentJuegoBinding actualiza la informacion.
    private fun establecerPregunta() {

        preguntaActual = preguntas[preguntasPosicion]
        // randomize the answers into a copy of the array
        respuestas = preguntaActual.respuestas.toMutableList()
        // and shuffle them
        respuestas.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.titulo_android_trivia_question, preguntasPosicion + 1, numeroPreguntas)
    }
}

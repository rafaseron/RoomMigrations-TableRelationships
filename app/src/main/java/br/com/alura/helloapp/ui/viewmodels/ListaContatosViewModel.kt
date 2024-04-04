package br.com.alura.helloapp.ui.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.helloapp.localData.preferences.PreferencesKey
import br.com.alura.helloapp.localData.room.entity.Contato
import br.com.alura.helloapp.localData.room.repository.ContatoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListaContatosUiState(
    val contatos: List<Contato> = emptyList(),
    val usuarioAtual: String? = null
)
@HiltViewModel
class ListaContatosViewModel @Inject constructor(private val contatoRepository: ContatoRepository, private val dataStore: DataStore<Preferences>): ViewModel() {

    private val _uiState = MutableStateFlow(ListaContatosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        buscaContatos()
    }

    fun buscaContatos() {

        viewModelScope.launch {
            val preferences = dataStore.data.first()
            val usuarioAtual = preferences[PreferencesKey.AUTHENTICATED_USER]
            usuarioAtual?.let {
               viewModelScope.launch {
                   val contactListFromAtualUsername = contatoRepository.getContactsFromUsername(it)
                   _uiState.value = _uiState.value.copy(contatos = contactListFromAtualUsername.first())
               }
            }
        }
    }

    suspend fun desloga() {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.LOGADO] = false
            preferences[PreferencesKey.AUTHENTICATED_USER] = "Guest"
        }
    }
}
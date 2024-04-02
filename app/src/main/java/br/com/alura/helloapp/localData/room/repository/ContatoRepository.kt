package br.com.alura.helloapp.localData.room.repository

import br.com.alura.helloapp.localData.room.dao.ContatoDao
import br.com.alura.helloapp.localData.room.entity.Contato
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ContatoRepository @Inject constructor(val contatoDao: ContatoDao){

    suspend fun insertContato(contato: Contato){
        return contatoDao.insert(contato)
    }

    private suspend fun deleteContato(contato: Contato){
        return contatoDao.delete(contato)
    }

    private suspend fun atualizarContato(contato: Contato){
        return contatoDao.update(contato)
    }

    suspend fun getAllContacts(): Flow<List<Contato>> {
        return contatoDao.buscaTodos()
    }

    suspend fun searchContactFromId(id: Long): Flow<Contato?>{
        return contatoDao.buscaPorId(id)
    }

    suspend fun deleteFromId(id: Long){
        val contatoPesquisado = searchContactFromId(id)
        val contatoSemFlow = contatoPesquisado.first()
        contatoSemFlow?.let {
            deleteContato(it)
        }
    }

    suspend fun updateContact(contato: Contato){
        val contatoPesquisado = searchContactFromId(contato.id)
        val contatoSemFlow = contatoPesquisado.first()
        contatoSemFlow?.let {
            atualizarContato(it)
        }
    }

}
package com.example.cmpt362project.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cmpt362project.models.Board
import com.example.cmpt362project.models.BoardUpdateData
import com.example.cmpt362project.repositories.BoardsRepository

class BoardListViewModel: ViewModel(){
    private val repository = BoardsRepository()
    val boardPic = MutableLiveData<Bitmap>()
    private var imageSet = false

    private val _boardsLiveData = MutableLiveData<List<Board>>()
    val boardsLiveData: LiveData<List<Board>> = _boardsLiveData

    fun fetchBoards(groupID:String){
        repository.fetchBoards(_boardsLiveData, groupID)
    }

    fun insert(board: Board){
        repository.insert(board)
    }

    fun delete(boardID:String, boardName:String, groupID: String){
        repository.delete(boardID, boardName, groupID)
    }

    fun update(boardID: String, boardUpdateData: BoardUpdateData){
        repository.update(boardID, boardUpdateData)
    }

    fun getImage() : Bitmap? {
        return if(imageSet) {
            boardPic.value
        } else {
            null
        }
    }

    fun setImage(bitmap: Bitmap){
        boardPic.value = bitmap
        imageSet = true
    }
}
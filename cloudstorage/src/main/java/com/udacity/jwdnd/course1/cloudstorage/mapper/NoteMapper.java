package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("insert into notes(notetitle, notedescription, userid) " +
            "values(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("update notes set notetitle=#{noteTitle}, notedescription=#{noteDescription}" +
            "where noteid = #{noteId} and userid = #{userId}")
    int updateNote(Note note);

    @Select("select * from notes where userid = #{userId}")
    List<Note> getNotes(int userId);

    @Delete("delete from notes where noteid = #{noteId} and userid = #{userId}")
    int deleteNote(int userId, int noteId);

    @Select("select * from notes where noteid = #{noteId} and userid = #{userId}")
    Note getNote(int userId, int noteId);

    @Select("select * from notes where userid = #{userId} and notetitle = #{noteTitle}")
    List<Note> getNotesWithTitle(int userId, String noteTitle);
}

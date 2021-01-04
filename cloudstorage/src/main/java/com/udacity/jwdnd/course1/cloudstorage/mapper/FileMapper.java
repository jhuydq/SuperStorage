package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("insert into files(filename, contenttype, filesize, userid, filedata) " +
            "values(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);


    @Select("select * from files where userid = #{userId}")
    List<File> getFiles(int userId);

    @Select("select * from files where userid = #{userId} and filename = #{fileName}")
    List<File> getFilesWithName(int userId, String fileName);

    @Delete("delete from files where fileid = #{fileId} and userid = #{userId}")
    int deleteFile(int userId, int fileId);

    @Select("select * from files where fileid = #{fileId} and userid = #{userId}")
    File getFile(int userId, int fileId);
}

package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Insert("insert into CREDENTIALS(url, username, key, password, userid) " +
            "values(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);

    @Update("update CREDENTIALS set url=#{url}, username=#{username}, key=#{key}, password=#{password}" +
            "where credentialid = #{credentialId} and userid = #{userId}")
    int updateCredential(Credential credential);

    @Select("select * from CREDENTIALS where userid = #{userId}")
    List<Credential> getCredentials(int userId);

    @Delete("delete from CREDENTIALS where credentialid = #{credentialId} and userid = #{userId}")
    int deleteCredential(int userId, int credentialId);
}

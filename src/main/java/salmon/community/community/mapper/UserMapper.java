package salmon.community.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import salmon.community.community.model.User;

/**
 * @author SalmonC
 * @date 2021-08-29 16:37
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (name ,account_id,token,gmt_create,gmt_modified) values (#{name} ,#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insertUser(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);
}

package salmon.community.community.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import salmon.community.community.model.User;

/**
 * @author SalmonC
 * @date 2021-08-29 16:37
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (name ,account_id,token,gmt_create,gmt_modified) values (#{name} ,#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insertUser(User user);

}

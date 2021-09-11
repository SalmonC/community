package salmon.community.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import salmon.community.model.Soup;
import salmon.community.model.SoupExample;

public interface SoupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    long countByExample(SoupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int deleteByExample(SoupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int insert(Soup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int insertSelective(Soup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    List<Soup> selectByExampleWithRowbounds(SoupExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    List<Soup> selectByExample(SoupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    Soup selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int updateByExampleSelective(@Param("record") Soup record, @Param("example") SoupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int updateByExample(@Param("record") Soup record, @Param("example") SoupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int updateByPrimaryKeySelective(Soup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SOUP
     *
     * @mbg.generated Fri Sep 10 16:07:34 CST 2021
     */
    int updateByPrimaryKey(Soup record);
}
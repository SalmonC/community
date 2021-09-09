package salmon.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import salmon.community.dto.QuestionDTO;
import salmon.community.dto.QuestionQueryDTO;
import salmon.community.model.Question;
import salmon.community.model.QuestionExample;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearchWithBLOBs(QuestionQueryDTO questionQueryDTO);

    List<QuestionDTO> selectHot();
}
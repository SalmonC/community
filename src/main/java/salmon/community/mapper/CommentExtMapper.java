package salmon.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import salmon.community.model.Comment;
import salmon.community.model.CommentExample;

import java.util.List;

public interface CommentExtMapper {

    int incCommentCount(Comment comment);
}
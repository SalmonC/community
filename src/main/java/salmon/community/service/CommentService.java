package salmon.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salmon.community.dto.CommentCreateDTO;
import salmon.community.dto.CommentDTO;
import salmon.community.enums.CommentTypeEnum;
import salmon.community.enums.NotificationStatusEnum;
import salmon.community.enums.NotificationTypeEnum;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.exception.CustomizeException;
import salmon.community.mapper.*;
import salmon.community.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SalmonC
 * @date 2021-09-04 14:30
 */
@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentExtMapper commentExtMapper;

    @Autowired
    NotificationMapper notificationMapper;

    @Transactional(rollbackFor = Exception.class)
    public void insert(User commentator, CommentCreateDTO commentCreateDTO) {
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        long time = System.currentTimeMillis();
        comment.setGmtCreate(time);
        comment.setGmtModified(time);
        comment.setCommentator(commentator.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            // 回复评论
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(parentComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            //增加评论数
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            //添加通知
            createNotify(comment, parentComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    //创建通知
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationTypeEnum, Long outerId) {
        if (receiver.equals(comment.getCommentator())) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(comment.getGmtCreate());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        List<Long> userIds = comments.stream().map(Comment::getCommentator).distinct().collect(Collectors.toList());

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));


        return comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            User user = userMap.get(comment.getCommentator());
            commentDTO.setUser(user);
            return commentDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteByMainQuestionId(Long questionId) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType())
                .andParentIdEqualTo(questionId);
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        comments.forEach(comment -> {
            commentMapper.deleteByPrimaryKey(comment.getId());
            //删除每个评论的二级评论
            CommentExample example = new CommentExample();
            example.createCriteria()
                    .andTypeEqualTo(CommentTypeEnum.COMMENT.getType())
                    .andParentIdEqualTo(comment.getId());
            commentMapper.deleteByExample(example);
        });
    }
}

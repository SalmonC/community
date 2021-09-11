package salmon.community.service;

/**
 * @author SalmonC
 * @date 2021-08-30 21:31
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;
import salmon.community.Constants;
import salmon.community.dto.PaginationDTO;
import salmon.community.dto.QuestionDTO;
import salmon.community.dto.QuestionQueryDTO;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.exception.CustomizeException;
import salmon.community.mapper.CommentMapper;
import salmon.community.mapper.QuestionExtMapper;
import salmon.community.mapper.QuestionMapper;
import salmon.community.mapper.UserMapper;
import salmon.community.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    CommentService commentService;

    public PaginationDTO listForIndex(String search, Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
            String[] split = StringUtils.split(search, " ");
            search = Arrays.stream(split).collect(Collectors.joining("|"));
        }
        Integer offset = size * (page - 1);
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setOffset(offset);
        questionQueryDTO.setSize(size);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        Integer totalPage;
        totalPage = totalCount / size + (totalCount % size == 0 ? 0 : 1);

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        List<Question> questions = questionExtMapper.selectBySearchWithBLOBs(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        paginationDTO.setTotalPage(totalPage);
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            String description = questionDTO.getDescription();
            if (description.length() > Constants.LONGEST_DESC_PREVIEWED) {
                String substring = description.substring(0, Constants.FOLDED_DESC_LENGTH) + "...";
                questionDTO.setDescription(substring);
            }
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;
    }

    public PaginationDTO listForProfile(Long userId, Integer page, Integer size) {
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        //获取问题总数
        Integer totalCount = (int) questionMapper.countByExample(example);
        Integer totalPage;
        totalPage = totalCount / size + (totalCount % size == 0 ? 0 : 1);

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        paginationDTO.setTotalPage(totalPage);
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            String description = questionDTO.getDescription();
            if (description.length() > Constants.LONGEST_DESC_PREVIEWED) {
                String substring = description.substring(0, Constants.FOLDED_DESC_LENGTH) + "...";
                questionDTO.setDescription(substring);
            }
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;
    }

    private PaginationDTO<QuestionDTO> getQuestionDTOPaginationDTO(Integer page, Integer size, QuestionExample example, Integer totalPage, Integer offset) {
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        paginationDTO.setTotalPage(totalPage);
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
            String description = questionDTO.getDescription();
            if (description.length() > Constants.LONGEST_DESC_PREVIEWED) {
                String substring = description.substring(0, Constants.FOLDED_DESC_LENGTH) + "...";
                questionDTO.setDescription(substring);
            }
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        paginationDTO.setPagination(totalPage, page, size);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreator()));
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            question.setGmtModified(System.currentTimeMillis());
            int updated = questionMapper.updateByPrimaryKeySelective(question);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",;");
        String tag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(tag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

    public List<QuestionDTO> selectHot() {
        return questionExtMapper.selectHot();
    }

    @Transactional(noRollbackFor = CustomizeException.class)
    public void deleteByToken(String token, Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        User user;
        if (users != null || users.size() > 0) {
            user = users.get(0);
        } else {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if (!user.getId().equals(question.getCreator())) {
            throw new CustomizeException(CustomizeErrorCode.NO_AUTHORITY);
        }
        try {
            //删除问题本身
            questionMapper.deleteByPrimaryKey(questionId);
            //删除一二级评论
            commentService.deleteByMainQuestionId(questionId);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}



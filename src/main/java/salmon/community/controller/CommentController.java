package salmon.community.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import salmon.community.dto.CommentCreateDTO;
import salmon.community.dto.CommentDTO;
import salmon.community.dto.ResultDTO;
import salmon.community.enums.CommentTypeEnum;
import salmon.community.exception.CustomizeErrorCode;
import salmon.community.model.Comment;
import salmon.community.model.User;
import salmon.community.provider.GithubProvider;
import salmon.community.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SalmonC
 * @date 2021-09-04 01:51
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    GithubProvider githubProvider;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            //此处不直接抛异常, 而是传输JSON,以实现页面局部刷新
            ResultDTO resultDTO = ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
            resultDTO.setGithubOAuthUrl(githubProvider.githubOAuthUrl);
            return resultDTO;
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        commentService.insert(user,commentCreateDTO);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id")Long id) {
        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOList);
    }
}

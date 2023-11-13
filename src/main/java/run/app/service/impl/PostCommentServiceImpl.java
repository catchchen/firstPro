package run.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import run.app.exception.BadRequestException;
import run.app.exception.ForbiddenException;
import run.app.exception.NotFoundException;
import run.app.model.entity.Post;
import run.app.model.entity.PostComment;
import run.app.model.enums.CommentViolationTypeEnum;
import run.app.model.properties.CommentProperties;
import run.app.repository.PostCommentRepository;
import run.app.repository.PostRepository;
import run.app.service.CommentBlackListService;
import run.app.service.OptionService;
import run.app.service.PostCommentService;
import run.app.service.UserService;
import run.app.service.assembler.comment.PostCommentAssembler;
import run.app.utils.ServletUtils;

/**
 * PostCommentService implementation class.
 *
 * @author ryanwang
 * @author johnniang
 * @author guqing
 * @date 2019-03-14
 */
@Slf4j
@Service
public class PostCommentServiceImpl extends BaseCommentServiceImpl<PostComment>
    implements PostCommentService {

    private final PostRepository postRepository;

    private final CommentBlackListService commentBlackListService;

    public PostCommentServiceImpl(PostCommentRepository postCommentRepository,
        PostRepository postRepository,
        UserService userService,
        OptionService optionService,
        CommentBlackListService commentBlackListService,
        ApplicationEventPublisher eventPublisher,
        PostCommentAssembler postCommentAssembler) {
        super(postCommentRepository, optionService, userService, eventPublisher,
            postCommentAssembler);
        this.postRepository = postRepository;
        this.commentBlackListService = commentBlackListService;
    }

    @Override
    public void validateTarget(@NonNull Integer postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(postId));

        if (post.getDisallowComment()) {
            throw new BadRequestException("该文章已经被禁止评论").setErrorData(postId);
        }
    }

    @Override
    public void validateCommentBlackListStatus() {
        CommentViolationTypeEnum banStatus =
            commentBlackListService.commentsBanStatus(ServletUtils.getRequestIp());
        Integer banTime = optionService
            .getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
        if (banStatus == CommentViolationTypeEnum.FREQUENTLY) {
            throw new ForbiddenException(String.format("您的评论过于频繁，请%s分钟之后再试。", banTime));
        }
    }

}

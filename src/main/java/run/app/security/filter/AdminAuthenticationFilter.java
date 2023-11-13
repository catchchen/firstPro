package run.app.security.filter;

import static run.app.model.support.HaloConst.ADMIN_TOKEN_HEADER_NAME;
import static run.app.model.support.HaloConst.ADMIN_TOKEN_QUERY_NAME;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import run.app.cache.AbstractStringCacheStore;
import run.app.config.properties.HaloProperties;
import run.app.exception.AuthenticationException;
import run.app.model.entity.User;
import run.app.security.authentication.AuthenticationImpl;
import run.app.security.context.SecurityContextHolder;
import run.app.security.context.SecurityContextImpl;
import run.app.security.handler.DefaultAuthenticationFailureHandler;
import run.app.security.service.OneTimeTokenService;
import run.app.security.support.UserDetail;
import run.app.security.util.SecurityUtils;
import run.app.service.OptionService;
import run.app.service.UserService;

/**
 * Admin authentication filter.
 *
 * @author johnniang
 */
@Slf4j
@Component
@Order(1)
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    private final HaloProperties haloProperties;

    private final UserService userService;

    public AdminAuthenticationFilter(AbstractStringCacheStore cacheStore,
        UserService userService,
        HaloProperties haloProperties,
        OptionService optionService,
        OneTimeTokenService oneTimeTokenService,
        ObjectMapper objectMapper) {
        super(haloProperties, optionService, cacheStore, oneTimeTokenService);
        this.userService = userService;
        this.haloProperties = haloProperties;

        addUrlPatterns("/api/admin/**", "/api/content/comments");

        addExcludeUrlPatterns(
            "/api/admin/login",
            "/api/admin/refresh/*",
            "/api/admin/installations",
            "/api/admin/migrations/halo",
            "/api/admin/is_installed",
            "/api/admin/password/code",
            "/api/admin/password/reset",
            "/api/admin/login/precheck"
        );

        // set failure handler
        DefaultAuthenticationFailureHandler failureHandler =
            new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(haloProperties.getMode().isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);

        setFailureHandler(failureHandler);

    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (!haloProperties.isAuthEnabled()) {
            // Set security
            userService.getCurrentUser().ifPresent(user ->
                SecurityContextHolder.setContext(
                    new SecurityContextImpl(new AuthenticationImpl(new UserDetail(user)))));

            // Do filter
            filterChain.doFilter(request, response);
            return;
        }

        // Get token from request
        String token = getTokenFromRequest(request);

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未登录，请登录后访问");
        }

        // Get user id from cache
        Optional<Integer> optionalUserId =
            cacheStore.getAny(SecurityUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            throw new AuthenticationException("Token 已过期或不存在").setErrorData(token);
        }

        // Get the user
        User user = userService.getById(optionalUserId.get());

        // Build user detail
        UserDetail userDetail = new UserDetail(user);

        // Set security
        SecurityContextHolder
            .setContext(new SecurityContextImpl(new AuthenticationImpl(userDetail)));

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
    }

}

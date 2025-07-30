package gift.handler;

import gift.annotation.EmailFromJwtToken;
import gift.exception.MemberExceptions;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import gift.auth.JwtAuth;

@Component
public class JwtEmailResolver implements HandlerMethodArgumentResolver {

    private final JwtAuth jwtAuth;

    public JwtEmailResolver(JwtAuth jwtAuth) {
        this.jwtAuth = jwtAuth;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(EmailFromJwtToken.class)
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MemberExceptions.InvalidAuthorizationHeaderException();
        }

        String token = authHeader.substring(7);
        jwtAuth.validateToken(token);

        return jwtAuth.getEmailFromToken(token);
    }
}

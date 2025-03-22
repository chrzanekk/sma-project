package pl.com.chrzanowski.sma.common.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<User> {

    private final UserService userService;
    private static final ThreadLocal<Optional<User>> currentAuditorCache = ThreadLocal.withInitial(Optional::empty);

    public AuditorAwareImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @NonNull
    public Optional<User> getCurrentAuditor() {
        if (currentAuditorCache.get().isPresent()) {
            return Optional.of(currentAuditorCache.get().get());
        }
        Long currentUserId = userService.getCurrentUserIdFromSecurityContext();
        Optional<User> auditor = Optional.empty();
        if (currentUserId != null) {
            User userProxy = new User();
            userProxy.setId(currentUserId);
            auditor = Optional.of(userProxy);
        }
        currentAuditorCache.set(auditor);
        return auditor;
    }

    // Opcjonalnie, po zakończeniu żądania, należy wyczyścić cache – można to zrobić np. w filtrze lub interceptorze.
    public static void clearCache() {
        currentAuditorCache.remove();
    }
}

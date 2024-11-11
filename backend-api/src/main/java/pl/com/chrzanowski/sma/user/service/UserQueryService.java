package pl.com.chrzanowski.sma.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;

import java.util.List;

public interface UserQueryService {

    List<UserDTO> findByFilter(UserFilter filter);

    Page<UserDTO> findByFilter(UserFilter filter, Pageable pageable);
}

package pl.com.chrzanowski.sma.user.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.user.dao.UserDao;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;
import pl.com.chrzanowski.sma.user.service.filter.UserQuerySpec;

import java.util.List;

@Service
@Transactional
public class UserQueryServiceImpl implements UserQueryService {

    private final Logger log = LoggerFactory.getLogger(UserQueryServiceImpl.class);

    private final UserDao userDao;
    private final UserMapper userMapper;

    public UserQueryServiceImpl(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public List<UserDTO> findByFilter(UserFilter filter) {
        log.debug("Query: Find all users by filter: {}", filter);
        BooleanBuilder specification = UserQuerySpec.buildPredicate(filter);
        return userMapper.toDtoList(userDao.findAll(specification));
    }

    @Override
    @Transactional
    public Page<UserDTO> findByFilter(UserFilter filter, Pageable pageable) {
        log.debug("Query: Find all users by filter and page: {}", filter);
        BooleanBuilder specification = UserQuerySpec.buildPredicate(filter);
        return userDao.findAll(specification, pageable).map(userMapper::toDto);
    }
}

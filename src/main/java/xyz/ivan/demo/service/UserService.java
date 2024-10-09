package xyz.ivan.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.ivan.demo.dto.PageDTO;
import xyz.ivan.demo.po.User;
import xyz.ivan.demo.query.PageQuery;
import xyz.ivan.demo.query.UserQuery;
import xyz.ivan.demo.vo.UserVO;

import java.util.List;


public interface UserService extends IService<User> {
    void deductBalance(Long id, Integer money);

    List<User> queryUsers(String name, Integer status, Integer maxBalance, Integer minBalance);

    UserVO getUserAndAddressById(Long id);

    List<UserVO> getUsersAndAddressesByIds(List<Long> ids);

    PageDTO<UserVO> queryUserPages(PageQuery pageQuery);
}

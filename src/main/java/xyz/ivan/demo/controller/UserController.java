package xyz.ivan.demo.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.ivan.demo.dto.UserFormDTO;
import xyz.ivan.demo.po.User;
import xyz.ivan.demo.query.UserQuery;
import xyz.ivan.demo.service.UserService;
import xyz.ivan.demo.vo.UserVO;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ApiOperation("新增用户")
    public void saveUser(@RequestBody UserFormDTO userFormDTO){
        // 1、将userFormDTO转为user
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        // 2、保存user
        userService.save(user);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public void removeUserById(@PathVariable("id") Long id){
        userService.removeById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public UserVO queryUserById(@PathVariable("id") Long id){
        return userService.getUserAndAddressById(id);
    }

    @GetMapping
    @ApiOperation("根据id集合查询用户")
    public List<UserVO> queryUsersByIds(@RequestParam("ids") List<Long> ids){
        return userService.getUsersAndAddressesByIds(ids);
    }

    @PutMapping("/{id}/deduction/{money}")
    @ApiOperation("根据id扣减用户余额")
    public void deductById(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减的余额") @PathVariable("money") Integer money
            ){
        userService.deductBalance(id, money);
    }

    @GetMapping("/list")
    @ApiOperation("复杂条件查询")
    public List<UserVO> queryUsers(UserQuery queryUser){
        String name = queryUser.getName();
        Integer status = queryUser.getStatus();
        Integer maxBalance = queryUser.getMaxBalance();
        Integer minBalance = queryUser.getMinBalance();
        List<User> users = userService.queryUsers(name, status, maxBalance, minBalance);
        return BeanUtil.copyToList(users, UserVO.class);
    }
}

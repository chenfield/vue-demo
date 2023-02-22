package com.example.demo.user;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao iUserDao;

    @Override
    public User add(User user) {
        iUserDao.add(user);
        return user;
    }


    @Override
    public int delete(int id) {
        return iUserDao.delete(id);
    }


    @Override
    public List<User> findAll() {
        return iUserDao.findAll();
    }


    @Override
    public User findById(Integer id) {
        return iUserDao.findById(id);
    }


    @Override
    public List<User> findByName(String name) {
        return iUserDao.findByName(name);
    }


    @Override
    public int getRowCount() {
        return iUserDao.getRowCount();
    }


    @Override
    public List<User> queryPage(Integer startRows, Integer size) {
        return iUserDao.queryPage(startRows, size);
    }


    @Override
    public int update(User user) {
        return iUserDao.update(user);
    }


}

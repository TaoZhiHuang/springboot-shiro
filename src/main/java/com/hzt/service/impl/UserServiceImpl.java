package com.hzt.service.impl;

import com.hzt.domain.User;
import com.hzt.mapper.UserMapper;
import com.hzt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public User queryUserByUserName(String username) {
		return userMapper.queryUserByUserName(username);
	}


}

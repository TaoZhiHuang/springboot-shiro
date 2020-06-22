package com.hzt.service.impl;

import com.hzt.domain.Role;
import com.hzt.mapper.RoleMapper;
import com.hzt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<String> queryRoleByUserId(Integer userId) {
		List<Role> list = roleMapper.queryRolesByUserId(userId);
		List<String> roles=new ArrayList<String>();
		for (Role role : list) {
			roles.add(role.getRolename());
		}
		return roles;
	}


}

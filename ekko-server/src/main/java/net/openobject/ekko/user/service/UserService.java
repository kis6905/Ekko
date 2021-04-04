package net.openobject.ekko.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.openobject.ekko.common.auth.payload.SignupRequest;
import net.openobject.ekko.user.builder.UserBuilder;
import net.openobject.ekko.user.dto.UserInfoRequest;
import net.openobject.ekko.user.dto.UserInfoResponse;
import net.openobject.ekko.user.entity.User;
import net.openobject.ekko.user.entity.UserERole;
import net.openobject.ekko.user.entity.UserRole;
import net.openobject.ekko.user.repository.UserRepository;
import net.openobject.ekko.user.repository.UserRoleRepository;

/**
 * UserService.java
 * <br/>
 * 사용자 서비스
 * 
 * @author  : SeHoon
 * @version : 1.0
 */
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserBuilder userBuilder;
	
	@Transactional(readOnly = true)
	public boolean existsByUserId(String userId) {
		return userRepository.existsByUserId(userId);
	}
	
	@Transactional(readOnly = true)
	public UserRole findByName(UserERole userERole){
		return userRoleRepository.findByName(userERole);
	}
	
	@Transactional(readOnly = false)
	public void registerUser(SignupRequest signUpReq) {
		User user = new User(signUpReq.getUserId(), encoder.encode(signUpReq.getPassword()), "testname", signUpReq.getUserEmailAddr(), signUpReq.getRole());
		userRepository.save(user);
	}
	
	@Transactional(readOnly = false)
	public void registerUser(User user) {
		userRepository.save(user);
	}
	
	@Transactional(readOnly = false)
	public UserInfoResponse modifyUser(UserInfoRequest userInfoReq) {
		
		User userEintity = userRepository.findByUserId(userInfoReq.getUserId()).orElse(null);
		userInfoReq.setNewPassword(encoder.encode(userInfoReq.getNewPassword()));
		userEintity = userEintity.update(userInfoReq);
		
		return userBuilder.buildDto(userEintity);
	}
	
}

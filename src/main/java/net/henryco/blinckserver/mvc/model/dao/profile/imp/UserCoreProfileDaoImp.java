package net.henryco.blinckserver.mvc.model.dao.profile.imp;

import net.henryco.blinckserver.mvc.model.dao.profile.UserCoreProfileDao;
import net.henryco.blinckserver.mvc.model.entity.profile.UserCoreProfile;
import net.henryco.blinckserver.mvc.model.repository.profile.UserCoreProfileRepository;
import net.henryco.blinckserver.util.dao.repo.BlinckRepositoryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Henry on 23/08/17.
 */
@Repository
public class UserCoreProfileDaoImp
		extends BlinckRepositoryProvider<UserCoreProfile, Long>
		implements UserCoreProfileDao {


	@Autowired
	public UserCoreProfileDaoImp(UserCoreProfileRepository baseProfileRepository) {
		super(baseProfileRepository);
	}

	private UserCoreProfileRepository getRepository() {
		return provideRepository();
	}


	@Override
	public List<UserCoreProfile> findByUserName(String username, int page, int size) {
		return getRepository().findAllByPublicProfile_Bio_UserName_NicknameLike(username, new PageRequest(page, size));
	}

	@Override
	public UserCoreProfile getByNickName(String nick) {
		return getRepository().getByPublicProfile_Bio_UserName_Nickname(nick);
	}

	@Override
	public boolean isNickNameExists(String nick) {
		return getRepository().existsByPublicProfile_Bio_UserName_Nickname(nick);
	}
}